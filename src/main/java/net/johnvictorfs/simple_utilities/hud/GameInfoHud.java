package net.johnvictorfs.simple_utilities.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;

import java.util.*;

@Environment(EnvType.CLIENT)
public class GameInfoHud implements Drawable {
    private final MinecraftClient client;
    private final TextRenderer fontRenderer;
    private Entity player;

    public GameInfoHud(MinecraftClient client) {
        this.client = client;
        this.fontRenderer = client.textRenderer;
    }

    public void draw() {
        this.player = this.client.player;

        this.drawInfos();

        this.client.getProfiler().pop();
    }

    private void drawInfos() {
        // Draw lines of Array of Game info in the screen
        int maxLineWidth = 10;
        List<String> lines = getGameInfo();

        for (String line : lines) {
            maxLineWidth = Math.max(maxLineWidth, this.fontRenderer.getStringWidth(line));
        }

        int top = 0;

        int lineHeight = this.fontRenderer.fontHeight + 2;
        // maxLineWidth = (int) (Math.ceil(maxLineWidth / 5.0D + 0.5D) * 5);
        // int scaleWidth = this.client.getWindow().getScaledWidth();
        // int left = (scaleWidth - maxLineWidth) / 12 - 2;
        int left = 4;

        for (String line : lines) {
            this.fontRenderer.draw(line, left, top + 4, 0x00E0E0E0);
            top += lineHeight;
        }

        if (this.player.isSprinting()) {
            int maxLineHeight = Math.max(10, this.fontRenderer.getStringWidth("Sprinting"));
            maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
            int scaleHeight = this.client.getWindow().getScaledHeight();
            int sprintingTop = scaleHeight - maxLineHeight;

            // Sprinting Info
            this.fontRenderer.draw("Sprinting", 2, sprintingTop + 20, 0x00E0E0E0);
        }
    }

    private static String capitalize(String str) {
        // Capitalize first letter of a String
        if (str == null) return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String getOffset(Direction facing) {
        String offset = "";

        if (facing.getOffsetX() > 0) {
            offset += "+X";
        } else if (facing.getOffsetX() < 0) {
            offset += "-X";
        }

        if (facing.getOffsetZ() > 0) {
            offset += " +Z";
        } else if (facing.getOffsetZ() < 0) {
            offset += " -Z";
        }

        return offset.trim();
    }

    private List<String> getGameInfo() {
        List<String> gameInfo = new ArrayList<>();

        Direction facing = this.player.getHorizontalFacing();

        String coordsFormat = "%.0f, %.0f, %.0f %s";

        String direction = "(" + capitalize(facing.asString()) + " " + getOffset(facing) + ")";

        // Coordinates and Direction info
        gameInfo.add(
                String.format(coordsFormat, this.player.getX(), this.player.getY(), this.player.getZ(), direction)
        );

        // Get everything from fps debug string until the 's' from 'fps'
        gameInfo.add(client.fpsDebugString.substring(0, client.fpsDebugString.indexOf("s") + 1));

        // Get translated biome info
        if (client.world != null) {
            gameInfo.add(I18n.translate(client.world.getBiome(this.player.getBlockPos()).getTranslationKey()) + " Biome");
        }

        return gameInfo;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
    }
}