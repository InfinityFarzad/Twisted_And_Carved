package net.farzad.twisted_and_carved.mixin.spirit_effects.tomahawk;


import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.init.client.TCParticles;
import net.farzad.twisted_and_carved.common.init.TCSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
            Level level = player.level();

            if (level instanceof ServerLevel serverWorld) {
                if (twistedGreataxe.getOwner() != player) {
                    owner.hurtServer(serverWorld, player.damageSources().playerAttack(player), 2);
                }
            }

            twistedGreataxe.shouldReturn = false;
            twistedGreataxe.noPhysics = false;
            twistedGreataxe.damageMultiplier += 0.5f;
            twistedGreataxe.resetGroundTime();
            twistedGreataxe.applyParryKnockback();

            level.playSound(null,owner.blockPosition(), TCSounds.PARRY,owner.getSoundSource(),1, Mth.randomBetween(level.getRandom(),0.75f,1f));
            level.addParticle(TCParticles.PARRY_PARTICLE,pos.x,pos.y + 0.85, pos.z,0.0,0.0,0.0);
        }
    }
}