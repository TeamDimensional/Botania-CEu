/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 9, 2014, 8:51:55 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;

public class TileAlfPortal extends TileMod implements ITickable {

	private static BlockPos[] getLivingwoodPositions() {
		List<BlockPos> positions = new ArrayList<>();
		for (int i = 1; i < ConfigHandler.elfPortalSize; i++) {
			// Bottom row
			positions.add(new BlockPos(-i, 0, 0));
			positions.add(new BlockPos(i, 0, 0));
			// Left-bottom and right-bottom
			positions.add(new BlockPos(ConfigHandler.elfPortalSize, i, 0));
			positions.add(new BlockPos(-ConfigHandler.elfPortalSize, i, 0));
			// Left-top and right-top
			positions.add(new BlockPos(ConfigHandler.elfPortalSize, i + ConfigHandler.elfPortalSize, 0));
			positions.add(new BlockPos(-ConfigHandler.elfPortalSize, i + ConfigHandler.elfPortalSize, 0));
			// Top row
			positions.add(new BlockPos(-i, 2 * ConfigHandler.elfPortalSize, 0));
			positions.add(new BlockPos(i, 2 * ConfigHandler.elfPortalSize, 0));
		}
		return positions.toArray(new BlockPos[0]);
	}

	private static final BlockPos[] LIVINGWOOD_POSITIONS = getLivingwoodPositions();

	private static final BlockPos[] GLIMMERING_LIVINGWOOD_POSITIONS = {
			new BlockPos(-ConfigHandler.elfPortalSize, ConfigHandler.elfPortalSize, 0),
			new BlockPos(ConfigHandler.elfPortalSize, ConfigHandler.elfPortalSize, 0),
			new BlockPos(0, ConfigHandler.elfPortalSize * 2, 0)
	};

	private static BlockPos[] getAirPositions() {
		List<BlockPos> positions = new ArrayList<>();
		for (int i = 1 - ConfigHandler.elfPortalSize; i < ConfigHandler.elfPortalSize; i++) {
			for (int j = 1; j < 2 * ConfigHandler.elfPortalSize; j++) {
				positions.add(new BlockPos(i, j, 0));
			}
		}
		return positions.toArray(new BlockPos[0]);
	}

	private static final BlockPos[] AIR_POSITIONS = getAirPositions();

	private static final String TAG_TICKS_OPEN = "ticksOpen";
	private static final String TAG_TICKS_SINCE_LAST_ITEM = "ticksSinceLastItem";
	private static final String TAG_STACK_COUNT = "stackCount";
	private static final String TAG_STACK = "portalStack";
	// Because our itemStack size may exceed 128, we need to write it separately
	private static final String TAG_STACK_SIZE = "portalStackCount";
	private static final String TAG_PORTAL_FLAG = "_elvenPortal";
	// This does not need to be persisted for server restarts, as the normal state means inserting the item resets the timer
	// and the worst thing that can happen is having to wait 5 extra ticks for the item
	private boolean hadRecipe = false;

	/**
	 * An extension of ArrayList that implements methods to manage ItemStacks within the ArrayList simultaneously
	 */
    public class AlfPortalInputs extends ArrayList<ItemStack> {
		/**
		 * Adds {@link ItemStack#getCount() stack.getCount} to a {@link ItemStack#isItemEqual(ItemStack) matching} stack within the ArrayList
		 * <p>If no match is found, it is added as a new element on the end</p>
		 * @param stack item to be added to either existing stacks or as a new element on the end
		 * @return true if added to an existing item, returns false if a new element was created instead
		 */
		@Override
		public boolean add(ItemStack stack) {
			if(stack.getItem() == ModItems.lexicon) {
				ItemStack stackCopy = stack.copy();
				super.add(stackCopy);
				return false;
			}
			for (ItemStack stackIn : this) {
				if (stackIn.isItemEqual(stack)) {
					stackIn.setCount(stackIn.getCount() + stack.getCount());
					return true;
				}
			}
			ItemStack stackCopy = stack.copy();
			super.add(stackCopy);
			return false;
		}

		/**
		 * Removes {@link ItemStack#getCount() stack.getCount} from a {@link AlfPortalInputs#getItemPosition(ItemStack) matching} stack within the ArrayList,
		 * or deletes the item from the array if it reaches 0
		 * <p style="font-size:0.8em; font-style:italic;">Note that this uses a different comparison method to {@link AlfPortalInputs#add(ItemStack)}</p>
		 * @param stack item to be removed from existing stacks
		 * @return true if removed successfully, returns false if it was unsuccessful
		 */
		public boolean remove(ItemStack stack) {
			int index = getItemPosition(stack);
			if (index >= this.size() || this.get(index).getCount() < stack.getCount()) {
				Botania.LOGGER.error("Botania-CEU Alfheim Portal tried to remove more items than were present in the queue \n" +
						"Internal error! Should not occur, if you ever see this, please open an issue at github.com/TeamDimensional/Botania-CEU \n" +
						"(note that this is not the original 1.12 version by Vazkii)");
				return false;
			}
			else if (this.get(index).getCount() - stack.getCount() == 0)
				super.remove(index);
			else
				this.get(index).setCount(this.get(index).getCount() - stack.getCount());
			return true;
		}

		/**
		 * @return index in the ArrayList that the requested ItemStack is at, or INT_MAX if it could not be found
		 */
		public int getItemPosition(ItemStack stack) {
			for(int i = 0; i < this.size(); i++) {
				ItemStack input = this.get(i);
				if (stack.getItem() == input.getItem() && stack.getItemDamage() == input.getItemDamage()) {
					return i;
				}
			}
			Botania.LOGGER.error("Could not find index of requested item in TileAlfPortal block, returning index of 0 instead");
			return Integer.MAX_VALUE;
		}
	}

	private final AlfPortalInputs stacksIn = new AlfPortalInputs();

	public int ticksOpen = 0;
	private int ticksSinceLastItem = 0;
	private List<BlockPos> pylonCache = new ArrayList<>();
	private boolean closeNow = false;
	private boolean explode = false;

	private static final Function<BlockPos, BlockPos> CONVERTER_X_Z = input -> new BlockPos(input.getZ(), input.getY(), input.getX());

	private static final Function<double[], double[]> CONVERTER_X_Z_FP = input -> new double[] { input[2], input[1], input[0] };

	private static final Function<BlockPos, BlockPos> CONVERTER_Z_SWAP = input -> new BlockPos(input.getX(), input.getY(), -input.getZ());

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(BlockPos l : LIVINGWOOD_POSITIONS)
			mb.addComponent(l.up(), ModBlocks.livingwood.getDefaultState());
		for(BlockPos g : GLIMMERING_LIVINGWOOD_POSITIONS)
			mb.addComponent(g.up(), ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.GLIMMERING));

		mb.addComponent(new BlockPos(0, 1, 0), ModBlocks.alfPortal.getDefaultState());
		mb.setRenderOffset(new BlockPos(0, -1, 0));

		return mb.makeSet();
	}

	private boolean checkSpecialCapabilities(ItemStack item) {
		if (ConfigHandler.elfPortalExplosion && item.getItem() == Items.BREAD) {
			explode = true;
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		IBlockState iBlockState = world.getBlockState(getPos());
		if(iBlockState.getValue(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.OFF) {
			ticksOpen = 0;
			return;
		}
		AlfPortalState state = iBlockState.getValue(BotaniaStateProps.ALFPORTAL_STATE);
		AlfPortalState newState = getValidState();

		ticksOpen++;

		AxisAlignedBB aabb = getPortalAABB();
		boolean open = ticksOpen > 60;
		ElvenPortalUpdateEvent event = new ElvenPortalUpdateEvent(this, aabb, open, stacksIn);
		MinecraftForge.EVENT_BUS.post(event);

		if(ticksOpen > 60) {
			ticksSinceLastItem++;
			if(ConfigHandler.elfPortalParticlesEnabled)
				blockParticle(state);

			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, aabb);
			if(!world.isRemote)
				for (EntityItem item : items) {
					if (item.isDead)
						continue;

					ItemStack stack = item.getItem();
					boolean consume;
					if (item.getEntityData().hasKey(TAG_PORTAL_FLAG)) {
						consume = false;
					} else if (stack.getItem() instanceof ItemLexicon) {
						consume = true;
					} else if (checkSpecialCapabilities(stack)) {
						consume = true;
					} else if ((!(stack.getItem() instanceof IElvenItem) || !((IElvenItem) stack.getItem()).isElvenItem(stack))) {
						consume = true;
					} else {
						consume = false;
					}

					if (consume) {
						item.setDead();
						if (validateItemUsage(stack)) {
							addItem(stack);
						}
						if (!hadRecipe) {
							ticksSinceLastItem = 0;
						}
					}
				}
			if(ticksSinceLastItem >= 5) {
				if(!world.isRemote) {
					resolveRecipes();
				}
			}
		}

		if(closeNow) {
			world.setBlockState(getPos(), ModBlocks.alfPortal.getDefaultState(), 1 | 2);
			for(int i = 0; i < 36; i++)
				blockParticle(state);
			closeNow = false;
		} else if(newState != state) {
			if(newState == AlfPortalState.OFF)
				for(int i = 0; i < 36; i++)
					blockParticle(state);
			world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.ALFPORTAL_STATE, newState), 1 | 2);
		} else if(explode) {
			world.createExplosion(null, pos.getX() + .5, pos.getY() + 2.0, pos.getZ() + .5, 3f, true);
			explode = false;
		}
	}

	private boolean validateItemUsage(ItemStack inputStack) {
		if (stacksIn.size() >= 1000) { // even the most extreme estimates of 1kb per itemstack, this comes nowhere near a chunk ban
			return false;
		}

		if(inputStack.getItem() == ModItems.lexicon)
			return true;

		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			for(Object o : recipe.getInputs()) {
				if(o instanceof String) {
					for(ItemStack target : OreDictionary.getOres((String) o)) {
						if(OreDictionary.itemMatches(target, inputStack, false))
							return true;
					}
				} else if(o instanceof ItemStack) {
					ItemStack target = (ItemStack) o;
					if(inputStack.getItem() == target.getItem() && inputStack.getItemDamage() == target.getItemDamage())
						return true;
				}
			}
		}

		return false;
	}

	private void blockParticle(AlfPortalState state) {
		int i = world.rand.nextInt(AIR_POSITIONS.length);
		double[] pos = new double[] {
				AIR_POSITIONS[i].getX() + 0.5F, AIR_POSITIONS[i].getY() + 0.5F, AIR_POSITIONS[i].getZ() + 0.5F
		};
		if(state == AlfPortalState.ON_X)
			pos = CONVERTER_X_Z_FP.apply(pos);

		float motionMul = 0.2F;
		Botania.proxy.wispFX(getPos().getX() + pos[0], getPos().getY() + pos[1], getPos().getZ() + pos[2], (float) (Math.random() * 0.25F), (float) (Math.random() * 0.5F + 0.5F), (float) (Math.random() * 0.25F), (float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
	}

	public boolean onWanded() {
		AlfPortalState state = world.getBlockState(getPos()).getValue(BotaniaStateProps.ALFPORTAL_STATE);
		if(state == AlfPortalState.OFF) {
			AlfPortalState newState = getValidState();
			if(newState != AlfPortalState.OFF) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.ALFPORTAL_STATE, newState), 1 | 2);
				return true;
			}
		}

		return false;
	}

	private AxisAlignedBB getPortalAABB() {
		int minX = 1 - ConfigHandler.elfPortalSize;
		// these 2 are exclusive
		int maxX = ConfigHandler.elfPortalSize;
		int maxY = ConfigHandler.elfPortalSize * 2;
		AxisAlignedBB aabb = new AxisAlignedBB(pos.add(minX, 1, 0), pos.add(maxX, maxY, 1));
		if(world.getBlockState(getPos()).getValue(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.ON_X)
			aabb = new AxisAlignedBB(pos.add(0, 1, minX), pos.add(1, maxY, maxX));

		return aabb;
	}

	private void addItem(ItemStack stack) {
		stacksIn.add(stack.copy());
	}

	private void resolveRecipes() {
		int i = 0;
		for(ItemStack stack : stacksIn) {
			if(!stack.isEmpty() && stack.getItem() instanceof ILexicon) {
				ILexicon lexicon = (ILexicon) stack.getItem();
				if (!lexicon.isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge)) {
					lexicon.unlockKnowledge(stack, BotaniaAPI.elvenKnowledge);
					ItemLexicon.setForcedPage(stack, LexiconData.elvenMessage.getUnlocalizedName());
                }
                spawnItem(stack);
                stacksIn.remove(i);
                return;
            }
			i++;
		}

		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
			List<ItemStack> matches = recipe.getMatches(stacksIn);

			int cumulativeCount = 0;

			for (ItemStack match : matches)
				cumulativeCount += match.getCount();

			if(cumulativeCount == recipe.getInputs().size()) {
				if(consumeMana(500, false)) {
					for(ItemStack match : matches)
						if (!stacksIn.remove(match))
							return;

					for(ItemStack output : recipe.getOutputs())
						spawnItem(output.copy());
				}
				return;
			}
		}

		hadRecipe = false;
	}

	private void spawnItem(ItemStack stack) {
		EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
		item.getEntityData().setBoolean(TAG_PORTAL_FLAG, true);
		world.spawnEntity(item);
		ticksSinceLastItem = 0;
		hadRecipe = true;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound cmp) {
		NBTTagCompound ret = super.writeToNBT(cmp);

		cmp.setInteger(TAG_STACK_COUNT, stacksIn.size());
		int i = 0;
		for(ItemStack stack : stacksIn) {
			NBTTagCompound stackcmp = stack.writeToNBT(new NBTTagCompound());
			cmp.setTag(TAG_STACK + i, stackcmp);
			cmp.setInteger(TAG_STACK_SIZE + i, stack.getCount());
			i++;
		}

		return ret;
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);

		int count = cmp.getInteger(TAG_STACK_COUNT);
		stacksIn.clear();
		for(int i = 0; i < count; i++) {
			NBTTagCompound stackcmp = cmp.getCompoundTag(TAG_STACK + i);
			ItemStack stack = new ItemStack(stackcmp);
			int stackSize = cmp.getInteger(TAG_STACK_SIZE + i);
			// When migrating from an old saved with an old version of Botania, there is no TAG_STACK_SIZE present
			// as all stacks are expected to have a size of 1
			stack.setCount(Math.max(stackSize, 1));
			stacksIn.add(stack);
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TICKS_OPEN, ticksOpen);
		cmp.setInteger(TAG_TICKS_SINCE_LAST_ITEM, ticksSinceLastItem);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		ticksOpen = cmp.getInteger(TAG_TICKS_OPEN);
		ticksSinceLastItem = cmp.getInteger(TAG_TICKS_SINCE_LAST_ITEM);
	}

	private AlfPortalState getValidState() {
		if(checkConverter(null))
			return AlfPortalState.ON_Z;

		if(checkConverter(CONVERTER_X_Z))
			return AlfPortalState.ON_X;

		return AlfPortalState.OFF;
	}

	private boolean checkConverter(Function<BlockPos, BlockPos> baseConverter) {
		return checkMultipleConverters(baseConverter) || checkMultipleConverters(CONVERTER_Z_SWAP, baseConverter);
	}

	@SafeVarargs
	private final boolean checkMultipleConverters(Function<BlockPos, BlockPos>... converters) {
		if(!check2DArray(AIR_POSITIONS, Blocks.AIR.getDefaultState(), true, converters))
			return false;
		if(!check2DArray(LIVINGWOOD_POSITIONS, ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.DEFAULT), false, converters))
			return false;
		if(!check2DArray(GLIMMERING_LIVINGWOOD_POSITIONS, ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.GLIMMERING), false, converters))
			return false;

		lightPylons();
		return true;
	}

	public List<BlockPos> locatePylons() {
		List<BlockPos> list = new ArrayList();
		int range = 5;

		IBlockState pylonState = ModBlocks.pylon.getDefaultState().withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.NATURA);
		IBlockState poolState = ModBlocks.pool.getDefaultState();

		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++)
				for(int k = -range; k < range + 1; k++) {
					BlockPos pos = new BlockPos(i, j, k);
					if(checkPosition(pos, pylonState, false) && checkPosition(pos.down(), poolState, true))
						list.add(pos);
				}

		return list;
	}

	public void lightPylons() {
		if(ticksOpen < 50)
			return;

		if(pylonCache == null || pylonCache.isEmpty() || pylonCache.size() < 2)
			pylonCache = locatePylons();

		for(BlockPos pos : pylonCache) {
			TileEntity tile = world.getTileEntity(getPos().add(pos));
			if(tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerPos = getPos();
			}
		}

		if(ticksOpen == 50)
			consumeMana(200000, true);
	}

	public boolean consumeMana(int totalCost, boolean close) {
		IBlockState pylonState = ModBlocks.pylon.getDefaultState().withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.NATURA);
		IBlockState poolState = ModBlocks.pool.getDefaultState();
		List<TilePool> consumePools = new ArrayList();
		int consumed = 0;

		for (BlockPos pos : pylonCache)
			if (!checkPosition(pos, pylonState, false) || !checkPosition(pos.down(), poolState, true)) {
				pylonCache = locatePylons();
			}

		if(pylonCache.size() < 2) {
			closeNow = true;
			return false;
		}

		int costPer = Math.max(1, totalCost / pylonCache.size());
		int expectedConsumption = costPer * pylonCache.size();

		for(BlockPos pos : pylonCache) {
			TileEntity tile = world.getTileEntity(getPos().add(pos));
			if(tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerPos = getPos();
			}

			tile = world.getTileEntity(getPos().add(pos).down());
			if(tile instanceof TilePool) {
				TilePool pool = (TilePool) tile;

				if(pool.getCurrentMana() < costPer) {
					closeNow = closeNow || close;
					return false;
				} else if(!world.isRemote) {
					consumePools.add(pool);
					consumed += costPer;
				}
			}
		}

		if(consumed >= expectedConsumption) {
			for(TilePool pool : consumePools)
				pool.recieveMana(-costPer);
			return true;
		}

		return false;
	}

	@SafeVarargs
	private final boolean check2DArray(BlockPos[] positions, IBlockState state, boolean onlyCheckBlock, Function<BlockPos, BlockPos>... converters) {
		for(BlockPos pos : positions) {
			for(Function<BlockPos, BlockPos> f : converters)
				if(f != null)
					pos = f.apply(pos);

			if(!checkPosition(pos, state, onlyCheckBlock))
				return false;
		}

		return true;
	}

	private boolean checkPosition(BlockPos pos, IBlockState state, boolean onlyCheckBlock) {
		BlockPos pos_ = getPos().add(pos);

		IBlockState stateat = world.getBlockState(pos_);
		Block blockat = stateat.getBlock();

		if(state.getBlock() == Blocks.AIR ? blockat.isAir(stateat, world, pos_) : blockat == state.getBlock())
			return onlyCheckBlock || stateat == state;

		return false;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
