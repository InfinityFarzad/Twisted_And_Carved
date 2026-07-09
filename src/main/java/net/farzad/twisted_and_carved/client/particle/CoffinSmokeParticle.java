package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class CoffinSmokeParticle extends SingleQuadParticle {
    private final SpriteSet spriteProvider;
    private final int rotAngle;

    public CoffinSmokeParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider, TextureAtlasSprite sprite) {
        super(world,x,y,z,sprite);
        this.spriteProvider = spriteProvider;
        this.lifetime = 20 * 5;
        this.scale(4F);
        this.hasPhysics = true;
        this.setSize(0.05f,0.05f);
        this.friction = 1;
        this.rotAngle = this.random.nextBoolean() ? 1 : -1;
        this.gravity = 0;
        this.speedUpWhenYMotionIsBlocked = true;
        this.yd = 0.05;
        this.zd = 0.08 * (this.random.nextBoolean() ? 1 : -1);
        this.xd = 0.08 * (this.random.nextBoolean() ? 1 : -1);
    }

    public void tick() {
        if (this.age++ >= this.lifetime || this.alpha <= 0) {
            this.remove();
        } else {
            this.roll += (float) (0.4 + this.random.nextFloat() / 500) / this.age * rotAngle;
            this.oRoll = this.roll;
            this.alpha -= 0.15f;
            this.setSpriteFromAge(spriteProvider);
        }
        super.tick();
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class CoffinSmokeParticleFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public CoffinSmokeParticleFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            CoffinSmokeParticle smoke = new CoffinSmokeParticle(world, x, y, z, this.spriteProvider, spriteProvider.first());
            smoke.setAlpha(0.9F);
            smoke.setSprite(spriteProvider.first());
            return smoke;
        }
    }
}
