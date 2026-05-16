package net.farzad.twisted_and_carved.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class ParryParticle extends BillboardParticle {

    public ParryParticle(ClientWorld clientWorld, double x, double y, double z,
                         SpriteProvider spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed,spriteProvider.getFirst());

        this.velocityMultiplier = 0f;
        this.scale = 0.25f;
        this.maxAge = 10;
        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        this.alpha = 0.005f;
        this.updateSprite(spriteProvider);
    }

    private void fadeOut() {
        this.alpha = ((1 / (float) maxAge) * age + 1);
    }

    @Override
    public void tick() {
        fadeOut();
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.scale = this.scale + 0.05f;
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
            return new ParryParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
