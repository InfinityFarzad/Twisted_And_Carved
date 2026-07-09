package net.farzad.twisted_and_carved.client.particle;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FalchionSlashEffect(float yaw) implements ParticleOptions {

    public static final MapCodec<FalchionSlashEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.FLOAT.fieldOf("yaw").forGetter(particleEffect -> particleEffect.yaw)).apply(instance, FalchionSlashEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FalchionSlashEffect> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, FalchionSlashEffect::yaw, FalchionSlashEffect::new);

    @Override
    public ParticleType<FalchionSlashEffect> getType() {
        return TCParticles.FALCHION_SLASH;
    }
}
