package net.johnvictorfs.simple_utilities.hud;

import net.johnvictorfs.simple_utilities.helpers.Colors;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        List<String> gameInfo = getGameInfo();
        drawArmourInfo();

        int lineHeight = this.fontRenderer.fontHeight + 2;
        int top = 0;
        int left = 4;

        for (String line : gameInfo) {
            this.fontRenderer.draw(line, left, top + 4, Colors.lightGray);
            top += lineHeight;
        }

        if (this.player.isSprinting()) {
            final String sprintingText = "Sprinting";

            int maxLineHeight = Math.max(10, this.fontRenderer.getStringWidth(sprintingText));
            maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
            int scaleHeight = this.client.getWindow().getScaledHeight();
            int sprintingTop = scaleHeight - maxLineHeight;

            // Sprinting Info
            this.fontRenderer.draw(sprintingText, 2, sprintingTop + 20, Colors.lightGray);
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

    private String zeroPadding(int number) {
        return (number >= 10) ? Integer.toString(number) : String.format("0%s", number);
    }

    private String secondsToString(int pTime) {
        final int min = pTime / 60;
        final int sec = pTime - (min * 60);

        final String strMin = zeroPadding(min);
        final String strSec = zeroPadding(sec);
        return String.format("%s:%s", strMin, strSec);
    }

    private void drawStatusEffectInfo() {
        if (this.client.player != null) {
            Map<StatusEffect, StatusEffectInstance> effects = this.client.player.getActiveStatusEffects();

            for (Map.Entry<StatusEffect, StatusEffectInstance> effect : effects.entrySet()) {
                String effectName = I18n.translate(effect.getKey().getTranslationKey());

                String duration = secondsToString(effect.getValue().getDuration() / 20);

                int color = effect.getKey().getColor();

                this.fontRenderer.draw(effectName + " " + duration, 40, 200, color);
            }
        }
    }

    private void drawArmourInfo() {
        List<ItemStack> armourItems = new ArrayList<>();

        int maxLineHeight = Math.max(10, this.fontRenderer.getStringWidth(""));

        for (ItemStack armourItem : this.player.getArmorItems()) {
            maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getStringWidth(I18n.translate(armourItem.getTranslationKey())));
            armourItems.add(armourItem);
        }

        maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
        int armourTop = this.client.getWindow().getScaledHeight() - maxLineHeight;

        int lineHeight = this.fontRenderer.fontHeight + 6;

        // Draw in order Helmet -> Chestplate -> Leggings -> Boots
        for (ItemStack armourItem : Lists.reverse(armourItems)) {
            if (armourItem.getMaxDamage() == 0) {
                // Skip empty slots
                continue;
            }

            this.client.getItemRenderer().renderGuiItemIcon(armourItem, 2, armourTop - 68);

            int currentDurability = armourItem.getMaxDamage() - armourItem.getDamage();
            String itemDurability = currentDurability + "/" + armourItem.getMaxDamage();
            this.fontRenderer.draw(itemDurability, 22, armourTop - 64, Colors.lightGray);

            armourTop += lineHeight;
        }
    }

    private static String parseTime(long time) {
        long hours = (time / 1000 + 6)%24;
        long minutes = (time % 1000) * 60 / 1000;
        String ampm = "AM";

        if (hours >= 12) {
            hours -= 12;
            ampm = "PM";
        }

        if (hours >= 12) {
            hours -= 12;
            ampm = "AM";
        }

        if (hours == 0) hours = 12;

        String mm = "0" + minutes;
        mm = mm.substring(mm.length() - 2);

        return hours + ":" + mm + " " + ampm;
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

            // Add current parsed time
            gameInfo.add(parseTime(client.world.getTimeOfDay()));
        }

        return gameInfo;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
    }
}