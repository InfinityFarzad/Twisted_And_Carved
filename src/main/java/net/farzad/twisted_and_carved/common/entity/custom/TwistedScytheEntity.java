package net.farzad.twisted_and_carved.common.entity.custom;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.util.ModDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

    public TwistedScytheEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.TWISTED_SCYTHE_ENTITY, owner, world, stack, null);
        this.isPlayingSound = false;
        this.prevOwner = owner;
    }

    public TwistedScytheEntity(EntityType<? extends TwistedScytheEntity> entityType, World world) {
        super(entityType, world);
    }

    private void playSound() {
        if (!this.isInGround() && !isPlayingSound) {
            if (!this.getWorld().isClient) {

                ScytheSoundLoopS2CPayload payload = new ScytheSoundLoopS2CPayload(this.getId());
                for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld) this.getWorld(), this.getBlockPos(), 20)) {
                    ServerPlayNetworking.send(player, payload);
                }
                isPlayingSound = true;
            }

        }
    }

    private void pullOwner(Entity owner) {
        Vec3d dir = this.getPos().subtract(owner.getPos());
        owner.addVelocity(dir.multiply(0.2));
        owner.velocityModified = true;
        this.shouldReturn = true;
    }

    private void pullTarget(Entity owner, LivingEntity target) {
        Vec3d dir = owner.getPos().subtract(target.getPos());
        target.addVelocity(dir.multiply(0.2));
        target.velocityModified = true;
        this.shouldReturn = true;
    }

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }


    public void tick() {
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity owner) {
            Vec3d targetPos = owner.getPos();
            double distance = targetPos.distanceTo(this.getPos());

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
                Vec3d direction = targetPos.subtract(this.getPos()).normalize();
                if (owner instanceof PlayerEntity player && this.getScytheStack(player) == null) {
                    this.discard();
                }

                this.setVelocity(direction);
                this.move(MovementType.SELF, this.getVelocity());
                this.setNoClip(true);
            }
            playSound();
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
            if (!this.getWorld().isClient && shouldReturn) {
                getScytheStack(player).set(ModDataComponents.TWISTED_SCYTHE_GRAPPLING, false);
                this.discard();
            }
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.getOwner() != null) {
            Entity owner = this.getOwner();
            float f = 5.5F;
            World world = this.getWorld();
            Entity entity = entityHitResult.getEntity();
            DamageSource damageSource = new DamageSource(
                    this.getOwner().getWorld().getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(ModDamageTypes.TOMAHAWK_DAMAGE.getValue()).get());

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

                if (entity instanceof LivingEntity livingEntity) {
                    double boxSize = (livingEntity.getBoundingBox().getLengthZ() + livingEntity.getBoundingBox().getLengthX() + livingEntity.getBoundingBox().getLengthY()) / 3;
                    if (boxSize >= 1.5) {
                        pullOwner(owner);
                    } else {
                        pullTarget(owner, livingEntity);
                    }

                    getScytheStack((PlayerEntity) this.getOwner()).set(ModDataComponents.TWISTED_SCYTHE_GRAPPLING, false);
                    this.shouldReturn = true;
                    this.isGripped = true;
                    this.onHit(livingEntity);
                }
            }

            this.shouldReturn = true;
            this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
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
        return new ItemStack(ModItems.TWISTED_SCYTHE);
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
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("shouldReturn", this.shouldReturn);
        nbt.putBoolean("isPlayingSound", this.isPlayingSound);
        nbt.putBoolean("isGripped", this.isGripped);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.shouldReturn = nbt.getBoolean("shouldReturn").get();
        this.isPlayingSound = nbt.getBoolean("isPlayingSound").get();
        this.isGripped = nbt.getBoolean("isGripped").get();
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}