package net.farzad.twisted_and_carved.common.enchantment;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.enchantment.custom.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    public static final RegistryKey<Enchantment> STRIDE = of("stride");
    public static final RegistryKey<Enchantment> SWEEPING = of("sweeping");
    public static final RegistryKey<Enchantment> HARVEST = of("harvest");
    public static final RegistryKey<Enchantment> TWISTING = of("twisting");
    public static final RegistryKey<Enchantment> TOMAHAWK = of("tomahawk");
    public static final RegistryKey<Enchantment> BLEEDING = of("bleeding");
    public static final RegistryKey<Enchantment> GRAPPLING = of("grappling");

    public static MapCodec<StrideEnchantmentEffect> STRIDE_EFFECT = register("stride_effect", StrideEnchantmentEffect.CODEC);
    public static MapCodec<SweepingEnchantmentEffect> SWEEPING_EFFECT = register("sweeping_effect", SweepingEnchantmentEffect.CODEC);
    public static MapCodec<HarvestEnchantmentEffect> HARVEST_EFFECT = register("harvest_effect", HarvestEnchantmentEffect.CODEC);
    public static MapCodec<TwistingEnchantmentEffect> TWISTING_EFFECT = register("twisting_effect", TwistingEnchantmentEffect.CODEC);
    public static MapCodec<TomahawkEnchantmentEffect> TOMAHAWK_EFFECT = register("tomahawk_effect", TomahawkEnchantmentEffect.CODEC);
    public static MapCodec<BleedingEnchantmentEffect> BLEEDING_EFFECT = register("bleeding_effect", BleedingEnchantmentEffect.CODEC);
    public static MapCodec<GrapplingEnchantmentEffect> GRAPPLING_EFFECT = register("grappling_effect", GrapplingEnchantmentEffect.CODEC);


    private static RegistryKey<Enchantment> of(String path) {
        Identifier id = Identifier.of(TwistedAndCarved.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, id), codec);
    }

    public static void init() {
    }
}