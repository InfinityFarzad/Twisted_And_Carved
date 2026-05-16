package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.util.interfaces.AttackChargableItemInterface;
import net.farzad.twisted_and_carved.common.util.interfaces.CritInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityAttackMixin {

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F",shift = At.Shift.BEFORE))
    private void twistedAndCarved$triggerCharge(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        ItemStack weapon = player.getWeaponStack();
        float attackCooldownProgressTwisted = player.getAttackCooldownProgress(0.5f);
        if (target instanceof LivingEntity living) {
            if (weapon.getItem() instanceof CritInterface critInterface) {
                boolean crit = attackCooldownProgressTwisted > 0.9 && player.fallDistance > (double)0.0F && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasBlindnessEffect() && !player.hasVehicle()  && !player.isSprinting();
                if (crit) {
                    critInterface.onCrit(player,living,weapon);
                }
            }
            if (weapon.getItem() instanceof AttackChargableItemInterface atkChargeInterface && attackCooldownProgressTwisted > 0.9) {
                atkChargeInterface.onFullAttack(player,living,weapon);
            }
        }

    }
}
