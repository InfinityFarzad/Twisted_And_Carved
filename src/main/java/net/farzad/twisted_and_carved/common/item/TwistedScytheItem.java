package net.farzad.twisted_and_carved.common.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.farzad.twisted_and_carved.client.particle.HarvestSlashEffect;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.CritInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TwistedScytheItem extends TwistedToolItem implements CritInterface {

    public TwistedScytheItem(float attackDamage, float attackSpeed, double attackRange, Settings settings) {
        super(applyToolSettings(settings, attackDamage, attackSpeed, attackRange));
    }

    public static Settings applyToolSettings(Settings settings, float attackDamage, float attackSpeed, double attackRange) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return settings.component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(new RegistryEntry[]{Blocks.COBWEB.getRegistryEntry()}), 15.0F), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)), 1.0F, 2, false)).attributeModifiers(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).repairable(TDTags.Items.TWISTED_TOOL_REPAIR_INGREDIENT).enchantable(15);
    }

    public static AttributeModifiersComponent createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (attackDamage), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type().equals("scythe");
    }

    private static void clearField(int range, World world, PlayerEntity user, Hand hand) {
        List<BlockPos> blocks = new ArrayList<>();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos pos = new BlockPos((int) user.getX() + x, (int) user.getY() + y, (int) user.getZ() + z);
                    if (world.getBlockState(pos).getBlock().getDefaultState().isIn(BlockTags.CROPS)) {
                        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, user.getSoundCategory(), 1.0F, 1.0F);
                        world.breakBlock(pos, true);
                        user.swingHand(hand);
                        blocks.add(pos);
                    }

                }
            }
        }

        if (blocks.isEmpty()) {
            user.sendMessage(Text.translatable("massage.twisted_and_carved.unable_to_harvest").formatted(Formatting.DARK_RED),true);
            user.getEntityWorld().playSound(user,user.getBlockPos(),SoundEvents.ENTITY_ITEM_BREAK.value(),user.getSoundCategory(),1,MathHelper.nextBetween(user.getRandom(),0.5f,0.7f));
        } else {
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(new HarvestSlashEffect(user.getYaw()), user.getX(), user.getY() + 0.5, user.getZ(), 1, 0, 0, 0, 1);
            }
        }

    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 78000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TRIDENT;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
        if (user instanceof PlayerEntity player) {
            if (Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "grappling")) {
                if (useTime < 10) {
                    return false;
                } else {
                    stack.set(TCDataComponents.TWISTED_SCYTHE_GRAPPLING,true);
                    if (world instanceof ServerWorld serverWorld) {
                        ProjectileEntity.spawnWithVelocity(TwistedScytheEntity::new, serverWorld, stack.copy(), user, 0.0F, (float) remainingUseTicks * 0.00005f, 1.0F);
                    }
                    if (!player.isInCreativeMode()) {
                        player.getItemCooldownManager().set(stack,20 * 5);
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

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        String ability = TwistedWeaponUtil.getAbilityID(itemStack);

        if (ability.equals(TwistedSpiritComponent.EMPTY.type())) {
            return ActionResult.FAIL;
        }
        else if (ability.equals("harvest")) {
            if (!user.isSneaking()) {
                return ActionResult.FAIL;
            } else {
                clearField(5,world,user,hand);
                if (!user.isInCreativeMode()) {
                    user.getItemCooldownManager().set(itemStack, 20);
                }
                return ActionResult.CONSUME;
            }

        } else {
            if (ability.equals("grappling") && !user.getStackInHand(hand).getOrDefault(TCDataComponents.TWISTED_SCYTHE_GRAPPLING,true) && user.getInventory().contains(itemStack)) {
                user.setCurrentHand(hand);
                return ActionResult.CONSUME;
            }
            else {
                return ActionResult.FAIL;
            }

        }
    }

    @Override
    public void onCrit(LivingEntity attacker, LivingEntity target, ItemStack stack) {
        if (TwistedWeaponUtil.getAbilityID(stack).equals("grappling")) {
            double boxSize = (target.getBoundingBox().getLengthZ() + target.getBoundingBox().getLengthX() + target.getBoundingBox().getLengthY()) / 3;
            double dis = target.getEntityPos().distanceTo(attacker.getEntityPos());
            double f = dis / 3.5;
            f /= boxSize > 1.4 ? boxSize : 1;
            f = boxSize > 1.4 ? dis / 3.5 / boxSize : dis / 3.5;
            Vec3d velocity = (new Vec3d(target.getX() - attacker.getX(), target.getY() - attacker.getY(), target.getZ() - attacker.getZ()).normalize().multiply(f * -1));
            target.setVelocity(velocity);
            System.out.println(boxSize);
            target.velocityDirty=true;
        }
    }
}
