package net.farzad.twisted_and_carved.mixin.spirit_effects.tomahawk;

import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Projectile.class)
public class ProjectileEntityMixin {

    @Inject(method = "isOutsideOwnerCollisionRange",at = @At("HEAD"), cancellable = true)
    private void twisted_and_carved$disableParryOwnershipTransfer(CallbackInfoReturnable<Boolean> cir) {
        Projectile projectile = (Projectile) (Object)this;
        if (projectile instanceof TwistedGreataxeEntity) {
            cir.setReturnValue(false);
        }
    }

}
