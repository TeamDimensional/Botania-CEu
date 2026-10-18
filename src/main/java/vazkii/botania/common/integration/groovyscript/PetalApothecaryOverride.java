/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.integration.groovyscript;

import java.util.List;
import java.util.Locale;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.Comp;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.Property;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.compat.mods.botania.Apothecary;
import com.cleanroommc.groovyscript.helper.Alias;

import vazkii.botania.common.core.handler.ConfigHandler;

@RegistryDescription
public class PetalApothecaryOverride extends Apothecary {

    @Override
    public List<String> getAliases() {
        return Alias.generateOfClass(Apothecary.class);
    }

    @Override
    public String getName() {
        return this.getAliases().get(0).toLowerCase(Locale.ENGLISH);
    }

    @RecipeBuilderDescription(example = @Example(".input(ore('blockGold'), ore('ingotIron'), item('minecraft:apple')).output(item('minecraft:golden_apple'))"))
    @Override
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilderOverride();
    }

    @Property(property = "input", comp = @Comp(gte = 1))
    public class RecipeBuilderOverride extends Apothecary.RecipeBuilder {

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg, 1, ConfigHandler.petalApothecaryCapacity, 1, 1);
        }

    }

}