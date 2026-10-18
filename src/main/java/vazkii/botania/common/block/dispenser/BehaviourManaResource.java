package vazkii.botania.common.block.dispenser;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.lib.LibItemNames;

public class BehaviourManaResource extends BehaviorDefaultDispenseItem {

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		if(stack.getItemDamage() == LibItemNames.MANA_RESOURCE_NUMBERS.get("enderAirBottle")) {
            World world = source.getWorld();
            IPosition position = BlockDispenser.getDispensePosition(source);
            EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
            EntityEnderAirBottle projectile = new EntityEnderAirBottle(world);
            projectile.setPosition(position.getX(), position.getY(), position.getZ());
            projectile.shoot((double)enumfacing.getXOffset(), (double)((float)enumfacing.getYOffset() + 0.1F), (double)enumfacing.getZOffset(), 1.1F, 4.0F);
            world.spawnEntity(projectile);
            stack.shrink(1);
		}

		return stack;
	}

}
