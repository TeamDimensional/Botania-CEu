/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.recipe;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;

public class RecipeMarimorphosis {
    private static class MarimorphosisItem extends WeightedRandom.Item {
        public MarimorphosisItem(int itemWeightIn, IBlockState output) {
			super(itemWeightIn);
			this.output = output;
		}

		final IBlockState output;
    }

    public final Predicate<IBlockState> inputMatcher;
    public final IBlockState[] inputs;
    public final IBlockState[] outputs;
    public final int biomeMultiplier;

	public static final Type[] TYPES = new Type[] {
			Type.FOREST,
			Type.PLAINS,
			Type.MOUNTAIN,
			Type.MUSHROOM,
			Type.SWAMP,
			Type.SANDY,
			Type.COLD,
			Type.MESA
	};

    public RecipeMarimorphosis(@Nonnull Predicate<IBlockState> input, @Nonnull IBlockState[] inputs, @Nonnull IBlockState[] output, int biomeMultiplier) {
        if (output.length != TYPES.length || Arrays.stream(output).anyMatch(x -> x == null)) {
            throw new IllegalArgumentException("There should be an item for every biome, and there should not be nulls");
        }
        if (Arrays.stream(inputs).anyMatch(x -> x == null)) {
            throw new IllegalArgumentException("The input blockstates should not be nulls");
        }
        if (biomeMultiplier < 0) {
            throw new IllegalArgumentException("Marimorphosis's biome multiplier should be > 1");
        }
        this.inputMatcher = input;
        this.outputs = output;
        this.biomeMultiplier = biomeMultiplier;
        this.inputs = inputs;
    }

    public IBlockState getOutput(Random random, Biome biome) {
        List<MarimorphosisItem> items = new ArrayList<>();
        Set<Type> types = BiomeDictionary.getTypes(biome);
        int goodBiome = biomeMultiplier == 0 ? 1 : biomeMultiplier;
        int badBiome = biomeMultiplier == 0 ? 0 : 1;
        for (int i = 0; i < TYPES.length; ++i) {
            items.add(new MarimorphosisItem(types.contains(TYPES[i]) ? goodBiome : badBiome, outputs[i]));
        }
        return WeightedRandom.getRandomItem(random, items).output;
    }
}
