package net.farzad.twisted_and_carved.common.util;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> STRIDE_ENCHANTABLE = createTag("stride_enchantable");
        public static final TagKey<Item> SWEEPING_ENCHANTABLE = createTag("sweeping_enchantable");
        public static final TagKey<Item> TWISTING_ENCHANTABLE = createTag("twisting_enchantable");
        public static final TagKey<Item> HARVEST_ENCHANTABLE = createTag("harvest_enchantable");
        public static final TagKey<Item> TOMAHAWK_ENCHANTABLE = createTag("tomahawk_enchantable");
        public static final TagKey<Item> TWISTED_TOOL = createTag("twisted_tool");
        public static final TagKey<Item> BLEEDING_ENCHANTABLE = createTag("bleeding_enchantable");
        public static final TagKey<Item> GRAPPLING_ENCHANTABLE = createTag("grappling_enchantable");
        public static final TagKey<Item> TWISTED_TOOL_REPAIR_INGREDIENT = createTag("twisted_tool_repair_ingredient");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(TwistedAndCarved.MOD_ID, name));
        }

    }

}

