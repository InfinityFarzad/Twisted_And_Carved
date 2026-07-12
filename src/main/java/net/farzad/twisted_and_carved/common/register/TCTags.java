package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class TCTags {
    public static class Items {
        public static final TagKey<Item> TWISTED_TOOL = createTag("twisted_tool");
        public static final TagKey<Item> TWISTED_TOOL_REPAIR_INGREDIENT = createTag("twisted_tool_repair_ingredient");
        public static final TagKey<Item> TWISTED_SPIRIT = createTag("twisted_spirit");
        public static final TagKey<Item> KARMIUM = createTag("karmium");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, TwistedAndCarved.id(name));
        }

    }

    public static class Biomes {
        public static final TagKey<Biome> TWISTED_FOREST = createTag("twisted_forest");


        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME, TwistedAndCarved.id(name));
        }

    }

}

