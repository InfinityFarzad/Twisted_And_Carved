package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.client.particle.custom.DashEffect;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.networking.ModNetworking;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideMixinInterface;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{

    @Shadow
    @Nullable
    protected ItemStack riptideStack;


    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique
    private int timerForDash = 1;

    @Inject(method = "tick", at = @At("HEAD"))
    private void twisted_and_carved$updateStrideStack(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity playerEntity && riptideStack != null) {
            ModNetworking.sendPacketToAllClients(playerEntity.getWorld(),new RiptideModificationPayload(playerEntity.getId(),riptideStack));
            if (riptideStack.isOf(ModItems.TWISTED_GREATAXE) && playerEntity.isUsingRiptide() && playerEntity.getWorld() instanceof  ServerWorld world) {
                world.spawnParticles(ModParticles.TWISTED_LEAF_PARTICLE,playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),5, MathHelper.nextBetween(world.getRandom(),-2,2),MathHelper.nextBetween(world.getRandom(),-2,2),MathHelper.nextBetween(world.getRandom(),-2,2),MathHelper.nextBetween(world.getRandom(),1,2));
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

    @ModifyVariable(method = "travelMidAir", at = @At("STORE"), ordinal = 1)
    private float twisted_and_carved$reduceStrideAirDrag(float x) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (livingEntity instanceof PlayerEntity player) {
            ItemStack stack = ((TwistedRiptideMixinInterface)player).twistedAndCarved$getRiptideStack();
            boolean isHolding = player.getMainHandStack().isOf(ModItems.TWISTED_GREATAXE) || player.getOffHandStack().isOf(ModItems.TWISTED_GREATAXE);
            if (player.getInventory().contains(stack) && isHolding && stack.isOf(ModItems.TWISTED_GREATAXE) && player.isUsingRiptide()) {
                return 1;
            } else {
                return x;
            }

        } else {
            return x;
        }

    }
    
}
