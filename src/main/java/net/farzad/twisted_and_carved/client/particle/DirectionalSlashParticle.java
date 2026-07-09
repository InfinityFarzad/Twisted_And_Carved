package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class DirectionalSlashParticle extends SingleQuadParticle {
    private final float yaw;
    private final SpriteSet spriteProvider;
    private final float offset;

    DirectionalSlashParticle(ClientLevel world, double x, double y, double z, float yaw, float scale, boolean hasZOffset, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0,spriteProvider.first());
        this.quadSize = scale;
        this.yaw = yaw;
        this.lifetime = 4;
        this.gravity = 0.0F;
        this.roll =0;
        this.offset = random.nextIntBetweenInclusive(-360,360) * (hasZOffset ? 1 : 0);
        this.oRoll =0;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.spriteProvider);
        }
    }

    @Override
    public float getQuadSize(float tickProgress) {
        return this.quadSize;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState submittable, Camera camera, Quaternionf q, float tickProgress) {
        Quaternionf rotation = new Quaternionf();

        rotation.rotateY((float) Math.toRadians (-this.yaw));
        rotation.rotateX((float) Math.toRadians(90));
        rotation.rotateY((float) Math.toRadians(offset));
        super.extractRotatedQuad(submittable, camera, rotation, tickProgress);

        rotation.rotateY((float) Math.toRadians(180));
        super.extractRotatedQuad(submittable, camera, rotation, tickProgress);
    }

    @Override
    public int getLightColor(float tint) {
        return 240;
    }

    @Environment(EnvType.CLIENT)
    public static class FalchionSlashFactory implements ParticleProvider<FalchionSlashEffect> {
        private final SpriteSet spriteProvider;

        public FalchionSlashFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(FalchionSlashEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new DirectionalSlashParticle(world, x, y, z, parameters.yaw(),4,true,spriteProvider);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class HarvestSlashFactory implements ParticleProvider<HarvestSlashEffect> {
        private final SpriteSet spriteProvider;

        public HarvestSlashFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(HarvestSlashEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new DirectionalSlashParticle(world, x, y, z, parameters.yaw(),1,false,spriteProvider);

        }
    }
}
