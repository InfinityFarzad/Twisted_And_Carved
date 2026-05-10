package net.farzad.twisted_and_carved.client.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Quaternionf;

public class DashParticle extends SpriteBillboardParticle {
    private final float yaw;
    private final float pitch;
    private final SpriteProvider spriteProvider;
    private final float offset;

    DashParticle(ClientWorld world, double x, double y, double z, float yaw, float pitch, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.scale = 2;
        this.alpha = 1.0f;
        this.pitch = pitch;
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
        if (this.age++ >= this.maxAge || this.alpha <= 0) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            this.alpha -= (float)(1.0 / (double)this.maxAge) ;
        }
    }

    @Override
    public float getSize(float tickProgress) {
        return this.scale;
    }

    @Override
    protected void render(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float tickProgress) {

        Quaternionf rotation = new Quaternionf();
        System.out.println(yaw + "yaw" + pitch + "pitch");
        rotation.rotateY(this.yaw);
        rotation.rotateX(-this.pitch);
        rotation.rotateZ((float) Math.toRadians(offset));
        super.render(vertexConsumer, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        super.render(vertexConsumer, camera, rotation, tickProgress);
    }
    @Override
    public int getBrightness(float tint) {
        return 200;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DashEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DashEffect dashEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DashParticle(clientWorld, d, e, f, dashEffect.yaw(),dashEffect.pitch(),spriteProvider);
        }
    }
}
