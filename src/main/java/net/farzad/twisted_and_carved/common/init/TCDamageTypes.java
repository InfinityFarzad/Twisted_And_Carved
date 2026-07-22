package net.farzad.twisted_and_carved.common.init;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class TCDamageTypes {

    public static final ResourceKey<DamageType> TOMAHAWK_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, TwistedAndCarved.id("tomahawk"));
    public static final ResourceKey<DamageType> FALCHION_SLASH = ResourceKey.create(Registries.DAMAGE_TYPE, TwistedAndCarved.id("falchion_slash"));
    public static final ResourceKey<DamageType> SWEEPING_SLASH = ResourceKey.create(Registries.DAMAGE_TYPE, TwistedAndCarved.id("sweeping_slash"));

}
