package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class DashParticle extends BillboardParticle {
    private final float yaw;
    private final float pitch;
    private final SpriteProvider spriteProvider;
    private final float offset;

    DashParticle(ClientWorld world, double x, double y, double z, float yaw, float pitch, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0,spriteProvider.getFirst());
        this.scale = 2;
        this.alpha = 1.0f;
        this.pitch = pitch;
        this.yaw = yaw;
        this.maxAge = 4;
        this.gravityStrength = 0.0F;
        this.zRotation =0;
        this.offset = world.getRandom().nextBetween(-360,360);
        this.lastZRotation =0;
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        this.spriteProvider = spriteProvider;
        this.updateSprite(spriteProvider);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge || this.alpha <= 0) {
            this.markDead();
        } else {
            this.updateSprite(this.spriteProvider);
            this.alpha -= (float)(1.0 / (double)this.maxAge) ;
        }
    }

    @Override
    public float getSize(float tickProgress) {
        return this.scale;
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    @Override
    protected void render(BillboardParticleSubmittable submittable, Camera camera, Quaternionf rotation, float tickProgress) {
        Quaternionf customRotation = new Quaternionf();
        customRotation.rotateY(this.yaw);
        customRotation.rotateX(-this.pitch);
        customRotation.rotateZ((float) Math.toRadians(offset));
        super.render(submittable, camera, customRotation, tickProgress);

        customRotation.rotateY((float) Math.toRadians(180));
        super.render(submittable, camera, customRotation, tickProgress);
    }

    @Override
    public int getBrightness(float tint) {
        return 200;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DashEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(DashEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new DashParticle(world, x, y, z, parameters.yaw(),parameters.pitch(),spriteProvider);
        }
    }
}
