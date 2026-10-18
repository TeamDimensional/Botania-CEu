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
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import vazkii.botania.client.integration.jei.OrechidRecipeLayout;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class OrechidRecipeCategory<T extends OrechidRecipeWrapper> implements IRecipeCategory<T> {

	public static final String UID = "botania.orechid";
	private final OrechidRecipeLayout layout;
	private final IDrawable background;
	private final String localizedName;
	private final String flowerName;

	public OrechidRecipeCategory(IGuiHelper guiHelper, int outputCount) {
		this(guiHelper, outputCount, "botania.nei.orechid", LibBlockNames.SUBTILE_ORECHID);
	}

	protected OrechidRecipeCategory(IGuiHelper guiHelper, int outputCount, String titleKey, String flowerName) {
		layout = new OrechidRecipeLayout(guiHelper, outputCount, 100);
		background = guiHelper.createBlankDrawable(layout.width, layout.height);
		localizedName = I18n.format(titleKey);
		this.flowerName = flowerName;
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
		itemStacks.init(flowerSlot, true, layout.inputX, 22);
		itemStacks.set(flowerSlot, ItemBlockSpecialFlower.ofType(flowerName));
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		layout.drawExtras(minecraft);
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}
}
