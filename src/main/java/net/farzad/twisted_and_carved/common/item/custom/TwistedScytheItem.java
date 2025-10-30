package net.farzad.twisted_and_carved.common.item.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
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
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.farzad.twisted_and_carved.common.util.EnchantmentUtil.hasEnchantment;

public class TwistedScytheItem extends TwistedToolItem {

    protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS;

    static {
        TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of((itemUsageContext) -> {
            return true;
        }, createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));
    }

    public TwistedScytheItem(float attackDamage, float attackSpeed, double attackRange, Settings settings) {
        super(applyToolSettings(settings, BlockTags.HOE_MINEABLE, attackDamage, attackSpeed, attackRange));
    }

    public static Settings applyToolSettings(Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, double attackRange) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return settings.component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(new RegistryEntry[]{Blocks.COBWEB.getRegistryEntry()}), 15.0F), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)), 1.0F, 2, false)).attributeModifiers(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).repairable(ModTags.Items.TWISTED_TOOL_REPAIR_INGREDIENT).enchantable(15);
    }

    public static AttributeModifiersComponent createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (attackDamage), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    public static Consumer<ItemUsageContext> createTillAction(BlockState result) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
        };
    }

    public static Consumer<ItemUsageContext> createTillAndDropAction(BlockState result, ItemConvertible droppedItem) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(droppedItem));
        };
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type() == "scythe";
    }

    private static void clearField(int range, World world, PlayerEntity user, Hand hand) {
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos pos = new BlockPos((int) user.getX() + x, (int) user.getY() + y, (int) user.getZ() + z);
                    if (world.getBlockState(pos).getBlock().getDefaultState().isIn(BlockTags.CROPS)) {
                        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, user.getSoundCategory(), 1.0F, 1.0F);

                        world.breakBlock(pos, true);
                        user.swingHand(hand);
                        user.getItemCooldownManager().set(user.getStackInHand(hand), 20 * 5);
                    }

                }
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 78000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
        if (user instanceof PlayerEntity player) {
            if (TwistedWeaponUtil.getAbilityID(stack) == "grappling") {
                if (useTime < 10) {
                    return false;
                } else {
                    stack.set(ModDataComponents.TWISTED_SCYTHE_GRAPPLING,true);
                    if (world instanceof ServerWorld serverWorld) {
                        ProjectileEntity.spawnWithVelocity(TwistedScytheEntity::new, serverWorld, stack.copy(), user, 0.0F, (float) remainingUseTicks * 0.00005f, 1.0F);
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
        if (TwistedWeaponUtil.getAbilityID(itemStack) == "harvest" || TwistedWeaponUtil.getAbilityID(itemStack) == "grappling") {
            if (TwistedWeaponUtil.getAbilityID(itemStack) == "harvest" && user.isSneaking()) {
                clearField(5,world,user,hand);
                return ActionResult.PASS;
            }
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        } else {
            return ActionResult.FAIL;
        }

    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        if (TwistedWeaponUtil.getAbilityID(context.getStack()) == "harvest") {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
            if (pair == null) {
                return ActionResult.PASS;
            } else {
                Predicate<ItemUsageContext> predicate = pair.getFirst();
                Consumer<ItemUsageContext> consumer = pair.getSecond();
                if (predicate.test(context) && !context.getPlayer().isSneaking() && TwistedWeaponUtil.getAbilityID(context.getStack()) == "harvest") {
                    PlayerEntity playerEntity = context.getPlayer();
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isClient) {
                        consumer.accept(context);
                        if (playerEntity != null) {
                            context.getStack().damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
                        }
                    }

                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.PASS;
                }
            }
        } else {
            return ActionResult.FAIL;
        }
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return super.canMine(stack,state,world,pos,user);
    }
}
