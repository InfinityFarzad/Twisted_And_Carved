package net.farzad.twisted_and_carved.common.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.register.TCDamageTypes;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCEntities;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class TwistedScytheEntity extends AbstractArrow {

    public boolean shouldReturn;
    public boolean isGripped;
    public boolean isPlayingSound;
    public LivingEntity prevOwner;
    public float damageMultiplier;
    public float scytheRot;

    public TwistedScytheEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(TCEntities.TWISTED_SCYTHE_ENTITY, owner, world, stack, null);
        this.isPlayingSound = false;
        this.prevOwner = owner;
    }

    public TwistedScytheEntity(EntityType<? extends TwistedScytheEntity> entityType, Level world) {
        super(entityType, world);
    }

    private void playSound() {
        if (!this.isInGround() && !isPlayingSound) {
            if (!this.level().isClientSide()) {

                ScytheSoundLoopS2CPayload payload = new ScytheSoundLoopS2CPayload(this.getId());
                for (ServerPlayer player : PlayerLookup.around((ServerLevel) this.level(), this.blockPosition(), 20)) {
                    ServerPlayNetworking.send(player, payload);
                }
                isPlayingSound = true;
            }

        }
    }

    private void updateRot() {
        if (this.scytheRot++ >= 360) {
            this.scytheRot = -360;
        }
        scytheRot += 0.005f;
    }

    public float getRot() {
        return this.scytheRot;
    }

    private void pullOwner(Entity owner) {
        Vec3 dir = this.position().subtract(owner.position());
        owner.push(dir.scale(0.15));
        owner.needsSync = true;
        this.shouldReturn = true;
    }

    private void pullTarget(Entity owner, LivingEntity target) {
        Vec3 dir = owner.position().subtract(target.position());
        target.push(dir.scale(0.15));
        target.needsSync = true;
        this.shouldReturn = true;
    }

    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    public void tick() {
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity owner) {
            Vec3 targetPos = owner.position();
            double distance = targetPos.distanceTo(this.position());

            if (this.isInGround()) {
                this.isGripped = true;
            }

            if ((distance > 57 && !this.isGripped) || this.inGroundTime > 8) {
                this.playSound(SoundEvents.TRIDENT_RETURN, 1.0F, 1.0F);
                this.shouldReturn = true;
                this.isGripped = false;
            }

            if (isGripped && !shouldReturn) {
                pullOwner(owner);
                owner.fallDistance = 0;
            }

            if (this.shouldReturn) {
                Vec3 direction = targetPos.subtract(this.position()).normalize();
                if (owner instanceof Player player && this.getScytheStack(player) == null) {
                    this.discard();
                }

                this.setDeltaMovement(direction);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setNoPhysics(true);
            }
            playSound();
            updateRot();
            super.tick();
        } else {
            this.discard();
        }
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 currentPosition, Vec3 nextPosition) {
        return this.shouldReturn ? null : super.findHitEntity(currentPosition, nextPosition);
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) && this.getOwner() != null && !player.isSpectator()) {
            if (!this.level().isClientSide() && shouldReturn) {
                getScytheStack(player).set(TCDataComponents.TWISTED_SCYTHE_GRAPPLING, false);
                this.discard();
            }
        }
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (this.getOwner() != null) {
            Entity ownerEntity = this.getOwner();
            Level world = this.level();
            Entity entity = entityHitResult.getEntity();
            float f = 5.5F;
            DamageSource damageSource = new DamageSource(
                    this.getOwner().level().registryAccess()
                            .lookupOrThrow(Registries.DAMAGE_TYPE)
                            .get(TCDamageTypes.TOMAHAWK_DAMAGE.identifier()).get());

            if (world instanceof ServerLevel serverWorld) {
                f = EnchantmentHelper.modifyDamage(serverWorld, this.getWeaponItem(), entity, damageSource, f);
            }

            if (entity.hurtOrSimulate(damageSource, f + damageMultiplier)) {
                if (entity.getType() == EntityType.ENDERMAN) {
                    return;
                }

                if (world instanceof ServerLevel serverWorld) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverWorld, entity, damageSource, this.getWeaponItem(), (item) -> this.kill(serverWorld));
                }
            }

            if (entity instanceof LivingEntity livingEntity && ownerEntity instanceof Player player) {
                double boxSize = (livingEntity.getBoundingBox().getZsize() + livingEntity.getBoundingBox().getXsize() + livingEntity.getBoundingBox().getYsize()) / 3;
                if (boxSize >= 1.5 || (livingEntity instanceof Player && player.isShiftKeyDown())) {
                    pullOwner(player);
                } else {
                    pullTarget(player, livingEntity);
                }

                getScytheStack(player).set(TCDataComponents.TWISTED_SCYTHE_GRAPPLING, false);
                this.shouldReturn = true;
                this.isGripped = true;
                this.doPostHurtEffects(livingEntity);
            }
            this.shouldReturn = true;
            this.deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 0.02, 0.2));
            this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0002;
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(TCItems.TWISTED_SCYTHE);
    }

    private ItemStack getScytheStack(Player player) {
        Inventory inv = player.getInventory();
        if (inv.findSlotMatchingItem(this.getWeaponItem()) != -1) {
            return inv.getItem(inv.findSlotMatchingItem(this.getWeaponItem()));
        } else {
            return player.getOffhandItem();
        }
    }

    // Data Storage
    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        super.addAdditionalSaveData(view);
        view.putBoolean("shouldReturn", this.shouldReturn);
        view.putBoolean("isPlayingSound", this.isPlayingSound);
        view.putBoolean("isGripped", this.isGripped);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.shouldReturn = view.getBooleanOr("shouldReturn",false);
        this.isPlayingSound = view.getBooleanOr("isPlayingSound",false);
        this.isGripped = view.getBooleanOr("isGripped",false);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}