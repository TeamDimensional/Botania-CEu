package vazkii.botania.common.integration.groovyscript;

import java.util.List;
import java.util.Locale;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.Comp;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.Property;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.compat.mods.botania.BrewRecipe;
import com.cleanroommc.groovyscript.helper.Alias;

import vazkii.botania.common.core.handler.ConfigHandler;

@RegistryDescription
public class BreweryOverride extends BrewRecipe {

    @Override
    public List<String> getAliases() {
        return Alias.generateOfClass(BrewRecipe.class);
    }

    @Override
    public String getName() {
        return this.getAliases().get(0).toLowerCase(Locale.ENGLISH);
    }

    @RecipeBuilderDescription(example = @Example(".input(item('minecraft:clay'), ore('ingotGold'), ore('gemDiamond')).brew(brew('absorption'))"))
    @Override
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilderOverride();
    }

    @Property(property = "input", comp = @Comp(gte = 1))
    public class RecipeBuilderOverride extends BrewRecipe.RecipeBuilder {

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg, 1, ConfigHandler.breweryCapacity, 0, 0);
            msg.add(brew == null, "Expected a valid output brew, got " + brew);
        }

    }

}