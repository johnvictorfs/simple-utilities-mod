package net.johnvictorfs.simple_utilities;

import net.fabricmc.api.ModInitializer;

public class SimpleUtilities implements ModInitializer {
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
