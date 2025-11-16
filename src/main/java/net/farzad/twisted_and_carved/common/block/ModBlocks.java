package net.farzad.twisted_and_carved.common.block;

import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.custom.CoffinBlock;
import net.farzad.twisted_and_carved.common.block.custom.KarmiumChainBlock;
import net.farzad.twisted_and_carved.common.block.custom.TwistedLeavesBlock;
import net.farzad.twisted_and_carved.common.block.custom.TwistedSaplingBlock;
import net.farzad.twisted_and_carved.common.world.ModSaplingGenerators;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

    public static final Block TWISTED_LOG = registerBlock("twisted_log",
            properties -> new PillarBlock(properties
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable()), true);

    public static final Block TWISTED_WOOD = registerBlock("twisted_wood",
            properties -> new PillarBlock(properties.instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()), true);

    public static final Block STRIPPED_TWISTED_LOG = registerBlock("stripped_twisted_log",
            properties -> new PillarBlock(properties
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()), true);

    public static final Block STRIPPED_TWISTED_WOOD = registerBlock("stripped_twisted_wood",
            properties -> new PillarBlock(properties.instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()), true);

    public static final Block TWISTED_PLANKS = registerBlock("twisted_planks",
            properties -> new Block(
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_STAIRS = registerBlock("twisted_stairs",
            properties -> new StairsBlock(
                    TWISTED_PLANKS.getDefaultState(), properties.instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()), true);

    public static final Block TWISTED_SLAB = registerBlock("twisted_slab",
            properties -> new SlabBlock(
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_FENCE = registerBlock("twisted_fence",
            properties -> new FenceBlock(
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_FENCE_GATE = registerBlock("twisted_fence_gate",
            properties -> new FenceGateBlock(ModWoodTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_BUTTON = registerBlock("twisted_button",
            properties -> new ButtonBlock(BlockSetTypeList.TWISTED, 10,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_PRESSURE_PLATE = registerBlock("twisted_pressure_plate",
            properties -> new PressurePlateBlock(BlockSetTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .solid()
                            .noCollision()
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_SAPLING = registerBlock("twisted_sapling",
            properties -> new TwistedSaplingBlock(ModSaplingGenerators.TWISTED_TREE, properties.nonOpaque()
                    .noCollision().ticksRandomly().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY), Blocks.GRASS_BLOCK), true);

    public static final Block TWISTED_TRAPDOOR = registerBlock("twisted_trapdoor",
            properties -> new TrapdoorBlock(BlockSetTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()), true);

    public static final Block TWISTED_DOOR = registerBlock("twisted_door",
            properties -> new DoorBlock(BlockSetTypeList.TWISTED,
                    properties.instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
                            .pistonBehavior(PistonBehavior.DESTROY)), true);

    public static final Block TWISTED_LEAVES = registerBlock("twisted_leaves",
            properties -> new TwistedLeavesBlock(1, ModParticles.TWISTED_LEAF_PARTICLE, properties
                    .mapColor(MapColor.CLEAR).strength(0.2F).ticksRandomly()
                    .sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque()
                    .allowsSpawning(Blocks::canSpawnOnLeaves).suffocates(Blocks::never)
                    .blockVision(Blocks::never).burnable().pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)), true);

    public static final Block KARMIUM_BLOCK = registerBlock("karmium_block",
            properties -> new Block(properties
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool().strength(6.0F, 6.0F)
                    .sounds(BlockSoundGroup.NETHERITE)), true);

    public static final Block KARMIUM_CHAIN = registerBlock("karmium_chain",properties -> new KarmiumChainBlock(properties.solid().requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.CHAIN).nonOpaque()),true);

    public static final Block TWISTED_VINE = registerBlock("twisted_vine",properties -> new VineBlock(properties.nonOpaque().replaceable().noCollision().ticksRandomly().strength(0.2F).sounds(BlockSoundGroup.VINE).burnable().pistonBehavior(PistonBehavior.DESTROY)),true);

    public static final Block TWISTED_COFFIN = registerBlock("twisted_coffin",
            properties -> new CoffinBlock(properties.sounds(BlockSoundGroup.DEEPSLATE_BRICKS).requiresTool().nonOpaque().strength(2.0f)),true);


    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function, boolean registerItem) {
        Block toRegister = function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TwistedAndCarved.MOD_ID, name))));
        if (registerItem) {
            registerBlockItem(name, toRegister);
        }

        return Registry.register(Registries.BLOCK, Identifier.of(TwistedAndCarved.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(TwistedAndCarved.MOD_ID, name),
                new BlockItem(block, new Item.Settings().component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted")).useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TwistedAndCarved.MOD_ID, name)))));
    }

    public static void init() {
    }
}
