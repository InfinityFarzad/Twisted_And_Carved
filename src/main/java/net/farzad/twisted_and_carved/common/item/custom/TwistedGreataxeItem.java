package net.farzad.twisted_and_carved.common.item.custom;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class TwistedGreataxeItem extends TwistedToolItem {

    final private int maxCharge = 14;

    public TwistedGreataxeItem(float attackDamage, float attackSpeed, double attackRange, Settings settings) {
        super(applyToolSettings(settings, BlockTags.AXE_MINEABLE, attackDamage, attackSpeed, attackRange));
    }

    public static Settings applyToolSettings(Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, double attackRange) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return settings.component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)), ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), 8.5f)), 1.0F, 2, false)).attributeModifiers(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).enchantable(15);
    }

    private static void setCharge(ItemStack stack, int value) {
        if (stack.contains(ModDataComponents.STRIDE_CHARGE)) {
            stack.set(ModDataComponents.STRIDE_CHARGE, value);
        }
    }

    private static int getCharge(ItemStack stack) {
        if (stack.contains(ModDataComponents.STRIDE_CHARGE)) {
            return stack.getOrDefault(ModDataComponents.STRIDE_CHARGE, 0);
        } else {
            return 0;
        }
    }

    private static void applyDashMovement(PlayerEntity user, ItemStack stack) {
        Vec3d dashDir = user.getRotationVec(1.0f).normalize();
        user.addVelocity(dashDir.x * 3.5, dashDir.y * 3.5, dashDir.z * 3.5);
        user.velocityModified = true;
        user.useRiptide(20 , 5, stack);
    }

    public static AttributeModifiersComponent createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (attackDamage), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ToolComponent toolComponent = stack.get(DataComponentTypes.TOOL);
        return toolComponent != null;
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type() == "greataxe";
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(0, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getCharge(stack) >= 1 && TwistedWeaponUtil.getAbilityID(stack) == "stride";
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ColorHelper.getArgb(255, 241, 178);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp(Math.round((float) getCharge(stack) * 13.0F / (float) maxCharge), 0, 13);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (getCharge(stack) < maxCharge && Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "stride")) {
            setCharge(stack, getCharge(stack) + 1);
        }

        super.postHit(stack, target, attacker);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
        if (user instanceof PlayerEntity player) {
            if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "stride")) {
                applyDashMovement(player, stack);
                setCharge(stack, getCharge(stack) - (maxCharge / 2));
                return true;

            } else if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "tomahawk")) {
                if (useTime < 10) {
                    return false;
                } else {
                    if (world instanceof ServerWorld serverWorld) {
                        TwistedGreataxeEntity.spawnWithVelocity(TwistedGreataxeEntity::new, player.getInventory().getSlotWithStack(stack), serverWorld, stack, user, 0.0F, (float) remainingUseTicks * 0.00005f, 1.0F);
                    }
                    player.getInventory().removeOne(stack);
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if ((Objects.equals(TwistedWeaponUtil.getAbilityID(itemStack), "tomahawk")) || (Objects.equals(TwistedWeaponUtil.getAbilityID(itemStack), "stride") && !(getCharge(itemStack) < (maxCharge / 2)))) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        } else {
            return ActionResult.FAIL;
        }

    }
}
