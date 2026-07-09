package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.farzad.twisted_and_carved.client.particle.DashEffect;
import net.farzad.twisted_and_carved.client.particle.FalchionSlashEffect;
import net.farzad.twisted_and_carved.client.particle.HarvestSlashEffect;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;


public class TCParticles {
    public static final SimpleParticleType TWISTED_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final SimpleParticleType TWISTED_GLAIVE_SWEEP = FabricParticleTypes.simple();
    public static final SimpleParticleType PARRY_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType TWISTED_LEAF_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType COFFIN_SMOKE = FabricParticleTypes.simple();
    public static final ParticleType<FalchionSlashEffect> FALCHION_SLASH = FabricParticleTypes.complex(FalchionSlashEffect.CODEC, FalchionSlashEffect.PACKET_CODEC);
    public static final ParticleType<HarvestSlashEffect> HARVEST_SLASH = FabricParticleTypes.complex(HarvestSlashEffect.CODEC, HarvestSlashEffect.PACKET_CODEC);
    public static final ParticleType<DashEffect> DASH_PARTICLE = FabricParticleTypes.complex(DashEffect.CODEC,DashEffect.PACKET_CODEC);
    public static final SimpleParticleType COFFIN_ASH = FabricParticleTypes.simple();


    public static void init() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("twisted_sweep_attack"), TWISTED_SWEEP_ATTACK);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("twisted_glaive_sweep"), TWISTED_GLAIVE_SWEEP);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("parry_particle"), PARRY_PARTICLE);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("twisted_leaf"), TWISTED_LEAF_PARTICLE);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("falchion_slash"), FALCHION_SLASH);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("harvest_slash"), HARVEST_SLASH);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("coffin_smoke"),COFFIN_SMOKE);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("coffin_ash"),COFFIN_ASH);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwistedAndCarved.id("dash_particle"),DASH_PARTICLE);
    }

}
