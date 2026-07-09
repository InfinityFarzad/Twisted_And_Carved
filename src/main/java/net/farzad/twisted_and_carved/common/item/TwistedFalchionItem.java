package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.client.particle.FalchionSlashEffect;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.register.TCDamageTypes;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TDSounds;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TwistedFalchionItem extends TwistedToolItem {

    public TwistedFalchionItem(float attackDamage, float attackSpeed, double attackRange, Properties settings) {
        super(applyToolSettings(settings, BlockTags.LEAVES, attackDamage, attackSpeed, attackRange));
    }

    public static Properties applyToolSettings(Properties settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, double attackRange) {
        HolderGetter<Block> registryEntryLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return settings.component(DataComponents.TOOL, new Tool(List.of(Tool.Rule.minesAndDrops(HolderSet.direct(new Holder[]{Blocks.COBWEB.builtInRegistryHolder()}), 15.0F), Tool.Rule.overrideSpeed(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), Tool.Rule.overrideSpeed(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F), Tool.Rule.overrideSpeed(registryEntryLookup.getOrThrow(effectiveBlocks),1.5f)), 1.0F, 2, false)).attributes(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponents.WEAPON, new Weapon(1)).enchantable(15);
    }

    public static ItemAttributeModifiers createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (attackDamage), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    private void setBlood(ItemStack stack, int value) {
        stack.set(TCDataComponents.BLOOD_CHARGE, value);
        System.out.println("used" + value);
    }

    private int getBlood(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.BLOOD_CHARGE, 0);
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
        return !user.hasInfiniteMaterials();
    }

    private void applySlashDamage(Level world, Player user) {
        if (world instanceof ServerLevel serverWorld) {
            Vec3 dir = user.pick(2.5,1,false).getLocation();
            AABB box = new AABB(dir.x,user.getY(),dir.z,dir.x + 1, dir.y + 1, dir.z + 1).inflate(0.5).move(new Vec3(-0.5,-0.5,-0.5));
            List<LivingEntity> entities = serverWorld.getEntitiesOfClass(LivingEntity.class,box,livingEntity -> livingEntity != user);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity && user instanceof Player player) {
                    livingEntity.hurtServer(serverWorld, entity.damageSources().source(TCDamageTypes.FALCHION_SLASH,user),5f);
                }
            }
        }
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type() == "falchion";
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (TwistedWeaponUtil.getAbilityID(stack).equals("bleeding")) {
            if (getBlood(stack) >= (100 / 3)) {
                if (world instanceof ServerLevel serverWorld) {
                    serverWorld.sendParticles(new FalchionSlashEffect(user.getYRot()), user.getX(), user.getY() + 0.5, user.getZ(), 1, 0, 0, 0, 1);
                    serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.MUD_HIT, user.getSoundSource(), 2.0F, Mth.randomBetween(user.getRandom(), 3.8f, 3.5f));
                    serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, user.getSoundSource(), 2.0F, Mth.randomBetween(user.getRandom(), 0.5f, 0.7f));
                    serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), TDSounds.SCYTHE_SWEEP_0, user.getSoundSource(), 1.0F, Mth.randomBetween(user.getRandom(), 0.7f, 1f));
                }
                applySlashDamage(world, user);
                user.swing(hand);
                setBlood(stack, getBlood(stack) - 100 / 3);
                user.getCooldowns().addCooldown(stack, 20 * 2);

            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void onFullAttack(LivingEntity attacker, LivingEntity target, ItemStack stack) {
        if (attacker instanceof Player player && TwistedWeaponUtil.getAbilityID(stack) == "bleeding") {
            int amount = player.level().random.nextIntBetweenInclusive(1, 3) * 5;
            if (!(getBlood(stack) + amount >= 100)) {
                setBlood(stack, getBlood(stack) + amount);
            } else {
                setBlood(stack, 100);
            }

        }
    }
}
