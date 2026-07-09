package net.farzad.twisted_and_carved.common.item;

import net.farzad.twisted_and_carved.client.particle.HarvestSlashEffect;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TwistedScytheItem extends TwistedToolItem {

    public TwistedScytheItem(float attackDamage, float attackSpeed, double attackRange, Properties settings) {
        super(applyToolSettings(settings, attackDamage, attackSpeed, attackRange));
    }

    public static Properties applyToolSettings(Properties settings, float attackDamage, float attackSpeed, double attackRange) {
        HolderGetter<Block> registryEntryLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return settings.component(DataComponents.TOOL, new Tool(List.of(Tool.Rule.minesAndDrops(HolderSet.direct(new Holder[]{Blocks.COBWEB.builtInRegistryHolder()}), 15.0F), Tool.Rule.overrideSpeed(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), Tool.Rule.overrideSpeed(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)), 1.0F, 2, false)).attributes(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponents.WEAPON, new Weapon(1)).repairable(TDTags.Items.TWISTED_TOOL_REPAIR_INGREDIENT).enchantable(15);
    }

    public static ItemAttributeModifiers createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (attackDamage), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type().equals("scythe");
    }

    private static void clearField(int range, Level world, Player user, InteractionHand hand) {
        List<BlockPos> blocks = new ArrayList<>();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos pos = new BlockPos((int) user.getX() + x, (int) user.getY() + y, (int) user.getZ() + z);
                    if (world.getBlockState(pos).getBlock().defaultBlockState().is(BlockTags.CROPS)) {
                        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, user.getSoundSource(), 1.0F, 1.0F);
                        world.destroyBlock(pos, true);
                        user.swing(hand);
                        blocks.add(pos);
                    }

                }
            }
        }

        if (blocks.isEmpty()) {
            user.sendOverlayMessage(Component.translatable("massage.twisted_and_carved.unable_to_harvest").withStyle(ChatFormatting.DARK_RED));
            user.level().playSound(user,user.blockPosition(),SoundEvents.ITEM_BREAK.value(),user.getSoundSource(),1,Mth.randomBetween(user.getRandom(),0.5f,0.7f));
        } else {
            if (world instanceof ServerLevel serverWorld) {
                serverWorld.sendParticles(new HarvestSlashEffect(user.getYRot()), user.getX(), user.getY() + 0.5, user.getZ(), 1, 0, 0, 0, 1);
            }
        }

    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 78000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getUseDuration(stack, user) - remainingUseTicks;
        if (user instanceof Player player) {
            if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "grappling")) {
                if (useTime < 10) {
                    return false;
                } else {
                    stack.set(TCDataComponents.TWISTED_SCYTHE_GRAPPLING,true);
                    if (world instanceof ServerLevel serverWorld) {
                        Projectile.spawnProjectileFromRotation(TwistedScytheEntity::new, serverWorld, stack.copy(), user, 0.0F, (float) remainingUseTicks * 0.00005f, 1.0F);
                    }
                    if (!player.hasInfiniteMaterials()) {
                        player.getCooldowns().addCooldown(stack,20 * 5);
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        String ability = TwistedWeaponUtil.getAbilityID(itemStack);

        if (ability.equals(TwistedSpiritComponent.EMPTY.type())) {
            return InteractionResult.FAIL;
        }
        else if (ability.equals("harvest")) {
            if (!user.isShiftKeyDown()) {
                return InteractionResult.FAIL;
            } else {
                clearField(5,world,user,hand);
                if (!user.hasInfiniteMaterials()) {
                    user.getCooldowns().addCooldown(itemStack, 20);
                }
                return InteractionResult.CONSUME;
            }

        } else {
            if (ability.equals("grappling") && !user.getItemInHand(hand).getOrDefault(TCDataComponents.TWISTED_SCYTHE_GRAPPLING,true) && user.getInventory().contains(itemStack)) {
                user.startUsingItem(hand);
                return InteractionResult.CONSUME;
            }
            else {
                return InteractionResult.FAIL;
            }

        }
    }

    @Override
    public void onCritAttack(LivingEntity attacker, LivingEntity target, ItemStack stack) {

        if (TwistedWeaponUtil.getAbilityID(stack).equals("grappling")) {
            double boxSize = (target.getBoundingBox().getZsize() + target.getBoundingBox().getXsize() + target.getBoundingBox().getYsize()) / 3;
            double dis = target.position().distanceTo(attacker.position());
            double f = dis / 3.5;
            f /= boxSize > 1.4 ? boxSize : 1;
            f = boxSize > 1.4 ? dis / 3.5 / boxSize : dis / 3.5;
            Vec3 velocity = (new Vec3(target.getX() - attacker.getX(), target.getY() - attacker.getY(), target.getZ() - attacker.getZ()).normalize().scale(f * -1));
            target.setDeltaMovement(velocity);
            System.out.println(boxSize);
            target.needsSync=true;
        }
    }
}
