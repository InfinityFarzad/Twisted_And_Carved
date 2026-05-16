package net.farzad.twisted_and_carved.mixin.spirit_effects.tomahawk;


import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.register.TDSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void twisted_and_carved$applyParryEffect(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        if (target instanceof TwistedGreataxeEntity twistedGreataxe) {
            Vec3d pos = twistedGreataxe.getOwner().getEntityPos().add(twistedGreataxe.getOwner().getRotationVector().multiply(2));
            PlayerEntity owner = (PlayerEntity) twistedGreataxe.getOwner();
            twistedGreataxe.applyParryKnockback();
            if (twistedGreataxe.getOwner().getEntityWorld() instanceof ServerWorld serverWorld) {
                if (twistedGreataxe.getOwner() != player) {
                    owner.damage(serverWorld, player.getDamageSources().playerAttack(player), 2);
                }
                serverWorld.playSound(null, owner.getX(), owner.getY(), owner.getZ(), TDSounds.PARRY, owner.getSoundCategory(), 8.0F, 1.0F);
                serverWorld.spawnParticles(TCParticles.PARRY_PARTICLE, pos.getX(), pos.getY() + 0.85, pos.getZ(), 1, 0.0, 0.0,0.0, 2);
            }
            twistedGreataxe.returnTimer = -6;
            twistedGreataxe.dealtDamage = false;
            twistedGreataxe.noClip = false;
            twistedGreataxe.resetInGroundTime = true;
            twistedGreataxe.damageMultiplier += 0.5f;
        }
    }
}