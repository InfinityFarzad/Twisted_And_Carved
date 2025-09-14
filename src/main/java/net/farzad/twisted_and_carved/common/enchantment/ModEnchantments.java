package net.farzad.twisted_and_carved.common.enchantment;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.farzad.twisted_and_carved.common.enchantment.custom.*;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnchantments extends FabricDynamicRegistryProvider {
    public ModEnchantments(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        register(entries, ModEnchantmentEffects.STRIDE, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.STRIDE_ENCHANTABLE),
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new StrideEnchantmentEffect())
        );

        register(entries, ModEnchantmentEffects.SWEEPING, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.SWEEPING_ENCHANTABLE)
                        ,
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new SweepingEnchantmentEffect())
        );

        register(entries, ModEnchantmentEffects.TWISTING, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.TWISTING_ENCHANTABLE),
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new TwistingEnchantmentEffect())
        );

        register(entries, ModEnchantmentEffects.HARVEST, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.HARVEST_ENCHANTABLE),
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new HarvestEnchantmentEffect())
        );

        register(entries, ModEnchantmentEffects.TOMAHAWK, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.TOMAHAWK_ENCHANTABLE),
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new TomahawkEnchantmentEffect())
        );

        register(entries, ModEnchantmentEffects.BLEEDING, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.BLEEDING_ENCHANTABLE),
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new BleedingEnchantmentEffect())
        );


        register(entries, ModEnchantmentEffects.GRAPPLING, Enchantment.builder(
                Enchantment.definition(
                        registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ModTags.Items.GRAPPLING_ENCHANTABLE),
                        3,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        5,
                        AttributeModifierSlot.HAND
                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK, EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new GrapplingEnchantmentEffect())
        );
    }

    private void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
        entries.add(key, builder.build(key.getValue()), resourceConditions);
    }

    @Override
    public String getName() {
        return "EnchantmentGenerator";
    }

}