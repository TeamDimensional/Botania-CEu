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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public abstract class ItemBauble extends ItemBaubleBase implements IBauble {

	public ItemBauble(String name, boolean canBeDisabled) {
		super(name, canBeDisabled);
	}

	public ItemBauble(String name) {
		super(name);
	}

	private @Nullable ActionResult<ItemStack> tryEquip(EntityPlayer player, int slotIndex, ItemStack inHand, ItemStack toEquip) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		ItemStack stackInSlot = baubles.getStackInSlot(slotIndex);
		IBauble baubleInSlot = stackInSlot.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
		if(stackInSlot.isEmpty() || baubleInSlot == null || baubleInSlot.canUnequip(stackInSlot, player)) {
			// If toEquip and stackInSlot are stacks with equal value but not identity, ItemStackHandler.setStackInSlot actually does nothing >.>
			// Prevent it from trying to be overly smart by going through empty first
			baubles.setStackInSlot(slotIndex, ItemStack.EMPTY);

			baubles.setStackInSlot(slotIndex, toEquip);
			((IBauble) toEquip.getItem()).onEquipped(toEquip, player);

			inHand.shrink(1);

			PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/bauble_wear"), "code_triggered");

			if(!stackInSlot.isEmpty()) {
				if(baubleInSlot != null) {
					baubleInSlot.onUnequipped(stackInSlot, player);
				}

				if(inHand.isEmpty()) {
					return ActionResult.newResult(EnumActionResult.SUCCESS, stackInSlot);
				} else {
					ItemHandlerHelper.giveItemToPlayer(player, stackInSlot);
				}
			}

			return ActionResult.newResult(EnumActionResult.SUCCESS, inHand);
		}

		return null;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (canBeDisabled && GuiScreen.isShiftKeyDown()) {
            ItemStack stack = player.getHeldItem(hand);
            if (!world.isRemote) {
                toggle(stack);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

		ItemStack stack = player.getHeldItem(hand);
		if(!EntityDoppleganger.isTruePlayer(player))
			return ActionResult.newResult(EnumActionResult.FAIL, stack);

		ItemStack toEquip = stack.copy();
		toEquip.setCount(1);

		if(canEquip(toEquip, player)) {
			if(world.isRemote)
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);

			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

			for(int i = 0; i < baubles.getSlots(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip, player) && baubles.getStackInSlot(i).isEmpty()) {
					ActionResult<ItemStack> result = tryEquip(player, i, stack, toEquip);
					if (result != null) {
						return result;
					}
				}
			}

			for(int i = 0; i < baubles.getSlots(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip, player)) {
					ActionResult<ItemStack> result = tryEquip(player, i, stack, toEquip);
					if (result != null) {
						return result;
					}
				}
			}
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

}
