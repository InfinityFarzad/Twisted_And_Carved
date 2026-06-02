package net.farzad.twisted_and_carved.common.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.register.TCDamageTypes;
import net.farzad.twisted_and_carved.common.register.TCEntities;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.util.PlayerInventoryUtil;
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
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TwistedGreataxeEntity extends PersistentProjectileEntity {

    private final static float DAMAGE = 5.0f;
    private final static float MAX_DISTANCE = 24.0f;

    public boolean shouldReturn;
    public LivingEntity prevOwner;
    public float damageMultiplier;
    private int slot;
    private boolean initiaitedSound;

    public TwistedGreataxeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(TCEntities.TWISTED_GREATAXE_ENTITY, owner, world, stack, null);
        this.prevOwner = owner;
    }

    public TwistedGreataxeEntity(EntityType<? extends TwistedGreataxeEntity> entityType, World world) {
        super(entityType, world);
    }

    public static <T extends ProjectileEntity> T spawnTwistedGreataxeWithVelocity(ProjectileCreator<T> creator, int slot, ServerWorld world, ItemStack projectileStack, LivingEntity shooter, float roll, float power, float divergence) {
        return ProjectileEntity.spawn(creator.create(world, shooter, projectileStack), world, projectileStack, (entity) -> {
            if (entity instanceof TwistedGreataxeEntity twistedGreataxe) {
                twistedGreataxe.setSlot(slot);
                twistedGreataxe.initiaitedSound = false;
            }
            entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), roll, power, divergence);
        });
    }



    /* - other - */

    public void resetGroundTime() {
        this.inGroundTime = 0;
    }

    public void applyParryKnockback() {
        if (this.getOwner() != null) {
            this.resetGroundTime();
            this.setVelocity(this.getOwner().getRotationVector().normalize().multiply(2));
            this.velocityDirty = true;
        }
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @org.jspecify.annotations.Nullable Entity deflector, @org.jspecify.annotations.Nullable LazyEntityReference<Entity> lazyEntityReference, boolean fromAttack) {
        return false;
    }


    /* - event - */

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwnerAlive() && shouldReturn) {
            if (!this.getEntityWorld().isClient() && this.shake <= 0) {
                if (this.tryPickup(player)) {
                    PlayerInventoryUtil.returnToSlot(player, this.slot, this.asItemStack());
                    this.discard();
                }
            }
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getEntityWorld();
        Entity targetEntity = entityHitResult.getEntity();
        DamageSource damageSource = getDamageSource(world);
        float damage = DAMAGE;


        if (targetEntity != this.getOwner()) {
            if (world instanceof ServerWorld serverWorld) {
                damage = EnchantmentHelper.getDamage(serverWorld, Objects.requireNonNull(this.getWeaponStack()), targetEntity, damageSource, damage);
                if (targetEntity.damage(serverWorld, damageSource, damage + damageMultiplier)) {
                    if (targetEntity instanceof LivingEntity livingEntity) {
                        this.knockback(livingEntity, damageSource);
                        this.onHit(livingEntity);
                    }
                    EnchantmentHelper.onTargetDamaged(serverWorld, targetEntity, damageSource, this.getWeaponStack(), (item) -> this.kill(serverWorld));
                }
            }
            this.shouldReturn = true;
        }

        this.deflect(ProjectileDeflection.SIMPLE, targetEntity, this.owner, false);
        this.setVelocity(this.getVelocity().multiply(0.2, 0.02, 0.2));
        this.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 1.0F);
    }

    protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        Vec3d pos = blockHitResult.getBlockPos().clampToWithin(blockHitResult.getPos());
        BlockPos blockPos = blockHitResult.getBlockPos();
        Entity owner = this.getOwner();
        LivingEntity livingOwner = owner instanceof LivingEntity living ? living : null;

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.addBlockBreakParticles(blockPos, world.getBlockState(blockPos));
        }

        EnchantmentHelper.onHitBlock(world, weaponStack, livingOwner, this, null, pos, world.getBlockState(blockHitResult.getBlockPos()), (item) -> this.kill(world));
        this.shake = 7;
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT_GROUND,1,1);
    }



    /* - conditions - */

    protected boolean tryPickup(PlayerEntity player) {
        boolean canItPickUp1;
        switch (this.pickupType.ordinal()) {
            case 0 -> canItPickUp1 = false;
            case 1 -> canItPickUp1 = PlayerInventoryUtil.hasEmptySlot(player, slot);
            case 2 -> canItPickUp1 = player.isInCreativeMode();
            default -> throw new MatchException(null, null);
        }

        return canItPickUp1 || this.isNoClip() && this.isOwner(player) && PlayerInventoryUtil.hasEmptySlot(player, slot);
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean shouldStopPlayingSound() {
        return this.isOnGround() || this.isInGround();
    }



    /* - update methods - */

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    private void playAmbientSound() {
        if (!this.getEntityWorld().isClient() && !initiaitedSound) {
            GreataxeSoundLoopS2CPayload payload = new GreataxeSoundLoopS2CPayload(this.getId());
            for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld) this.getEntityWorld(), this.getBlockPos(), 20)) {
                ServerPlayNetworking.send(player, payload);
            }
            initiaitedSound = false;
        }
    }

    @Override
    public void tick() {
        World world = this.getEntityWorld();
        Entity entity = this.getOwner();
        Vec3d pos = this.getEntityPos();

        if (isOwnerAlive() && entity instanceof PlayerEntity ownerEntity) {
            Vec3d ownerPos = ownerEntity.getEntityPos();

            if (!shouldReturn) {
                if (inGroundTime > 4) {
                    shouldReturn = true;
                }

                if (pos.distanceTo(ownerPos) > MAX_DISTANCE) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN,1,1);
                    this.shouldReturn = true;
                }
            } else {
                Vec3d target = new Vec3d(ownerPos.getX(), ownerPos.getY() + 0.8, ownerPos.getZ());
                Vec3d direction = target.subtract(pos.add(entity.getVelocity())).normalize();

                this.setVelocity(direction.multiply(0.55));
                this.setNoClip(true);
                this.move(MovementType.SELF, this.getVelocity());
            }

        } else if (!isOwnerAlive()) {
            ItemScatterer.spawn(world,pos.x,pos.y,pos.z,this.asItemStack());
            this.remove(RemovalReason.DISCARDED);
        }


        //this.playAmbientSound();
        super.tick();
    }


    /* - data sync - */

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("ShouldReturn", this.shouldReturn);
        view.putBoolean("SoundInit", this.initiaitedSound);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.shouldReturn = view.getBoolean("ShouldReturn", false);
        this.initiaitedSound = view.getBoolean("SoundInit", false);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }



    /* - getter and setters */

    @Override
    protected double getGravity() {
        return 0.0002;
    }

    protected float getDragInWater() {
        return 0.99F;
    }

    public ItemStack getWeaponStack() {
        return this.getItemStack();
    }

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(TCItems.TWISTED_GREATAXE);
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT;
    }

    public void setSlot(int value) {
        slot = value;
    }

    private DamageSource getDamageSource(World world) {
        return new DamageSource(
                world.getRegistryManager()
                        .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(TCDamageTypes.TOMAHAWK_DAMAGE.getValue()).get());
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.shouldReturn ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

}