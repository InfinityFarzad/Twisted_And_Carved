package net.farzad.twisted_and_carved.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class DotParticle extends BillboardParticle {
    protected DotParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider provider) {
        super(clientWorld, d, e, f,provider.getFirst());
        this.gravityStrength = 0.0f;
        this.maxAge = (int) (20 * 1.8f);
        this.collidesWithWorld = false;
        this.velocityY = 0.08;
        this.scale = 0.02F;
        setSprite(provider.getFirst());
    }

    @Override
    public void tick() {
        if (age >= maxAge || scale < 0) {
            this.markDead();
        } else {

            if (age > maxAge / 3) {
                if (age > maxAge / 4 * 3) {
                    this.velocityY -= 0.002;
                } else {
                    this.velocityY += 0.002;
                    this.scale -= 0.002f;
                }
            }
        }
        super.tick();
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new DotParticle(world, x, y, z,spriteProvider);
        }
    }
}
