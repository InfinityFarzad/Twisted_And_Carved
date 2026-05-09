package net.farzad.twisted_and_carved.client.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class CoffinSmokeParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private final int rotAngle;

    public CoffinSmokeParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.maxAge = 20 * 5;
        this.scale(4F);
        this.collidesWithWorld = true;
        this.setBoundingBoxSpacing(0.05f,0.05f);
        this.velocityMultiplier = 1;
        this.rotAngle = this.random.nextBoolean() ? 1 : -1;
        this.gravityStrength = 0;
        this.ascending = true;
        this.velocityY = 0.05;
        this.velocityZ = 0.08 * (this.random.nextBoolean() ? 1 : -1);
        this.velocityX = 0.08 * (this.random.nextBoolean() ? 1 : -1);
    }

    public void tick() {
        if (this.age++ >= this.maxAge || this.alpha <= 0) {
            this.markDead();
        } else {
            this.angle += (float) (0.4 + this.random.nextFloat() / 500) / this.age * rotAngle;
            this.lastAngle = this.angle;
            this.alpha -= 0.15f;
            this.setSpriteForAge(spriteProvider);
        }
        super.tick();
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class CoffinSmokeParticleFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public CoffinSmokeParticleFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            CoffinSmokeParticle smoke = new CoffinSmokeParticle(clientWorld, d, e, f, this.spriteProvider);
            smoke.setAlpha(0.9F);
            smoke.setSprite(this.spriteProvider);
            return smoke;
        }
    }
}
