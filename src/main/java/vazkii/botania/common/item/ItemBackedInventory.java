package vazkii.botania.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ItemBackedInventory extends InventoryBasic {

	private static final String TAG_SLOT = "Slot";
	private final ItemStack stack;
	private final String tag;

	public ItemBackedInventory(ItemStack stack, int size, String tag) {
		super("", false, size);
		this.stack = stack;
		this.tag = tag;

		NBTTagList list = ItemNBTHelper.getList(stack, tag, Constants.NBT.TAG_COMPOUND, false);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound cmp = list.getCompoundTagAt(i);
			int slot = cmp.getByte(TAG_SLOT);
			if(slot >= 0 && slot < size)
				super.setInventorySlotContents(slot, new ItemStack(cmp));
		}
	}

	public static boolean hasItems(ItemStack stack, String tag) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(tag, Constants.NBT.TAG_LIST);
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
		return !stack.isEmpty();
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack result = super.decrStackSize(index, count);
		if(!result.isEmpty())
			markDirty();

		return result;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack result = super.removeStackFromSlot(index);
		if(!result.isEmpty())
			markDirty();

		return result;
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		markDirty();
	}

	@Override
	public void clear() {
		super.clear();
		markDirty();
	}

	@Override
	public void markDirty() {
		super.markDirty();

		NBTTagList list = new NBTTagList();
		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack slotStack = getStackInSlot(i);
			if(!slotStack.isEmpty()) {
				NBTTagCompound cmp = new NBTTagCompound();
				cmp.setByte(TAG_SLOT, (byte) i);
				slotStack.writeToNBT(cmp);
				list.appendTag(cmp);
			}
		}

		ItemNBTHelper.setList(stack, tag, list);
	}

	public static class ItemHandler extends InvWrapper {

		private static final InventoryBasic EMPTY_INVENTORY = new InventoryBasic("", false, 0);

		private final Supplier<? extends ItemBackedInventory> inventorySupplier;

		public ItemHandler(Supplier<? extends ItemBackedInventory> inventorySupplier) {
			super(EMPTY_INVENTORY);
			this.inventorySupplier = inventorySupplier;
		}

		@Override
		public ItemBackedInventory getInv() {
			return inventorySupplier.get();
		}
	}

}
