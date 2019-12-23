package net.johnvictorfs.simple_utilities;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ToggleSprint {
    public FabricKeyBinding toggleSprintKeybind;
    public boolean isSprinting = false;

    public void registerToggleSprint() {
        // Create and register Toggle Sprint Keybind with default as 'r'
        toggleSprintKeybind = FabricKeyBinding.Builder.create(
                new Identifier("simple_utilities", "toggle_sprint"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "Simple Utilities"
        ).build();

        KeyBindingRegistry.INSTANCE.register(toggleSprintKeybind);
    }

    public void handleToggleSprintPress(MinecraftClient client) {
        // Toggle sprint mode
        this.isSprinting = !this.isSprinting;

        if (client.player != null) {
            client.player.setSprinting(this.isSprinting);
        }
    }
}
