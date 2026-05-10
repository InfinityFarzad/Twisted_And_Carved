package net.farzad.twisted_and_carved.client.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class DotParticle extends SpriteBillboardParticle {
    protected DotParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider provider) {
        super(clientWorld, d, e, f);
        this.gravityStrength = 0.0f;
        this.maxAge = (int) (20 * 1.3f);
        this.collidesWithWorld = false;
        this.velocityY = 0.15;
        this.scale = 0.02F;
        setSprite(provider);
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
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
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
            return new DotParticle(world, x, y, z,spriteProvider);
        }
    }
}
