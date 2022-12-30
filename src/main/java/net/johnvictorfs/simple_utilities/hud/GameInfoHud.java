package net.johnvictorfs.simple_utilities.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig;
import net.johnvictorfs.simple_utilities.hud.elements.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

import java.util.List;


@Environment(EnvType.CLIENT)
public class GameInfoHud {
    private final MinecraftClient client;
    private ClientPlayerEntity player;
    private MatrixStack matrixStack;

    private List<SimpleHudElement> hudElements;


    public static SimpleUtilitiesConfig config;

    public GameInfoHud(MinecraftClient client) {
        this.client = client;

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

        this.hudElements = List.of(
                new EquipmentInfoElement(client, matrixStack),
                new GameInfoElement(client, matrixStack),
                new SprintingInfoElement(client, matrixStack),
                new StatusEffectElement(client, matrixStack));

        RenderSystem.enableBlend();

        this.drawInfos();

        this.client.getProfiler().pop();
    }

    private void drawInfos() {
        for (SimpleHudElement element : hudElements) {
            element.draw();
        }
    }
}
