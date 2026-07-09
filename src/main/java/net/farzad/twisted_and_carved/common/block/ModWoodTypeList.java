package net.farzad.twisted_and_carved.common.block;


import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypeList {
    public static final WoodType TWISTED = new WoodType(
            Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "twisted").toString(),
            BlockSetTypeList.TWISTED,
            SoundType.WOOD,
            SoundType.HANGING_SIGN,
            SoundEvents.FENCE_GATE_CLOSE,
            SoundEvents.FENCE_GATE_OPEN
    );
}
