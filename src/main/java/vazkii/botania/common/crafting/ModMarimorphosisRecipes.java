/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.crafting;

import java.util.stream.IntStream;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.common.block.ModFluffBlocks;

public class ModMarimorphosisRecipes {
    public static void init() {
        IBlockState[] outputs = IntStream.range(0, 8)
            .mapToObj(x -> ModFluffBlocks.biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, BiomeStoneVariant.values()[x]))
            .toArray(IBlockState[]::new);
        BotaniaAPI.registerMarimorphosisRecipe(BlockStateMatcher.forBlock(Blocks.STONE), outputs, 12);
    }
}
