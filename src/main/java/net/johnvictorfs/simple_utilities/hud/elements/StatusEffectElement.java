package net.johnvictorfs.simple_utilities.hud.elements;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig.Point;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig;
import net.johnvictorfs.simple_utilities.helpers.StringUtils;
import net.johnvictorfs.simple_utilities.hud.GameInfoHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Map;

import static net.johnvictorfs.simple_utilities.hud.GameInfoHud.config;

@Environment(EnvType.CLIENT)


public class StatusEffectElement implements SimpleHudElement {
    private final TextRenderer fontRenderer;
    private ClientPlayerEntity player;
    private MatrixStack matrixStack;
    private Point position;

    public StatusEffectElement(MinecraftClient client, MatrixStack matrixStack) {
        this.fontRenderer = client.textRenderer;
        this.player = client.player;
        this.matrixStack = matrixStack;
    }

    public void draw() {
        position = config.statusElements.statusEffectInfoPosition;
        if (this.player != null) {
            Map<StatusEffect, StatusEffectInstance> effects = this.player.getActiveStatusEffects();

            for (Map.Entry<StatusEffect, StatusEffectInstance> effect : effects.entrySet()) {
                String effectName = I18n.translate(effect.getKey().getTranslationKey());

                String duration = StringUtils.secondsToString(effect.getValue().getDuration() / 20);

                int color = effect.getKey().getColor();

                this.fontRenderer.drawWithShadow(this.matrixStack, effectName + " " + duration,  position.x, position.y, color);
            }
        }
    }


}
