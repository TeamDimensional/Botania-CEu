/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;

public class OrechidIgnemRecipeWrapper extends OrechidRecipeWrapper {

	public OrechidIgnemRecipeWrapper() {
		super(BotaniaAPI.oreWeightsNether);
	}

	@Override
	protected ItemStack getInputStack() {
		return new ItemStack(Blocks.NETHERRACK, 64);
	}
}
