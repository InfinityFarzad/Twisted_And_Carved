package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(method = "deflect", at = @At("HEAD"), cancellable = true)
    private void twistedAndCarved$cancelProjectileOwnership(ProjectileDeflection deflection, Entity deflector, Entity owner, boolean fromAttack, CallbackInfoReturnable<Boolean> cir) {
        ProjectileEntity proj = (ProjectileEntity) (Object) this;

        if (proj instanceof TwistedGreataxeEntity twistedGreataxe) {
            twistedGreataxe.setOwner(twistedGreataxe.getOwner());
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldLeaveOwner",at = @At("HEAD"), cancellable = true)
    private void twisted_and_carved$ihatemojang(CallbackInfoReturnable<Boolean> cir) {
        ProjectileEntity projectile = (ProjectileEntity) (Object)this;
        if (projectile instanceof TwistedGreataxeEntity) {
            cir.setReturnValue(false);
        }
    }

}
