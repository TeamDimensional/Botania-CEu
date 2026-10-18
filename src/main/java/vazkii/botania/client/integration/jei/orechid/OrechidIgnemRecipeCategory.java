/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.IGuiHelper;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class OrechidIgnemRecipeCategory extends OrechidRecipeCategory<OrechidIgnemRecipeWrapper> {

	public static final String UID = "botania.orechid_ignem";

	public OrechidIgnemRecipeCategory(IGuiHelper guiHelper, int outputCount) {
		super(guiHelper, outputCount, "botania.nei.orechidIgnem", LibBlockNames.SUBTILE_ORECHID_IGNEM);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}
}
