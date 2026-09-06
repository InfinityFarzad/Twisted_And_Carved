package net.farzad.twisted_and_carved.common.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.init.TCDamageTypes;
import net.farzad.twisted_and_carved.common.init.TCEntities;
import net.farzad.twisted_and_carved.common.init.TCItems;
import net.farzad.twisted_and_carved.common.util.PlayerInventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TwistedGreataxeEntity extends AbstractArrow {

    private final static float DAMAGE = 5.0f;
    private final static float MAX_DISTANCE = 24.0f;

    public boolean shouldReturn;
    public LivingEntity prevOwner;
    public float damageMultiplier;
    private int slot;
    private boolean initiaitedSound;
    private int parry_count = 0;

    public TwistedGreataxeEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(TCEntities.TWISTED_GREATAXE_ENTITY, owner, world, stack, null);
        this.prevOwner = owner;
    }

    public TwistedGreataxeEntity(EntityType<? extends TwistedGreataxeEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static <T extends Projectile> T spawnTwistedGreataxeWithVelocity(ProjectileFactory<T> creator, int slot, ServerLevel world, ItemStack projectileStack, LivingEntity shooter, float roll, float power, float divergence) {
        return Projectile.spawnProjectile(creator.create(world, shooter, projectileStack), world, projectileStack, (entity) -> {
            if (entity instanceof TwistedGreataxeEntity twistedGreataxe) {
                twistedGreataxe.setSlot(slot);
                twistedGreataxe.initiaitedSound = false;
            }
            entity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), roll, power, divergence);
        });
    }



    /* - other - */

    public void resetGroundTime() {
        this.inGroundTime = 0;
    }

    public void applyParryKnockback() {
        if (this.getOwner() != null) {
            this.resetGroundTime();
            this.setDeltaMovement(this.getOwner().getLookAngle().normalize().scale(2 + parry_count));
            this.needsSync = true;
            this.parry_count++;
        }
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @org.jspecify.annotations.Nullable Entity deflector, @org.jspecify.annotations.Nullable EntityReference<Entity> lazyEntityReference, boolean fromAttack) {
        return false;
    }


    /* - event - */

    @Override
    public void playerTouch(Player player) {
        if (this.isOwnerAlive() && shouldReturn) {
            if (!this.level().isClientSide() && this.shakeTime <= 0) {
                if (this.tryPickup(player) && this.getOwner() == player) {
                    PlayerInventoryUtil.returnToSlot(player, this.slot, this.getPickupItem());
                    this.discard();
                }
            }
        }
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Level world = this.level();
        Entity targetEntity = entityHitResult.getEntity();
        DamageSource damageSource = getDamageSource(world);
        float damage = DAMAGE;


        if (targetEntity != this.getOwner()) {
            if (world instanceof ServerLevel serverWorld) {
                damage = EnchantmentHelper.modifyDamage(serverWorld, Objects.requireNonNull(this.getWeaponItem()), targetEntity, damageSource, damage);
                if (targetEntity.hurtServer(serverWorld, damageSource, damage + damageMultiplier)) {
                    if (targetEntity instanceof LivingEntity livingEntity) {
                        this.doKnockback(livingEntity, damageSource);
                        this.doPostHurtEffects(livingEntity);
                    }
                    EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverWorld, targetEntity, damageSource, this.getWeaponItem(), (item) -> this.kill(serverWorld));
                }
            }
            this.shouldReturn = true;
        }

        this.deflect(ProjectileDeflection.REVERSE, targetEntity, this.owner, false);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 0.02, 0.2));
        this.playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.0F);
    }

    protected void hitBlockEnchantmentEffects(ServerLevel world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        Vec3 pos = blockHitResult.getBlockPos().clampLocationWithin(blockHitResult.getLocation());
        BlockPos blockPos = blockHitResult.getBlockPos();
        Entity owner = this.getOwner();
        LivingEntity livingOwner = owner instanceof LivingEntity living ? living : null;

        if (world instanceof ServerLevel serverWorld) {
            serverWorld.addDestroyBlockEffect(blockPos, world.getBlockState(blockPos));
        }

        EnchantmentHelper.onHitBlock(world, weaponStack, livingOwner, this, null, pos, world.getBlockState(blockHitResult.getBlockPos()), (item) -> this.kill(world));
        this.shakeTime = 7;
        this.playSound(SoundEvents.TRIDENT_HIT_GROUND,1,1);
    }



    /* - conditions - */

    protected boolean tryPickup(Player player) {
        boolean canItPickUp1;
        switch (this.pickup.ordinal()) {
            case 0 -> canItPickUp1 = false;
            case 1 -> canItPickUp1 = PlayerInventoryUtil.hasEmptySlot(player, slot);
            case 2 -> canItPickUp1 = player.isCreative();
            default -> throw new MatchException(null, null);
        }

        return canItPickUp1 || this.isNoPhysics() && this.ownedBy(player) && PlayerInventoryUtil.hasEmptySlot(player, slot);
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean shouldStopPlayingSound() {
        return this.onGround() || this.isInGround();
    }



    /* - update methods - */

    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    private void playAmbientSound() {
        if (!this.level().isClientSide() && !initiaitedSound) {
            GreataxeSoundLoopS2CPayload payload = new GreataxeSoundLoopS2CPayload(this.getId());
            for (ServerPlayer player : PlayerLookup.around((ServerLevel) this.level(), this.blockPosition(), 20)) {
                ServerPlayNetworking.send(player, payload);
            }
            initiaitedSound = false;
        }
    }

    @Override
    public void tick() {
        Level world = this.level();
        Entity entity = this.getOwner();
        Vec3 pos = this.position();

        if (isOwnerAlive() && entity instanceof Player ownerEntity) {
            Vec3 ownerPos = ownerEntity.position();

            if (!shouldReturn) {
                if (inGroundTime > 4) {
                    shouldReturn = true;
                }

                if (pos.distanceTo(ownerPos) > MAX_DISTANCE) {
                    this.playSound(SoundEvents.TRIDENT_RETURN,1,1);
                    this.shouldReturn = true;
                }
            } else {
                Vec3 target = new Vec3(ownerPos.x(), ownerPos.y() + 0.8, ownerPos.z());
                Vec3 direction = target.subtract(pos.add(entity.getDeltaMovement())).normalize().scale(0.5);

                if (direction.lengthSqr() >= 2) {
                    this.setDeltaMovement(direction);
                } else {
                    this.addDeltaMovement(direction.scale(0.05f));
                }

                this.setNoPhysics(true);
                this.move(MoverType.SELF, this.getDeltaMovement());
            }

        } else if (!isOwnerAlive()) {
            Containers.dropItemStack(world,pos.x,pos.y,pos.z,this.getPickupItem());
            this.remove(RemovalReason.DISCARDED);
        }


        //this.playAmbientSound();
        super.tick();
    }


    /* - data sync - */

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        super.addAdditionalSaveData(view);
        view.putBoolean("ShouldReturn", this.shouldReturn);
        view.putBoolean("SoundInit", this.initiaitedSound);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.shouldReturn = view.getBooleanOr("ShouldReturn", false);
        this.initiaitedSound = view.getBooleanOr("SoundInit", false);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }



    /* - getter and setters */

    @Override
    protected double getDefaultGravity() {
        return 0.0002;
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(TCItems.TWISTED_GREATAXE);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    public void setSlot(int value) {
        slot = value;
    }

    private DamageSource getDamageSource(Level world) {
        return new DamageSource(
                world.registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(TCDamageTypes.TOMAHAWK_DAMAGE.identifier()).get());
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 currentPosition, Vec3 nextPosition) {
        return this.shouldReturn ? null : super.findHitEntity(currentPosition, nextPosition);
    }

}