package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class TwistedSmithingTemplateItem extends SmithingTemplateItem {

    private static final Formatting DESCRIPTION_FORMATTING;
    private static final Text TWISTED_UPGRADE_APPLIES_TO_TEXT;
    private static final Text TWISTED_UPGRADE_INGREDIENTS_TEXT;
    private static final Text TWISTED_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT;
    private static final Text TWISTED_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT;
    private static final Identifier EMPTY_SLOT_GREATAXE_TEXTURE;
    private static final Identifier EMPTY_SLOT_FALCHION_TEXTURE;
    private static final Identifier EMPTY_SLOT_GLAIVE_TEXTURE;
    private static final Identifier EMPTY_SLOT_SCYTHE_TEXTURE;
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE;

    static {
        DESCRIPTION_FORMATTING = Formatting.BLUE;
        TWISTED_UPGRADE_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.applies_to"))).formatted(DESCRIPTION_FORMATTING);
        TWISTED_UPGRADE_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.ingredients"))).formatted(DESCRIPTION_FORMATTING);
        TWISTED_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.base_slot_description")));
        TWISTED_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(TwistedAndCarved.MOD_ID, "smithing_template.twisted_upgrade.additions_slot_description")));
        EMPTY_SLOT_GREATAXE_TEXTURE = Identifier.of(TwistedAndCarved.MOD_ID, "container/slot/greataxe");
        EMPTY_SLOT_GLAIVE_TEXTURE = Identifier.of(TwistedAndCarved.MOD_ID, "container/slot/glaive");
        EMPTY_SLOT_FALCHION_TEXTURE = Identifier.of(TwistedAndCarved.MOD_ID, "container/slot/falchion");
        EMPTY_SLOT_SCYTHE_TEXTURE = Identifier.of(TwistedAndCarved.MOD_ID, "container/slot/scythe");
        EMPTY_SLOT_INGOT_TEXTURE = Identifier.ofVanilla("container/slot/ingot");
    }

    private final Text appliesToText;
    private final Text ingredientsText;
    private final Text baseSlotDescriptionText;
    private final Text additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;

    public TwistedSmithingTemplateItem(Text appliesToText, Text ingredientsText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, Settings settings) {
        super(appliesToText, ingredientsText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, settings);
        this.appliesToText = appliesToText;
        this.ingredientsText = ingredientsText;
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static TwistedSmithingTemplateItem createTwistedUpgrade(Settings settings) {
        return new TwistedSmithingTemplateItem(TWISTED_UPGRADE_APPLIES_TO_TEXT, TWISTED_UPGRADE_INGREDIENTS_TEXT, TWISTED_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, TWISTED_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT, getTwistedUpgradeEmptyBaseSlotTextures(), getTwistedUpgradeEmptyAdditionsSlotTextures(), settings);
    }

    private static List<Identifier> getTwistedUpgradeEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_GLAIVE_TEXTURE, EMPTY_SLOT_FALCHION_TEXTURE, EMPTY_SLOT_SCYTHE_TEXTURE, EMPTY_SLOT_GREATAXE_TEXTURE);
    }

    private static List<Identifier> getTwistedUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE);
    }

    public Text getBaseSlotDescription() {
        return this.baseSlotDescriptionText;
    }

    public Text getAdditionsSlotDescription() {
        return this.additionsSlotDescriptionText;
    }

    public List<Identifier> getEmptyBaseSlotTextures() {
        return this.emptyBaseSlotTextures;
    }

    public List<Identifier> getEmptyAdditionsSlotTextures() {
        return this.emptyAdditionsSlotTextures;
    }

}
