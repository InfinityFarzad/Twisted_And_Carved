package net.farzad.twisted_and_carved.common.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCEntities;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.register.TCDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class TwistedScytheEntity extends PersistentProjectileEntity {

    // Variables
    public boolean shouldReturn;
    public boolean isGripped;
    public boolean isPlayingSound;
    public LivingEntity prevOwner;
    public float damageMultiplier;
    public float scytheRot;

    public TwistedScytheEntity(World world, LivingEntity owner, ItemStack stack) {
        super(TCEntities.TWISTED_SCYTHE_ENTITY, owner, world, stack, null);
        this.isPlayingSound = false;
        this.prevOwner = owner;
    }

    public TwistedScytheEntity(EntityType<? extends TwistedScytheEntity> entityType, World world) {
        super(entityType, world);
    }

    private void playSound() {
        if (!this.isInGround() && !isPlayingSound) {
            if (!this.getEntityWorld().isClient()) {

                ScytheSoundLoopS2CPayload payload = new ScytheSoundLoopS2CPayload(this.getId());
                for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld) this.getEntityWorld(), this.getBlockPos(), 20)) {
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
        Vec3d dir = this.getEntityPos().subtract(owner.getEntityPos());
        owner.addVelocity(dir.multiply(0.15));
        owner.velocityDirty = true;
        this.shouldReturn = true;
    }

    private void pullTarget(Entity owner, LivingEntity target) {
        Vec3d dir = owner.getEntityPos().subtract(target.getEntityPos());
        target.addVelocity(dir.multiply(0.15));
        target.velocityDirty = true;
        this.shouldReturn = true;
    }

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    public void tick() {
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity owner) {
            Vec3d targetPos = owner.getEntityPos();
            double distance = targetPos.distanceTo(this.getEntityPos());

            if (this.isInGround()) {
                this.isGripped = true;
            }

            if ((distance > 57 && !this.isGripped) || this.inGroundTime > 8) {
                this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 1.0F, 1.0F);
                this.shouldReturn = true;
                this.isGripped = false;
            }

            if (isGripped && !shouldReturn) {
                pullOwner(owner);
                owner.fallDistance = 0;
            }

            if (this.shouldReturn) {
                Vec3d direction = targetPos.subtract(this.getEntityPos()).normalize();
                if (owner instanceof PlayerEntity player && this.getScytheStack(player) == null) {
                    this.discard();
                }

                this.setVelocity(direction);
                this.move(MovementType.SELF, this.getVelocity());
                this.setNoClip(true);
            }
            playSound();
            updateRot();
            super.tick();
        } else {
            this.discard();
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.shouldReturn ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) && this.getOwner() != null && !player.isSpectator()) {
            if (!this.getEntityWorld().isClient() && shouldReturn) {
                getScytheStack(player).set(TCDataComponents.TWISTED_SCYTHE_GRAPPLING, false);
                this.discard();
            }
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.getOwner() != null) {
            Entity ownerEntity = this.getOwner();
            World world = this.getEntityWorld();
            Entity entity = entityHitResult.getEntity();
            float f = 5.5F;
            DamageSource damageSource = new DamageSource(
                    this.getOwner().getEntityWorld().getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(TCDamageTypes.TOMAHAWK_DAMAGE.getValue()).get());

            if (world instanceof ServerWorld serverWorld) {
                f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
            }

            if (entity.sidedDamage(damageSource, f + damageMultiplier)) {
                if (entity.getType() == EntityType.ENDERMAN) {
                    return;
                }

                if (world instanceof ServerWorld serverWorld) {
                    EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack(), (item) -> this.kill(serverWorld));
                }
            }

            if (entity instanceof LivingEntity livingEntity && ownerEntity instanceof PlayerEntity player) {
                double boxSize = (livingEntity.getBoundingBox().getLengthZ() + livingEntity.getBoundingBox().getLengthX() + livingEntity.getBoundingBox().getLengthY()) / 3;
                if (boxSize >= 1.5 || (livingEntity instanceof PlayerEntity && player.isSneaking())) {
                    pullOwner(player);
                } else {
                    pullTarget(player, livingEntity);
                }

                getScytheStack(player).set(TCDataComponents.TWISTED_SCYTHE_GRAPPLING, false);
                this.shouldReturn = true;
                this.isGripped = true;
                this.onHit(livingEntity);
            }
            this.shouldReturn = true;
            this.deflect(ProjectileDeflection.SIMPLE, entity, this.owner, false);
            this.setVelocity(this.getVelocity().multiply(0.2, 0.02, 0.2));
            this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
        }
    }

    @Override
    protected double getGravity() {
        return 0.0002;
    }

    protected float getDragInWater() {
        return 0.99F;
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT;
    }

    public ItemStack getWeaponStack() {
        return this.getItemStack();
    }

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(TCItems.TWISTED_SCYTHE);
    }

    private ItemStack getScytheStack(PlayerEntity player) {
        PlayerInventory inv = player.getInventory();
        if (inv.getSlotWithStack(this.getWeaponStack()) != -1) {
            return inv.getStack(inv.getSlotWithStack(this.getWeaponStack()));
        } else {
            return player.getOffHandStack();
        }
    }

    // Data Storage
    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("shouldReturn", this.shouldReturn);
        view.putBoolean("isPlayingSound", this.isPlayingSound);
        view.putBoolean("isGripped", this.isGripped);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.shouldReturn = view.getBoolean("shouldReturn",false);
        this.isPlayingSound = view.getBoolean("isPlayingSound",false);
        this.isGripped = view.getBoolean("isGripped",false);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}