package net.johnvictorfs.simple_utilities.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.johnvictorfs.simple_utilities.helpers.Colors;

@Config(name = "simple_utilities")
public class SimpleUtilitiesConfig implements ConfigData {
    @ConfigEntry.Gui.TransitiveObject
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
        public boolean toggleNetherCoordinateConversion = false;

        // 追加分
        public boolean togglePlayerName = true;
        public boolean toggleServerName = true;
        public boolean toggleServerAddress = true;

        @ConfigEntry.ColorPicker
        public int textColor = Colors.white;
    }
}
