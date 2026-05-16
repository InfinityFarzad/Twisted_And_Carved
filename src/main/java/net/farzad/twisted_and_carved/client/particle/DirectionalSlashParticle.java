package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class DirectionalSlashParticle extends BillboardParticle {
    private final float yaw;
    private final SpriteProvider spriteProvider;
    private final float offset;

    DirectionalSlashParticle(ClientWorld world, double x, double y, double z, float yaw, float scale, boolean hasZOffset, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0,spriteProvider.getFirst());
        this.scale = scale;
        this.yaw = yaw;
        this.maxAge = 4;
        this.gravityStrength = 0.0F;
        this.zRotation =0;
        this.offset = random.nextBetween(-360,360) * (hasZOffset ? 1 : 0);
        this.lastZRotation =0;
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        this.spriteProvider = spriteProvider;
        this.updateSprite(spriteProvider);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.updateSprite(this.spriteProvider);
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
    protected void render(BillboardParticleSubmittable submittable, Camera camera, Quaternionf q, float tickProgress) {
        Quaternionf rotation = new Quaternionf();

        rotation.rotateY((float) Math.toRadians (-this.yaw));
        rotation.rotateX((float) Math.toRadians(90));
        rotation.rotateY((float) Math.toRadians(offset));
        super.render(submittable, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        super.render(submittable, camera, rotation, tickProgress);
    }

    @Override
    public int getBrightness(float tint) {
        return 240;
    }

    @Environment(EnvType.CLIENT)
    public static class FalchionSlashFactory implements ParticleFactory<FalchionSlashEffect> {
        private final SpriteProvider spriteProvider;

        public FalchionSlashFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(FalchionSlashEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new DirectionalSlashParticle(world, x, y, z, parameters.yaw(),4,true,spriteProvider);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class HarvestSlashFactory implements ParticleFactory<HarvestSlashEffect> {
        private final SpriteProvider spriteProvider;

        public HarvestSlashFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(HarvestSlashEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new DirectionalSlashParticle(world, x, y, z, parameters.yaw(),1,false,spriteProvider);

        }
    }
}
