package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.SmithingTemplateItem;
import java.util.List;

public class TwistedSmithingTemplateItem extends SmithingTemplateItem {

    private static final ChatFormatting DESCRIPTION_FORMATTING;
    private static final Component TWISTED_UPGRADE_APPLIES_TO_TEXT;
    private static final Component TWISTED_UPGRADE_INGREDIENTS_TEXT;
    private static final Component TWISTED_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT;
    private static final Component TWISTED_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT;
    private static final Identifier EMPTY_SLOT_GREATAXE_TEXTURE;
    private static final Identifier EMPTY_SLOT_FALCHION_TEXTURE;
    private static final Identifier EMPTY_SLOT_GLAIVE_TEXTURE;
    private static final Identifier EMPTY_SLOT_SCYTHE_TEXTURE;
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE;

    static {
        DESCRIPTION_FORMATTING = ChatFormatting.BLUE;
        TWISTED_UPGRADE_APPLIES_TO_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMATTING);
        TWISTED_UPGRADE_INGREDIENTS_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMATTING);
        TWISTED_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.base_slot_description")));
        TWISTED_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.additions_slot_description")));
        EMPTY_SLOT_GREATAXE_TEXTURE = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "container/slot/greataxe");
        EMPTY_SLOT_GLAIVE_TEXTURE = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "container/slot/glaive");
        EMPTY_SLOT_FALCHION_TEXTURE = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "container/slot/falchion");
        EMPTY_SLOT_SCYTHE_TEXTURE = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "container/slot/scythe");
        EMPTY_SLOT_INGOT_TEXTURE = Identifier.withDefaultNamespace("container/slot/ingot");
    }

    private final Component appliesToText;
    private final Component ingredientsText;
    private final Component baseSlotDescriptionText;
    private final Component additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;

    public TwistedSmithingTemplateItem(Component appliesToText, Component ingredientsText, Component baseSlotDescriptionText, Component additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, Properties settings) {
        super(appliesToText, ingredientsText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, settings);
        this.appliesToText = appliesToText;
        this.ingredientsText = ingredientsText;
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static TwistedSmithingTemplateItem createTwistedUpgrade(Properties settings) {
        return new TwistedSmithingTemplateItem(TWISTED_UPGRADE_APPLIES_TO_TEXT, TWISTED_UPGRADE_INGREDIENTS_TEXT, TWISTED_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, TWISTED_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT, getTwistedUpgradeEmptyBaseSlotTextures(), getTwistedUpgradeEmptyAdditionsSlotTextures(), settings);
    }

    private static List<Identifier> getTwistedUpgradeEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_GLAIVE_TEXTURE, EMPTY_SLOT_FALCHION_TEXTURE, EMPTY_SLOT_SCYTHE_TEXTURE, EMPTY_SLOT_GREATAXE_TEXTURE);
    }

    private static List<Identifier> getTwistedUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE);
    }

    public Component getBaseSlotDescription() {
        return this.baseSlotDescriptionText;
    }

    public Component getAdditionSlotDescription() {
        return this.additionsSlotDescriptionText;
    }

    public List<Identifier> getBaseSlotEmptyIcons() {
        return this.emptyBaseSlotTextures;
    }

    public List<Identifier> getAdditionalSlotEmptyIcons() {
        return this.emptyAdditionsSlotTextures;
    }

}
