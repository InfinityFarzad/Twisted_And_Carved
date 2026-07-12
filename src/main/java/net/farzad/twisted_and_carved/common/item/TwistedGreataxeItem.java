package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCNetworking;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class TwistedGreataxeItem extends TwistedToolItem {

    final private int maxCharge = 14;

    public TwistedGreataxeItem(float attackDamage, float attackSpeed, double attackRange, Properties settings) {
        super(applyToolSettings(settings, BlockTags.MINEABLE_WITH_AXE, attackDamage, attackSpeed, attackRange));
    }

    public static Properties applyToolSettings(Properties settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, double attackRange) {
        HolderGetter<Block> registryEntryLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return settings.component(DataComponents.TOOL, new Tool(List.of(Tool.Rule.deniesDrops(registryEntryLookup.getOrThrow(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)), Tool.Rule.minesAndDrops(registryEntryLookup.getOrThrow(effectiveBlocks), 8.5f)), 1.0F, 2, false)).attributes(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponents.WEAPON, new Weapon(1)).enchantable(15);
    }

    private static void setCharge(ItemStack stack, int value) {
        if (stack.has(TCDataComponents.STRIDE_CHARGE)) {
            stack.set(TCDataComponents.STRIDE_CHARGE, value);
        }
    }

    private static int getCharge(ItemStack stack) {
        if (stack.has(TCDataComponents.STRIDE_CHARGE)) {
            return stack.getOrDefault(TCDataComponents.STRIDE_CHARGE, 0);
        } else {
            return 0;
        }
    }

    private static void applyDashMovement(Player user, ItemStack stack) {
        Vec3 dashDir = user.getViewVector(1.0f).normalize();
        user.setDeltaMovement(dashDir.x * 4.8, dashDir.y * 1.5, dashDir.z * 4.8);
        user.needsSync = true;
        user.startAutoSpinAttack(20 , 5, stack);
    }

    public static ItemAttributeModifiers createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (attackDamage), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(TwistedAndCarved.id("base_attack_range"), attackRange, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
        Tool toolComponent = stack.get(DataComponents.TOOL);
        return toolComponent != null;
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type() == "greataxe";
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(0, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCharge(stack) >= 1 && TwistedWeaponUtil.getAbilityID(stack) == "stride";
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return ARGB.color(255, 241, 178);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Mth.clamp(Math.round((float) getCharge(stack) * 13.0F / (float) maxCharge), 0, 13);
    }

    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }

    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 20000;
    }

    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getUseDuration(stack, user) - remainingUseTicks;
        if (user instanceof Player player) {
            if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "stride")) {
                applyDashMovement(player, stack);
                player.playSound(SoundEvents.TRIDENT_RIPTIDE_2.value(),1f,Mth.randomBetween(player.getRandom(),0.6f,0.7f));
                player.level().playSound(player,player.blockPosition(),SoundEvents.TRIDENT_RIPTIDE_3.value(),player.getSoundSource(),10f,Mth.randomBetween(player.getRandom(),1f,2f));

                if (!user.hasInfiniteMaterials()) {
                    setCharge(stack, getCharge(stack) - (maxCharge / 2));
                    player.getCooldowns().addCooldown(stack,20);
                }
                return true;

            } else if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "tomahawk")) {
                if (useTime < 6) {
                    return false;
                } else {
                    player.playSound(SoundEvents.TRIDENT_THROW.value(),1f,Mth.randomBetween(player.getRandom(),0.9f,1f));
                    if (world instanceof ServerLevel serverWorld) {
                        TwistedGreataxeEntity.spawnTwistedGreataxeWithVelocity(TwistedGreataxeEntity::new, player.getInventory().findSlotMatchingItem(stack), serverWorld, stack, user, 0.0F, 3f, 0.0F);
                    }
                    if (!user.hasInfiniteMaterials()) {
                        stack.shrink(1);
                        player.getCooldowns().addCooldown(stack,20 * 5);
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if ((Objects.equals(TwistedWeaponUtil.getAbilityID(itemStack), "tomahawk")) || (Objects.equals(TwistedWeaponUtil.getAbilityID(itemStack), "stride") && !(getCharge(itemStack) < (maxCharge / 2)))) {
            user.startUsingItem(hand);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.FAIL;
        }

    }

    @Override
    public void onFullAttack(LivingEntity attacker, LivingEntity target, ItemStack stack) {
        if (getCharge(stack) < maxCharge && Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "stride")) {
            setCharge(stack, getCharge(stack) + 1);
        }
    }
}