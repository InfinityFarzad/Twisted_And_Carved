package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TCItemGroups {
    public static final RegistryKey<ItemGroup> TWISTED_AND_CARVED_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(TwistedAndCarved.MOD_ID, "twisted_and_carved"));
    public static final ItemGroup TWISTED_AND_CARVED_ITEM_GROUP = FabricItemGroup.builder()
            .texture(TwistedAndCarved.id("textures/gui/container/tab_items.png"))
            .noRenderedName()
            .noScrollbar()
            .icon(() -> new ItemStack(TCItems.TWISTED_GREATAXE))
            .displayName(Text.translatable("itemGroup.twisted_and_carved"))
            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, TWISTED_AND_CARVED_ITEM_GROUP_KEY, TWISTED_AND_CARVED_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(TWISTED_AND_CARVED_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(TCItems.TWISTED_GREATAXE);
            itemGroup.add(TCItems.TWISTED_GLAIVE);
            itemGroup.add(TCItems.TWISTED_FALCHION);
            itemGroup.add(TCItems.TWISTED_SCYTHE);
            itemGroup.add(TCItems.TWISTED_UPGRADE_TEMPLATE);
            itemGroup.add(TCItems.TWISTED_GREATAXE_PIECE);
            itemGroup.add(TCItems.TWISTED_GLAIVE_PIECE);
            itemGroup.add(TCItems.TWISTED_FALCHION_PIECE);
            itemGroup.add(TCItems.TWISTED_SCYTHE_PIECE);
            itemGroup.add(TCBlocks.TWISTED_LOG);
            itemGroup.add(TCBlocks.STRIPPED_TWISTED_LOG);
            itemGroup.add(TCBlocks.TWISTED_WOOD);
            itemGroup.add(TCBlocks.STRIPPED_TWISTED_WOOD);
            itemGroup.add(TCBlocks.TWISTED_PLANKS);
            itemGroup.add(TCBlocks.TWISTED_STAIRS);
            itemGroup.add(TCBlocks.TWISTED_SLAB);
            itemGroup.add(TCBlocks.TWISTED_FENCE);
            itemGroup.add(TCBlocks.TWISTED_FENCE_GATE);
            itemGroup.add(TCBlocks.TWISTED_DOOR);
            itemGroup.add(TCBlocks.TWISTED_TRAPDOOR);
            itemGroup.add(TCBlocks.TWISTED_PRESSURE_PLATE);
            itemGroup.add(TCBlocks.TWISTED_BUTTON);
            itemGroup.add(TCBlocks.TWISTED_LEAVES);
            itemGroup.add(TCBlocks.TWISTED_SAPLING);
            itemGroup.add(TCBlocks.TWISTED_COFFIN);
            itemGroup.add(TCBlocks.KARMIUM_RAILING);
            itemGroup.add(TCBlocks.KARMIUM_CHAIN);
            itemGroup.add(TCBlocks.KARMIUM_BLOCK);
            itemGroup.add(TCItems.KARMIUM_INGOT);
            itemGroup.add(TCItems.RAW_KARMIUM);
            itemGroup.add(TCItems.KARMIUM_NUGGET);
            itemGroup.add(TCItems.BLEEDING_SPIRIT);
            itemGroup.add(TCItems.GRAPPLING_SPIRIT);
            itemGroup.add(TCItems.HARVEST_SPIRIT);
            itemGroup.add(TCItems.SWEEPING_SPIRIT);
            itemGroup.add(TCItems.STRIDE_SPIRIT);
            itemGroup.add(TCItems.TOMAHAWK_SPIRIT);

        });
    }
}
