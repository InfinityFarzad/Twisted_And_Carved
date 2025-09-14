package net.farzad.twisted_and_carved.common.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record HarvestEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<HarvestEnchantmentEffect> CODEC = MapCodec.unit(HarvestEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, net.minecraft.entity.Entity user, Vec3d pos) {
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}