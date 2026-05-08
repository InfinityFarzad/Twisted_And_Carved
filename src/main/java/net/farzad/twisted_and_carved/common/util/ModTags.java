package net.farzad.twisted_and_carved.common.util;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> TWISTED_TOOL = createTag("twisted_tool");
        public static final TagKey<Item> TWISTED_TOOL_REPAIR_INGREDIENT = createTag("twisted_tool_repair_ingredient");
        public static final TagKey<Item> TWISTED_SPIRIT = createTag("twisted_spirit");
        public static final TagKey<Item> KARMIUM = createTag("karmium");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(TwistedAndCarved.MOD_ID, name));
        }

    }

}

