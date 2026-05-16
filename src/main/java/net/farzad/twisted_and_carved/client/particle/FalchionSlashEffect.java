package net.farzad.twisted_and_carved.client.particle;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record FalchionSlashEffect(float yaw) implements ParticleEffect {

    public static final MapCodec<FalchionSlashEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.FLOAT.fieldOf("yaw").forGetter(particleEffect -> particleEffect.yaw)).apply(instance, FalchionSlashEffect::new)
    );
    public static final PacketCodec<RegistryByteBuf, FalchionSlashEffect> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, FalchionSlashEffect::yaw, FalchionSlashEffect::new);

    @Override
    public ParticleType<FalchionSlashEffect> getType() {
        return TCParticles.FALCHION_SLASH;
    }
}
