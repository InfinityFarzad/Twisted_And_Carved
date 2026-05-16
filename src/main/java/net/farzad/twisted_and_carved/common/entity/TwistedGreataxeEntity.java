package net.farzad.twisted_and_carved.common.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.register.TCEntities;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.register.TCDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.farzad.twisted_and_carved.common.util.EntityUtil.hasEmptySlot;
import static net.farzad.twisted_and_carved.common.util.EntityUtil.returnToSlot;

public class TwistedGreataxeEntity extends PersistentProjectileEntity {

    public boolean dealtDamage;
    public boolean resetInGroundTime;
    public int returnTimer;
    public boolean isPlayingSound;
    public LivingEntity prevOwner;
    private int slot;
    public float damageMultiplier;

    public TwistedGreataxeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(TCEntities.TWISTED_GREATAXE_ENTITY, owner, world, stack, null);
        this.isPlayingSound = false;
        this.prevOwner = owner;
    }

    public TwistedGreataxeEntity(EntityType<? extends TwistedGreataxeEntity> entityType, World world) {
        super(entityType, world);
    }

    public static <T extends ProjectileEntity> T spawnWithVelocity(ProjectileCreator<T> creator, int slot, ServerWorld world, ItemStack projectileStack, LivingEntity shooter, float roll, float power, float divergence) {
        return spawn(creator.create(world, shooter, projectileStack), world, projectileStack, (entity) -> {
            if (entity instanceof TwistedGreataxeEntity twistedGreataxe) {
                twistedGreataxe.setSlot(slot);
            }
            entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), roll, power, divergence);
        });
    }

    public void setSlot(int value) {
        slot = value;
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    private void playSound() {
        if (!this.isInGround() && !isPlayingSound) {
            if (!this.getEntityWorld().isClient()) {

                GreataxeSoundLoopS2CPayload payload = new GreataxeSoundLoopS2CPayload(this.getId());
                for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld) this.getEntityWorld(), this.getBlockPos(), 20)) {
                    ServerPlayNetworking.send(player, payload);
                }
                isPlayingSound = true;
            }

        }
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (getOwner() != null && this.getEntityPos().distanceTo(getOwner().getEntityPos()) >= 25 && !dealtDamage){
            this.dealtDamage = true;
            this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 1.0F, 1.0F);
            this.setVelocity(Vec3d.ZERO);
        }

        if (resetInGroundTime) {
            this.inGroundTime = 0;
            this.resetInGroundTime = false;
        }

        if (this.getOwner() != null) {
            if (distanceTo(this.getOwner()) > 25 || (this.getVelocity().x < 0 && this.getVelocity().y < 0 && this.getVelocity().z < 0)) {
                this.dealtDamage = true;
            }
            this.getOwner().fallDistance = 0;
            //double distance = this.getEntityPos().distanceTo(this.getOwner().getEntityPos()) / 5;
            //this.setVelocity(this.getVelocity().x / distance,this.getVelocity().y / distance, this.getVelocity().z / distance );
        }
        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoClip()) && entity != null) {
            if (!this.isOwnerAlive()) {
                World var4 = this.getEntityWorld();
                if (var4 instanceof ServerWorld serverWorld) {
                    if (this.pickupType == PickupPermission.ALLOWED) {
                        this.dropStack(serverWorld, this.asItemStack(), 0.1F);
                    }
                }

                this.discard();
            } else {
                if (!(entity instanceof PlayerEntity) && this.getEntityPos().distanceTo(entity.getEyePos()) < (double) entity.getWidth() + 1.0) {
                    this.discard();
                    return;
                }

                this.setNoClip(true);
                Vec3d target = new Vec3d(entity.getX(),entity.getY() + 0.8,entity.getZ());
                Vec3d direction = target.subtract(this.getEntityPos().add(entity.getVelocity())).normalize();
                //double distance = this.getEntityPos().distanceTo(this.getOwner().getEntityPos()) / 5;

                this.setVelocity(direction.multiply(0.55));
                //this.setVelocity(this.getVelocity().x / distance,this.getVelocity().y / distance, this.getVelocity().z / distance);
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 1.0F, 1.0F);
                }
                this.move(MovementType.SELF, this.getVelocity());
                ++this.returnTimer;
            }
        }

        playSound();
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

    public void applyParryKnockback() {
        if (this.getOwner() != null) {
            this.velocityDirty = true;
            this.setVelocity(this.getOwner().getRotationVector().normalize().multiply(2));
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @org.jspecify.annotations.Nullable Entity deflector, @org.jspecify.annotations.Nullable LazyEntityReference<Entity> lazyEntityReference, boolean fromAttack) {
        return false;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 5.5F;
        World world = this.getEntityWorld();
        DamageSource damageSource = new DamageSource(
                this.getOwner().getEntityWorld().getRegistryManager()
                        .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(TCDamageTypes.TOMAHAWK_DAMAGE.getValue()).get());

        if (entity != this.getOwner()) {
            if (world instanceof ServerWorld serverWorld) {
                f = EnchantmentHelper.getDamage(serverWorld, Objects.requireNonNull(this.getWeaponStack()), entity, damageSource, f);
            }

            this.dealtDamage = true;
            if (entity.sidedDamage(damageSource, f + damageMultiplier)) {
                if (entity.getType() == EntityType.ENDERMAN) {
                    return;
                }

                if (world instanceof ServerWorld serverWorld) {
                    EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack(), (item) -> this.kill(serverWorld));
                }

                if (entity instanceof LivingEntity livingEntity) {
                    this.knockback(livingEntity, damageSource);
                    this.onHit(livingEntity);
                }
            }
        }

        this.deflect(ProjectileDeflection.SIMPLE, entity, this.owner, false);
        this.setVelocity(this.getVelocity().multiply(0.2, 0.02, 0.2));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
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

    protected boolean tryPickup(PlayerEntity player) {
        boolean canItPickUp1;
        switch (this.pickupType.ordinal()) {
            case 0 -> canItPickUp1 = false;
            case 1 -> canItPickUp1 = hasEmptySlot(player,slot);
            case 2 -> canItPickUp1 = player.isInCreativeMode();
            default -> throw new MatchException(null, null);
        }

        return canItPickUp1 || this.isNoClip() && this.isOwner(player) && hasEmptySlot(player,slot);
    }

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(TCItems.TWISTED_GREATAXE);
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() != null) {
            if (!this.getEntityWorld().isClient() && (this.isInGround() || this.isNoClip()) && this.shake <= 0) {
                if (this.tryPickup(player)) {
                    returnToSlot(player, this.slot, this.asItemStack());
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("DealtDamage", this.dealtDamage);
        view.putBoolean("IsPlayingSound", this.isPlayingSound);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.dealtDamage = view.getBoolean("DealtDamage",false);
        this.isPlayingSound = view.getBoolean("IsPlayingSound",false);
    }

    @Override
    protected double getGravity() {
        return 0.0002;
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