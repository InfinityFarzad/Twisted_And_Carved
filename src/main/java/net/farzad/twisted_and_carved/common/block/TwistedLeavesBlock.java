package net.farzad.twisted_and_carved.common.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TwistedLeavesBlock extends UntintedParticleLeavesBlock {

    public TwistedLeavesBlock(float leafParticleChance, ParticleOptions leafParticleEffect, Properties settings) {
        super(leafParticleChance, leafParticleEffect, settings);
    }

    @Override
    protected boolean decaying(BlockState state) {
        return false;
    }

}
