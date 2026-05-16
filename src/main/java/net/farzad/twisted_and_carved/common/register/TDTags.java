package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class TDTags {
    public static class Items {
        public static final TagKey<Item> TWISTED_TOOL = createTag("twisted_tool");
        public static final TagKey<Item> TWISTED_TOOL_REPAIR_INGREDIENT = createTag("twisted_tool_repair_ingredient");
        public static final TagKey<Item> TWISTED_SPIRIT = createTag("twisted_spirit");
        public static final TagKey<Item> KARMIUM = createTag("karmium");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(TwistedAndCarved.MOD_ID, name));
        }

    }

    public static class Biomes {
        public static final TagKey<Biome> THICK_FOG = createTag("thick_fog");
        public static final TagKey<Biome> TWISTED_FOREST = createTag("twisted_forest");


        private static TagKey<Biome> createTag(String name) {
            return TagKey.of(RegistryKeys.BIOME, Identifier.of(TwistedAndCarved.MOD_ID, name));
        }

    }

}

