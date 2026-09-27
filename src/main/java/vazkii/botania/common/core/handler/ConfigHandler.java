/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 13, 2014, 9:01:32 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lib.LibMisc;

@Config(modid = LibMisc.MOD_ID)
public final class ConfigHandler {

	@Config.Name("shaders.enabled")
	@Config.LangKey("botania.config.useShaders")
	@Config.Comment("Set this to false to disable the use of shaders for some of the mod's renders.")
	public static boolean useShaders = true;

	@Config.Name("lexicon.enable.rotatingItems")
	@Config.LangKey("botania.config.lexiconRotatingItems")
	@Config.Comment("Set this to false to disable the rotating items in the petal and rune entries in the Lexica Botania.")
	public static boolean lexiconRotatingItems = true;

	@Config.Name("lexicon.enable.justifiedText")
	@Config.LangKey("botania.config.lexiconJustifiedText")
	@Config.Comment("Set this to true to enable justified text in the Lexica Botania's text pages.")
	public static boolean lexiconJustifiedText = false;

	@Config.Name("powerSystem.subtle")
	@Config.LangKey("botania.config.subtlePowerSystem")
	@Config.Comment("Set this to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.")
	public static boolean subtlePowerSystem = false;

	@Config.Name("wandBeam.static")
	@Config.LangKey("botania.config.staticWandBeam")
	@Config.Comment("Set this to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old Botania versions. Warning: Disabled by default because it may be laggy.")
	public static boolean staticWandBeam = false;

	@Config.Name("boundBlock.wireframe.enabled")
	@Config.LangKey("botania.config.boundBlockWireframe")
	@Config.Comment("Set this to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).")
	public static boolean boundBlockWireframe = true;

	@Config.Name("lexicon.render.3D")
	@Config.LangKey("botania.config.lexicon3dModel")
	@Config.Comment("Set this to false to disable the animated 3D render for the Lexica Botania.")
	public static boolean lexicon3dModel = true;

	@Config.Name("flowerParticles.frequency")
	@Config.LangKey("botania.config.flowerParticleFrequency")
	@Config.Comment("The frequency in which particles spawn from normal (worldgen) mystical flowers")
	public static double flowerParticleFrequency = 0.75F;

	@Config.Name("blockBreakingParticles.enabled")
	@Config.LangKey("botania.config.blockBreakParticles")
	@Config.Comment("Set this to false to remove the block breaking particles from the flowers and other items in the mod.")
	public static boolean blockBreakParticles = true;

	@Config.Name("blockBreakingParticlesTool.enabled")
	@Config.LangKey("botania.config.blockBreakParticlesTool")
	@Config.Comment("Set this to false to remove the block breaking particles from the Mana Shatterer, as there can be a good amount in higher levels.")
	public static boolean blockBreakParticlesTool = true;

	@Config.Name("elfPortal.particles.enabled")
	@Config.LangKey("botania.config.elfPortalParticlesEnabled")
	@Config.Comment("Set this to false to disable the particles in the elven portal.")
	public static boolean elfPortalParticlesEnabled = true;

	@Config.Name("chargeAnimation.enabled")
	@Config.LangKey("botania.config.chargingAnimationEnabled")
	@Config.Comment("Set this to false to disable the animation when an item is charging on top of a mana pool.")
	public static boolean chargingAnimationEnabled = true;

	@Config.Name("manaSpreaders.silent")
	@Config.LangKey("botania.config.silentSpreaders")
	@Config.Comment("Set this to true to disable the mana spreader shooting sound.")
	public static boolean silentSpreaders = false;

	@Config.Name("baubleRender.enabled")
	@Config.LangKey("botania.config.renderBaubles")
	@Config.Comment("Set this to false to disable rendering of baubles in the player.")
	public static boolean renderBaubles = true;

	@Config.Name("seasonalFeatures.enabled")
	@Config.LangKey("botania.config.enableSeasonalFeatures")
	@Config.Comment("Set this to false to disable seasonal features, such as halloween and christmas.")
	public static boolean enableSeasonalFeatures = true;

	@Config.Name("quickLookup.useShift")
	@Config.LangKey("botania.config.useShiftForQuickLookup")
	@Config.Comment("Set this to true to use Shift instead of Ctrl for the inventory lexica botania quick lookup feature.")
	public static boolean useShiftForQuickLookup = false;

	@Config.Name("quickLookup.lexicaOfflineMode")
	@Config.LangKey("botania.config.lexicaOfflineMode")
	@Config.Comment("Set this to true to disable the wiki lookup feature of the lexica, for offline usage.")
	public static boolean lexicaOfflineMode = false;

	@Config.Name("armorModels.enable")
	@Config.LangKey("botania.config.enableArmorModels")
	@Config.Comment("Set this to false to disable custom armor models.")
	public static boolean enableArmorModels = true;

	@Config.Name("fancySkybox.enable")
	@Config.LangKey("botania.config.enableFancySkybox")
	@Config.Comment("Set this to true to disable the fancy skybox. Part of Garden of Glass.")
	public static boolean enableFancySkybox = false;

	@Config.Name("manaBar.height")
	@Config.LangKey("botania.config.manaBarHeight")
	@Config.Comment("The height of the mana display bar in above the XP bar. You can change this if you have a mod that changes where the XP bar is.")
	public static int manaBarHeight = 29;

	@Config.Name("flightBar.height")
	@Config.LangKey("botania.config.flightBarHeight")
	@Config.Comment("The height of the Flugel Tiara flight bar. You can change this if you have a mod that adds a bar in that spot.")
	public static int flightBarHeight = 49;

	@Config.Name("flightBarBreath.height")
	@Config.LangKey("botania.config.flightBarBreathHeight")
	@Config.Comment("The height of the Flugel Tiara flight bar if your breath bar is shown. You can change this if you have a mod that adds a bar in that spot.")
	public static int flightBarBreathHeight = 59;

	@Config.Name("shaders.secondaryUnit")
	@Config.LangKey("botania.config.glSecondaryTextureUnit")
	@Config.Comment("The GL Texture Unit to use for the secondary sampler passed in to the Lexica Botania's category button shader. DO NOT TOUCH THIS IF YOU DON'T KNOW WHAT YOU'RE DOING")
	public static int glSecondaryTextureUnit = 7;

	@Config.Name("staticFloaters.enabled")
	@Config.LangKey("botania.config.staticFloaters")
	@Config.Comment("Set this to true if you use lots of floating flowers and are experiencing rendering lag. Will disable the floating flowers' animations and render them statically for a major performance boost. Hit F3+A in-world after toggling this.")
	public static boolean staticFloaters = false;

	@Config.Name("debugInfo.enabled")
	@Config.LangKey("botania.config.debugInfo")
	@Config.Comment("Set to false to disable Botania's messages in the F3 debug screen")
	public static boolean debugInfo = true;

	@Config.Name("matrixMode.enabled")
	@Config.LangKey("botania.config.matrixMode")
	@Config.Comment("Set this to true if you are the chosen one. For lovers of glitch art and just general mad people.")
	public static boolean matrixMode = false;

	@Config.Name("references.enabled")
	@Config.LangKey("botania.config.referencesEnabled")
	@Config.Comment("Set this to false to disable the references in the flower tooltips. (You monster D:)")
	public static boolean referencesEnabled = true;

	@Config.Name("versionChecking.enabled")
	@Config.LangKey("botania.config.versionCheckEnabled")
	@Config.Comment("Set this to false to disable checking and alerting when new Botania CEu versions come out. (keywords for noobs: update notification message)")
	public static boolean versionCheckEnabled = true;

	@Config.Name("spreader.posShift")
	@Config.LangKey("botania.config.spreaderPositionShift")
	@Config.Comment("Do not ever touch this value if not asked to. Possible symptoms of doing so include your head turning backwards, the appearance of Titans near the walls or you being trapped in a game of Sword Art Online.")
	public static int spreaderPositionShift = 1;

	@Config.Name("spreader.traceTime")
	@Config.LangKey("botania.config.spreaderTraceTime")
	@Config.Comment("How many ticks into the future will mana spreaders attempt to predict where mana bursts go? Setting this lower will improve spreader performance, but will cause them to not fire at targets that are too far away.")
	public static int spreaderTraceTime = 400;

	@Config.Name("flower.forceCheck")
	@Config.LangKey("botania.config.flowerForceCheck")
	@Config.Comment("Turn this off ONLY IF you're on an extremely large world with an exaggerated count of Mana Spreaders/Mana Pools and are experiencing TPS lag. This toggles whether flowers are strict with their checking for connecting to pools/spreaders or just check whenever possible.")
	public static boolean flowerForceCheck = true;

	@Config.Name("enderPickpocket.enabled")
	@Config.LangKey("botania.config.enderPickpocketEnabled")
	@Config.Comment("Set to false to disable the ability for the Hand of Ender to pickpocket other players' ender chests.")
	public static boolean enderPickpocketEnabled = true;

	@Config.Name("fallenKanade.enabled")
	@Config.LangKey("botania.config.fallenKanadeEnabled")
	@Config.Comment("Set this to false to disable the Fallen Kanade flower (gives Regeneration). This config option is here for those using Blood Magic. Note: Turning this off will not remove ones already in the world, it'll simply prevent the crafting.")
	public static boolean fallenKanadeEnabled = true;

	@Config.Name("darkQuartz.enabled")
	@Config.LangKey("botania.config.darkQuartzEnabled")
	@Config.Comment("Set this to false to disable the Smokey Quartz blocks. This config option is here for those using Thaumic Tinkerer")
	public static boolean darkQuartzEnabled = true;

	@Config.Name("manaEnchanter.enabled")
	@Config.LangKey("botania.config.enchanterEnabled")
	@Config.Comment("Set this to false to disable the Mana Enchanter. Since some people find it OP or something. This only disables the entry and creation. Old ones that are already in the world will stay.")
	public static boolean enchanterEnabled = true;

	@Config.Name("manaFluxfield.enabled")
	@Config.LangKey("botania.config.fluxfieldEnabled")
	@Config.Comment("Set this to false to disable the Mana Fluxfield (generates RF from mana). This only disables the entry and creation. Old ones that are already in the world will stay.")
	public static boolean fluxfieldEnabled = true;

	@Config.Name("relics.enabled")
	@Config.LangKey("botania.config.relicsEnabled")
	@Config.Comment("Set this to false to disable the Relic System. This only disables the entries, drops and achievements. Old ones that are already in the world will stay.")
	public static boolean relicsEnabled = true;

	@Config.Name("ringOfOdin.fireResist")
	@Config.LangKey("botania.config.ringOfOdinFireResist")
	@Config.Comment("Set this to false to make the Ring of Odin not apply fire resistance. Mostly for people who use Witchery transformations.")
	public static boolean ringOfOdinFireResist = true;

	@Config.Name("magnetRing.invert")
	@Config.LangKey("botania.config.invertMagnetRing")
	@Config.Comment("Set this to true to invert the Ring of Magnetization's controls (from shift to stop to shift to work)")
	public static boolean invertMagnetRing = false;

	@Config.Name("thaumraftStabilizers.enabled")
	@Config.LangKey("botania.config.enableThaumcraftStablizers")
	@Config.Comment("Set this to false to disable Thaumcraft Infusion Stabilizing in botania blocks")
	public static boolean enableThaumcraftStablizers = true;

	@Config.Name("thaumcraftAspects.enabled")
	@Config.LangKey("botania.config.enableThaumcraftAspects")
	@Config.Comment("Set this to false to disable Thaumcraft aspects on Botania items")
	public static boolean enableThaumcraftAspects = true;

	@Config.Name("albedoLights.enabled")
	@Config.LangKey("botania.config.enableAlbedo")
	@Config.Comment("Set this to false to disable Albedo compat for moving colored lights on some Botania entities")
	public static boolean enableAlbedo = true;

	@Config.Name("shedding.enable")
	@Config.LangKey("botania.config.enableShedding")
	@Config.Comment("Set this to true to enable the Shedding feature from 1.7.10. You'll need to load the game to generate the options. No options are enabled by default. This increases load time.")
	public static boolean enableShedding = false;

	@Config.Name("harvestLevel.weightLens")
	@Config.LangKey("botania.config.harvestLevelWeight")
	@Config.Comment("The harvest level of the Mana Lens: Weight. 3 is diamond level. Defaults to 2 (iron level)")
	public static int harvestLevelWeight = 2;

	@Config.Name("harvestLevel.boreLens")
	@Config.LangKey("botania.config.harvestLevelBore")
	@Config.Comment("The harvest level of the Mana Lens: Bore. 3 is diamond level. Defaults to 3")
	public static int harvestLevelBore = 3;

	@Config.Name("worldgen.flower.quantity")
	@Config.LangKey("botania.config.flowerQuantity")
	@Config.Comment("The quantity of Botania flower patches to generate in the world, defaults to 2, the lower the number the less patches generate.")
	public static int flowerQuantity = 2;

	@Config.Name("worldgen.flower.density")
	@Config.LangKey("botania.config.flowerDensity")
	@Config.Comment("The density of each Botania flower patch generated, defaults to 2, the lower the number, the less each patch will have.")
	public static int flowerDensity = 2;

	@Config.Name("worldgen.flower.patchSize")
	@Config.LangKey("botania.config.flowerPatchSize")
	@Config.Comment("The size of each Botania flower patch, defaults to 6. The larger this is the farther the each patch can spread")
	public static int flowerPatchSize = 6;

	@Config.Name("worldgen.flower.patchChance")
	@Config.LangKey("botania.config.flowerPatchChance")
	@Config.Comment("The inverse chance for a Botania flower patch to be generated, defaults to 16. The higher this value is the less patches will exist and the more flower each will have.")
	public static int flowerPatchChance = 16;

	@Config.Name("worldgen.flower.tallChance")
	@Config.LangKey("botania.config.flowerTallChance")
	@Config.Comment("The chance for a Botania flower generated in a patch to be a tall flower. 0.1 is 10%, 1 is 100%.")
	public static double flowerTallChance = 0.05;

	@Config.Name("worldgen.mushroom.quantity")
	@Config.LangKey("botania.config.mushroomQuantity")
	@Config.Comment("The quantity of Botania mushrooms to generate underground, in the world, defaults to 40, the lower the number the less patches generate.")
	public static int mushroomQuantity = 40;

	@Config.Name("ceu.runicAltarCatalysts")
	@Config.LangKey("botania.config.runicAltarCatalysts")
	@Config.Comment("The items that can be used as the catalyst for Runic Altar. These items must not be in any Runic Altar recipes. Syntax is mod_id:item_id or mod_id:item_id@meta (for meta > 0). Default is botania:livingrock.")
	public static String[] runicAltarCatalysts = { "botania:livingrock" };

	@Config.Name("ceu.runicAltarRetainedItems")
	@Config.LangKey("botania.config.runicAltarRetainedItems")
	@Config.Comment("The items that should be retained after the Runic Altar finishes a craft. By default, includes all of Botania's runes.")
	public static String[] runicAltarRetainedItems = IntStream.rangeClosed(0, 15).mapToObj(i -> "botania:rune@" + i)
					.toArray(String[]::new);

	@Config.Name("ceu.petalApothecaryCatalysts")
	@Config.LangKey("botania.config.petalApothecaryCatalysts")
	@Config.Comment("The items that can be used as the catalyst for Petal Apothecary. These items must not be in any Petal Apothecary recipes. Syntax is mod_id:item_id or mod_id:item_id@meta (for meta > 0). The default is empty, which means any seed item can be used as a catalyst.")
	public static String[] petalApothecaryCatalysts = {};

	@Config.Name("ceu.elfPortalSize")
	@Config.LangKey("botania.config.elfPortalSize")
	@Config.Comment("The size of the Elf portal. 1 is a 1x1 square on the internals, 2 is a 3x3 square, etc. Defaults to 2. Warning: large numbers cause lag.")
	public static int elfPortalSize = 2;

	@Config.Name("ceu.petalApothecaryCapacity")
	@Config.LangKey("botania.config.petalApothecaryCapacity")
	@Config.Comment("The maximum number of inputs Petal Apothecary can process at the same time. Defaults to 16. Warning: reducing this size will remove excess items from the Petal Apothecaries in existing worlds.")
	public static int petalApothecaryCapacity = 16;

	@Config.Name("ceu.runicAltarCapacity")
	@Config.LangKey("botania.config.runicAltarCapacity")
	@Config.Comment("The maximum number of inputs Runic Altar can process at the same time. Defaults to 16. Warning: changing this can have unexpected effects on existing worlds.")
	public static int runicAltarCapacity = 16;

	@Config.Name("ceu.breweryCapacity")
	@Config.LangKey("botania.config.breweryCapacity")
	@Config.Comment("The maximum number of inputs Botanical Brewery can process at the same time. Does not count the bottle. Defaults to 6. Warning: changing this in any way will empty the Botanical Breweries in existing worlds.")
	public static int breweryCapacity = 6;

	@Config.Name("ceu.jeiCatalysts")
	@Config.LangKey("botania.config.addCatalystsToJEI")
	@Config.Comment("Should Runic Altar and Petal Apothecary catalysts be displayed in JEI?")
	public static boolean addCatalystsToJEI = true;

	@Config.Name("ceu.showManaNumbers")
	@Config.LangKey("botania.config.showManaNumbers")
	@Config.Comment("Should recipes show all the numbers in JEI? Breaks Botania's immmersion.")
	public static boolean showManaNumbers = false;

	@Config.Name("ceu.gog.disablePetalApothecaryBucketBehavior")
	@Config.LangKey("botania.config.disablePetalApothecaryBucketBehavior")
	@Config.Comment("Should Petal Apothecary be NOT clickable with a bucket? Part of Garden of Glass.")
	public static boolean disablePetalApothecaryBucketBehavior = false;

	@Config.Name("ceu.gog.registerGogResources")
	@Config.LangKey("botania.config.registerGogResources")
	@Config.Comment("Should resources from Garden of Glass, like Pebbles, be enabled? This controls the existence of items themselves.")
	public static boolean registerGogResources = false;

	@Config.Name("ceu.gog.buffedOrechid")
	@Config.LangKey("botania.config.buffedOrechid")
	@Config.Comment("Should Orechid be significantly more powerful? Part of Garden of Glass. You probably don't want this otherwise.")
	public static boolean buffedOrechid = false;

	@Config.Name("ceu.gog.registerGogRecipes")
	@Config.LangKey("botania.config.registerGogRecipes")
	@Config.Comment("Should recipes from Garden of Glass be enabled?")
	public static boolean registerGogRecipes = false;

	@Config.Name("ceu.gog.obtainablePebbles")
	@Config.LangKey("botania.config.obtainablePebbles")
	@Config.Comment("Should Pebbles be obtainable by shift-right-clicking grass blocks? Part of Garden of Glass.")
	public static boolean obtainablePebbles = false;

	@Config.Name("ceu.gog.obtainableWaterBowl")
	@Config.LangKey("botania.config.obtainableWaterBowl")
	@Config.Comment("Should Water Bowl be obtainable by right-clicking water? Part of Garden of Glass.")
	public static boolean obtainableWaterBowl = false;

	@Config.Name("ceu.gog.lexiconGogRecipe")
	@Config.LangKey("botania.config.lexiconGogRecipe")
	@Config.Comment("Should Lexicon be obtainable by right-clicking a Mana Flame with a sapling? Part of Garden of Glass.")
	public static boolean lexiconGogRecipe = false;

	@Config.Name("ceu.gog.registerSkyblockHooks")
	@Config.LangKey("botania.config.registerSkyblockHooks")
	@Config.Comment("Should hooks related to skyblock islands be enabled? Part of Garden of Glass.")
	public static boolean registerSkyblockHooks = false;

	@Config.Name("ceu.gog.diverseSeedDrops")
	@Config.LangKey("botania.config.diverseSeedDrops")
	@Config.Comment("Should seeds dropped from Tall Grass be diversified? Part of Garden of Glass.")
	public static boolean diverseSeedDrops = false;

	@Config.Name("ceu.opManaEnchanter")
	@Config.LangKey("botania.config.opManaEnchanter")
	@Config.Comment("Should Mana Enchanter be able to add enchantments to items that are already enchanted?")
	public static boolean opManaEnchanter = false;

	@Config.Name("ceu.heiseiDream.immuneEntities")
	@Config.LangKey("botania.config.heiseiDreamImmuneEntities")
	@Config.Comment("Entities immune to Heisei Dream. Syntax is modid:entity_name.")
	public static String[] heiseiDreamImmuneEntities = {};

	@Config.Ignore
	public static Set<String> runicAltarCatalystsSet = new HashSet<>();

	@Config.Ignore
	public static Set<String> runicAltarRetainedItemsSet = new HashSet<>();

	@Config.Ignore
	public static Set<String> petalApothecaryCatalystsSet = new HashSet<>();

	@Config.Ignore
	public static Set<ResourceLocation> heiseiDreamImmuneEntitiesSet = new HashSet<>();

	@Config.Ignore
	private static File configFolder;

	// restore vars that addons could have referenced
	@Config.Ignore
	public static boolean useAdaptativeConfig = false;
	@Config.Ignore
	public static boolean altFlowerTextures = false;
	@Config.Ignore
	public static boolean enableFancySkyboxInNormalWorlds = false;
	@Config.Ignore
	public static boolean useVanillaParticleLimiter = true;

	public static void loadHeiseiDreamImmuneEntities() {
		heiseiDreamImmuneEntitiesSet.clear();
		for(String entry : heiseiDreamImmuneEntities) {
			String entityId = entry.trim();
			if(!entityId.isEmpty()) {
				heiseiDreamImmuneEntitiesSet.add(new ResourceLocation(entityId));
			}
		}
	}

	@Config.LangKey("botania.config.generatingFlowers")
	@Config.Comment("Configure generation values for all the flowers. Note! You should not edit this unless you are very experienced with Botania, since it can change the brittle balance of the mod.")
	public static GeneratingFlowers genFlowers = new GeneratingFlowers();

	public static class GeneratingFlowers {
		@Config.Name("rosaArcana.playerXp")
		@Config.LangKey("botania.config.generating.rosaArcana.playerXp")
		@Config.Comment("Mana generated by Rosa Arcana when draining player's XP")
		@Config.RangeInt(min = 0)
		public int manaFromPlayerDrain = 50;

		@Config.Name("rosaArcana.orbXp")
		@Config.LangKey("botania.config.generating.rosaArcana.orbXp")
		@Config.Comment("Mana generated by Rosa Arcana when draining an XP orb in world, multiplied by its size")
		@Config.RangeInt(min = 0)
		public int manaFromXpOrb = 35;

		@Config.Name("dandelifeon.range")
		@Config.LangKey("botania.config.generating.dandelifeon.range")
		@Config.Comment("Range at which Dandelifeon plays the Game of Life")
		@Config.RangeInt(min = 5, max = 20)
		public int dandelifeonRange = 12;

		@Config.Name("dandelifeon.speed")
		@Config.LangKey("botania.config.generating.dandelifeon.speed")
		@Config.Comment("A game step will happen once every this many ticks. Fewer is more mana generated")
		@Config.RangeInt(min = 1, max = 20)
		public int dandelifeonSpeed = 10;

		@Config.Name("dandelifeon.lifetime")
		@Config.LangKey("botania.config.generating.dandelifeon.lifetime")
		@Config.Comment("Optimal lifetime for cells in Dandelifeon. Cells living longer than this won't generate more power")
		@Config.RangeInt(min = 1, max = 500)
		public int dandelifeonLifetime = 100;

		@Config.Name("dandelifeon.manaPerGen")
		@Config.LangKey("botania.config.generating.dandelifeon.mana")
		@Config.Comment("Mana generated by a single Dandelifeon cell, multiplied by cell's lifetime")
		@Config.RangeInt(min = 1)
		public int dandelifeonMana = 60;

		@Config.Name("endoflame.maxBurnTime")
		@Config.LangKey("botania.config.generating.endoflame.burnTime")
		@Config.Comment("Maximum burn time of fuels in Endoflames. 32000 is twice the Coal Block")
		@Config.RangeInt(min = 100)
		public int endoflameBurnTime = 32000;

		@Config.Name("endoflame.burnTimeMult")
		@Config.LangKey("botania.config.generating.endoflame.burnTimeMult")
		@Config.Comment("Endoflame's fuel lifetime will be multiplied by this amount")
		@Config.RangeDouble(min = 0.01, max = 10.0)
		public double endoflameBurnTimeMult = 0.5;

		@Config.Name("endoflame.generation")
		@Config.LangKey("botania.config.generating.endoflame.generation")
		@Config.Comment("Endoflame generation per tick when burning a fuel")
		@Config.RangeInt(min = 1, max = 50)
		public int endoflameMana = 3;

		@Config.Name("entropinnyum.generation")
		@Config.LangKey("botania.config.generating.entropinnyum.generation")
		@Config.Comment("Mana generated by Entropinnyum per TNT exploded")
		@Config.RangeInt(min = 100, max = 100000)
		public int entropinnyumMana = 6500;

		@Config.Name("gourmaryllis.maxFood")
		@Config.LangKey("botania.config.generating.gourmaryllis.maxFood")
		@Config.Comment("Maximum saturation of foods in Gourmaryllis, in half-shacks")
		@Config.RangeInt(min = 1, max = 30)
		public int gourmMaxFood = 12;

		@Config.Name("gourmaryllis.generation")
		@Config.LangKey("botania.config.generating.gourmaryllis.generation")
		@Config.Comment("Mana generation multiplier in Gourmaryllis")
		@Config.RangeInt(min = 1, max = 500)
		public int gourmMultiplier = 70;

		@Config.Name("gourmaryllis.capacity")
		@Config.LangKey("botania.config.generating.gourmaryllis.capacity")
		@Config.Comment("Maximum mana Gourmaryllis can hold")
		@Config.RangeInt(min = 100, max = 100000)
		public int gourmCapacity = 9000;

		@Config.Name("hydroangeas.generation")
		@Config.LangKey("botania.config.generating.hydroangeas.generation")
		@Config.Comment("Mana generated by Hydroangeas every tick")
		@Config.RangeInt(min = 1, max = 50)
		public int hydroangeasMana = 1;

		@Config.Name("kekimurus.generation")
		@Config.LangKey("botania.config.generating.kekimurus.generation")
		@Config.Comment("Mana generated by Kekimurus per slice of cake")
		@Config.RangeInt(min = 100, max = 9000)
		public int kekimurusMana = 1800;

		@Config.Name("kekimurus.delay")
		@Config.LangKey("botania.config.generating.kekimurus.delay")
		@Config.Comment("Time in ticks between two consecutive cake bites for Kekimurus")
		@Config.RangeInt(min = 1, max = 500)
		public int kekimurusDelay = 80;

		@Config.Name("munchdew.generation")
		@Config.LangKey("botania.config.generating.munchdew.generation")
		@Config.Comment("Mana generated by Munchdew per Leaf block")
		@Config.RangeInt(min = 10, max = 2000)
		public int munchdewMana = 160;

		@Config.Name("munchdew.delay")
		@Config.LangKey("botania.config.generating.munchdew.delay")
		@Config.Comment("Time in ticks between two consecutive leaves eaten for Munchdew")
		@Config.RangeInt(min = 1, max = 500)
		public int munchdewDelay = 4;

		@Config.Name("narslimmus.generation")
		@Config.LangKey("botania.config.generating.narslimmus.generation")
		@Config.Comment("Amount of mana generated by Narslimmus for smallest slimes, this will be higher with bigger slimes")
		@Config.RangeInt(min = 50, max = 1500)
		public int narslimmusMana = 1200;

		@Config.Name("narslimmus.delay")
		@Config.LangKey("botania.config.generating.narslimmus.delay")
		@Config.Comment("Time in ticks between two consecutive slimes eaten for Narslimmus")
		@Config.RangeInt(min = 1, max = 500)
		public int narslimmusDelay = 5;

		@Config.Name("rafflowsia.generation")
		@Config.LangKey("botania.config.generating.rafflowsia.generation")
		@Config.Comment("Mana generated by Rafflowsia per eaten flower")
		@Config.RangeInt(min = 50, max = 3000)
		public int rafflowsiaMana = 2100;

		@Config.Name("rafflowsia.delay")
		@Config.LangKey("botania.config.generating.rafflowsia.delay")
		@Config.Comment("Time in ticks between two consecutive flowers eaten for Rafflowsia")
		@Config.RangeInt(min = 1, max = 500)
		public int rafflowsiaDelay = 40;

		@Config.Name("shulkmenot.generation")
		@Config.LangKey("botania.config.generating.shulkmenot.generation")
		@Config.Comment("Mana generated by Shulk Me Not per trigger")
		@Config.RangeInt(min = 100, max = 100000)
		public int shulkMana = 75000;

		@Config.Name("spectrolus.generation")
		@Config.LangKey("botania.config.generating.spectrolus.generation")
		@Config.Comment("Mana generated by Spectrolus per wool block eaten")
		@Config.RangeInt(min = 100, max = 16000)
		public int spectrolusMana = 2400;

		@Config.Name("thermalily.generation")
		@Config.LangKey("botania.config.generating.thermalily.generation")
		@Config.Comment("Mana generated by Thermalily every tick while eating Lava")
		@Config.RangeInt(min = 1, max = 50)
		public int thermalilyMana = 20;

		@Config.Name("thermalily.delay")
		@Config.LangKey("botania.config.generating.thermalily.delay")
		@Config.Comment("Time in ticks between two inputs of Thermalily")
		@Config.RangeInt(min = 100, max = 12000)
		public int thermalilyDelay = 6000;

		@Config.Name("thermalily.burnTime")
		@Config.LangKey("botania.config.generating.thermalily.burnTime")
		@Config.Comment("Burn time of Lava Bucket in Thermalily")
		@Config.RangeInt(min = 100, max = 12000)
		public int thermalilyBurnTime = 900;

	}

    @Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(LibMisc.MOD_ID)) {
                ConfigManager.sync(LibMisc.MOD_ID, Config.Type.INSTANCE);
				loadItemSets();
            }
        }
    }

	public static void setConfigFolder(File configFolder) {
		ConfigHandler.configFolder = configFolder;
	}

	private static void loadItemSets() {
		runicAltarCatalystsSet.clear();
		runicAltarCatalystsSet.addAll(InventoryHelper.expandMetaInStringifiedStacks(runicAltarCatalysts));
		runicAltarRetainedItemsSet.clear();
		runicAltarRetainedItemsSet.addAll(InventoryHelper.expandMetaInStringifiedStacks(runicAltarRetainedItems));
		petalApothecaryCatalystsSet.clear();
		petalApothecaryCatalystsSet.addAll(InventoryHelper.expandMetaInStringifiedStacks(petalApothecaryCatalysts));
		loadHeiseiDreamImmuneEntities();

		SheddingHandler.loadFromConfig(new Configuration(new File(configFolder, "botaniashedding.cfg")));
	}

	private static void checkGogSettings() {
		if (obtainablePebbles && !registerGogResources) {
			throw new IllegalArgumentException(
					"If obtainablePebbles is enabled, registerGogResources must also be enabled!");
		}
		if (obtainableWaterBowl && !registerGogResources) {
			throw new IllegalArgumentException(
					"If obtainableWaterBowl is enabled, registerGogResources must also be enabled!");
		}
	}

	private static void checkItemID(String description, String s) {
		ItemStack ds = InventoryHelper.destringifyStack(s);
		if (ds == null) {
			throw new IllegalArgumentException("Item " + s + " used as " + description + " does not exist");
		}
	}

	public static void postInitValidation() {
		loadItemSets();
		checkGogSettings();
		if (runicAltarCatalystsSet.isEmpty())
			throw new IllegalArgumentException("Must have at least 1 Runic Altar catalyst");
		for (String s : runicAltarCatalysts)
			checkItemID("Runic Altar Catalyst", s);
		for (String s : runicAltarRetainedItems)
			checkItemID("Runic Altar Retained Item", s);
		for (String s : petalApothecaryCatalysts)
			checkItemID("Petal Apothecary Catalyst", s);
	}

}
