package net.farzad.twisted_and_carved.common.util;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> TOMAHAWK_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, TwistedAndCarved.id("tomahawk"));
    public static final RegistryKey<DamageType> FALCHION_SLASH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, TwistedAndCarved.id("falchion_slash"));
    public static final RegistryKey<DamageType> SWEEPING_SLASH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, TwistedAndCarved.id("sweeping_slash"));

}
