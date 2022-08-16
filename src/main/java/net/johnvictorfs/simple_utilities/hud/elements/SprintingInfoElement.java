package net.johnvictorfs.simple_utilities.hud.elements;

import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig.Point;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static net.johnvictorfs.simple_utilities.hud.GameInfoHud.config;

public class SprintingInfoElement implements SimpleHudElement {
    private final TextRenderer fontRenderer;
    private final ClientPlayerEntity player;
    private final MatrixStack matrixStack;

    private MinecraftClient client;

    public SprintingInfoElement(MinecraftClient client, MatrixStack matrixStack) {
        this.client = client;
        this.fontRenderer = client.textRenderer;
        this.player = client.player;
        this.matrixStack = matrixStack;
    }

    public void draw() {
        Point position = config.statusElements.sprintingInfoPosition;
        if (!(config.statusElements.toggleSprintStatus && (this.client.options.sprintKey.isPressed() || this.player.isSprinting()))) {
            return;
        }
        final String sprintingText = (Text.translatable("text.hud.simple_utilities.sprinting")).getString();

        int maxLineHeight = Math.max(10, this.fontRenderer.getWidth(sprintingText));
        maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
        int scaleHeight = this.client.getWindow().getScaledHeight();
        int sprintingTop = scaleHeight - maxLineHeight;

        // Sprinting Info
        this.fontRenderer.drawWithShadow(this.matrixStack, sprintingText,  position.x,  position.y, config.statusElements.textColor);
    }
}
