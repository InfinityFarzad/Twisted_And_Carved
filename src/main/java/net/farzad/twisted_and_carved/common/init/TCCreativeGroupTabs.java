package net.farzad.twisted_and_carved.common.init;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TCCreativeGroupTabs {
    public static final ResourceKey<CreativeModeTab> TWISTED_AND_CARVED_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TwistedAndCarved.id("twisted_and_carved"));
    public static final CreativeModeTab TWISTED_AND_CARVED_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .backgroundTexture(TwistedAndCarved.id("textures/gui/container/tab_items.png"))
            .hideTitle()
            .noScrollBar()
            .icon(() -> new ItemStack(TCItems.TWISTED_GREATAXE))
            .title(Component.translatable("itemGroup.twisted_and_carved"))
            .build();

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TWISTED_AND_CARVED_CREATIVE_TAB_KEY, TWISTED_AND_CARVED_CREATIVE_TAB);

        CreativeModeTabEvents.modifyOutputEvent(TWISTED_AND_CARVED_CREATIVE_TAB_KEY).register(itemGroup -> {
            itemGroup.accept(TCItems.TWISTED_GREATAXE);
            itemGroup.accept(TCItems.TWISTED_GLAIVE);
            itemGroup.accept(TCItems.TWISTED_FALCHION);
            itemGroup.accept(TCItems.TWISTED_SCYTHE);
            itemGroup.accept(TCItems.TWISTED_UPGRADE_TEMPLATE);
            itemGroup.accept(TCItems.TWISTED_GREATAXE_PIECE);
            itemGroup.accept(TCItems.TWISTED_GLAIVE_PIECE);
            itemGroup.accept(TCItems.TWISTED_FALCHION_PIECE);
            itemGroup.accept(TCItems.TWISTED_SCYTHE_PIECE);
            itemGroup.accept(TCBlocks.TWISTED_LOG);
            itemGroup.accept(TCBlocks.STRIPPED_TWISTED_LOG);
            itemGroup.accept(TCBlocks.TWISTED_WOOD);
            itemGroup.accept(TCBlocks.STRIPPED_TWISTED_WOOD);
            itemGroup.accept(TCBlocks.TWISTED_PLANKS);
            itemGroup.accept(TCBlocks.TWISTED_STAIRS);
            itemGroup.accept(TCBlocks.TWISTED_SLAB);
            itemGroup.accept(TCBlocks.TWISTED_FENCE);
            itemGroup.accept(TCBlocks.TWISTED_FENCE_GATE);
            itemGroup.accept(TCBlocks.TWISTED_DOOR);
            itemGroup.accept(TCBlocks.TWISTED_TRAPDOOR);
            itemGroup.accept(TCBlocks.TWISTED_PRESSURE_PLATE);
            itemGroup.accept(TCBlocks.TWISTED_BUTTON);
            itemGroup.accept(TCBlocks.TWISTED_LEAVES);
            itemGroup.accept(TCBlocks.TWISTED_SAPLING);
            itemGroup.accept(TCBlocks.TWISTED_COFFIN);
            itemGroup.accept(TCBlocks.SPIRIT_FORGE);
            itemGroup.accept(TCBlocks.KARMIUM_RAILING);
            itemGroup.accept(TCBlocks.KARMIUM_CHAIN);
            itemGroup.accept(TCBlocks.KARMIUM_BLOCK);
            itemGroup.accept(TCItems.KARMIUM_INGOT);
            itemGroup.accept(TCItems.RAW_KARMIUM);
            itemGroup.accept(TCItems.KARMIUM_NUGGET);
            itemGroup.accept(TCItems.BLEEDING_SPIRIT);
            itemGroup.accept(TCItems.GRAPPLING_SPIRIT);
            itemGroup.accept(TCItems.HARVEST_SPIRIT);
            itemGroup.accept(TCItems.SWEEPING_SPIRIT);
            itemGroup.accept(TCItems.STRIDE_SPIRIT);
            itemGroup.accept(TCItems.TOMAHAWK_SPIRIT);

        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(tabOutput -> {
            tabOutput.insertAfter(Items.PALE_OAK_BUTTON,
                    TCBlocks.TWISTED_LOG,
                    TCBlocks.STRIPPED_TWISTED_LOG,
                    TCBlocks.TWISTED_WOOD,
                    TCBlocks.STRIPPED_TWISTED_WOOD,
                    TCBlocks.TWISTED_PLANKS,
                    TCBlocks.TWISTED_STAIRS,
                    TCBlocks.TWISTED_SLAB,
                    TCBlocks.TWISTED_FENCE,
                    TCBlocks.TWISTED_FENCE_GATE,
                    TCBlocks.TWISTED_DOOR,
                    TCBlocks.TWISTED_TRAPDOOR,
                    TCBlocks.TWISTED_PRESSURE_PLATE,
                    TCBlocks.TWISTED_BUTTON
            );
            tabOutput.insertAfter(Items.NETHERITE_BLOCK,TCBlocks.KARMIUM_BLOCK, TCBlocks.KARMIUM_RAILING, TCBlocks.KARMIUM_CHAIN);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(tabOutput -> {
            tabOutput.insertAfter(Items.PALE_OAK_LOG,TCBlocks.TWISTED_LOG);
            tabOutput.insertAfter(Items.PALE_OAK_LEAVES,TCBlocks.TWISTED_LEAVES);
            tabOutput.insertAfter(Items.PALE_OAK_SAPLING,TCBlocks.TWISTED_SAPLING);
            tabOutput.insertAfter(Items.PODZOL,TCBlocks.FESTERING_ROOTS);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
            output.insertAfter(Items.MACE,TCItems.TWISTED_SCYTHE,TCItems.TWISTED_FALCHION,TCItems.TWISTED_GLAIVE,TCItems.TWISTED_GREATAXE);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.insertAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,TCItems.TWISTED_UPGRADE_TEMPLATE);
            output.accept(TCItems.TWISTED_SCYTHE_PIECE);
            output.accept(TCItems.TWISTED_FALCHION_PIECE);
            output.accept(TCItems.TWISTED_GLAIVE_PIECE);
            output.accept(TCItems.TWISTED_GREATAXE_PIECE);

            output.insertAfter(Items.NETHERITE_INGOT,TCItems.KARMIUM_INGOT);
            output.insertAfter(Items.RAW_GOLD,TCItems.RAW_KARMIUM);


        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
            output.insertAfter(Items.RESPAWN_ANCHOR,TCBlocks.TWISTED_COFFIN);
            output.insertAfter(Items.ENCHANTING_TABLE,TCBlocks.SPIRIT_FORGE);

        });

    }
}
            /*itemGroup.insertAfter(Items.PALE_OAK_BUTTON, TCBlocks.TWISTED_LOG);
            itemGroup.insertAfter(TCBlocks.TWISTED_LOG, TCBlocks.STRIPPED_TWISTED_LOG);
            itemGroup.insertAfter(TCBlocks.STRIPPED_TWISTED_LOG, TCBlocks.TWISTED_WOOD);
            itemGroup.insertAfter(TCBlocks.TWISTED_WOOD, TCBlocks.STRIPPED_TWISTED_WOOD);
            itemGroup.insertAfter(TCBlocks.STRIPPED_TWISTED_WOOD, TCBlocks.TWISTED_PLANKS);
            itemGroup.insertAfter(TCBlocks.TWISTED_PLANKS, TCBlocks.TWISTED_STAIRS);
            itemGroup.insertAfter(TCBlocks.TWISTED_STAIRS, TCBlocks.TWISTED_SLAB);
            itemGroup.insertAfter(TCBlocks.TWISTED_SLAB, TCBlocks.TWISTED_FENCE);
            itemGroup.insertAfter(TCBlocks.TWISTED_FENCE, TCBlocks.TWISTED_FENCE_GATE);
            itemGroup.insertAfter(TCBlocks.TWISTED_FENCE_GATE, TCBlocks.TWISTED_DOOR);
            itemGroup.insertAfter(TCBlocks.TWISTED_DOOR, TCBlocks.TWISTED_TRAPDOOR);
            itemGroup.insertAfter(TCBlocks.TWISTED_TRAPDOOR, TCBlocks.TWISTED_PRESSURE_PLATE);
            itemGroup.insertAfter(TCBlocks.TWISTED_PRESSURE_PLATE, TCBlocks.TWISTED_TRAPDOOR);*/