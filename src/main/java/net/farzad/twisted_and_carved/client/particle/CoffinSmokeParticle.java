package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

public class CoffinSmokeParticle extends BillboardParticle {
    private final SpriteProvider spriteProvider;
    private final int rotAngle;

    public CoffinSmokeParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, Sprite sprite) {
        super(world,x,y,z,sprite);
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
            this.zRotation += (float) (0.4 + this.random.nextFloat() / 500) / this.age * rotAngle;
            this.lastZRotation = this.zRotation;
            this.alpha -= 0.15f;
            this.updateSprite(spriteProvider);
        }
        super.tick();
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class CoffinSmokeParticleFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public CoffinSmokeParticleFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            CoffinSmokeParticle smoke = new CoffinSmokeParticle(world, x, y, z, this.spriteProvider, spriteProvider.getFirst());
            smoke.setAlpha(0.9F);
            smoke.setSprite(spriteProvider.getFirst());
            return smoke;
        }
    }
}
