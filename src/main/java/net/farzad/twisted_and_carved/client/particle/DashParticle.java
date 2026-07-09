package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class DashParticle extends SingleQuadParticle {
    private final float yaw;
    private final float pitch;
    private final SpriteSet spriteProvider;
    private final float offset;

    DashParticle(ClientLevel world, double x, double y, double z, float yaw, float pitch, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0,spriteProvider.first());
        this.quadSize = 2;
        this.alpha = 1.0f;
        this.pitch = pitch;
        this.yaw = yaw;
        this.lifetime = 4;
        this.gravity = 0.0F;
        this.roll =0;
        this.offset = world.getRandom().nextIntBetweenInclusive(-360,360);
        this.oRoll =0;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime || this.alpha <= 0) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.spriteProvider);
            this.alpha -= (float)(1.0 / (double)this.lifetime) ;
        }
    }

    @Override
    public float getQuadSize(float tickProgress) {
        return this.quadSize;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState submittable, Camera camera, Quaternionf rotation, float tickProgress) {
        Quaternionf customRotation = new Quaternionf();
        customRotation.rotateY(this.yaw);
        customRotation.rotateX(-this.pitch);
        customRotation.rotateZ((float) Math.toRadians(offset));
        super.extractRotatedQuad(submittable, camera, customRotation, tickProgress);

        customRotation.rotateY((float) Math.toRadians(180));
        super.extractRotatedQuad(submittable, camera, customRotation, tickProgress);
    }

    @Override
    protected int getLightCoords(float a) {
        return 200;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<DashEffect> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(DashEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new DashParticle(world, x, y, z, parameters.yaw(),parameters.pitch(),spriteProvider);
        }
    }
}
