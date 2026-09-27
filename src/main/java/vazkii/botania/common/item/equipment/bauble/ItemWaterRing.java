/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 17, 2014, 3:44:24 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final double MAX_SPEED = 1.3;

	public ItemWaterRing() {
		super(LibItemNames.WATER_RING, true);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		super.onWornTick(stack, entity);
		if (!(entity instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entity;

		if(player.isInsideOfMaterial(Material.WATER) && isActive(stack)) {
			ItemStack firstRing = BaublesApi.getBaublesHandler(player).getStackInSlot(1);
			if(!firstRing.isEmpty() && firstRing.getItem() instanceof ItemWaterRing && firstRing != stack) {
				return;
			}

			Vec3d motionVec = new Vec3d(player.motionX, player.motionY, player.motionZ);
			double strafe = player.moveStrafing, forward = player.moveForward;
			double sin = MathHelper.sin((float) (player.rotationYaw * Math.PI / 180)), cos = MathHelper.cos((float) (player.rotationYaw * Math.PI / 180));
			Vec3d movementDir = new Vec3d(strafe * cos - forward * sin, 0, forward * cos + strafe * sin);
			if (movementDir.lengthSquared() > 1e-6 && !player.capabilities.isFlying) {
				motionVec = motionVec.add(movementDir.normalize().scale(ConfigHandler.baubles.waterRingImpulse));
				if (motionVec.length() > MAX_SPEED) {
					motionVec = motionVec.normalize().scale(MAX_SPEED);
				}
				player.motionX = motionVec.x;
				player.motionZ = motionVec.z;
			}
			player.motionY *= ConfigHandler.baubles.waterRingVerticalVelocity;

			PotionEffect effect = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
			if(effect == null) {
				PotionEffect neweffect = new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, -42, true, true);
				player.addPotionEffect(neweffect);
			}

			if(player.getAir() <= 1) {
				int mana = ManaItemHandler.requestMana(stack, player, 300, true);
				if (mana > 0)
					player.setAir(mana);
			}
		} else onUnequipped(stack, player);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		PotionEffect effect = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
		if(effect != null && effect.getAmplifier() == -42)
			player.removePotionEffect(MobEffects.NIGHT_VISION);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
