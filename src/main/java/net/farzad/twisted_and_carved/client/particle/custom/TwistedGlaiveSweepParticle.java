package net.farzad.twisted_and_carved.client.particle.custom;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;


public class TwistedGlaiveSweepParticle extends SpriteBillboardParticle {

    private float rotationAngle = 0;

    public TwistedGlaiveSweepParticle(ClientWorld clientWorld, double x, double y, double z,
                                      SpriteProvider spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed);

        this.velocityMultiplier = 0f;
        this.scale = 1f;
        this.maxAge = 15;
        this.setSpriteForAge(spriteProvider);
        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        this.alpha = 0.005f;
    }

    @Override
    protected void render(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float tickProgress) {

        Quaternionf rotation = new Quaternionf();

        rotation.rotateY((float) Math.toRadians(rotationAngle * -1));
        rotation.rotateX((float) Math.toRadians(90));
        super.render(vertexConsumer, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        rotation.rotateZ((float) Math.toRadians(rotationAngle * -1));
        super.render(vertexConsumer, camera, rotation, tickProgress);

    }

    private void fadeOut() {
        this.alpha = (-(1 / (float) maxAge) * age + 1);
    }

    @Override
    public void tick() {

        fadeOut();
        rotationAngle = (rotationAngle + 15) % 360;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.scale = this.scale + 0.5f;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new TwistedGlaiveSweepParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
