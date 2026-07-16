package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.*;
import net.farzad.twisted_and_carved.common.world.TCSaplingGenerators;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class TCBlocks {

    public static final Block TWISTED_LOG = registerBlock("twisted_log",
            properties -> new RotatedPillarBlock(properties
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), true);

    public static final Block TWISTED_WOOD = registerBlock("twisted_wood",
            properties -> new RotatedPillarBlock(properties.instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()), true);

    public static final Block STRIPPED_TWISTED_LOG = registerBlock("stripped_twisted_log",
            properties -> new RotatedPillarBlock(properties
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()), true);

    public static final Block TWISTED_SHELF = registerBlock("twisted_shelf",
            properties ->
                    new ShelfBlock(properties.instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF).ignitedByLava().strength(2.0f, 3.0f)),true);

    public static final Block STRIPPED_TWISTED_WOOD = registerBlock("stripped_twisted_wood",
            properties -> new RotatedPillarBlock(properties.instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()), true);

    public static final Block TWISTED_PLANKS = registerBlock("twisted_planks",
            properties -> new Block(
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block TWISTED_STAIRS = registerBlock("twisted_stairs",
            properties -> new StairBlock(
                    TWISTED_PLANKS.defaultBlockState(), properties.instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()), true);

    public static final Block TWISTED_SLAB = registerBlock("twisted_slab",
            properties -> new SlabBlock(
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block TWISTED_FENCE = registerBlock("twisted_fence",
            properties -> new FenceBlock(
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block TWISTED_FENCE_GATE = registerBlock("twisted_fence_gate",
            properties -> new FenceGateBlock(ModWoodTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block FESTERING_ROOTS = registerBlock("festering_roots", properties -> new GrassBlock(properties.strength(0.5f).sound(SoundType.MUDDY_MANGROVE_ROOTS)),true);

    public static final Block TWISTED_BUTTON = registerBlock("twisted_button",
            properties -> new ButtonBlock(BlockSetTypeList.TWISTED, 10,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block TWISTED_PRESSURE_PLATE = registerBlock("twisted_pressure_plate",
            properties -> new PressurePlateBlock(BlockSetTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .forceSolidOn()
                            .noCollision()
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block TWISTED_SAPLING = registerBlock("twisted_sapling",
            properties -> new SaplingBlock(TCSaplingGenerators.TWISTED_TREE, properties.noOcclusion()
                    .noCollision().randomTicks().instabreak()
                    .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), true);

    public static final Block TWISTED_TRAPDOOR = registerBlock("twisted_trapdoor",
            properties -> new TrapDoorBlock(BlockSetTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()), true);

    public static final Block TWISTED_DOOR = registerBlock("twisted_door",
            properties -> new DoorBlock(BlockSetTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)), true);

    public static final Block TWISTED_LEAVES = registerBlock("twisted_leaves",
            properties -> new TwistedLeavesBlock(0.25f, TCParticles.TWISTED_LEAF_PARTICLE, properties
                    .mapColor(MapColor.NONE).strength(0.2F).randomTicks()
                    .sound(SoundType.AZALEA_LEAVES).noOcclusion()
                    .isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(Blocks::never)
                    .isViewBlocking(Blocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(Blocks::never)), true);

    public static final Block KARMIUM_BLOCK = registerBlock("karmium_block",
            properties -> new Block(properties
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops().strength(6.0F, 6.0F)
                    .sound(SoundType.NETHERITE_BLOCK)), false);

    public static final Block KARMIUM_CHAIN = registerBlock("karmium_chain",properties -> new KarmiumChainBlock(properties.forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()),true);

    public static final Block KARMIUM_RAILING = registerBlock("karmium_railing", properties -> new KarmiumFence(properties.strength(2.0f).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)),true);

    public static final Block TWISTED_VINE = registerBlock("twisted_vine",properties -> new VineBlock(properties.noOcclusion().replaceable().noCollision().randomTicks().strength(0.2F).sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY)),true);

    public static final Block TWISTED_COFFIN = registerBlock("twisted_coffin",
            properties -> new CoffinBlock(properties.sound(SoundType.DEEPSLATE_BRICKS).requiresCorrectToolForDrops().noOcclusion().strength(2.0f)),true);

    public static final Block POTTED_TWISTED_SAPLING = registerBlock("potted_twisted_sappling", properties -> new FlowerPotBlock(TWISTED_SAPLING,properties.instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)),false);

    public static final Block SPIRIT_FORGE = registerBlock("spirit_forge", SpiritForgingTable::new, true);

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, boolean registerItem) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwistedAndCarved.id(name))));
        if (registerItem) {
            registerBlockItem(name, toRegister);
        }

        return Registry.register(BuiltInRegistries.BLOCK, TwistedAndCarved.id(name), toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, TwistedAndCarved.id(name),
                new BlockItem(block, new Item.Properties().component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted")).useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, TwistedAndCarved.id(name)))));

    }

    public static void init() {}
}
