package net.farzad.twisted_and_carved.common.world;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ModSaplingGenerators {
    public static final SaplingGenerator TWISTED_TREE = new SaplingGenerator(TwistedAndCarved.MOD_ID + ":twisted_tree",
            Optional.of(ModConfiguredFeatures.TWISTED_TREE_SMALL_KEY), Optional.empty(), Optional.empty());
}