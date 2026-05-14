package net.farzad.twisted_and_carved.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Shadow
    private static boolean fogEnabled;

/*    @Inject(method = "applyFog", at = @At("RETURN"))
    private static void twisted_and_carved$ihateyoumojang(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickProgress, CallbackInfoReturnable<Fog> cir) {
        if (camera.getFocusedEntity().getWorld().getBiome(camera.getBlockPos()).isIn(ModTags.Biomes.TWISTED_FOREST) && fogEnabled && thickenFog)
        {
            fog
        }
    }*/





}
