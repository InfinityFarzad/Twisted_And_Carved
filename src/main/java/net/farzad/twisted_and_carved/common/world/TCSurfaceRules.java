package net.farzad.twisted_and_carved.common.world;

import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class TCSurfaceRules {
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource TWISTED_GRASS = makeStateRule(TCBlocks.FESTERING_ROOTS);

    public static SurfaceRules.RuleSource makeRules()
    {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource materialRule7 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0,false, CaveSurface.FLOOR), TWISTED_GRASS), DIRT);

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(TCBiomes.TWISTED_FOREST),materialRule7),
                // Default to a grass and dirt surface
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), grassSurface)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block)
    {
        return SurfaceRules.state(block.defaultBlockState());
    }
}