package net.farzad.twisted_and_carved.mixin.spirit_effects.tomahawk;


import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.common.register.TDSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void twisted_and_carved$applyParryEffect(Entity target, CallbackInfo ci) {
        Player player = (Player) (Object)this;
        if (target instanceof TwistedGreataxeEntity twistedGreataxe) {
            Vec3 pos = twistedGreataxe.getOwner().position().add(twistedGreataxe.getOwner().getLookAngle().scale(2));
            Player owner = (Player) twistedGreataxe.getOwner();
            twistedGreataxe.applyParryKnockback();
            if (twistedGreataxe.getOwner().level() instanceof ServerLevel serverWorld) {
                if (twistedGreataxe.getOwner() != player) {
                    owner.hurtServer(serverWorld, player.damageSources().playerAttack(player), 2);
                }
                serverWorld.playSound(null, owner.getX(), owner.getY(), owner.getZ(), TDSounds.PARRY, owner.getSoundSource(), 8.0F, 1.0F);
                serverWorld.sendParticles(TCParticles.PARRY_PARTICLE, pos.x(), pos.y() + 0.85, pos.z(), 1, 0.0, 0.0,0.0, 2);
            }
            twistedGreataxe.resetGroundTime();
            twistedGreataxe.shouldReturn = false;
            twistedGreataxe.noPhysics = false;
            twistedGreataxe.damageMultiplier += 0.5f;
        }
    }
}