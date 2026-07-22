package net.farzad.twisted_and_carved.common.world;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.init.TCConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;
import java.util.Optional;

public class TCSaplingGenerators {
    public static final TreeGrower TWISTED_TREE = new TreeGrower(TwistedAndCarved.MOD_ID + ":twisted_tree",
            Optional.of(TCConfiguredFeatures.TWISTED_TREE_KEY), Optional.empty(), Optional.empty());
}