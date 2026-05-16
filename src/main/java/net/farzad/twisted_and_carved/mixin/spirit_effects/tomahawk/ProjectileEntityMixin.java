package net.farzad.twisted_and_carved.mixin.spirit_effects.tomahawk;

import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(method = "hasLeftOwner",at = @At("HEAD"), cancellable = true)
    private void twisted_and_carved$disableParryOwnershipTransfer(CallbackInfoReturnable<Boolean> cir) {
        ProjectileEntity projectile = (ProjectileEntity) (Object)this;
        if (projectile instanceof TwistedGreataxeEntity) {
            cir.setReturnValue(false);
        }
    }

}
