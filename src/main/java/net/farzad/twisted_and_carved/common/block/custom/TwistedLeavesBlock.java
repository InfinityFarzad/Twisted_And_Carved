package net.farzad.twisted_and_carved.common.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.UntintedParticleLeavesBlock;
import net.minecraft.particle.ParticleEffect;

public class TwistedLeavesBlock extends UntintedParticleLeavesBlock {

    public TwistedLeavesBlock(float leafParticleChance, ParticleEffect leafParticleEffect, Settings settings) {
        super(leafParticleChance, leafParticleEffect, settings);
    }

    @Override
    protected boolean shouldDecay(BlockState state) {
        return false;
    }

}
