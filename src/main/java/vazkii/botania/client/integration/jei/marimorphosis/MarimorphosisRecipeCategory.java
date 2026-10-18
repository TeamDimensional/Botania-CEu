package vazkii.botania.client.integration.jei.marimorphosis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipeMarimorphosis;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.client.integration.jei.OrechidRecipeLayout;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class MarimorphosisRecipeCategory implements IRecipeCategory<MarimorphosisRecipeWrapper> {
	public static final String UID = "botania.marimorphosis";
	private final OrechidRecipeLayout layout;
	private final IDrawable background;
	private final String localizedName;
	private final String flowerName;
    private ItemStack[] displays;
	private static final double BIOME_SCALE = 0.8;

	public MarimorphosisRecipeCategory(IGuiHelper guiHelper) {
		layout = new OrechidRecipeLayout(guiHelper, 8, 20, 8);
		background = guiHelper.createBlankDrawable(layout.width, layout.height + OrechidRecipeLayout.SLOT_SIZE);
		localizedName = I18n.format("botania.nei.marimorphosis");
		this.flowerName = LibBlockNames.SUBTILE_MARIMORPHOSIS;
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

    public void createBiomeMarkers(IIngredientRegistry registry) {
        displays = new ItemStack[]{
            new ItemStack(Item.getItemFromBlock(Blocks.SAPLING)),
            new ItemStack(Item.getItemFromBlock(Blocks.TALLGRASS), 1, 1),
            new ItemStack(Item.getItemFromBlock(Blocks.STONE)),
            new ItemStack(Item.getItemFromBlock(Blocks.MYCELIUM)),
            new ItemStack(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM)),
            new ItemStack(Item.getItemFromBlock(Blocks.CACTUS)),
            new ItemStack(Item.getItemFromBlock(Blocks.ICE)),
            new ItemStack(Item.getItemFromBlock(Blocks.HARDENED_CLAY)),
        };
    }

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull MarimorphosisRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    
        itemStacks.init(0, true, layout.inputX, 0);
        itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        for (int i = 0; i < RecipeMarimorphosis.TYPES.length; ++i) {
            itemStacks.init(i + 1, false, i * OrechidRecipeLayout.SLOT_SIZE, OrechidRecipeLayout.OUTPUT_Y);
            itemStacks.set(i + 1, ingredients.getOutputs(VanillaTypes.ITEM).get(i));
        }
		itemStacks.init(9, true, layout.inputX, 22);
		itemStacks.set(9, ItemBlockSpecialFlower.ofType(flowerName));
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		layout.drawExtras(minecraft);

        if (displays == null) {
            createBiomeMarkers(JEIBotaniaPlugin.ingredientRegistry);
        }

		GlStateManager.enableDepth();
		RenderHelper.enableGUIStandardItemLighting();
        int idx = 0;
		int posDelta = OrechidRecipeLayout.SLOT_SIZE / 2;
        for (ItemStack biomeMarker : displays) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(
				idx * OrechidRecipeLayout.SLOT_SIZE + posDelta, OrechidRecipeLayout.OUTPUT_Y + OrechidRecipeLayout.SLOT_SIZE + posDelta, 0);
			GlStateManager.scale(BIOME_SCALE, BIOME_SCALE, BIOME_SCALE);
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, biomeMarker, 1 - posDelta, 1 - posDelta);
			GlStateManager.popMatrix();
            idx++;
        }
		GlStateManager.disableBlend();
		RenderHelper.disableStandardItemLighting();
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}
    
}
