package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.register.TCDamageTypes;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.common.register.TCSounds;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TwistedGlaiveItem extends TwistedToolItem {

    public TwistedGlaiveItem(Properties settings) {
        super(settings);
    }

    public static ItemAttributeModifiers createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(TwistedAndCarved.id("base_attack_range"), attackRange, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (!user.getCooldowns().isOnCooldown(user.getMainHandItem()) && TwistedWeaponUtil.getAbilityID(user.getItemInHand(hand)) == "sweeping") {
            if (user.level() instanceof ServerLevel serverWorld) {

                AABB baseBox = user.getBoundingBox().inflate(4).deflate(0, user.getBoundingBox().getYsize() - 2, 0);
                List<LivingEntity> entities = serverWorld.getEntitiesOfClass(LivingEntity.class,
                        baseBox,
                        entity -> entity != user && entity.isAlive() && !entity.isSpectator() && entity instanceof LivingEntity);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        double distance = entity.position().distanceTo(user.position());
                        livingEntity.hurtServer(serverWorld, user.damageSources().source(TCDamageTypes.SWEEPING_SLASH,user), (float) (4 / distance * 2.5f));
                        livingEntity.knockback(0.05 * distance,user.getX(), user.getY());
                    }
                }
                serverWorld.sendParticles(TCParticles.TWISTED_GLAIVE_SWEEP, user.getX(), user.getY() + 1.0, user.getZ(), 1, 0, 0, 0, 1);
                serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), TCSounds.TWISTED_GLAIVE_SWEEP, user.getSoundSource(), 8.0F, 1.0F);

            }
            user.swing(hand);
            if (user.isCreative()) {
                user.getCooldowns().addCooldown(user.getItemInHand(hand), 20 * 4);
            }
        }
        return super.use(world, user, hand);
    }
    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type().equals("glaive");
    }

}
