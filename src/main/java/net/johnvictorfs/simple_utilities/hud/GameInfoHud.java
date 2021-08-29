package net.johnvictorfs.simple_utilities.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig;
import net.johnvictorfs.simple_utilities.helpers.Colors;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.johnvictorfs.simple_utilities.mixin.GameClientMixin;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class GameInfoHud {
    private final MinecraftClient client;
    private final TextRenderer fontRenderer;
    private ClientPlayerEntity player;
    private MatrixStack matrixStack;
    private final ItemRenderer itemRenderer;
    private SimpleUtilitiesConfig config;

    public GameInfoHud(MinecraftClient client) {
        this.client = client;
        this.fontRenderer = client.textRenderer;
        this.itemRenderer = client.getItemRenderer();

        this.config = AutoConfig.getConfigHolder(SimpleUtilitiesConfig.class).getConfig();

        AutoConfig.getConfigHolder(SimpleUtilitiesConfig.class).registerSaveListener((manager, data) -> {
            // Update local config when new settings are saved
            this.config = data;
            return ActionResult.SUCCESS;
        });
    }

    public void draw(MatrixStack matrixStack) {
        if (!config.statusElements.toggleSimpleUtilitiesHUD) return;

        this.player = this.client.player;

        this.matrixStack = matrixStack;

        RenderSystem.enableBlend();

        this.drawInfos();

        this.client.getProfiler().pop();
    }

    private void drawInfos() {
        // Draw lines of Array of Game info in the screen
        List<String> gameInfo = getGameInfo();

        if (config.statusElements.toggleEquipmentStatus && !(this.client.currentScreen instanceof ChatScreen)) {
            drawEquipmentInfo();
        }

        int lineHeight = this.fontRenderer.fontHeight + 2;
        int top = 0;
        int left = 4;

        for (String line : gameInfo) {
            this.fontRenderer.drawWithShadow(this.matrixStack, line, left, top + 4, config.statusElements.textColor);
            top += lineHeight;
        }

        if (config.statusElements.toggleSprintStatus && (this.client.options.keySprint.isPressed() || this.player.isSprinting())) {
            this.drawSprintingInfo();
        }
    }

    private void drawSprintingInfo() {
        final String sprintingText = "Sprinting";

        int maxLineHeight = Math.max(10, this.fontRenderer.getWidth(sprintingText));
        maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
        int scaleHeight = this.client.getWindow().getScaledHeight();
        int sprintingTop = scaleHeight - maxLineHeight;

        // Sprinting Info
        this.fontRenderer.drawWithShadow(this.matrixStack, sprintingText, 2, sprintingTop + 20, config.statusElements.textColor);
    }

    private static String capitalize(String str) {
        // Capitalize first letter of a String
        if (str == null) return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String getOffset(Direction facing) {
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

                this.fontRenderer.drawWithShadow(this.matrixStack, effectName + " " + duration, 40, 200, color);
            }
        }
    }

    private void drawEquipmentInfo() {
        List<ItemStack> equippedItems = new ArrayList<>();
        PlayerInventory inventory = this.player.getInventory();
        //int maxLineHeight = Math.max(10, this.fontRenderer.getWidth(""));
        int maxLineHeight = Math.max(100, this.fontRenderer.fontHeight);

        ItemStack mainHandItem = inventory.getMainHandStack();
        //maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getWidth(I18n.translate(mainHandItem.getTranslationKey())));
        equippedItems.add(mainHandItem);

        for (ItemStack secondHandItem : inventory.offHand) {
            //maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getWidth(I18n.translate(secondHandItem.getTranslationKey())));
            equippedItems.add(secondHandItem);
        }

        for (ItemStack armourItem : this.player.getInventory().armor) {
            //maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getWidth(I18n.translate(armourItem.getTranslationKey())));
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

            this.itemRenderer.renderInGuiWithOverrides(equippedItem, 2, itemTop - 68);

            if (equippedItem.getMaxDamage() != 0) {
                int currentDurability = equippedItem.getMaxDamage() - equippedItem.getDamage();

                String itemDurability = currentDurability + "/" + equippedItem.getMaxDamage();

                // Default Durability Color
                int color = config.statusElements.textColor;

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

                this.fontRenderer.drawWithShadow(this.matrixStack, itemDurability, 22, itemTop - 64, color);
            } else {
                int inventoryCount = inventory.count(equippedItem.getItem());
                int count = equippedItem.getCount();

                if (inventoryCount > 1) {
                    String itemCount = count + " (" + inventoryCount + ")";
                    this.fontRenderer.drawWithShadow(this.matrixStack, itemCount, 22, itemTop - 64, config.statusElements.textColor);
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

        if (config.statusElements.toggleCoordinatesStatus || config.statusElements.toggleDirectionStatus) {
            String coordDirectionStatus = "";
            Direction facing = this.player.getHorizontalFacing();
            String translatedDirection = new TranslatableText("text.direction.simple_utilities." + facing.asString()).getString();
            String direction = translatedDirection + " " + getOffset(facing);

            if (config.statusElements.toggleCoordinatesStatus) {
                String coordsFormat = "%.0f, %.0f, %.0f";
                coordDirectionStatus += String.format(coordsFormat, this.player.getX(), this.player.getY(), this.player.getZ());

                if (config.statusElements.toggleDirectionStatus) {
                    coordDirectionStatus += " (" + direction + ")";
                }
            } else if (config.statusElements.toggleDirectionStatus) {
                coordDirectionStatus += direction;
            }

            gameInfo.add(coordDirectionStatus);
        }

        if (config.statusElements.toggleFpsStatus) {
            // Get everything from fps debug string until the 's' from 'fps'
            // gameInfo.add(client.fpsDebugString.substring(0, client.fpsDebugString.indexOf("s") + 1));
            gameInfo.add(String.format("%d fps", ((GameClientMixin) MinecraftClient.getInstance()).getCurrentFps()));
        }

        // Get translated biome info
        if (client.world != null) {
            if (config.statusElements.toggleBiomeStatus) {
                Biome biome = this.client.world.getBiome(player.getBlockPos());
                Identifier biomeIdentifier = this.client.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);

                if (biomeIdentifier != null) {
                    String biomeName = new TranslatableText("biome." + biomeIdentifier.getNamespace() + "." + biomeIdentifier.getPath()).getString();
                    gameInfo.add(new TranslatableText("text.hud.simple_utilities.biome", capitalize(biomeName)).getString());
                }
            }

            if (config.statusElements.toggleGameTimeStatus) {
                // Add current parsed time
                gameInfo.add(parseTime(client.world.getTimeOfDay()));
            }
        }

        return gameInfo;
    }
}
