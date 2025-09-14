package net.farzad.twisted_and_carved.client.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.farzad.twisted_and_carved.client.particle.custom.FalchionSlashEffect;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModParticles {
    public static final SimpleParticleType TWISTED_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final SimpleParticleType TWISTED_GLAIVE_SWEEP = FabricParticleTypes.simple();
    public static final SimpleParticleType PARRY_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType TWISTED_LEAF_PARTICLE = FabricParticleTypes.simple();
    public static final ParticleType<FalchionSlashEffect> FALCHION_SLASH = FabricParticleTypes.complex(FalchionSlashEffect.CODEC,FalchionSlashEffect.PACKET_CODEC);

    public static void init() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "twisted_sweep_attack"), TWISTED_SWEEP_ATTACK);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "twisted_glaive_sweep"), TWISTED_GLAIVE_SWEEP);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "parry_particle"), PARRY_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "twisted_leaf"), TWISTED_LEAF_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "falchion_slash"), FALCHION_SLASH);
    }

}
