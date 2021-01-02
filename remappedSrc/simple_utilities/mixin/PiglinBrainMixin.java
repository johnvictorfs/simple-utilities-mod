package net.johnvictorfs.simple_utilities.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(value = PiglinBrain.class)
public class PiglinBrainMixin {
    private static double enderPearlTrades = 0;

    @Inject(method = "getBarteredItem", at = @At("HEAD"))
    private static void getBarteredItem(PiglinEntity piglinEntity, CallbackInfoReturnable<List<ItemStack>> cir) {
        double tradeCount = 1_000_000_000;
        for (int trades = 0; trades < tradeCount; trades++) {
            LootTable lootTable = Objects.requireNonNull(piglinEntity.world.getServer()).getLootManager().getTable(LootTables.PIGLIN_BARTERING_GAMEPLAY);
            List<ItemStack> list = lootTable.generateLoot((new LootContext.Builder((ServerWorld)piglinEntity.world)).parameter(LootContextParameters.THIS_ENTITY, piglinEntity).random(piglinEntity.world.random).build(LootContextTypes.BARTER));

            list.forEach(itemStack -> {
                if (itemStack.getItem().equals(Items.ENDER_PEARL)) {
                    enderPearlTrade();
                }
            });
        }

        System.out.println("Ender Pearls in 10,000,000 trades: " + PiglinBrainMixin.enderPearlTrades);
        double dropRate = PiglinBrainMixin.enderPearlTrades / tradeCount;
        System.out.println("Drop Rate: " + dropRate + " (" + (dropRate * 100) + "%" + ")");
        System.out.println("Expected Drop Rate: 4.72%");
    }

    private static void enderPearlTrade() {
        enderPearlTrades++;
    }
}
