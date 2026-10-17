/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrechidRecipeWrapper implements IRecipeWrapper {

	private static final int HIDDEN_POSITION = -10000;
	private static final int PAGE_BUTTON_WIDTH = 18;
	private final List<ItemStack> outputStacks;
	private IGuiItemStackGroup itemStacks;
	private OrechidRecipeCategory.Layout layout;
	private int page;

	public OrechidRecipeWrapper() {
		this(BotaniaAPI.oreWeights);
	}

	protected OrechidRecipeWrapper(Map<String, Integer> oreWeights) {
		List<ItemStack> outputs;
		try {
			outputs = readOutputs(oreWeights);
		} catch (RuntimeException | LinkageError failure) {
			Botania.LOGGER.warn("Cannot display {}: unable to read the loaded ore weights", getClass().getSimpleName(), failure);
			outputs = Collections.emptyList();
		}
		outputStacks = Collections.unmodifiableList(outputs);
	}

	protected ItemStack getInputStack() {
		return new ItemStack(Blocks.STONE, 64);
	}

	public int getOutputCount() {
		return outputStacks.size();
	}

	private static List<ItemStack> readOutputs(Map<String, Integer> oreWeights) {
		Map<OutputKey, WeightedOutput> grouped = new LinkedHashMap<>();
		long allWeight = 0;
		long validWeight = 0;
		for(Map.Entry<String, Integer> entry : new LinkedHashMap<>(oreWeights).entrySet()) {
			String name = entry.getKey();
			Integer weight = entry.getValue();
			if(name == null || name.isEmpty() || weight == null || weight < 0) {
				throw new IllegalArgumentException("Invalid ore weight: " + name + "=" + weight);
			}
			allWeight += weight;
			if(allWeight > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Ore weight total exceeds WeightedRandom's integer limit");
			}
			if(weight == 0) {
				continue;
			}
			ItemStack stack = resolveOre(name);
			if(stack.isEmpty()) {
				continue; // Orechid rerolls names with no ItemBlock, so exclude them from the denominator.
			}
			validWeight += weight;
			OutputKey key = new OutputKey(stack);
			WeightedOutput existing = grouped.get(key);
			if(existing == null) {
				grouped.put(key, new WeightedOutput(stack, weight));
			} else {
				existing.weight += weight;
			}
		}

		List<WeightedOutput> sorted = new ArrayList<>(grouped.values());
		sorted.sort(Comparator.comparingLong((WeightedOutput output) -> output.weight).reversed()
				.thenComparing(output -> String.valueOf(output.stack.getItem().getRegistryName()))
				.thenComparingInt(output -> output.stack.getItemDamage()));
		List<ItemStack> outputs = new ArrayList<>(sorted.size());
		for(WeightedOutput output : sorted) {
			// Preserve Botania's approximate yield per 64 input blocks, including its minimum of one.
			output.stack.setCount(Math.max(1, Math.round((float) output.weight * 64 / validWeight)));
			outputs.add(output.stack);
		}
		return outputs;
	}

	private static ItemStack resolveOre(String name) {
		for(ItemStack candidate : OreDictionary.getOres(name, false)) {
			if(candidate == null || candidate.isEmpty() || !(candidate.getItem() instanceof ItemBlock)) {
				continue;
			}
			ItemStack copy = candidate.copy();
			Block block = Block.getBlockFromItem(copy.getItem());
			// Match the flower's first ItemBlock and raw metadata, including wildcard metadata.
			IBlockState state = block.getStateFromMeta(copy.getItemDamage());
			if(state == null) {
				throw new IllegalArgumentException("Ore has no block state: " + name);
			}
			ItemStack output = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			if(output.isEmpty()) {
				throw new IllegalArgumentException("Ore has no displayable block item: " + name);
			}
			output.setTagCompound(null); // Orechid places a block state, not the ore dictionary stack's NBT.
			return output;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, getInputStack());
		List<ItemStack> copies = new ArrayList<>(outputStacks.size());
		for(ItemStack stack : outputStacks) {
			copies.add(stack.copy());
		}
		ingredients.setOutputs(VanillaTypes.ITEM, copies);
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		if(layout == null || layout.footerY < 0) {
			return;
		}
		FontRenderer font = minecraft.fontRenderer;
		int textY = layout.footerY + (OrechidRecipeCategory.FOOTER_HEIGHT - font.FONT_HEIGHT) / 2;
		String pageText = (page + 1) + " / " + getPageCount();
		font.drawString(pageText, (recipeWidth - font.getStringWidth(pageText)) / 2, textY, 0x606060);
		font.drawString("<", (PAGE_BUTTON_WIDTH - font.getStringWidth("<")) / 2, textY,
				page > 0 ? 0x404040 : 0xA0A0A0);
		font.drawString(">", OrechidRecipeCategory.WIDTH - (PAGE_BUTTON_WIDTH + font.getStringWidth(">")) / 2,
				textY, page + 1 < getPageCount() ? 0x404040 : 0xA0A0A0);
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		if(mouseButton != 0 || layout == null || layout.footerY < 0
				|| mouseY < layout.footerY || mouseY >= layout.footerY + OrechidRecipeCategory.FOOTER_HEIGHT) {
			return false;
		}
		if(mouseX >= 0 && mouseX < PAGE_BUTTON_WIDTH) {
			setPage(page - 1);
			return true;
		}
		if(mouseX >= OrechidRecipeCategory.WIDTH - PAGE_BUTTON_WIDTH && mouseX < OrechidRecipeCategory.WIDTH) {
			setPage(page + 1);
			return true;
		}
		return false;
	}

	void bindLayout(IGuiItemStackGroup itemStacks, OrechidRecipeCategory.Layout layout) {
		this.itemStacks = itemStacks;
		this.layout = layout;
		page = Math.max(0, Math.min(page, getPageCount() - 1));
		itemStacks.init(0, true, OrechidRecipeCategory.INPUT_X, 0);
		itemStacks.set(0, getInputStack());
		int first = page * layout.pageSize;
		int last = Math.min(first + layout.pageSize, outputStacks.size());
		for(int index = 0; index < outputStacks.size(); index++) {
			initSlot(index, index >= first && index < last ? index - first : -1);
		}
	}

	private void setPage(int newPage) {
		if(newPage == page || newPage < 0 || newPage >= getPageCount()) {
			return;
		}
		int oldPage = page;
		page = newPage;
		positionPage(oldPage, false);
		positionPage(page, true);
	}

	private void positionPage(int targetPage, boolean visible) {
		int first = targetPage * layout.pageSize;
		int last = Math.min(first + layout.pageSize, outputStacks.size());
		for(int index = first; index < last; index++) {
			initSlot(index, visible ? index - first : -1);
		}
	}

	private void initSlot(int index, int visibleIndex) {
		int x = visibleIndex < 0 ? HIDDEN_POSITION : visibleIndex % OrechidRecipeCategory.COLUMNS * OrechidRecipeCategory.SLOT_SIZE;
		int y = visibleIndex < 0 ? HIDDEN_POSITION : OrechidRecipeCategory.OUTPUT_Y
				+ visibleIndex / OrechidRecipeCategory.COLUMNS * OrechidRecipeCategory.SLOT_SIZE;
		// Every product keeps its own output slot, even on hidden pages, for whole-filter transfers.
		itemStacks.init(index + 1, false, x, y);
		itemStacks.set(index + 1, outputStacks.get(index).copy());
	}

	private int getPageCount() {
		return layout == null ? 1 : Math.max(1, (outputStacks.size() + layout.pageSize - 1) / layout.pageSize);
	}

	private static final class WeightedOutput {
		final ItemStack stack;
		long weight;

		WeightedOutput(ItemStack stack, long weight) {
			this.stack = stack;
			this.weight = weight;
		}
	}

	private static final class OutputKey {
		final Item item;
		final int metadata;

		OutputKey(ItemStack stack) {
			item = stack.getItem();
			metadata = stack.getItemDamage();
		}

		@Override
		public boolean equals(Object other) {
			if(!(other instanceof OutputKey)) {
				return false;
			}
			OutputKey key = (OutputKey) other;
			return item == key.item && metadata == key.metadata;
		}

		@Override
		public int hashCode() {
			return 31 * System.identityHashCode(item) + metadata;
		}
	}
}
