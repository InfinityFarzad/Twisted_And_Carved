package net.farzad.twisted_and_carved.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.interfaces.CritInterface;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.sound.ModSounds;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected float getDamageAgainst(Entity target, float baseDamage, DamageSource damageSource) {
        return baseDamage;
    }


    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @ModifyExpressionValue(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
    )
    private boolean twisted_and_carved$supportSweeping(boolean original) {
        return original || getMainHandStack().isIn(ModTags.Items.TWISTED_TOOL);
    }

    @Inject(method = "spawnSweepAttackParticles", at = @At(value = "HEAD"), cancellable = true)
    private void twisted_and_carved$injectSpawnSweepAttackParticles(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.TWISTED_TOOL)) {
            double d = -MathHelper.sin(player.getYaw() * 0.017453292F);
            double e = MathHelper.cos(player.getYaw() * 0.017453292F);
            if (player.getWorld() instanceof ServerWorld) {
                ((ServerWorld) player.getWorld()).spawnParticles(ModParticles.TWISTED_SWEEP_ATTACK, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            info.cancel();
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
    private void twisted_and_carved$applyCritEffect(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        if (player.getMainHandStack().getItem() instanceof CritInterface crit) {
            crit.onCrit(player,(LivingEntity) target,player.getMainHandStack());
        }
    }


    @Inject(method = "attack", at = @At("HEAD"))
    private void twisted_and_carved$drawParryScreen(Entity target, CallbackInfo ci) {
        if (target instanceof TwistedGreataxeEntity twistedGreataxe) {
            twistedGreataxe.applyParryKnockback(500000, this.getX() - twistedGreataxe.getX(), this.getZ() - twistedGreataxe.getZ());
            if (twistedGreataxe.getOwner().getWorld() instanceof ServerWorld serverWorld) {
                PlayerEntity owner = (PlayerEntity) twistedGreataxe.getOwner();
                if (twistedGreataxe.getOwner() != this) {
                    owner.damage(serverWorld, this.getDamageSources().playerAttack((PlayerEntity) (Object) this), 2);
                }
                serverWorld.playSound(null, owner.getX(), owner.getY(), owner.getZ(), ModSounds.PARRY, owner.getSoundCategory(), 8.0F, 1.0F);
                Vec3d pos = twistedGreataxe.getOwner().getPos().add(twistedGreataxe.getOwner().getRotationVector().multiply(2));
                serverWorld.spawnParticles(ModParticles.PARRY_PARTICLE, pos.getX(), pos.getY() + 0.85, pos.getZ(), 1, 0.0, 0.0,0.0, 2);
            }
            twistedGreataxe.returnTimer = -6;
            twistedGreataxe.dealtDamage = false;
            twistedGreataxe.noClip = false;
            twistedGreataxe.resetInGroundTime = true;
            twistedGreataxe.damageMultiplier += 0.5f;
            twistedGreataxe.addVelocity(twistedGreataxe.getVelocity().multiply(5));

        }
    }
}