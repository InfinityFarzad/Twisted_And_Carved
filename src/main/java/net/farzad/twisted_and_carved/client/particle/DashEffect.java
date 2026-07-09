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

public record DashEffect(float yaw, float pitch) implements ParticleOptions {

    public static final MapCodec<DashEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.FLOAT.fieldOf("yaw").forGetter(particleEffect -> particleEffect.yaw)).and(Codec.FLOAT.fieldOf("pitch").forGetter(particleEffect -> particleEffect.pitch)).apply(instance, DashEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DashEffect> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, DashEffect::yaw, ByteBufCodecs.FLOAT,DashEffect::pitch,DashEffect::new);

    @Override
    public ParticleType<DashEffect> getType() {
        return TCParticles.DASH_PARTICLE;
    }
}
