package net.johnvictorfs.simple_utilities.hud;

import net.johnvictorfs.simple_utilities.helpers.Colors;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.johnvictorfs.simple_utilities.mixin.GameClientMixin;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class GameInfoHud implements Drawable {
    private final MinecraftClient client;
    private final TextRenderer fontRenderer;
    private ClientPlayerEntity player;

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
        drawEquipementInfo();

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

    private void drawEquipementInfo() {
        List<ItemStack> equippedItems = new ArrayList<>();
        PlayerInventory inventory = this.player.inventory;
        int maxLineHeight = Math.max(10, this.fontRenderer.getStringWidth(""));

        ItemStack mainHandItem = inventory.getMainHandStack();
        maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getStringWidth(I18n.translate(mainHandItem.getTranslationKey())));
        equippedItems.add(mainHandItem);

        for (ItemStack secondHandItem : inventory.offHand) {
            maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getStringWidth(I18n.translate(secondHandItem.getTranslationKey())));
            equippedItems.add(secondHandItem);
        }

        for (ItemStack armourItem : this.player.inventory.armor) {
            maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getStringWidth(I18n.translate(armourItem.getTranslationKey())));
            equippedItems.add(armourItem);
        }

        maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
        int itemTop = this.client.getWindow().getScaledHeight() - maxLineHeight;

        int lineHeight = this.fontRenderer.fontHeight + 6;

        // Draw in order Helmet -> Chestplate -> Leggings -> Boots
        for (ItemStack equippedItem : Lists.reverse(equippedItems)) {
            if (equippedItem.getItem().equals(Blocks.AIR.asItem())) {
                // Skip empty slots
                continue;
            }

            this.client.getItemRenderer().renderGuiItemIcon(equippedItem, 2, itemTop - 68);

            if (equippedItem.getMaxDamage() != 0) {
                int currentDurability = equippedItem.getMaxDamage() - equippedItem.getDamage();

                String itemDurability = currentDurability + "/" + equippedItem.getMaxDamage();

                // Default Durability Color
                int color = Colors.lightGray;

                if (currentDurability < equippedItem.getMaxDamage()) {
                    // Start as Green if item has lost at least 1 durability
                    color = Colors.lightGreen;
                }
                if (currentDurability <= (equippedItem.getMaxDamage() / 1.5)) {
                    color = Colors.lightYellow;
                }
                if (currentDurability <= (equippedItem.getMaxDamage() / 2.5)) {
                    color = Colors.lightOrange;
                }
                if (currentDurability <= (equippedItem.getMaxDamage()) / 4) {
                    color = Colors.lightRed;
                }

                this.fontRenderer.draw(itemDurability, 22, itemTop - 64, color);
            } else {
                int count = equippedItem.getCount();

                if (count > 1) {
                    String itemCount = String.valueOf(count);
                    this.fontRenderer.draw(itemCount, 22, itemTop - 64, Colors.lightGray);
                }
            }

            itemTop += lineHeight;
        }
    }

    private static String parseTime(long time) {
        long hours = (time / 1000 + 6) % 24;
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
        gameInfo.add(String.format(coordsFormat, this.player.getX(), this.player.getY(), this.player.getZ(), direction));

        // Get everything from fps debug string until the 's' from 'fps'
        // gameInfo.add(client.fpsDebugString.substring(0, client.fpsDebugString.indexOf("s") + 1));
        gameInfo.add(String.format("%d fps", ((GameClientMixin) client.getInstance()).getCurrentFps()));

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