package net.johnvictorfs.simple_utilities.hud.elements;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig.Point;
import net.johnvictorfs.simple_utilities.helpers.StringUtils;
import net.johnvictorfs.simple_utilities.mixin.GameClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

import static net.johnvictorfs.simple_utilities.helpers.StringUtils.capitalize;
import static net.johnvictorfs.simple_utilities.hud.GameInfoHud.config;

public class GameInfoElement implements SimpleHudElement{
    private final TextRenderer fontRenderer;
    private ClientPlayerEntity player;
    private MatrixStack matrixStack;
    private MinecraftClient client;

    public GameInfoElement(MinecraftClient client, MatrixStack matrixStack){
        this.client = client;
        this.fontRenderer = client.textRenderer;
        this.player = client.player;
        this.matrixStack = matrixStack;

    }
    public void draw(){
        Point position = config.statusElements.gameInfoElementPosition;
        List<String> gameInfo = getGameInfo();

        int lineHeight = this.fontRenderer.fontHeight + 2;
        int top = position.y;
        int left = position.x;

        for (String line : gameInfo) {
            this.fontRenderer.drawWithShadow(this.matrixStack, line, left, top, config.statusElements.textColor);
            top += lineHeight;
        }
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
    private List<String> getGameInfo() {
        List<String> gameInfo = new ArrayList<>();

        if (config.statusElements.toggleCoordinatesStatus || config.statusElements.toggleDirectionStatus) {
            String coordDirectionStatus = "";
            Direction facing = this.player.getHorizontalFacing();
            String translatedDirection = Text.translatable("text.direction.simple_utilities." + facing.asString()).getString();
            String direction = translatedDirection + " " + getOffset(facing);

            if (config.statusElements.toggleCoordinatesStatus) {
                String coordsFormat = "%d, %d, %d";
                coordDirectionStatus += String.format(coordsFormat, (int) this.player.getX(), (int) this.player.getY(), (int) this.player.getZ());

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

        // Get translated biome info
        if (client.world != null) {
            if (config.statusElements.toggleBiomeStatus) {
                RegistryEntry<Biome> biome = this.client.world.getBiome(player.getBlockPos());
                Identifier biomeIdentifier = this.client.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome.value());

                if (biomeIdentifier != null) {
                    String biomeName = Text.translatable("biome." + biomeIdentifier.getNamespace() + "." + biomeIdentifier.getPath()).getString();
                    gameInfo.add(Text.translatable("text.hud.simple_utilities.biome", capitalize(biomeName)).getString());
                }
            }

            if (config.statusElements.toggleGameTimeStatus) {
                // Add current parsed time
                gameInfo.add(StringUtils.parseTime(client.world.getTimeOfDay()));
            }
        }

        // 追加分

        if (config.statusElements.togglePlayerName) {
            gameInfo.add(player.getEntityName());
        }

        if (config.statusElements.toggleServerName) {
            String serverName = "Singleplayer";
            try {
                serverName = client.getCurrentServerEntry().name;
            } catch (Exception e) {

            }
            gameInfo.add(serverName);
        }

        if (config.statusElements.toggleServerAddress) {
            String serverIp = "N/A";
            try {
                serverIp = client.getCurrentServerEntry().address;
            } catch (Exception e) {

            }
            gameInfo.add(serverIp);
        }

        return gameInfo;
    }
}
