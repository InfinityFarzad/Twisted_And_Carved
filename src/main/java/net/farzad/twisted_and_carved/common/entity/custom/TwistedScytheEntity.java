package net.farzad.twisted_and_carved.common.entity.custom;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.util.ModDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.farzad.twisted_and_carved.common.util.EntityUtil.hasEmptySlot;
import static net.farzad.twisted_and_carved.common.util.EntityUtil.returnToSlot;

public class TwistedScytheEntity extends PersistentProjectileEntity {

    public boolean shouldReturn;
    public boolean isGripped;
    public boolean isPlayingSound;
    public LivingEntity prevOwner;
    public float damageMultiplier;
    private ItemStack stack;

    public TwistedScytheEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.TWISTED_SCYTHE_ENTITY, owner, world, stack, null);
        this.isPlayingSound = false;
        this.prevOwner = owner;
        this.stack = stack;
    }

    public TwistedScytheEntity(EntityType<? extends TwistedScytheEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    private void playSound() {
        if (!this.isInGround() && !isPlayingSound) {
            if (!this.getWorld().isClient) {

                GreataxeSoundLoopS2CPayload payload = new GreataxeSoundLoopS2CPayload(this.getId());
                for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld) this.getWorld(), this.getBlockPos(), 20)) {
                    ServerPlayNetworking.send(player, payload);
                }
                isPlayingSound = true;
            }

        }
    }

    private void pullOwner(Entity entity) {
        Vec3d velocity = (new Vec3d(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ()).multiply(2).normalize());
        entity.addVelocity(velocity.multiply(2).normalize());

        entity.velocityModified = true;
    }
    /*
    * TODO :
    *  make the scythe return to the inventory when "shouldReturn" is true
    * */


    public void tick() {

        if (this.inGroundTime > 75) {
            this.shouldReturn = true;
            this.isGripped = false;
        }

        if (this.isInGround()) {
            this.isGripped = true;
        }
        Entity entity = this.getOwner();
        if (entity != null) {
            if (distanceTo(this.getOwner()) > 35 || (distanceTo(this.getOwner()) <= 8 && inGroundTime >= 1 && isGripped)) {
                this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 1.0F, 1.0F);
                this.shouldReturn = true;
                this.isGripped = false;
            }else
            if (isGripped && !shouldReturn) {
                pullOwner(entity);
            }

        }

        if ((this.shouldReturn || this.isNoClip()) && entity != null) {
            if (!this.isOwnerAlive()) {
                this.discard();
            } else {
                if (!(entity instanceof PlayerEntity) && this.getPos().distanceTo(entity.getEyePos()) < (double) entity.getWidth() + 1.0) {
                    this.discard();
                    return;
                }
            }
        }

        //playSound();
        super.tick();
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.shouldReturn ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.getOwner() != null) {
            Entity entity = entityHitResult.getEntity();
            float f = 5.5F;
            DamageSource damageSource = new DamageSource(
                    this.getOwner().getWorld().getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(ModDamageTypes.TOMAHAWK_DAMAGE.getValue()).get());
            World var7 = this.getWorld();
            if (var7 instanceof ServerWorld serverWorld) {
                f = EnchantmentHelper.getDamage(serverWorld, Objects.requireNonNull(this.getWeaponStack()), entity, damageSource, f);
            }

            this.shouldReturn = true;
            if (entity.sidedDamage(damageSource, f + damageMultiplier)) {
                if (entity.getType() == EntityType.ENDERMAN) {
                    return;
                }

                var7 = this.getWorld();
                if (var7 instanceof ServerWorld serverWorld) {
                    EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack(), (item) -> this.kill(serverWorld));
                }

                if (entity instanceof LivingEntity livingEntity) {
                    pullOwner(livingEntity);
                    getScytheStack((PlayerEntity) this.getOwner()).set(ModDataComponents.TWISTED_SCYTHE_GRAPPLING,false);
                    this.shouldReturn = true;
                    this.isGripped = true;
                    this.onHit(livingEntity);
                }
            }

            this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
            this.setVelocity(this.getVelocity().multiply(0.2, 0.02, 0.2));
            this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
        }
    }

    protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        Vec3d vec3d = blockHitResult.getBlockPos().clampToWithin(blockHitResult.getPos());
        Entity var6 = this.getOwner();
        LivingEntity var10002;
        if (var6 instanceof LivingEntity livingEntity) {
            var10002 = livingEntity;
        } else {
            var10002 = null;
        }

        EnchantmentHelper.onHitBlock(world, weaponStack, var10002, this, null, vec3d, world.getBlockState(blockHitResult.getBlockPos()), (item) -> this.kill(world));
    }

    public ItemStack getWeaponStack() {
        return this.getItemStack();
    }

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.TWISTED_SCYTHE);
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT;
    }

    private ItemStack getScytheStack(PlayerEntity player) {
        PlayerInventory inv = player.getInventory();
        ItemStack stack1 = inv.getStack(inv.getSlotWithStack(this.getWeaponStack()));
        return stack1;
    }


    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            if (!this.getWorld().isClient && (this.isInGround() || this.isNoClip() || shouldReturn)) {
                getScytheStack(player).set(ModDataComponents.TWISTED_SCYTHE_GRAPPLING,false);
                this.discard();
            }
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.shouldReturn = nbt.getBoolean("DealtDamage").get();
        this.isPlayingSound = nbt.getBoolean("IsPlayingSound").get();
        this.isGripped = nbt.getBoolean("isGripped").get();
    }

    @Override
    protected double getGravity() {
        return 0.0002;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("DealtDamage", this.shouldReturn);
        nbt.putBoolean("IsPlayingSound", this.isPlayingSound);
        nbt.putBoolean("isGripped", this.isGripped);
    }

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }

    }

    protected float getDragInWater() {
        return 0.99F;
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}