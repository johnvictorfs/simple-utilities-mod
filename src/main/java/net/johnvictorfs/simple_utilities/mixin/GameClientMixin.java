package net.johnvictorfs.simple_utilities.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface GameClientMixin {
    @Accessor("currentFps")
    public abstract int getCurrentFps();
}
