package vazkii.botania.common.block.dispenser;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class BehaviourGlassBottle extends BehaviorDefaultDispenseItem {

    private IBehaviorDispenseItem original;

    public BehaviourGlassBottle(IBehaviorDispenseItem original) {
        this.original = original;
    }
    
	@Nonnull
	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();

		if (world.provider instanceof WorldProviderEnd) {
			List<EntityAreaEffectCloud> list = world.getEntitiesWithinAABB(EntityAreaEffectCloud.class,
					new AxisAlignedBB(source.getBlockPos(), source.getBlockPos()).grow(3.5D),
					entity -> entity != null && entity.isEntityAlive()
							&& entity.getParticle() == EnumParticleTypes.DRAGON_BREATH);
			if (list.isEmpty()) {
                // Normally it is reasonable to put the item back into dispenser, however in this case both Ender Air Bottle and Empty Bottle have behaviors
                EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                ItemStack itemstack = new ItemStack(ModItems.manaResource, 1, LibItemNames.MANA_RESOURCE_NUMBERS.get("enderAirBottle"));
                BehaviorDefaultDispenseItem.doDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
                source.getWorld().playEvent(1000, source.getBlockPos(), 0);
                source.getWorld().playEvent(2000, source.getBlockPos(), enumfacing.getXOffset() + 1 + (enumfacing.getZOffset() + 1) * 3);
                stack.shrink(1);
                return stack;
			}
        }

        return original.dispense(source, stack);
	}
}
