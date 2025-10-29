package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.item.custom.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;


public class ModItems {

    public static final Item TWISTED_UPGRADE_TEMPLATE = register("twisted_upgrade_smithing_template", TwistedSmithingTemplateItem::createTwistedUpgrade, new Item.Settings().rarity(Rarity.UNCOMMON).component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted")));

    /* - Twisted Weapon Registry - */

    public static final Item TWISTED_GREATAXE = register("twisted_greataxe", (settings) -> new TwistedGreataxeItem(9f, -3.0f, 0.1, settings
            .maxCount(1)
            .rarity(Rarity.UNCOMMON).useCooldown(8)
            .component(ModDataComponents.STRIDE_CHARGE, 0)
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
    ), new Item.Settings());

    public static final Item TWISTED_GLAIVE =
            register("twisted_glaive", TwistedGlaiveItem::new, new Item.Settings()
                    .attributeModifiers(TwistedGlaiveItem.createAttributeModifiers(7, -2.8f, 0.3))
                    .rarity(Rarity.UNCOMMON).useCooldown(4)
                    .maxCount(1)
                    .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
                    .enchantable(ToolMaterial.NETHERITE.enchantmentValue())
            );

    public static final Item TWISTED_SPIRIT =
            register("twisted_spirit",Item::new, new Item.Settings()
                    .rarity(Rarity.UNCOMMON).fireproof().maxCount(1)
            );

    public static final Item TWISTED_FALCHION =
            register("twisted_falchion", (settings) -> {
                return new TwistedFalchionItem(6, -2.5f, -0.1, settings);
            }, new Item.Settings()
                    .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
                    .component(ModDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY)
                    .component(ModDataComponents.BLOOD_CHARGE, 0)
                    .rarity(Rarity.UNCOMMON)
                    .maxCount(1));

    public static final Item TWISTED_SCYTHE = register("twisted_scythe", (settings) -> {
        return new TwistedScytheItem(9, -3.0f, 0.3, settings);
    }, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .component(ModDataComponents.TWISTED_SCYTHE_GRAPPLING, false)
            .rarity(Rarity.UNCOMMON)
            .maxCount(1)
            .useCooldown(5));

    /* Twisted Tool Piece Register */

    public static final Item TWISTED_SCYTHE_PIECE = register("twisted_scythe_piece", TwistedItemPieceItem::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireproof()
            .maxCount(1));

    public static final Item TWISTED_GLAIVE_PIECE = register("twisted_glaive_piece", TwistedItemPieceItem::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireproof()
            .maxCount(1));

    public static final Item TWISTED_GREATAXE_PIECE = register("twisted_greataxe_piece", TwistedItemPieceItem::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireproof()
            .maxCount(1));

    public static final Item TWISTED_FALCHION_PIECE = register("twisted_falchion_piece", TwistedItemPieceItem::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireproof()
            .maxCount(1));

    public static final Item KARMIUM_INGOT = register("karmium_ingot", Item::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .rarity(Rarity.UNCOMMON));

    public static final Item RAW_KARMIUM = register("raw_karmium", Item::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .rarity(Rarity.UNCOMMON));

    public static final Item BLEEDING_SPIRIT = register("bleeding_spirit", TwistedSpiritItem::new, new Item.Settings()
            .component(DataComponentTypes.TOOLTIP_STYLE, Identifier.ofVanilla("twisted"))
            .component(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY)
            .rarity(Rarity.UNCOMMON));


    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {

        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TwistedAndCarved.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void init() {
    }

}
