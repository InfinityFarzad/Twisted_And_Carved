package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import net.farzad.twisted_and_carved.client.particle.DashEffect;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideSetterInterface;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements TwistedRiptideSetterInterface {

    @Shadow
    @Nullable
    protected ItemStack autoSpinAttackItemStack;

    @Unique
    private int timerForDash = 1;

    @Override
    public void twistedAndCarved$setRiptideStack(ItemStack stack) {
        this.autoSpinAttackItemStack = stack;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void twisted_and_carved$updateStrideStack(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Level level = entity.level();

        if (entity instanceof Player playerEntity && autoSpinAttackItemStack != null) {
            if (autoSpinAttackItemStack.is(TCItems.TWISTED_GREATAXE) && playerEntity.isAutoSpinAttack()) {
                double ox = Mth.randomBetweenInclusive(level.getRandom(),-2,2);
                double oy = Mth.randomBetweenInclusive(level.getRandom(),-2,2);
                double oz = Mth.randomBetweenInclusive(level.getRandom(),-2,2);

                if (timerForDash <= 0) {
                    timerForDash = 1;
                    Vec3 vel = playerEntity.getDeltaMovement();
                    float dpi = (float) (Math.atan2(vel.y,Math.sqrt(vel.x * vel.x + vel.z * vel.z)));
                    float dya = (float) Math.atan2(vel.x,vel.z);
                    level.addParticle(new DashEffect(dya, dpi), playerEntity.getX(), playerEntity.getY() + 0.5, playerEntity.getZ(),0,0,0);
                } else {
                    timerForDash--;
                }

                level.addParticle(TCParticles.TWISTED_LEAF_PARTICLE
                        ,playerEntity.getX() - ox
                        ,playerEntity.getY() - oy
                        ,playerEntity.getZ() - oz,
                        ox,
                        oy,
                        oz
                );
            }
        }
    }

/*  - code for disabling air drag -
status  :  unused -  reason  :  stupidly overpowered
    @ModifyVariable(method = "travelMidAir", at = @At("STORE"), ordinal = 1)
    private float twisted_and_carved$reduceStrideAirDrag(float x) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (livingEntity instanceof PlayerEntity player) {
            boolean isHolding = player.getMainHandStack().isOf(ModItems.TWISTED_GREATAXE) || player.getOffHandStack().isOf(ModItems.TWISTED_GREATAXE);
            if (riptideStack != null && player.getInventory().contains(riptideStack) && isHolding && riptideStack.isOf(ModItems.TWISTED_GREATAXE) && player.isUsingRiptide()) {
                return 1;
            } else {
                return x;
            }

        } else {
            return x;
        }

    }
*/

}
