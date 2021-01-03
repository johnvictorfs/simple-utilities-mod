package net.johnvictorfs.simple_utilities;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.TranslatableText;
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
                GLFW.GLFW_KEY_K,
                "key.category.simple_utilities.hud"
        );

        KeyBinding toggleHudKeybinding = KeyBindingHelper.registerKeyBinding(toggleHudKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (toggleHudKeybinding.wasPressed()) {
                SimpleUtilitiesConfig config = this.configHolder.getConfig();

                String chatMessage = "key.simple_utilities.toggle_hud.chat_message.on";
                if (config.statusElements.toggleSimpleUtilitiesHUD) {
                    chatMessage = "key.simple_utilities.toggle_hud.chat_message.off";
                }

                client.player.sendMessage(new TranslatableText(chatMessage), true);
                config.statusElements.toggleSimpleUtilitiesHUD = !config.statusElements.toggleSimpleUtilitiesHUD;
                AutoConfig.getConfigHolder(SimpleUtilitiesConfig.class).save();
            }
        });
    }
}
