/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.integration.jei.marimorphosis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipeMarimorphosis;
import vazkii.botania.client.integration.jei.OrechidRecipeLayout;

public class MarimorphosisRecipeWrapper implements IRecipeWrapper {

	private final RecipeMarimorphosis recipe;

	public MarimorphosisRecipeWrapper(RecipeMarimorphosis recipe) {
		this.recipe = recipe;
	}

	private ItemStack stackFromBlockState(IBlockState output) {
		Block block = output.getBlock();
		return new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(output));
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		List<ItemStack> inputStacks = new ArrayList<>();
		for (IBlockState in : recipe.inputs) {
			inputStacks.add(stackFromBlockState(in));
		}
		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(inputStacks);
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);

		List<ItemStack> outputStacks = new ArrayList<>();
		for (IBlockState in : recipe.outputs) {
			outputStacks.add(stackFromBlockState(in));
		}
		ingredients.setOutputs(VanillaTypes.ITEM, outputStacks);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> tooltips = new ArrayList<>();
		if (mouseY > OrechidRecipeLayout.OUTPUT_Y + OrechidRecipeLayout.SLOT_SIZE && mouseY <= OrechidRecipeLayout.OUTPUT_Y + 2 * OrechidRecipeLayout.SLOT_SIZE) {
			int selectedBiome = mouseX / OrechidRecipeLayout.SLOT_SIZE;
			tooltips.add(I18n.format("botania.nei.marimorphosis_biome." + (recipe.biomeMultiplier == 0 ? "required" : "preferred") + "." + selectedBiome));
		}
		return tooltips;
	}

}
