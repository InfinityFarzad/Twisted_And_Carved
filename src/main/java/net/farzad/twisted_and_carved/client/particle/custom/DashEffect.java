package net.farzad.twisted_and_carved.client.particle.custom;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record DashEffect(float yaw, float pitch) implements ParticleEffect {

    public static final MapCodec<DashEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.FLOAT.fieldOf("yaw").forGetter(particleEffect -> particleEffect.yaw)).and(Codec.FLOAT.fieldOf("pitch").forGetter(particleEffect -> particleEffect.pitch)).apply(instance, DashEffect::new)
    );
    public static final PacketCodec<RegistryByteBuf, DashEffect> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, DashEffect::yaw, PacketCodecs.FLOAT,DashEffect::pitch,DashEffect::new);

    @Override
    public ParticleType<DashEffect> getType() {
        return ModParticles.DASH_PARTICLE;
    }
}
