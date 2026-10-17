/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class OrechidRecipeCategory<T extends OrechidRecipeWrapper> implements IRecipeCategory<T> {

	public static final String UID = "botania.orechid";
	static final int COLUMNS = 9;
	static final int SLOT_SIZE = 18;
	static final int WIDTH = COLUMNS * SLOT_SIZE;
	static final int INPUT_X = (WIDTH - SLOT_SIZE) / 2;
	static final int OUTPUT_Y = 72;
	static final int FOOTER_HEIGHT = 13;
	private static final int MAX_ROWS = 6;
	private final Layout layout;
	private final IDrawable background;
	private final String localizedName;
	private final String flowerName;
	private final IDrawable slot;
	private final IDrawableStatic arrowBackground;
	private final IDrawableAnimated arrow;

	public OrechidRecipeCategory(IGuiHelper guiHelper, int outputCount) {
		this(guiHelper, outputCount, "botania.nei.orechid", LibBlockNames.SUBTILE_ORECHID);
	}

	protected OrechidRecipeCategory(IGuiHelper guiHelper, int outputCount, String titleKey, String flowerName) {
		layout = new Layout(outputCount);
		background = guiHelper.createBlankDrawable(WIDTH, layout.height);
		localizedName = I18n.format(titleKey);
		this.flowerName = flowerName;
		slot = guiHelper.getSlotDrawable();
		ResourceLocation texture = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
		arrowBackground = guiHelper.createDrawable(texture, 24, 132, 24, 17);
		arrow = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(texture, 82, 128, 24, 17),
				200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull T recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		recipeWrapper.bindLayout(itemStacks, layout);
		int flowerSlot = recipeWrapper.getOutputCount() + 1;
		itemStacks.init(flowerSlot, true, INPUT_X, 22);
		itemStacks.set(flowerSlot, ItemBlockSpecialFlower.ofType(flowerName));
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		slot.draw(minecraft, INPUT_X, 0);
		for(int index = 0; index < layout.pageSize; index++) {
			slot.draw(minecraft, index % COLUMNS * SLOT_SIZE, OUTPUT_Y + index / COLUMNS * SLOT_SIZE);
		}
		GlStateManager.pushMatrix();
		try {
			GlStateManager.translate((WIDTH + arrowBackground.getHeight()) / 2.0F, 44, 0);
			GlStateManager.rotate(90, 0, 0, 1);
			arrowBackground.draw(minecraft, 0, 0);
			arrow.draw(minecraft, 0, 0);
		} finally {
			GlStateManager.popMatrix();
		}
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

	static final class Layout {
		final int pageSize;
		final int footerY;
		final int height;

		Layout(int outputCount) {
			int rows = Math.max(1, Math.min(MAX_ROWS, (outputCount + COLUMNS - 1) / COLUMNS));
			pageSize = rows * COLUMNS;
			int gridBottom = OUTPUT_Y + rows * SLOT_SIZE;
			footerY = outputCount > pageSize ? gridBottom : -1;
			height = gridBottom + (footerY < 0 ? 0 : FOOTER_HEIGHT);
		}
	}
}
