package net.johnvictorfs.simple_utilities;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class SimpleUtilities implements ClientModInitializer {
    private ConfigHolder<SimpleUtilitiesConfig> configHolder;

    @Override
    public void onInitializeClient() {
        /*
          This code runs as soon as Minecraft is in a mod-load-ready state.
          However, some things (like resources) may still be unitialized.
          Proceed with mild caution.
         */
        System.out.println("Simple Utilities Mod started.");

        this.configHolder = AutoConfig.register(SimpleUtilitiesConfig.class, Toml4jConfigSerializer::new);

        this.registerKeybindings();
    }

    void registerKeybindings() {
        KeyBinding toggleHudKey = new KeyBinding(
                "key.simple_utilities.toggle_hud",
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "key.category.simple_utilities.hud"
        );

        KeyBinding toggleHudKeybinding = KeyBindingHelper.registerKeyBinding(toggleHudKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (toggleHudKeybinding.wasPressed()) {
                SimpleUtilitiesConfig config = this.configHolder.getConfig();

                String chatMessage = "key.simple_utilities.toggle_hud.chat_message.on";
                if (config.uiConfig.toggleSimpleUtilitiesHUD) {
                    chatMessage = "key.simple_utilities.toggle_hud.chat_message.off";
                }

                client.player.sendMessage(Text.translatable(chatMessage), true);
                config.uiConfig.toggleSimpleUtilitiesHUD = !config.uiConfig.toggleSimpleUtilitiesHUD;
                AutoConfig.getConfigHolder(SimpleUtilitiesConfig.class).save();
            }
        });
    }
}
