# Simple Utilities Mod (Minecraft 1.19.3)

This is an unofficial Fork due to the original creator no longer respondiong to PR or New features.

A Minecraft Mod that enhances the Game's HUD with some simple utilities like the following:
- **NEW FORK HUD Features:**
    - Current Player Speed
    - Light Status
    - Fixed Location Bugs
    - New Settings System
    - Configurable Hud
    - Upgradeable Feature Set


- **Original HUD Features:**
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
- Download the latest Mod `.jar` from [Github](https://github.com/soradgaming/simple-utilities-mod/releases/latest)
- Put the downloaded Mod `.jar` in the `.minecraft/mods` folder
- Done!

---

<details>
<summary>
Building from source
</summary>

- Clone the project with `git clone https://github.com/soradgaming/simple-utilities-mod.git`
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

- Add current status effects duration to HUD
</details>
