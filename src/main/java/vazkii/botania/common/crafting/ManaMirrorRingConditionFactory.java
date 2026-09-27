package vazkii.botania.common.crafting;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import vazkii.botania.common.core.handler.ConfigHandler;

public class ManaMirrorRingConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		boolean value = JsonUtils.getBoolean(json, "value", false);
		return () -> value == (ConfigHandler.baubles.manaMirrorBauble == 1);
	}
}
