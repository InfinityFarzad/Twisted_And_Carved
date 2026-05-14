package net.farzad.twisted_and_carved.client.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Quaternionf;

public class DirectionalSlashParticle extends SpriteBillboardParticle {
    private final float yaw;
    private final SpriteProvider spriteProvider;
    private final float offset;

    DirectionalSlashParticle(ClientWorld world, double x, double y, double z, float yaw, float scale, boolean hasZOffset, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.scale = scale;
        this.yaw = yaw;
        this.maxAge = 4;
        this.gravityStrength = 0.0F;
        this.angle =0;
        this.offset = random.nextBetween(-360,360) * (hasZOffset ? 1 : 0);
        this.lastAngle =0;
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
        }
    }

    @Override
    public float getSize(float tickProgress) {
        return this.scale;
    }

    @Override
    protected void render(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float tickProgress) {

        Quaternionf rotation = new Quaternionf();

        rotation.rotateY((float) Math.toRadians (-this.yaw));
        rotation.rotateX((float) Math.toRadians(90));
        rotation.rotateY((float) Math.toRadians(offset));
        super.render(vertexConsumer, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        super.render(vertexConsumer, camera, rotation, tickProgress);
    }
    @Override
    public int getBrightness(float tint) {
        return 240;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class FalchionSlashFactory implements ParticleFactory<FalchionSlashEffect> {
        private final SpriteProvider spriteProvider;

        public FalchionSlashFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(FalchionSlashEffect slashEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DirectionalSlashParticle(clientWorld, d, e, f, slashEffect.yaw(),2,true,spriteProvider);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class HarvestSlashFactory implements ParticleFactory<HarvestSlashEffect> {
        private final SpriteProvider spriteProvider;

        public HarvestSlashFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(HarvestSlashEffect slashEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DirectionalSlashParticle(clientWorld, d, e, f, slashEffect.yaw(),1,false,spriteProvider);
        }
    }
}
