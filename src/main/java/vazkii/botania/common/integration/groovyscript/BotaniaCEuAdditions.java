/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.botania.Apothecary;
import com.cleanroommc.groovyscript.compat.mods.botania.Botania;
import com.cleanroommc.groovyscript.compat.mods.botania.BrewRecipe;
import com.cleanroommc.groovyscript.compat.mods.botania.RuneAltar;

public class BotaniaCEuAdditions extends Botania {

    public final RunicAltarCatalysts runeAltarCatalysts = new RunicAltarCatalysts();
    public final RunicAltarRetainedItems runeAltarRetained = new RunicAltarRetainedItems();
    public final PetalApothecaryCatalysts petalApothecaryCatalysts = new PetalApothecaryCatalysts();
    public final TerrestrialAgglomerationPlate agglomeration = new TerrestrialAgglomerationPlate();
    public final RuneAltar runeAltar = new RunicAltarOverride();
    public final BrewRecipe brewRecipe = new BreweryOverride();
    public final Apothecary apothecary = new PetalApothecaryOverride();

    @Override
    public void initialize(GroovyContainer<?> owner) {
        super.initialize(owner);
        addProperty(runeAltar);
        addProperty(brewRecipe);
        addProperty(apothecary);
    }

}
