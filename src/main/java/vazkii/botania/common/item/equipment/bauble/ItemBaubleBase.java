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

import baubles.api.BaublesApi;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public abstract class ItemBaubleBase extends ItemMod implements ICosmeticAttachable, IPhantomInkable {

	private static final String TAG_HASHCODE = "playerHashcode";
	private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
	private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";
	private static final String TAG_COSMETIC_ITEM = "cosmeticItem";
	private static final String TAG_PHANTOM_INK = "phantomInk";
	private static final String TAG_DISABLED = "baubleDisabled";

	protected final boolean canBeDisabled;

	public ItemBaubleBase(String name, boolean canBeDisabled) {
		super(name);
		this.canBeDisabled = canBeDisabled;
		setMaxStackSize(1);
	}

	public ItemBaubleBase(String name) {
		this(name, false);
	}

    protected void toggle(ItemStack stack) {
        boolean value = ItemNBTHelper.getBoolean(stack, TAG_DISABLED, false);
        ItemNBTHelper.setBoolean(stack, TAG_DISABLED, !value);
    }

    protected static boolean isActive(ItemStack stack) {
        return !ItemNBTHelper.getBoolean(stack, TAG_DISABLED, false);
    }

	// Apparently baubles doesn't unequip on death, which causes attribute modifiers to get weird/desync on respawn
	// See Baubles#236
	// Do it for our baubles if they're going to drop
	// TODO there is still some weirdness going on when dying/returning in the End, figure that out
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent evt) {
		if(!evt.getEntityLiving().world.isRemote
				&& evt.getEntityLiving() instanceof EntityPlayer
				&& !evt.getEntityLiving().world.getGameRules().getBoolean("keepInventory")
				&& !((EntityPlayer) evt.getEntityLiving()).isSpectator()) {
			IItemHandler inv = BaublesApi.getBaublesHandler((EntityPlayer) evt.getEntityLiving());
			for(int i = 0; i < inv.getSlots(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty() && stack.getItem().getRegistryName().getNamespace().equals(LibMisc.MOD_ID)) {
					((ItemBauble) stack.getItem()).onUnequipped(stack, evt.getEntityLiving());
				}
			}
		}
	}

    public boolean canEquip(ItemStack canEquip, EntityLivingBase player) {
        return true;
    }

	protected boolean shouldAddBaublesTooltip(ItemStack itemStack, World world) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> stacks, ITooltipFlag flags) {
		if (!shouldAddBaublesTooltip(itemStack, world)) {
			return;
		}

		if (GuiScreen.isShiftKeyDown())
			addHiddenTooltip(itemStack, world, stacks, flags);
		else
			addStringToTooltip(I18n.format("botaniamisc.shiftinfo"), stacks);

        if (!isActive(itemStack)) {
            addStringToTooltip(I18n.format("botaniamisc.baubleDisabled"), stacks);
        }
	}

	@SideOnly(Side.CLIENT)
	public void addHiddenTooltip(ItemStack par1ItemStack, World world, List<String> stacks, ITooltipFlag flags) {
		String key = RenderHelper.getKeyDisplayString("keybind.baublesinventory");
		if (key == null) {
			// Baubles EX :(
			key = RenderHelper.getKeyDisplayString("Open Baubles' Sidebar");
		}

		if(key != null)
			addStringToTooltip(I18n.format("botania.baubletooltip", key), stacks);

		ItemStack cosmetic = getCosmeticItem(par1ItemStack);
		if(!cosmetic.isEmpty())
			addStringToTooltip(I18n.format("botaniamisc.hasCosmetic", cosmetic.getDisplayName()), stacks);

		if(hasPhantomInk(par1ItemStack))
			addStringToTooltip(I18n.format("botaniamisc.hasPhantomInk"), stacks);
	}

	protected void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(getLastPlayerHashcode(stack) != player.hashCode()) {
			onEquippedOrLoadedIntoWorld(stack, player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		if(player != null) {
			if(!player.world.isRemote) {
				player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.equipBauble, SoundCategory.PLAYERS, 0.1F, 1.3F);
				PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/bauble_wear"), "code_triggered");
			}

			onEquippedOrLoadedIntoWorld(stack, player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {}

	@Override
	public ItemStack getCosmeticItem(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM, true);
		if(cmp == null)
			return ItemStack.EMPTY;
		return new ItemStack(cmp);
	}

	@Override
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(!cosmetic.isEmpty())
			cmp = cosmetic.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return getCosmeticItem(itemStack);
	}

	public static UUID getBaubleUUID(ItemStack stack) {
		long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
		if(most == 0) {
			UUID uuid = UUID.randomUUID();
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, uuid.getMostSignificantBits());
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, uuid.getLeastSignificantBits());
			return getBaubleUUID(stack);
		}

		long least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0);
		return new UUID(most, least);
	}

	public static int getLastPlayerHashcode(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_HASHCODE, 0);
	}

	public static void setLastPlayerHashcode(ItemStack stack, int hash) {
		ItemNBTHelper.setInt(stack, TAG_HASHCODE, hash);
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}
}
