package net.farzad.twisted_and_carved.common.block;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class BlockSetTypeList {
    public static final BlockSetType TWISTED = new BlockSetType(Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "twisted").toString());
}