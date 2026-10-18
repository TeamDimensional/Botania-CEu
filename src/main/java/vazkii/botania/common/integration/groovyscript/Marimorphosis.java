/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.integration.groovyscript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.Comp;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.Property;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderMethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderRegistrationMethod;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.StandardListRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeMarimorphosis;

@RegistryDescription
public class Marimorphosis extends StandardListRegistry<RecipeMarimorphosis> {

    @Override
    public Collection<RecipeMarimorphosis> getRecipes() {
        return BotaniaAPI.marimorphosisRecipes;
    }

    @MethodDescription(example = @Example("blockstate('minecraft:stone')"))
    public boolean removeByInput(IBlockState input) {
        if (getRecipes().removeIf(recipe -> {
            boolean found = recipe.inputMatcher.test(input);
            if (found)
                addBackup(recipe);
            return found;
        }))
            return true;

        GroovyLog.msg("Error removing Botania Marimorphosis recipe")
                .add("could not find recipe with input {}", input)
                .error()
                .post();
        return false;
    }

    @MethodDescription
    public boolean removeByInput(Block input) {
        return removeByInput(input.getDefaultState());
    }

    @RecipeBuilderDescription(example = {
            @Example(".input(blockstate('minecraft:dirt')).forest(blockstate('botania:biomestonea:variant=forest')).plains(blockstate('botania:biomestonea:variant=plains')).mountain(blockstate('minecraft:emerald_block')).mushroom(blockstate('botania:biomestonea:variant=fungal')).swamp(blockstate('botania:biomestonea:variant=swamp')).desert(blockstate('minecraft:sand')).cold(blockstate('minecraft:diamond_block')).mesa(blockstate('minecraft:gold_block'))"),
            @Example(".input(blockstate('minecraft:cobblestone')).allBiomes(blockstate('minecraft:tnt')).biomeMultiplier(0)")
    })
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    public class RecipeBuilder extends AbstractRecipeBuilder<RecipeMarimorphosis> {

        @Property(ignoresInheritedMethods = true, comp = @Comp(gte = 1))
        protected final List<IBlockState> input = new ArrayList<>();
        @Property(ignoresInheritedMethods = true, comp = @Comp(eq = 8))
        protected final IBlockState[] outputs = new IBlockState[RecipeMarimorphosis.TYPES.length];
        @Property(defaultValue = "12", comp = @Comp(gte = 0))
        protected int biomeMultiplier = 12;

        @RecipeBuilderMethodDescription
        public RecipeBuilder input(IBlockState input) {
            this.input.add(input);
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder input(Block input) {
            for (IBlockState state : input.getBlockState().getValidStates())
                this.input.add(state);
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder biomeMultiplier(int biomeMultiplier) {
            this.biomeMultiplier = biomeMultiplier;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder allBiomes(IBlockState output) {
            Arrays.fill(outputs, output);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder output(int index, IBlockState output) {
            outputs[index] = output;
            return this;
        }

        // The indexes match the order of RecipeMarimorphosis.TYPES:
        // Forest, Plains, Mountain, Mushroom, Swamp, Desert (SANDY), Cold, Mesa

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder forest(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.FOREST), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder plains(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.PLAINS), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder mountain(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.MOUNTAIN), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder mushroom(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.MUSHROOM), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder swamp(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.SWAMP), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder desert(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.SANDY), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder cold(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.COLD), output);
        }

        @RecipeBuilderMethodDescription(field = "outputs")
        public RecipeBuilder mesa(IBlockState output) {
            return output(RecipeMarimorphosis.TYPE_INDEXES.get(Type.MESA), output);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public RecipeMarimorphosis register() {
            if (!validate())
                return null;

            RecipeMarimorphosis recipe = BotaniaAPI.registerMarimorphosisRecipe(input.toArray(new IBlockState[0]),
                    outputs, biomeMultiplier);
            addScripted(recipe);
            return recipe;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding Botania Marimorphosis recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateItems(msg);
            validateFluids(msg);
            msg.add(input.isEmpty(), "At least one input blockstate must be defined");
            msg.add(input.contains(null), "Input blockstates must not be null");
            for (int i = 0; i < outputs.length; i++)
                msg.add(outputs[i] == null, "Output for {} biome must not be null", RecipeMarimorphosis.TYPES[i]);
            msg.add(biomeMultiplier < 0, "Biome multiplier must be at least 0, got {}", biomeMultiplier);
        }

    }

}
