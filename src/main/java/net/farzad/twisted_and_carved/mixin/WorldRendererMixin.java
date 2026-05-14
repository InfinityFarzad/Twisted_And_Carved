package net.farzad.twisted_and_carved.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    @Nullable
    private ClientWorld world;

    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;useThickFog(II)Z")
    )
    private boolean twisted_and_carved$applyFog(boolean original) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.getWorld() != null && client.getCameraEntity() != null) {
            return original || client.player.getWorld().getBiome(client.getCameraEntity().getBlockPos()).isIn(ModTags.Biomes.THICK_FOG);
        } else {
            return original;
        }
    }

}
