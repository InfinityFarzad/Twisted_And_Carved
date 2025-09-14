package net.farzad.twisted_and_carved.common.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> TWISTED_AND_CARVED_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(TwistedAndCarved.MOD_ID, "twisted_and_carved"));
    public static final ItemGroup TWISTED_AND_CARVED_ITEM_GROUP = FabricItemGroup.builder()
            .texture(Identifier.of(TwistedAndCarved.MOD_ID, "textures/gui/container/tab_items.png"))
            .noRenderedName()
            .noScrollbar()
            .icon(() -> new ItemStack(ModItems.TWISTED_GREATAXE))
            .displayName(Text.translatable("itemGroup.twisted_and_carved"))
            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, TWISTED_AND_CARVED_ITEM_GROUP_KEY, TWISTED_AND_CARVED_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(TWISTED_AND_CARVED_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.TWISTED_GREATAXE);
            itemGroup.add(ModItems.TWISTED_GLAIVE);
            itemGroup.add(ModItems.TWISTED_FALCHION);
            itemGroup.add(ModItems.TWISTED_SCYTHE);
            itemGroup.add(ModItems.TWISTED_UPGRADE_TEMPLATE);
            itemGroup.add(ModItems.TWISTED_GREATAXE_PIECE);
            itemGroup.add(ModItems.TWISTED_GLAIVE_PIECE);
            itemGroup.add(ModItems.TWISTED_FALCHION_PIECE);
            itemGroup.add(ModItems.TWISTED_SCYTHE_PIECE);
            itemGroup.add(ModBlocks.TWISTED_LOG);
            itemGroup.add(ModBlocks.STRIPPED_TWISTED_LOG);
            itemGroup.add(ModBlocks.TWISTED_WOOD);
            itemGroup.add(ModBlocks.STRIPPED_TWISTED_WOOD);
            itemGroup.add(ModBlocks.TWISTED_PLANKS);
            itemGroup.add(ModBlocks.TWISTED_STAIRS);
            itemGroup.add(ModBlocks.TWISTED_SLAB);
            itemGroup.add(ModBlocks.TWISTED_FENCE);
            itemGroup.add(ModBlocks.TWISTED_FENCE_GATE);
            itemGroup.add(ModBlocks.TWISTED_DOOR);
            itemGroup.add(ModBlocks.TWISTED_TRAPDOOR);
            itemGroup.add(ModBlocks.TWISTED_PRESSURE_PLATE);
            itemGroup.add(ModBlocks.TWISTED_BUTTON);
            itemGroup.add(ModBlocks.TWISTED_LEAVES);
            itemGroup.add(ModBlocks.TWISTED_SAPLING);
            itemGroup.add(ModBlocks.KARMIUM_BLOCK);
            itemGroup.add(ModItems.KARMIUM_INGOT);
            itemGroup.add(ModItems.RAW_KARMIUM);
        });
    }
}
