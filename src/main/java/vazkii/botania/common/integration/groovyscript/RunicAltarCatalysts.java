package vazkii.botania.common.integration.groovyscript;

import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.InventoryHelper;

@RegistryDescription
public class RunicAltarCatalysts extends VirtualizedRegistry<String> {

    @Override
    public void onReload() {
        removeScripted().forEach(ConfigHandler.runicAltarCatalystsSet::remove);
        restoreFromBackup().forEach(ConfigHandler.runicAltarCatalystsSet::add);
    }

    @MethodDescription(example = @Example("item('minecraft:gold_block')"), type = MethodDescription.Type.ADDITION)
    public void add(ItemStack catalyst) {
        String recipe = InventoryHelper.stringifyStack(catalyst);
        addScripted(recipe);
        ConfigHandler.runicAltarCatalystsSet.add(recipe);
    }

    @MethodDescription(example = @Example("item('botania:livingrock')"), type = MethodDescription.Type.REMOVAL)
    public boolean remove(ItemStack catalyst) {
        String recipe = InventoryHelper.stringifyStack(catalyst);
        addBackup(recipe);
        return ConfigHandler.runicAltarCatalystsSet.remove(recipe);
    }

}
