package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TCItemGroups {
    public static final ResourceKey<CreativeModeTab> TWISTED_AND_CARVED_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "twisted_and_carved"));
    public static final CreativeModeTab TWISTED_AND_CARVED_ITEM_GROUP = FabricItemGroup.builder()
            .backgroundTexture(TwistedAndCarved.id("textures/gui/container/tab_items.png"))
            .hideTitle()
            .noScrollBar()
            .icon(() -> new ItemStack(TCItems.TWISTED_GREATAXE))
            .title(Component.translatable("itemGroup.twisted_and_carved"))
            .build();

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TWISTED_AND_CARVED_ITEM_GROUP_KEY, TWISTED_AND_CARVED_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(TWISTED_AND_CARVED_ITEM_GROUP_KEY).register(itemGroup -> {
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
    }
}
