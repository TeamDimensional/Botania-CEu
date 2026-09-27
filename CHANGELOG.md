# Changelog

## [r1.10-376] - 2026-09-27

### Tweaks

- Mana Mirror can now be used as a Bauble
    - This is configurable to instead register a Mirror Band or disable the feature altogether
    - In either case Mana Mirror will still be usable from the inventory
- Adjusted the mechanics of Ring of Chordata to make it feel less slippery
- Right-clicking to equip Baubles of types having many slots (rings or types configured by the modpack to have multiple slots) will now prioritize free slots if possible

### Config

- Added an option to disallow certain mobs to be targeted by Heisei Dream (thank you Windanesz!)
- Added a number of config options to various baubles

### Fixes

- Bauble Case and Flower Bag will no longer void items (thank you Aedial!)
- Using Ring of Chordata with Aqua Acrobatics installed will no longer make the player unreasonably fast

## [r1.10-375] - 2026-08-22

### Tweaks

- Certain Baubles can now be disabled with shift-right-click
    - This supports: Cirrus Amulet, Nimbus Pendant, Tainted Blood Pendant, Snowflake Pendant, Ring of Magnetization, Greater Ring of Magnetization, Sojourner's Sash, Globetrotter's Sash, Planestrider's Sash, Ring of Chordata, Ring of Loki
- Mana Enchanter will now display particles changing their color as the enchantment process is going
- Added a config option to make Mana Enchanter able to upgrade already enchanted items

### Fixes

- Mana Enchanter can no longer apply enchantments for free due to integer overflow
- Mana Enchanter can now apply enchantments over level 127
- Ender Air will no longer block picking up Dragon's Breath (thank you Brycey92!)
    - Note that existing Ender Air automations might need to be replaced due to this change
- Added a generic colored-item localization (i.e. generic Petal) for modpack devs
- Mana Prism will now cap the amount of mana in bursts if lenses such as Messenger are used
- Fixed a bug where the same Mana Prism could affect the same burst twice

## [r1.10-374] - 2026-07-18

### Fixes

- Fixed Terrestrial Agglomeration Plate recipe cheese
- Fixed certain mod items randomly getting empty NBT tags
    - This fix is not 100% tested, please report any NBT-related bugs to the tracker
- Fixed the advanced Baubles tooltip not working correctly

## [r1.10-373a] - 2026-06-01

### Fixes

- Hotfixed compatibility with Botania Tweaks

## [r1.10-373] - 2026-05-31

### Configuration

- Migrated to a modern annotation-driven Config manager
- Added config fields for every generating flower

### Fixes

- Fixed a bug where Water Runes would be unexpectedly deleted from Runic Altar crafts

## [r1.10-372] - 2026-04-14

### Build
- Migrated the project to RetroFuturaGradle

### QOL
- Elven Portal will no longer get stuck when it receives more than 4 distinct stacks per second
- Runes dropped out of Runic Altars will no longer try to enter adjacent Runic Altars

### Fixes
- Cleaned up the mod version checker and made it functional again

## [r1.10-371] - 2026-03-24

### Fixes
- Fixed several oddities with features added in r1.10-370
- Improved Petal Apothecary JEI handler
- Removed some parts of the mod auto-updater as per new CF rules

## [r1.10-370] - 2026-03-18

### Features
- Runic Altar and Petal Apothecary catalysts will now be shown in JEI
- Ported Terrestrial Agglomeration Plate from Botanic Additions
- Added Groovyscript support

### QOL
- Livingrock will no longer be placed in world after right-clicking a Runic Altar if there's one in the altar already

### Configuration
- Added a config option to display more numbers (off by default)

### Performance
- Optimized performance of Alfheim Portals in several cases (thanks playfuldoggo!)

## [r1.10-369] - 2025-08-23

### Configuration
- Botanical Brewery's capacity is now configurable

### Fixes
- Fixed a client crash when recursively rendering a Botanical Brewery (i.e., when it is added into an inventory of another Botanical Brewery)

## [r1.10-368] - 2025-01-23

### Configuration
- Runic Altar and Petal Apothecary now support multiple options for catalysts

### Fixes
- Fixed a dupe with Redstone Drawer and Corporea Funnel

## [r1.10-367] - 2025-01-16

### QOL
- Elves no longer hate Bread
- Alfheim Portal now has a capacity of 128 stacks, which means it will be harder to get a chunk ban with

### Configuration
- Garden of Glass additions can now be enabled by editing the config file, without having to install GoG

### Fixes
- Fixed a dupe with Necrodermal Virus when used on dead Donkeys

## [r1.10-366] - 2025-01-15

### QOL
- The catalyst item will not be shown in the Runic Altar's HUD if it is already in it

### Configuration
- The size of Alfheim Portal is now configurable
- The capacity of Runic Altars and Petal Apothecaries is now configurable

### Fixes
- Updating from a save on original Botania will no longer cause a crash

## [r1.10-365] - 2025-01-14

### QOL
- Runic Altar will now store the catalyst item inside its inventory to prevent despawning and stack combining

### Configuration
- Catalyst items for Runic Altar and Petal Apothecary are now configurable
- Items that get retained after a Runic Altar craft are now configurable

### Miscellanous
- Migrated to a newer version of ForgeGradle
