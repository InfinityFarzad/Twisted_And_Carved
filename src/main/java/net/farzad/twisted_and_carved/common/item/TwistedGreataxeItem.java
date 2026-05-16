package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.register.TCNetworking;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.AttackChargableItemInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
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

public class TwistedGreataxeItem extends TwistedToolItem implements AttackChargableItemInterface {

    final private int maxCharge = 14;

    public TwistedGreataxeItem(float attackDamage, float attackSpeed, double attackRange, Settings settings) {
        super(applyToolSettings(settings, BlockTags.AXE_MINEABLE, attackDamage, attackSpeed, attackRange));
    }

    public static Settings applyToolSettings(Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, double attackRange) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return settings.component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)), ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), 8.5f)), 1.0F, 2, false)).attributeModifiers(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).enchantable(15);
    }

    private static void setCharge(ItemStack stack, int value) {
        if (stack.contains(TCDataComponents.STRIDE_CHARGE)) {
            stack.set(TCDataComponents.STRIDE_CHARGE, value);
        }
    }

    private static int getCharge(ItemStack stack) {
        if (stack.contains(TCDataComponents.STRIDE_CHARGE)) {
            return stack.getOrDefault(TCDataComponents.STRIDE_CHARGE, 0);
        } else {
            return 0;
        }
    }

    private static void applyDashMovement(PlayerEntity user, ItemStack stack) {
        Vec3d dashDir = user.getRotationVec(1.0f).normalize();
        user.setVelocity(dashDir.x * 4.8, dashDir.y * 1.5, dashDir.z * 4.8);
        user.velocityDirty = true;
        user.useRiptide(20 , 5, stack);
        if (user.getEntityWorld() instanceof ServerWorld serverWorld) {
            TCNetworking.sendPacketToAllClients(serverWorld,new RiptideModificationPayload(user.getId(),stack));
        }
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
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type() == "greataxe";
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

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TRIDENT;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
        if (user instanceof PlayerEntity player) {
            if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "stride")) {
                applyDashMovement(player, stack);
                player.playSound(SoundEvents.ITEM_TRIDENT_RIPTIDE_2.value(),1f,MathHelper.nextBetween(player.getRandom(),0.6f,0.7f));
                player.getEntityWorld().playSound(player,player.getBlockPos(),SoundEvents.ITEM_TRIDENT_RIPTIDE_3.value(),player.getSoundCategory(),10f,MathHelper.nextBetween(player.getRandom(),1f,2f));

                if (!user.isInCreativeMode()) {
                    setCharge(stack, getCharge(stack) - (maxCharge / 2));
                    player.getItemCooldownManager().set(stack,20);
                }
                return true;

            } else if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "tomahawk")) {
                if (useTime < 10) {
                    return false;
                } else {
                    if (world instanceof ServerWorld serverWorld) {
                        TwistedGreataxeEntity.spawnWithVelocity(TwistedGreataxeEntity::new, player.getInventory().getSlotWithStack(stack), serverWorld, stack, user, 0.0F, (float) remainingUseTicks * 0.00005f, 1.0F);
                    }
                    if (!user.isInCreativeMode()) {
                        player.getInventory().removeOne(stack);
                        player.getItemCooldownManager().set(stack,20 * 5);
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

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if ((Objects.equals(TwistedWeaponUtil.getAbilityID(itemStack), "tomahawk")) || (Objects.equals(TwistedWeaponUtil.getAbilityID(itemStack), "stride") && !(getCharge(itemStack) < (maxCharge / 2)))) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        } else {
            return ActionResult.FAIL;
        }

    }

    @Override
    public void onFullAttack(LivingEntity attacker, LivingEntity target, ItemStack stack) {
        if (getCharge(stack) < maxCharge && Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "stride")) {
            setCharge(stack, getCharge(stack) + 1);
        }
    }
}