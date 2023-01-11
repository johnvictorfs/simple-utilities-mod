package net.johnvictorfs.simple_utilities.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig;
import net.johnvictorfs.simple_utilities.helpers.Colors;
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
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.*;

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
        if (!config.uiConfig.toggleSimpleUtilitiesHUD) return;

        this.player = this.client.player;

        this.matrixStack = matrixStack;

        RenderSystem.enableBlend();

        this.drawInfos();

        this.client.getProfiler().pop();
    }

    private void drawInfos() {
        // Draw lines of Array of Game info in the screen
        List<String> gameInfo = getGameInfo();

        if (config.uiConfig.toggleEquipmentStatus && !(this.client.currentScreen instanceof ChatScreen)) {
            drawEquipmentInfo();
        }

        // Get the longest string in the array
        int longestString = 0;
        int BoxWidth = 0;
        for (String s : gameInfo) {
            if (s.length() > longestString) {
                longestString = s.length();
                BoxWidth = this.fontRenderer.getWidth(s);
            }
        }

        int lineHeight = this.fontRenderer.fontHeight + 2;
        int configXPosition = config.statusElements.Xcords;
        int yAxis = (((this.client.getWindow().getScaledHeight()) - ((lineHeight + 4) * gameInfo.size())) + (lineHeight + 4)) * (config.statusElements.Ycords) / 100;
        int xAxis = (((this.client.getWindow().getScaledWidth() - 4) - 4) - (BoxWidth)) * configXPosition / 100;

        // Add Padding to left of the screen
        if (xAxis <= 4) {
            xAxis = 4;
        }

        for (String line : gameInfo) {
            int offset = 0;
            if (configXPosition >= 50) {
                int lineLength = this.fontRenderer.getWidth(line);
                offset = (BoxWidth - lineLength);
            }

            this.fontRenderer.drawWithShadow(this.matrixStack, line, xAxis + offset, yAxis + 4, config.uiConfig.textColor);
            yAxis += lineHeight;
        }

        if (config.uiConfig.toggleSprintStatus && (this.client.options.sprintKey.isPressed() || this.player.isSprinting())) {
            this.drawSprintingInfo();
        }
    }

    private void drawSprintingInfo() {
        final String sprintingText = (Text.translatable("text.hud.simple_utilities.sprinting")).getString();

        int yAxis = (((this.client.getWindow().getScaledHeight() - this.fontRenderer.fontHeight + 2) - 4) * (config.uiConfig.sprintStatusLocationY) / 100);
        int xAxis = (((this.client.getWindow().getScaledWidth() - this.fontRenderer.getWidth(sprintingText)) - 8) * (config.uiConfig.sprintStatusLocationX) / 100);

        // Add Padding to left of the screen
        if (xAxis <= 4) {
            xAxis = 4;
        }

        // Add Padding to top of the screen
        if (yAxis <= 4) {
            yAxis = 4;
        }

        // Sprinting Info
        this.fontRenderer.drawWithShadow(this.matrixStack, sprintingText, xAxis, yAxis, config.uiConfig.textColor);
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
        // Get Items from Inventory
        List<EquipmentInfoStack> equipmentInfo = new ArrayList<>(
                Arrays.asList(
                        new EquipmentInfoStack(this.player.getInventory().getArmorStack(3)),
                        new EquipmentInfoStack(this.player.getInventory().getArmorStack(2)),
                        new EquipmentInfoStack(this.player.getInventory().getArmorStack(1)),
                        new EquipmentInfoStack(this.player.getInventory().getArmorStack(0)),
                        new EquipmentInfoStack(this.player.getOffHandStack()),
                        new EquipmentInfoStack(this.player.getMainHandStack())
                )
        );

        // Remove Air Blocks from the list
        equipmentInfo.removeIf(equipment -> equipment.getItem().getItem().equals(Blocks.AIR.asItem()));

        // Get each Items String and Color
        for (EquipmentInfoStack index : equipmentInfo) {
            ItemStack item = index.getItem();

            if (item.getMaxDamage() != 0) {
                int currentDurability = item.getMaxDamage() - item.getDamage();

                // Draw Durability
                index.setText(String.format("%s/%s", currentDurability, item.getMaxDamage()));

                // Default Durability Color
                if (currentDurability <= (item.getMaxDamage()) / 4) {
                    index.setColor(Colors.lightRed);
                } else if (currentDurability <= (item.getMaxDamage() / 2.5)) {
                    index.setColor(Colors.lightOrange);
                } else if (currentDurability <= (item.getMaxDamage() / 1.5)) {
                    index.setColor(Colors.lightYellow);
                } else if (currentDurability < item.getMaxDamage()) {
                    index.setColor(Colors.lightGreen);
                } else {
                    index.setColor(config.uiConfig.textColor);
                }
            } else {
                // Draw Count
                index.setText((item.getCount() + " (" + this.player.getInventory().count(item.getItem()) + ")"));
            }
        }

        // Get the longest string in the array
        int longestString = 0;
        int BoxWidth = 0;
        for (EquipmentInfoStack index : equipmentInfo) {
            String s = index.getText();
            if (s.length() > longestString) {
                longestString = s.length();
                BoxWidth = this.fontRenderer.getWidth(s);
            }
        }

        // Screen Size Calculations
        int configX = config.uiConfig.equipmentLocationX;
        int lineHeight = this.fontRenderer.fontHeight + 8;
        int yAxis = (this.client.getWindow().getScaledHeight() - (lineHeight * equipmentInfo.size())) * (config.uiConfig.equipmentLocationY) / 100;
        int xAxis = ((this.client.getWindow().getScaledWidth() - 8) - (BoxWidth + 20)) * configX / 100;

        // Add Padding to left of the screen
        if (xAxis <= 4) {
            xAxis = 4;
        }

        // Add Padding to top of the screen
        if (yAxis <= 4) {
            yAxis = 4;
        }

        // Draw All Items on Screen
        for (EquipmentInfoStack index : equipmentInfo) {
            ItemStack item = index.getItem();

            if (configX >= 50) {
                int lineLength = this.fontRenderer.getWidth(index.getText());
                int offset = (BoxWidth - lineLength);

                this.fontRenderer.drawWithShadow(this.matrixStack, index.getText(), xAxis + offset + 2, yAxis, index.getColor());
                this.itemRenderer.renderInGuiWithOverrides(item, xAxis + BoxWidth + 6, yAxis - 5);
            } else {
                this.itemRenderer.renderInGuiWithOverrides(item, xAxis, yAxis - 5);
                this.fontRenderer.drawWithShadow(this.matrixStack, index.getText(), xAxis + 20, yAxis, index.getColor());
            }
            yAxis += lineHeight;
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
            String translatedDirection = Text.translatable("text.direction.simple_utilities." + facing.asString()).getString();
            String direction = translatedDirection + " " + getOffset(facing);

            if (config.statusElements.toggleCoordinatesStatus) {
                coordDirectionStatus += String.format("%d, %d, %d", this.player.getBlockPos().getX(), this.player.getBlockPos().getY(), this.player.getBlockPos().getZ());

                if (config.statusElements.toggleDirectionStatus) {
                    coordDirectionStatus += " (" + direction + ")";
                }
            } else if (config.statusElements.toggleDirectionStatus) {
                coordDirectionStatus += direction;
            }

            gameInfo.add(coordDirectionStatus);
        }

        if (config.statusElements.toggleNetherCoordinateConversion) {
            String coordsFormat = "X: %.0f, Z: %.0f";
            if (this.player.getWorld().getRegistryKey().getValue().toString().equals("minecraft:overworld")) {
                gameInfo.add("Nether: " + String.format(coordsFormat, this.player.getX() / 8, this.player.getZ() / 8));
            } else if (this.player.getWorld().getRegistryKey().getValue().toString().equals("minecraft:the_nether")) {
                gameInfo.add("Overworld: " + String.format(coordsFormat, this.player.getX() * 8, this.player.getZ() * 8));
            }
        }

        if (config.statusElements.toggleFpsStatus) {
            // Get everything from fps debug string until the 's' from 'fps'
            // gameInfo.add(client.fpsDebugString.substring(0, client.fpsDebugString.indexOf("s") + 1));
            gameInfo.add(String.format("%d fps", ((GameClientMixin) MinecraftClient.getInstance()).getCurrentFps()));
        }

        // Player Speed
        if (config.statusElements.togglePlayerSpeedStatus) {
            // Calculating Speed
            Vec3d playerPosVec = Objects.requireNonNull(client.player).getPos();
            double travelledX = playerPosVec.x - client.player.prevX;
            double travelledZ = playerPosVec.z - client.player.prevZ;
            double currentSpeed = MathHelper.sqrt((float)(travelledX * travelledX + travelledZ * travelledZ));
            //double currentVertSpeed = playerPosVec.y - client.player.prevY;
            gameInfo.add(String.format("%.2f m/s", currentSpeed / 0.05F));
        }

        // Get translated biome info
        if (client.world != null) {

            // Light Level
            if (config.statusElements.toggleLightLevelStatus) {
                int lightLevel = client.world.getLightLevel(Objects.requireNonNull(client.player).getBlockPos());
                gameInfo.add("Light Level: " + lightLevel);
            }

            if (config.statusElements.toggleBiomeStatus) {
                Optional<RegistryKey<Biome>> biome = this.client.world.getBiome(player.getBlockPos()).getKey();

                if (biome.isPresent()) {
                    String biomeName = Text.translatable("biome." + biome.get().getValue().getNamespace() + "." + biome.get().getValue().getPath()).getString();
                    gameInfo.add(Text.translatable("text.hud.simple_utilities.biome", capitalize(biomeName)).getString());
                }
            }

            if (config.statusElements.toggleGameTimeStatus) {
                // Add current parsed time
                gameInfo.add(parseTime(client.world.getTimeOfDay()));
            }
        }

        if (config.statusElements.togglePlayerName) {
            gameInfo.add(player.getEntityName());
        }

        // Just Don't Create a Text Line if there is no Info
        if (client.getCurrentServerEntry() != null) {
            if (config.statusElements.toggleServerName) {
                String serverName = client.getCurrentServerEntry().name;
                gameInfo.add(serverName);
            }

            if (config.statusElements.toggleServerAddress) {
                String serverIp = client.getCurrentServerEntry().address;
                gameInfo.add(serverIp);
            }
        }



        return gameInfo;
    }

    private static class EquipmentInfoStack {
        private final ItemStack item;
        private int color;
        private String text;

        public EquipmentInfoStack(ItemStack item) {
            this.item = item;
            this.text = "";
            this.color = 0x00E0E0E0;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getColor() {
            return color;
        }

        public String getText() {
            return text;
        }
    }
}
