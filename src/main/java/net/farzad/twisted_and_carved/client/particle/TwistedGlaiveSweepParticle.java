package net.farzad.twisted_and_carved.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;


public class TwistedGlaiveSweepParticle extends BillboardParticle {

    private float rotationAngle = 0;

    public TwistedGlaiveSweepParticle(ClientWorld clientWorld, double x, double y, double z,
                                      SpriteProvider spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed,spriteProvider.getFirst());

        this.velocityMultiplier = 0f;
        this.scale = 1f;
        this.maxAge = 15;
        this.updateSprite(spriteProvider);
        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        this.alpha = 0.005f;
    }

    @Override
    protected void render(BillboardParticleSubmittable submittable, Camera camera, Quaternionf q, float tickProgress) {
        Quaternionf rotation = new Quaternionf();

        rotation.rotateY((float) Math.toRadians(rotationAngle * -1));
        rotation.rotateX((float) Math.toRadians(90));
        super.render(submittable, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        rotation.rotateZ((float) Math.toRadians(rotationAngle * -1));
        super.render(submittable, camera, rotation, tickProgress);
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
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }


    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new TwistedGlaiveSweepParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
