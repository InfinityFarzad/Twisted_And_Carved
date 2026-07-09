package net.farzad.twisted_and_carved.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class ParryParticle extends SingleQuadParticle {

    public ParryParticle(ClientLevel clientWorld, double x, double y, double z,
                         SpriteSet spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed,spriteProvider.first());

        this.friction = 0f;
        this.quadSize = 0.25f;
        this.lifetime = 10;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.alpha = 0.005f;
        this.setSpriteFromAge(spriteProvider);
    }

    private void fadeOut() {
        this.alpha = ((1 / (float) lifetime) * age + 1);
    }

    @Override
    public void tick() {
        fadeOut();
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.quadSize = this.quadSize + 0.05f;
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
            return new ParryParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
