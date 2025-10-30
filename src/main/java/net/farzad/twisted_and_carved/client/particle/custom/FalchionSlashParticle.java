package net.farzad.twisted_and_carved.client.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Quaternionf;

public class FalchionSlashParticle extends SpriteBillboardParticle {
    private float yaw;
    private final SpriteProvider spriteProvider;
    private final float offset;

    FalchionSlashParticle(ClientWorld world, double x, double y, double z, float yaw, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.scale = 4;
        this.yaw = yaw;
        this.maxAge = 4;
        this.gravityStrength = 0.0F;
        this.angle =0;
        this.offset = world.getRandom().nextBetween(-360,360);
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
    public static class Factory implements ParticleFactory<FalchionSlashEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(FalchionSlashEffect slashEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            FalchionSlashParticle slashParticle = new FalchionSlashParticle(clientWorld, d, e, f, slashEffect.yaw(),spriteProvider);

            return slashParticle;
        }
    }
}
