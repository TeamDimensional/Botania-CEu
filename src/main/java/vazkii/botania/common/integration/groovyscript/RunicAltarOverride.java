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
import com.cleanroommc.groovyscript.compat.mods.botania.RuneAltar;
import com.cleanroommc.groovyscript.helper.Alias;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.InventoryHelper;

@RegistryDescription
public class RunicAltarOverride extends RuneAltar {

    @RecipeBuilderDescription(example = @Example(".input(ore('gemEmerald'), item('minecraft:apple')).output(item('minecraft:diamond')).mana(500)"))
    @Override
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilderUpgraded();
    }

    @Override
    public List<String> getAliases() {
        return Alias.generateOfClass(RuneAltar.class);
    }

    @Override
    public String getName() {
        return this.getAliases().get(0).toLowerCase(Locale.ENGLISH);
    }

    @Property(property = "input", comp = @Comp(gte = 1, unique = "groovyscript.wiki.botania_ceu.rune_altar.input.required"))
    @Property(property = "output", comp = @Comp(gte = 1, lte = 1))
    public class RecipeBuilderUpgraded extends RuneAltar.RecipeBuilder {

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg, 1, ConfigHandler.runicAltarCapacity, 1, 1);
            msg.add(
                    input.stream()
                            .anyMatch(x -> ConfigHandler.runicAltarCatalystsSet.stream()
                                    .anyMatch(y -> x.test(InventoryHelper.destringifyStack(y)))),
                    "input cannot contain a runic altar catalyst item");
            msg.add(mana < 1, "mana must be at least 1, got " + mana);
        }
    }

}
