package net.farzad.twisted_and_carved.common.entity;

import com.google.common.collect.ImmutableList;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.register.TCEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.*;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class LostMerchantEntity extends AbstractVillager {

    public LostMerchantEntity(EntityType<? extends AbstractVillager> entityType, Level world) {
        super(TCEntities.LOST_MERCHANT_ENTITY, world);
    }

    public static AttributeSupplier.Builder createDefaultAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,20)
                .add(Attributes.FOLLOW_RANGE,20)
                .add(Attributes.MOVEMENT_SPEED,0.85);
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAlive() && !this.isTrading()) {
            if (!this.level().isClientSide()) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean shouldShowName() {
        return hasCustomName();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), i));
        }
    }

    @Override
    protected void updateTrades(ServerLevel world) {
        MerchantOffers offers = this.getOffers();
        this.addOffersFromTradeSet(world, offers,TradeSets.WANDERING_TRADER_BUYING);
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean sold) {
        return super.getTradeUpdatedSound(sold);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}
