package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideMixinInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{

    @Inject(method = "tick", at = @At("HEAD"))
    private void twisted_and_carved$updateStrideStack(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity playerEntity) {
            ((TwistedRiptideMixinInterface)playerEntity).twistedAndCarved$setRiptideStack(((RiptideStackAccesor)playerEntity).riptideStack());
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
