/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OrechidRecipeLayout {
	public static final int MAX_ROWS = 6;
	public static final int SLOT_SIZE = 18;
	public static final int FOOTER_HEIGHT = 13;
	public static final int OUTPUT_Y = 72;

    public final int pageSize;
    public final int footerY;
    public final int height;
	public final int columns;
	public final int width;
	public final int inputX;
	private final IDrawable slot;
	private final IDrawableStatic arrowBackground;
	private final IDrawableAnimated arrow;

    public OrechidRecipeLayout(IGuiHelper guiHelper, int outputCount, int arrowTime) {
		this(guiHelper, outputCount, arrowTime, 9);
	}

    public OrechidRecipeLayout(IGuiHelper guiHelper, int outputCount, int arrowTime, int columns) {
		this.columns = columns;
		this.width = columns * SLOT_SIZE;
		this.inputX = (width - SLOT_SIZE) / 2;
        int rows = Math.max(1, Math.min(MAX_ROWS, (outputCount + columns - 1) / columns));
        pageSize = rows * columns;
        int gridBottom = OUTPUT_Y + rows * SLOT_SIZE;
        footerY = outputCount > pageSize ? gridBottom : -1;
        height = gridBottom + (footerY < 0 ? 0 : FOOTER_HEIGHT);

		slot = guiHelper.getSlotDrawable();
		ResourceLocation texture = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
		arrowBackground = guiHelper.createDrawable(texture, 24, 132, 24, 17);
		arrow = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(texture, 82, 128, 24, 17),
				arrowTime, IDrawableAnimated.StartDirection.LEFT, false);
    }

	public void drawExtras(@Nonnull Minecraft minecraft) {
		slot.draw(minecraft, inputX, 0);
		for(int index = 0; index < pageSize; index++) {
			slot.draw(minecraft, index % columns * SLOT_SIZE, OUTPUT_Y + index / columns * SLOT_SIZE);
		}
		GlStateManager.pushMatrix();
		try {
			GlStateManager.translate((width + arrowBackground.getHeight()) / 2.0F, 44, 0);
			GlStateManager.rotate(90, 0, 0, 1);
			arrowBackground.draw(minecraft, 0, 0);
			arrow.draw(minecraft, 0, 0);
		} finally {
			GlStateManager.popMatrix();
		}
	}

}