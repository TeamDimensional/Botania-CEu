/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.integration.groovyscript;

import java.util.Collection;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.GroovyLog.Msg;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.Comp;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.Property;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderMethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderRegistrationMethod;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.StandardListRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeTerrestrialAgglomeration;
import vazkii.botania.common.block.ModBlocks;

@RegistryDescription
public class TerrestrialAgglomerationPlate extends StandardListRegistry<RecipeTerrestrialAgglomeration> {

    @Override
    public Collection<RecipeTerrestrialAgglomeration> getRecipes() {
        return BotaniaAPI.terraPlateRecipes;
    }

    @MethodDescription(example = @Example("item('botania:manaresource', 4)"))
    public boolean removeByOutput(IIngredient output) {
        if (getRecipes().removeIf(recipe -> {
            boolean found = output.test(recipe.getRecipeOutputCopy());
            if (found)
                addBackup(recipe);
            return found;
        }))
            return true;

        GroovyLog.msg("Error removing Botania Terrestrial Agglomeration Plate recipe")
                .add("could not find recipe with output {}", output)
                .error()
                .post();
        return false;
    }

    @RecipeBuilderDescription(example = {
            @Example(".input(item('minecraft:iron_ingot'), ore('ingotManasteel'), item('minecraft:stone') * 2).output(item('minecraft:diamond')).mana(150000).colorStart(0xFF0000).colorEnd(0xFFFF00)"),
            @Example(".input(item('minecraft:diamond')).output(item('minecraft:dirt')).mana(1000000).centerBlock(blockstate('minecraft:dirt')).centerBlockReplacement(blockstate('minecraft:diamond_block'))")
    })
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    @Property(property = "input", comp = @Comp(gte = 1))
    @Property(property = "output", comp = @Comp(eq = 1))
    public class RecipeBuilder extends AbstractRecipeBuilder<RecipeTerrestrialAgglomeration> {

        @Property(comp = @Comp(gte = 1))
        protected int mana = 0;
        @Property(defaultValue = "0x0000FF")
        protected int inputColor = 0x0000FF;
        @Property(defaultValue = "0x00FF00")
        protected int outputColor = 0x00FF00;
        @Property()
        protected final IBlockState[] blocks = new IBlockState[] {
                ModBlocks.livingrock.getDefaultState(),
                Blocks.LAPIS_BLOCK.getDefaultState(),
                ModBlocks.livingrock.getDefaultState(),
                null, null, null
        };

        @RecipeBuilderMethodDescription
        public RecipeBuilder mana(int mana) {
            this.mana = mana;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "inputColor")
        public RecipeBuilder colorStart(int color) {
            this.inputColor = color;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputColor")
        public RecipeBuilder colorEnd(int color) {
            this.outputColor = color;
            return this;
        }

        private RecipeBuilder replaceBlock(int i, IBlockState block) {
            blocks[i] = block;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "blocks")
        public RecipeBuilder centerBlock(IBlockState input) {
            return replaceBlock(0, input);
        }

        @RecipeBuilderMethodDescription(field = "blocks")
        public RecipeBuilder edgeBlock(IBlockState input) {
            return replaceBlock(1, input);
        }

        @RecipeBuilderMethodDescription(field = "blocks")
        public RecipeBuilder cornerBlock(IBlockState input) {
            return replaceBlock(2, input);
        }

        @RecipeBuilderMethodDescription(field = "blocks")
        public RecipeBuilder centerBlockReplacement(IBlockState input) {
            return replaceBlock(3, input);
        }

        @RecipeBuilderMethodDescription(field = "blocks")
        public RecipeBuilder edgeBlockReplacement(IBlockState input) {
            return replaceBlock(4, input);
        }

        @RecipeBuilderMethodDescription(field = "blocks")
        public RecipeBuilder cornerBlockReplacement(IBlockState input) {
            return replaceBlock(5, input);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public RecipeTerrestrialAgglomeration register() {
            RecipeTerrestrialAgglomeration recipe = BotaniaAPI.registerTerraPlateRecipe(mana, inputColor, outputColor,
                    output.get(0), input.stream().map(x -> {
                        if (x instanceof OreDictIngredient) {
                            return ((OreDictIngredient) x).getOreDict();
                        }
                        return x.getFirst();
                    }).toArray(), blocks);
            addScripted(recipe);
            return recipe;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding Botania Terrestrial Agglomeration Plate recipe";
        }

        @Override
        public void validate(Msg msg) {
            validateItems(msg, 1, Integer.MAX_VALUE, 1, 1);
            validateFluids(msg);
            msg.add(mana <= 0, "Mana amount must be positive");
            msg.add(blocks[0] == null, "Center blockstate must not be null");
            msg.add(blocks[1] == null, "Edge blockstate must not be null");
            msg.add(blocks[2] == null, "Corner blockstate must not be null");
            msg.add(input.stream()
                    .anyMatch(x -> !(x instanceof OreDictIngredient) && x.getMatchingStacks().length != 1),
                    "All non-ore ingredients must only match 1 item");
        }

    }

}
