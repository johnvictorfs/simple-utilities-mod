package net.johnvictorfs.simple_utilities.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.johnvictorfs.simple_utilities.helpers.Colors;

@Config(name = "simple_utilities")
public class SimpleUtilitiesConfig implements ConfigData {
    @ConfigEntry.Gui.TransitiveObject
    public StatusElements statusElements = new StatusElements();

    public static class Point{
        public int x;
        public int y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static class StatusElements {
        // Main Toggle
        public boolean toggleSimpleUtilitiesHUD = true;

        // "Game Info"
        @ConfigEntry.Gui.CollapsibleObject()
        public Point gameInfoElementPosition = new Point(0,0);
        public boolean toggleCoordinatesStatus = true;
        public boolean toggleDirectionStatus = true;
        public boolean toggleFpsStatus = true;
        public boolean toggleBiomeStatus = true;
        public boolean toggleGameTimeStatus = true;
        public boolean toggleNetherCoordinateConversion = false;
        public boolean togglePlayerName = true;
        public boolean toggleServerName = true;
        public boolean toggleServerAddress = true;

        // Equipment Info
        @ConfigEntry.Gui.CollapsibleObject()
        public Point equipmentInfoPosition = new Point(0,0);
        public boolean toggleEquipmentStatus = true;

        // Sprinting Info
        @ConfigEntry.Gui.CollapsibleObject()
        public Point sprintingInfoPosition = new Point(0,0);
        public boolean toggleSprintStatus = true;
        // StatusEffect Info
        @ConfigEntry.Gui.CollapsibleObject()
        public Point statusEffectInfoPosition = new Point(0,0);

        @ConfigEntry.ColorPicker
        public int textColor = Colors.white;
    }
}
