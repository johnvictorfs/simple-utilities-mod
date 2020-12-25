package net.johnvictorfs.simple_utilities;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class SimpleUtilities implements ModInitializer {
    @Override
    public void onInitialize() {
        /*
          This code runs as soon as Minecraft is in a mod-load-ready state.
          However, some things (like resources) may still be unitialized.
          Proceed with mild caution.
         */
        System.out.println("Simple Utilities Mod started.");

        KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simple_utilities.toggle_hud", GLFW.GLFW_KEY_K, "key.category.simple_utilities.hud"));
    }
}
