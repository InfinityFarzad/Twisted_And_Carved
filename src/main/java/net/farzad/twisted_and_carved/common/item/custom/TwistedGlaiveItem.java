package net.farzad.twisted_and_carved.common.item.custom;

import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.sound.ModSounds;
import net.farzad.twisted_and_carved.common.util.ModDamageTypes;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class TwistedGlaiveItem extends TwistedToolItem {

    public TwistedGlaiveItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!user.getItemCooldownManager().isCoolingDown(user.getMainHandStack()) && TwistedWeaponUtil.getAbilityID(user.getStackInHand(hand)) == "sweeping") {
            if (user.getWorld() instanceof ServerWorld serverWorld) {

                Box baseBox = user.getBoundingBox().expand(4).contract(0, user.getBoundingBox().getLengthY() - 2, 0);
                List<LivingEntity> entities = serverWorld.getEntitiesByClass(LivingEntity.class,
                        baseBox,
                        entity -> entity != user);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        double distance = entity.getPos().distanceTo(user.getPos());
                        livingEntity.damage(serverWorld, user.getDamageSources().create(ModDamageTypes.SWEEPING_SLASH,user), (float) (4 / distance * 2.5f));
                        livingEntity.takeKnockback(0.05 * distance,user.getX(), user.getY());
                    }
                }
                serverWorld.spawnParticles(ModParticles.TWISTED_GLAIVE_SWEEP, user.getX(), user.getY() + 1.0, user.getZ(), 1, 0, 0, 0, 1);
                serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.TWISTED_GLAIVE_SWEEP, user.getSoundCategory(), 8.0F, 1.0F);

            }
            user.swingHand(hand);
            if (user.isCreative()) {
                user.getItemCooldownManager().set(user.getStackInHand(hand), 20 * 4);
            }
        }
        return super.use(world, user, hand);
    }
    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type().equals("glaive");
    }

}
