/**
 * This class was created by <wizzerinus>. It's distributed as
 * part of the Botania CEu Mod. Get the Source Code in github:
 * https://github.com/TeamDimensional/Botania-CEU
 *
 * Botania CEu is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.integration.groovyscript;

import com.cleanroommc.groovyscript.api.documentation.annotations.Admonition;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.InventoryHelper;

@RegistryDescription(admonition = @Admonition(value = "groovyscript.wiki.botania.petal_apothecary_catalysts.note0", type = Admonition.Type.WARNING))
public class PetalApothecaryCatalysts extends VirtualizedRegistry<String> {

    @Override
    public void onReload() {
        removeScripted().forEach(ConfigHandler.petalApothecaryCatalystsSet::remove);
        restoreFromBackup().forEach(ConfigHandler.petalApothecaryCatalystsSet::add);
    }

    @MethodDescription(example = @Example("item('minecraft:sapling')"), type = MethodDescription.Type.ADDITION)
    public void add(ItemStack catalyst) {
        String recipe = InventoryHelper.stringifyStack(catalyst);
        addScripted(recipe);
        ConfigHandler.petalApothecaryCatalystsSet.add(recipe);
    }

    @MethodDescription(example = {})
    public boolean remove(ItemStack catalyst) {
        String recipe = InventoryHelper.stringifyStack(catalyst);
        addBackup(recipe);
        return ConfigHandler.petalApothecaryCatalystsSet.remove(recipe);
    }
}
