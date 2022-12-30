package net.johnvictorfs.simple_utilities.hud.elements;

import com.google.common.collect.Lists;
import net.johnvictorfs.simple_utilities.config.SimpleUtilitiesConfig.Point;
import net.johnvictorfs.simple_utilities.helpers.Colors;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.johnvictorfs.simple_utilities.hud.GameInfoHud.config;

public class EquipmentInfoElement implements SimpleHudElement{
    private final TextRenderer fontRenderer;
    private ClientPlayerEntity player;
    private MatrixStack matrixStack;
    private final ItemRenderer itemRenderer;
    private MinecraftClient client;

    public EquipmentInfoElement(MinecraftClient client, MatrixStack matrixStack) {
        this.client = client;
        this.fontRenderer = client.textRenderer;
        this.itemRenderer = client.getItemRenderer();
        this.player = client.player;
        this.matrixStack = matrixStack;

    }

    public void draw() {
        Point position = config.statusElements.equipmentInfoPosition;
        // This is the amount of space that should go between where the icon is rendered and where the text begins.
        final int ICON_OFFSET = 20;

        // This is to center the durability text relative to the icon.
        final int VERTICAL_TEXT_OFFSET = 4;
        if (config.statusElements.toggleEquipmentStatus && !(this.client.currentScreen instanceof ChatScreen)) {

            List<ItemStack> equippedItems = new ArrayList<>();
            PlayerInventory inventory = this.player.getInventory();
            int maxLineHeight = Math.max(10, this.fontRenderer.getWidth(""));

            ItemStack mainHandItem = inventory.getMainHandStack();
            maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getWidth(I18n.translate(mainHandItem.getTranslationKey())));
            equippedItems.add(mainHandItem);

            for (ItemStack secondHandItem : inventory.offHand) {
                maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getWidth(I18n.translate(secondHandItem.getTranslationKey())));
                equippedItems.add(secondHandItem);
            }

            for (ItemStack armourItem : this.player.getInventory().armor) {
                maxLineHeight = Math.max(maxLineHeight, this.fontRenderer.getWidth(I18n.translate(armourItem.getTranslationKey())));
                equippedItems.add(armourItem);
            }

            maxLineHeight = (int) (Math.ceil(maxLineHeight / 5.0D + 0.5D) * 5);
            int itemTop = maxLineHeight;

            int lineHeight = this.fontRenderer.fontHeight + VERTICAL_TEXT_OFFSET;

            // Draw in order Helmet -> Chestplate -> Leggings -> Boots
            for (ItemStack equippedItem : Lists.reverse(equippedItems)) {
                if (equippedItem.getItem().equals(Blocks.AIR.asItem())) {
                    // Skip empty slots
                    continue;
                }
                int iconYPos = itemTop + position.y;
                int textYPos = iconYPos + VERTICAL_TEXT_OFFSET;
                this.itemRenderer.renderInGuiWithOverrides(equippedItem, position.x, iconYPos);

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

                    this.fontRenderer.drawWithShadow(this.matrixStack, itemDurability,  position.x + ICON_OFFSET, textYPos, color);
                } else {
                    int inventoryCount = inventory.count(equippedItem.getItem());
                    int count = equippedItem.getCount();

                    if (inventoryCount > 1) {
                        String itemCount = count + " (" + inventoryCount + ")";
                        this.fontRenderer.drawWithShadow(this.matrixStack, itemCount, position.x + ICON_OFFSET, textYPos, config.statusElements.textColor);
                    }
                }

                itemTop += lineHeight;
            }
        }
    }
}
