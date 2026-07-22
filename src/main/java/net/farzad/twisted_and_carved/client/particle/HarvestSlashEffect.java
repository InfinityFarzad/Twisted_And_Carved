package net.farzad.twisted_and_carved.client.particle;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.farzad.twisted_and_carved.common.init.client.TCParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record HarvestSlashEffect(float yaw) implements ParticleOptions {

    public static final MapCodec<HarvestSlashEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.FLOAT.fieldOf("yaw").forGetter(particleEffect -> particleEffect.yaw)).apply(instance, HarvestSlashEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HarvestSlashEffect> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, HarvestSlashEffect::yaw, HarvestSlashEffect::new);

    @Override
    public ParticleType<HarvestSlashEffect> getType() {
        return TCParticles.HARVEST_SLASH;
    }
}
