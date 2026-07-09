package net.farzad.twisted_and_carved.client.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;


public class TwistedGlaiveSweepParticle extends SingleQuadParticle {

    private float rotationAngle = 0;

    public TwistedGlaiveSweepParticle(ClientLevel clientWorld, double x, double y, double z,
                                      SpriteSet spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed,spriteProvider.first());

        this.friction = 0f;
        this.quadSize = 1f;
        this.lifetime = 15;
        this.setSpriteFromAge(spriteProvider);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.alpha = 0.005f;
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState submittable, Camera camera, Quaternionf q, float tickProgress) {
        Quaternionf rotation = new Quaternionf();

        rotation.rotateY((float) Math.toRadians(rotationAngle * -1));
        rotation.rotateX((float) Math.toRadians(90));
        super.extractRotatedQuad(submittable, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        rotation.rotateZ((float) Math.toRadians(rotationAngle * -1));
        super.extractRotatedQuad(submittable, camera, rotation, tickProgress);
    }

    private void fadeOut() {
        this.alpha = (-(1 / (float) lifetime) * age + 1);
    }

    @Override
    public void tick() {

        fadeOut();
        rotationAngle = (rotationAngle + 15) % 360;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.quadSize = this.quadSize + 0.5f;
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new TwistedGlaiveSweepParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
