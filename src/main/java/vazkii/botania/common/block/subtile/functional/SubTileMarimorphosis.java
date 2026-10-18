/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 29, 2015, 8:17:55 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;

public class SubTileMarimorphosis extends SubTileFunctional {

	private static final int COST = 12;
	private static final int RANGE = 8;
	private static final int RANGE_Y = 5;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	static class MarimorphosisChoice {
		final BlockPos target;
		final IBlockState blockOutput;

		MarimorphosisChoice(BlockPos target, IBlockState blockOutput) {
			this.target = target;
			this.blockOutput = blockOutput;
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(supertile.getWorld().isRemote)
			return;

		if(ticksExisted % 2 == 0 && mana >= COST) {
			if(refreshRedstoneSignal() > 0)
				return;

			MarimorphosisChoice choice = makeChoice();
			if(choice != null) {
				supertile.getWorld().setBlockState(choice.target, choice.blockOutput);
				if(ConfigHandler.blockBreakParticles)
					supertile.getWorld().playEvent(2001, choice.target, Block.getStateId(choice.blockOutput));

				mana -= COST;
				sync();
			}
		}
	}

	private MarimorphosisChoice makeChoice() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-range, -rangeY, -range), getPos().add(range, rangeY, range))) {
			IBlockState state = supertile.getWorld().getBlockState(pos);
			if(BotaniaAPI.getMarimorphosisRecipe(state) != null)
				possibleCoords.add(pos);
		}

		if(possibleCoords.isEmpty())
			return null;
		BlockPos target = possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
		IBlockState state = supertile.getWorld().getBlockState(target);
		IBlockState output = BotaniaAPI.getMarimorphosisRecipe(state).getOutput(supertile.getWorld().rand, supertile.getWorld().getBiome(target));
		if (output == null)
			return null;
		return new MarimorphosisChoice(target, output);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
	}

	@Override
	public int getColor() {
		return 0x769897;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	protected boolean shouldPollRedstone() {
		return false;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.marimorphosis;
	}

	public static class Mini extends SubTileMarimorphosis {
		@Override public int getRange() { return RANGE_MINI; }
		@Override public int getRangeY() { return RANGE_Y_MINI; }
	}

}
