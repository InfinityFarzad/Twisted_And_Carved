package net.farzad.twisted_and_carved.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class DotParticle extends SingleQuadParticle {
    protected DotParticle(ClientLevel clientWorld, double d, double e, double f, SpriteSet provider) {
        super(clientWorld, d, e, f,provider.first());
        this.gravity = 0.0f;
        this.lifetime = (int) (20 * 1.8f);
        this.hasPhysics = false;
        this.yd = 0.08;
        this.quadSize = 0.02F;
        setSprite(provider.first());
    }

    @Override
    public void tick() {
        if (age >= lifetime || quadSize < 0) {
            this.remove();
        } else {

            if (age > lifetime / 3) {
                if (age > lifetime / 4 * 3) {
                    this.yd -= 0.002;
                } else {
                    this.yd += 0.002;
                    this.quadSize -= 0.002f;
                }
            }
        }
        super.tick();
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new DotParticle(world, x, y, z,spriteProvider);
        }
    }
}
