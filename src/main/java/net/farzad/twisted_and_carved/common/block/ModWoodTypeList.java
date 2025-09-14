package net.farzad.twisted_and_carved.common.block;


import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.block.WoodType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModWoodTypeList {
    public static final WoodType TWISTED = new WoodType(
            Identifier.of(TwistedAndCarved.MOD_ID, "twisted").toString(),
            BlockSetTypeList.TWISTED,
            BlockSoundGroup.WOOD,
            BlockSoundGroup.HANGING_SIGN,
            SoundEvents.BLOCK_FENCE_GATE_CLOSE,
            SoundEvents.BLOCK_FENCE_GATE_OPEN
    );
}
