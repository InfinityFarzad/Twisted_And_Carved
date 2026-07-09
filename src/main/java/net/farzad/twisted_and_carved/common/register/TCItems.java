package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

import java.util.function.Function;


public class TCItems {

    public static final Item TWISTED_UPGRADE_TEMPLATE = register("twisted_upgrade_smithing_template", TwistedSmithingTemplateItem::createTwistedUpgrade, new Item.Properties().rarity(Rarity.UNCOMMON).component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted")));

    public static final Item KARMIUM_BLOCK = register("karmium_block", (properties) -> new BlockItem(TCBlocks.KARMIUM_BLOCK,properties) , new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON));

    public static final Item KARMIUM_INGOT = register("karmium_ingot", Item::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON));

    public static final Item RAW_KARMIUM = register("raw_karmium", Item::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON));

    public static final Item KARMIUM_NUGGET = register("karmium_nugget", Item::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON));


    // - Weapon Pieces -

    public static final Item TWISTED_SCYTHE_PIECE = register("twisted_scythe_piece", TwistedItemPieceItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireResistant()
            .stacksTo(1));

    public static final Item TWISTED_GLAIVE_PIECE = register("twisted_glaive_piece", TwistedItemPieceItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireResistant()
            .stacksTo(1));

    public static final Item TWISTED_GREATAXE_PIECE = register("twisted_greataxe_piece", TwistedItemPieceItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireResistant()
            .stacksTo(1));

    public static final Item TWISTED_FALCHION_PIECE = register("twisted_falchion_piece", TwistedItemPieceItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .rarity(Rarity.UNCOMMON)
            .fireResistant()
            .stacksTo(1));





    // - Weapons -

    public static final Item TWISTED_GREATAXE = register("twisted_greataxe", (settings) -> new TwistedGreataxeItem(8.5f, -3.0f, 0.1, settings
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .component(TCDataComponents.STRIDE_CHARGE, 0)
            .component(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY)
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
    ), new Item.Properties());

    public static final Item TWISTED_GLAIVE =
            register("twisted_glaive", TwistedGlaiveItem::new, new Item.Properties()
                    .attributes(TwistedGlaiveItem.createAttributeModifiers(7, -2.8f, 0.3))
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
                    .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
                    .component(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY)
                    .enchantable(ToolMaterial.NETHERITE.enchantmentValue())
            );

    public static final Item TWISTED_FALCHION =
            register("twisted_falchion", (settings) -> {
                return new TwistedFalchionItem(6, -2.4f, -0.15, settings);
            }, new Item.Properties()
                    .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
                    .component(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY)
                    .component(TCDataComponents.BLOOD_CHARGE, 0)
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1));

    public static final Item TWISTED_SCYTHE = register("twisted_scythe", (settings) -> {
        return new TwistedScytheItem(8, -3.2f, 0.25, settings);
    }, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SCYTHE_GRAPPLING, false)
            .component(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY)
            .rarity(Rarity.UNCOMMON)
            .stacksTo(1));




    // - Spirits -

    public static final Item BLEEDING_SPIRIT = register("bleeding_spirit", TwistedSpiritItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SPIRIT_DATA, new TwistedSpiritComponent("falchion","bleeding"))
            .rarity(Rarity.UNCOMMON));

    public static final Item GRAPPLING_SPIRIT = register("grappling_spirit", TwistedSpiritItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SPIRIT_DATA, new TwistedSpiritComponent("scythe","grappling"))
            .rarity(Rarity.UNCOMMON));

    public static final Item TOMAHAWK_SPIRIT = register("tomahawk_spirit", TwistedSpiritItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SPIRIT_DATA, new TwistedSpiritComponent("greataxe","tomahawk"))
            .rarity(Rarity.UNCOMMON));

    public static final Item STRIDE_SPIRIT = register("stride_spirit", TwistedSpiritItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SPIRIT_DATA, new TwistedSpiritComponent("greataxe","stride"))
            .rarity(Rarity.UNCOMMON));

    public static final Item SWEEPING_SPIRIT = register("sweeping_spirit", TwistedSpiritItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SPIRIT_DATA, new TwistedSpiritComponent("glaive","sweeping"))
            .rarity(Rarity.UNCOMMON));

    public static final Item HARVEST_SPIRIT = register("harvest_spirit", TwistedSpiritItem::new, new Item.Properties()
            .component(DataComponents.TOOLTIP_STYLE, Identifier.withDefaultNamespace("twisted"))
            .component(TCDataComponents.TWISTED_SPIRIT_DATA, new TwistedSpiritComponent("scythe","harvest"))
            .rarity(Rarity.UNCOMMON));


    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void init() {
    }

}
