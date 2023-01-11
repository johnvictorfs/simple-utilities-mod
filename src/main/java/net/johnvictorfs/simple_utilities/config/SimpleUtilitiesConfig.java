package net.johnvictorfs.simple_utilities.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.johnvictorfs.simple_utilities.helpers.Colors;

@Config(name = "simple_utilities")
public class SimpleUtilitiesConfig implements ConfigData {
    public static class StatusElements {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int Xcords = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int Ycords = 0;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleCoordinatesStatus = true;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleDirectionStatus = true;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleNetherCoordinateConversion = false;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleFpsStatus = true;
        @ConfigEntry.Gui.Tooltip
        public boolean togglePlayerSpeedStatus = true;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleLightLevelStatus = false;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleBiomeStatus = true;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleGameTimeStatus = true;
        @ConfigEntry.Gui.Tooltip
        public boolean togglePlayerName = false;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleServerName = false;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleServerAddress = false;
    }

    public static class UIConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean toggleSimpleUtilitiesHUD = true;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleEquipmentStatus = false;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int equipmentLocationX = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int equipmentLocationY = 60;
        @ConfigEntry.Gui.Tooltip
        public boolean toggleSprintStatus = false;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int sprintStatusLocationX = 0;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int sprintStatusLocationY = 90;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int textColor = Colors.white;
    }

    @ConfigEntry.Gui.TransitiveObject
    public UIConfig uiConfig = new UIConfig();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public StatusElements statusElements = new StatusElements();
}
