package net.johnvictorfs.simple_utilities;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class SimpleUtilities implements ModInitializer {
//    private ToggleSprint toggleSprint = new ToggleSprint();

    @Override
    public void onInitialize() {
        /*
          This code runs as soon as Minecraft is in a mod-load-ready state.
          However, some things (like resources) may still be unitialized.
          Proceed with mild caution.
         */
        System.out.println("Simple Utilities Mod started.");
    }
}
