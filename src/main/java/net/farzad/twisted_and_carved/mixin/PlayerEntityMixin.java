package net.farzad.twisted_and_carved.mixin;


import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
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

    @Inject(method = "attack", at = @At("HEAD"))
    private void veilrend$supportsweeping(Entity target, CallbackInfo ci) {
        if (target.isAttackable() && this.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.TWISTED_TOOL)) {
            if (!target.handleAttack(this)) {
                PlayerEntity player = (PlayerEntity) (Object) this;
                float f = this.isUsingRiptide() ? this.riptideAttackDamage : (float) this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
                ItemStack itemStack = this.getWeaponStack();
                DamageSource damageSource = Optional.ofNullable(itemStack.getItem().getDamageSource(this)).orElse(this.getDamageSources().playerAttack(player));
                float g = this.getDamageAgainst(target, f, damageSource) - f;
                float h = player.getAttackCooldownProgress(0.5F);
                f *= 0.2F + h * h * 0.8F;
                g *= h;
                player.resetLastAttackedTicks();
                if (target.getType().isIn(EntityTypeTags.REDIRECTABLE_PROJECTILE) && target instanceof ProjectileEntity projectileEntity) {
                    if (projectileEntity.deflect(ProjectileDeflection.REDIRECTED, this, this, true)) {
                        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory());
                        return;
                    }
                }

                if (f > 0.0F || g > 0.0F) {
                    boolean bl = h > 0.9F;
                    boolean bl2;
                    if (this.isSprinting() && bl) {
                        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
                        bl2 = true;
                    } else {
                        bl2 = false;
                    }

                    f += itemStack.getItem().getBonusAttackDamage(target, f, damageSource);
                    boolean bl3 = bl && this.fallDistance > (double) 0.0F && !this.isOnGround() && !this.isClimbing() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle() && target instanceof LivingEntity && !this.isSprinting();
                    if (bl3) {
                        f *= 1.5F;
                    }

                    float i = f + g;
                    boolean bl4 = false;
                    if (bl && !bl3 && !bl2 && this.isOnGround()) {
                        double d = this.getMovement().horizontalLengthSquared();
                        double e = (double) this.getMovementSpeed() * (double) 2.5F;
                        if (d < MathHelper.square(e) && (this.getStackInHand(Hand.MAIN_HAND).isIn(ItemTags.SWORDS) || this.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.TWISTED_TOOL))) {
                            bl4 = true;
                        }
                    }

                    float j = 0.0F;
                    if (target instanceof LivingEntity livingEntity) {
                        j = livingEntity.getHealth();
                    }

                    Vec3d vec3d = target.getVelocity();
                    boolean bl5 = target.sidedDamage(damageSource, i);
                    if (bl5) {
                        float k = this.getAttackKnockbackAgainst(target, damageSource) + (bl2 ? 1.0F : 0.0F);
                        if (k > 0.0F) {
                            if (target instanceof LivingEntity livingEntity2) {
                                livingEntity2.takeKnockback(k * 0.5F, MathHelper.sin(this.getYaw() * ((float) Math.PI / 180F)), -MathHelper.cos(this.getYaw() * ((float) Math.PI / 180F)));
                            } else {
                                target.addVelocity(-MathHelper.sin(this.getYaw() * ((float) Math.PI / 180F)) * k * 0.5F, 0.1, MathHelper.cos(this.getYaw() * ((float) Math.PI / 180F)) * k * 0.5F);
                            }

                            this.setVelocity(this.getVelocity().multiply(0.6, 1.0F, 0.6));
                            this.setSprinting(false);
                        }

                        if (bl4) {
                            float l = 1.0F + (float) this.getAttributeValue(EntityAttributes.SWEEPING_DAMAGE_RATIO) * f;

                            for (LivingEntity livingEntity3 : this.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(1.0F, 0.25F, 1.0F))) {
                                if (livingEntity3 != this && livingEntity3 != target && !this.isTeammate(livingEntity3)) {
                                    if (livingEntity3 instanceof ArmorStandEntity armorStandEntity) {
                                        if (armorStandEntity.isMarker()) {
                                            continue;
                                        }
                                    }

                                    if (this.squaredDistanceTo(livingEntity3) < (double) 9.0F) {
                                        float m = this.getDamageAgainst(livingEntity3, l, damageSource) * h;
                                        World var22 = this.getWorld();
                                        if (var22 instanceof ServerWorld serverWorld) {
                                            if (livingEntity3.damage(serverWorld, damageSource, m)) {
                                                livingEntity3.takeKnockback(0.4F, MathHelper.sin(this.getYaw() * ((float) Math.PI / 180F)), -MathHelper.cos(this.getYaw() * ((float) Math.PI / 180F)));
                                                EnchantmentHelper.onTargetDamaged(serverWorld, livingEntity3, damageSource);
                                            }
                                        }
                                    }
                                }
                            }

                            if (itemStack.isOf(ModItems.TWISTED_SCYTHE)) {
                                int scytheSoundNum = getWorld().getRandom().nextBetween(0, 2);
                                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.SCYTHE_SWEEP_0, this.getSoundCategory(), 8.0F, 1.0F * scytheSoundNum);

                            } else {
                                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            }

                            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            player.spawnSweepAttackParticles();
                        }

                        if (target instanceof ServerPlayerEntity && target.velocityModified) {
                            ((ServerPlayerEntity) target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
                            target.velocityModified = false;
                            target.setVelocity(vec3d);
                        }

                        if (bl3) {
                            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
                            player.addCritParticles(target);
                        }

                        if (!bl3 && !bl4) {
                            if (bl) {
                                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
                            } else {
                                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
                            }
                        }

                        if (g > 0.0F) {
                            player.addEnchantedHitParticles(target);
                        }

                        this.onAttacking(target);
                        Entity entity = target;
                        if (target instanceof EnderDragonPart) {
                            entity = ((EnderDragonPart) target).owner;
                        }

                        boolean bl6 = false;
                        World world = this.getWorld();
                        if (world instanceof ServerWorld serverWorld2) {
                            if (entity instanceof LivingEntity livingEntity3) {
                                bl6 = itemStack.postHit(livingEntity3, this);
                            }

                            EnchantmentHelper.onTargetDamaged(serverWorld2, target, damageSource);
                        }

                        if (!this.getWorld().isClient && !itemStack.isEmpty() && entity instanceof LivingEntity) {
                            if (bl6) {
                                itemStack.postDamageEntity((LivingEntity) entity, this);
                            }

                            if (itemStack.isEmpty()) {
                                if (itemStack == this.getMainHandStack()) {
                                    this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                                } else {
                                    this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                                }
                            }
                        }

                        if (target instanceof LivingEntity) {
                            float n = j - ((LivingEntity) target).getHealth();
                            player.increaseStat(Stats.DAMAGE_DEALT, Math.round(n * 10.0F));
                            if (this.getWorld() instanceof ServerWorld && n > 2.0F) {
                                int o = (int) ((double) n * (double) 0.5F);
                                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getBodyY(0.5F), target.getZ(), o, 0.1, 0.0F, 0.1, 0.2);
                            }
                        }

                        player.addExhaustion(0.1F);
                    } else {
                        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
                    }
                }

            }
        }
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

    @Inject(method = "attack", at = @At("HEAD"))
    private void twisted_and_carved$drawParryScreen(Entity target, CallbackInfo ci) {
        if (target instanceof TwistedGreataxeEntity twistedGreataxe) {
            if (twistedGreataxe.getOwner() == null) {
                twistedGreataxe.setOwner(this);
            }
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