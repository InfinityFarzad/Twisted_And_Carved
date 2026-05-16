package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.client.particle.DashEffect;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideSetterInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
    protected ItemStack riptideStack;

    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique
    private int timerForDash = 1;

    @Override
    public void twistedAndCarved$setRiptideStack(ItemStack stack) {
        riptideStack = stack;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void twisted_and_carved$updateStrideStack(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity playerEntity && riptideStack != null) {
            if (riptideStack.isOf(TCItems.TWISTED_GREATAXE) && playerEntity.isUsingRiptide() && playerEntity.getEntityWorld() instanceof  ServerWorld world) {
                world.spawnParticles(TCParticles.TWISTED_LEAF_PARTICLE,playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),5, MathHelper.nextBetween(world.getRandom(),-2,2),MathHelper.nextBetween(world.getRandom(),-2,2),MathHelper.nextBetween(world.getRandom(),-2,2),MathHelper.nextBetween(world.getRandom(),1,2));
                if (timerForDash <= 0) {
                    timerForDash =1;
                    Vec3d vel = playerEntity.getVelocity();
                    float dpi = (float) (Math.atan2(vel.y,Math.sqrt(vel.x * vel.x + vel.z * vel.z)));
                    float dya = (float) Math.atan2(vel.x,vel.z);

                    world.spawnParticles(new DashEffect(dya, dpi), playerEntity.getX(), playerEntity.getY() + 0.5, playerEntity.getZ(),1,0,0,0,0);
                } else {
                    timerForDash--;
                }

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
