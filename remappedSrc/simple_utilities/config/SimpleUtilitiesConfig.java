package net.johnvictorfs.simple_utilities.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.johnvictorfs.simple_utilities.helpers.Colors;

@Config(name = "simple_utilities")
public class SimpleUtilitiesConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public StatusElements statusElements = new StatusElements();

    public static class StatusElements {
        public boolean toggleSimpleUtilitiesHUD = true;
        public boolean toggleCoordinatesStatus = true;
        public boolean toggleDirectionStatus = true;
        public boolean toggleEquipmentStatus = true;
        public boolean toggleFpsStatus = true;
        public boolean toggleSprintStatus = true;
        public boolean toggleBiomeStatus = true;
        public boolean toggleGameTimeStatus = true;
        public boolean togglePlayerEffects = true;

        @ConfigEntry.ColorPicker
        public int textColor = Colors.white;
    }
}
