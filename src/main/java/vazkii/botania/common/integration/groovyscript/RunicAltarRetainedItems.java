package vazkii.botania.common.integration.groovyscript;

import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.InventoryHelper;

@RegistryDescription
public class RunicAltarRetainedItems extends VirtualizedRegistry<String> {

    @Override
    public void onReload() {
        removeScripted().forEach(ConfigHandler.runicAltarRetainedItemsSet::remove);
        restoreFromBackup().forEach(ConfigHandler.runicAltarRetainedItemsSet::add);
    }

    @MethodDescription(example = @Example("item('botania:manaresource')"), type = MethodDescription.Type.ADDITION)
    public void add(ItemStack catalyst) {
        String recipe = InventoryHelper.stringifyStack(catalyst);
        addScripted(recipe);
        ConfigHandler.runicAltarCatalystsSet.add(recipe);
    }

    @MethodDescription(example = @Example("item('botania:rune', 1)"), type = MethodDescription.Type.REMOVAL)
    public boolean remove(ItemStack catalyst) {
        String recipe = InventoryHelper.stringifyStack(catalyst);
        addBackup(recipe);
        return ConfigHandler.runicAltarCatalystsSet.remove(recipe);
    }
}
