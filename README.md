# Simple Utilities Mod (Minecraft 1.15 - 1.19)

Available to download from [Modrinth](https://modrinth.com/mod/simple-hud-utilities) or [Curseforge](https://www.curseforge.com/minecraft/mc-mods/simple-utilities).

Built using [Fabric Example Mod Template](https://github.com/FabricMC/fabric-example-mod) and made with the [Fabric](https://fabricmc.net) modding toolchain for Minecraft.

A Minecraft Mod that enhances the Game's HUD with some simple utilities like the following:

- **HUD Features:**
    - Simplified coordinates that are available at all times on the screen (Example: `39, 64, 200` as `X, Y, Z`)
    - Cardinal directions and which Coordinates are increasing/decreasing (Example: `(East X+)` when looking East, where the `X` coordinate is increasing)
    - Current armour and hand items and their durabilities, available at all times on the screen
        - Different colors based on how low the durability is
    - Current Game time in AM/PM format
    - Current sprinting status
    - Current framerate
    - Current Biome the player is on
    - Nether/overworld coordinates conversion
    - Toggle key (editable in Controls screen, default is `k`)
    - Settings page and config file with Mod Menu
        - **Requires [Mod Menu mod](https://www.curseforge.com/minecraft/mc-mods/modmenu) to open settings screen in-game**
        - Config file: `.minecraft/config/simple_utilities.toml` (does not require Mod Menu mod to edit)

---

## Images

|           In-game HUD                 | Settings page (Requires [ModMenu Mod](https://www.curseforge.com/minecraft/mc-mods/modmenu)) |
| --------------------------------------| ----------------------------------------- |
| ![In-game HUD](images/ingame_hud.png) | ![Settings HUD](images/settings_hud.png)  |

---

## Installation

- Install [Fabric Loader](https://fabricmc.net/use/) on your Minecraft client
    - I recommended installing with the [PolyMC](https://github.com/PolyMC/PolyMC) Minecraft client, which allows you to install Fabric in one click in the Minecraft instance settings
- Download the latest Mod `.jar` from [Github](https://github.com/johnvictorfs/simple-utilities-mod/releases/latest) or from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/simple-utilities)
- Put the downloaded Mod `.jar` in the `.minecraft/mods` folder
    - Or if you're using PolyMC, open the Minecraft instance settings you're using, and look for the option to add a Mod, then select the `.jar` file you downloaded
- Done!

---

<details>
<summary>
Building from source
</summary>

- Clone the project with `git clone https://github.com/johnvictorfs/simple-utilities-mod.git`
- Cd into the project's directory `cd simple-utilities-mod`
- Run `./gradlew build` to build the `.jar`
- Built Mod `.jar` files will be located at `build/libs`
    - Example: `build/libs/simple-utilities-mod-1.0.0.jar`
    - This will be the Mod `.jar` file you can put in your `.minecraft/mods` folder
</details>

---

<details>
<summary>
Planned features
</summary>

- Add configuration to move any specific HUD elements around the screen (drag-and-drop if possible)
- Add option for E counter (entities) and C counter (chunk sections) (possibly M/C counters as well (monsters/creatures)) 
- Add current status effects duration to HUD
- Add Sun/Moon icons to the current game time, so it's easier to notice if it's Day or Night
</details>

---

## FAQ

- **Does this Mod work on versions below 1.15?**
    - No, it *might* work on 1.14 with some changes, but not anything below 1.14, since this Mod is made with Fabric, which only supports Minecraft 1.14 and above.

- **Will this Mod get me banned from *X multiplayer server*?**
    - Maybe, maybe not, the Mod is entirely Client-sided and does not require it to be installed on the Server, and mostly shows things already available to you at all times like coordinates and Cardinal directions, like an extended but simplified F3 Menu, but it has some exceptions, like very specific Game time, so some servers may not allow it, do look into the Server's rules carefully before using it, do **not** create issues here asking about that, since I won't know.

- **Will you add '*X feature not present in the [Planned Features](#planned-features) section*'**?
    - Maybe, and only if it fits with the other features of the mod, create [an issue](https://github.com/johnvictorfs/simple-utilities-mod/issues/new) about it, I only work on this project on my spare time, but I'd be happy to add wanted features if I can get around to it.
