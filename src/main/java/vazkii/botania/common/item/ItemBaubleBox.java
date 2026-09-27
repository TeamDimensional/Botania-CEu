/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [25/11/2015, 19:46:11 (GMT)]
 */
package vazkii.botania.common.item;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBaubleBox extends ItemMod {

	private static final String TAG_ITEMS = "InvItems";

	public ItemBaubleBox() {
		super(LibItemNames.BAUBLE_BOX);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
		return new InvProvider(stack);
	}

	private static class InvProvider implements ICapabilitySerializable<NBTBase> {

		private final ItemStack stack;
		private final IItemHandlerModifiable inv;

		private InvProvider(ItemStack stack) {
			this.stack = stack;
			inv = new ItemBackedInventory.ItemHandler(() -> getInventory(stack));
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
			else return null;
		}

		@Override
		public NBTBase serializeNBT() {
			// Clear legacy capability data after moving it into the item tag
			return new NBTTagList();
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			if(!ItemBackedInventory.hasItems(stack, TAG_ITEMS)) {
				CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
			}
		}
	}

	public static ItemBackedInventory getInventory(ItemStack stack) {
		return new ItemBackedInventory(stack, 24, TAG_ITEMS) {
			@Override
			public boolean isItemValidForSlot(int slot, @Nonnull ItemStack toInsert) {
				if(toInsert.isEmpty())
					return false;

				boolean isBauble = toInsert.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
				return toInsert.getItem() instanceof IManaItem || isBauble
						|| ContainerBaubleBox.RODS.contains(toInsert.getItem().getRegistryName());
			}
		};
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.openGui(Botania.instance, LibGuiIDs.BAUBLE_BOX, world, hand == EnumHand.OFF_HAND ? 1 : 0, 0, 0);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

}
