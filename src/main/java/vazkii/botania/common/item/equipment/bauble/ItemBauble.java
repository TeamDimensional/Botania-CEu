/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 20, 2014, 3:30:06 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.IBauble;

import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public abstract class ItemBauble extends ItemBaubleBase implements IBauble {

	public ItemBauble(String name, boolean canBeDisabled) {
		super(name, canBeDisabled);
	}

	public ItemBauble(String name) {
		super(name);
	}

}
