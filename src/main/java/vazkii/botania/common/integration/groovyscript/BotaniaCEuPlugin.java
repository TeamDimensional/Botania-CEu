package vazkii.botania.common.integration.groovyscript;

import java.util.ArrayList;
import java.util.Collection;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import vazkii.botania.common.lib.LibMisc;

public class BotaniaCEuPlugin implements GroovyPlugin {

    public static BotaniaCEuAdditions get() {
        return new BotaniaCEuAdditions();
    }

    @Override
    public String getContainerName() {
        return LibMisc.MOD_NAME;
    }

    @Override
    public String getModId() {
        return LibMisc.MOD_ID;
    }

    @Override
    public GroovyPropertyContainer createGroovyPropertyContainer() {
        return get();
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> arg0) {
    }

    @Override
    public @NotNull Priority getOverridePriority() {
        return Priority.OVERRIDE;
    }

    @Override
    public Collection<String> getAliases() {
        Collection<String> info = new ArrayList<>();
        info.add(LibMisc.MOD_NAME);
        info.add("botania_ceu");
        return info;
    }

}
