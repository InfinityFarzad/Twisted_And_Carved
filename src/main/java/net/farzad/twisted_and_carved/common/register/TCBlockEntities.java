package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.entity.CoffinBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TCBlockEntities {
    public static final BlockEntityType<CoffinBlockEntity> COFFIN_BLOCK_ENTITY =
            register("coffin_block_entity", CoffinBlockEntity::new, TCBlocks.TWISTED_COFFIN);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        Identifier id = TwistedAndCarved.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void init() {}
}
