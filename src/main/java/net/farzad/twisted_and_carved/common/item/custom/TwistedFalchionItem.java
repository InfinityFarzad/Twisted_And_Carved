package net.farzad.twisted_and_carved.common.item.custom;

import net.farzad.twisted_and_carved.client.particle.custom.FalchionSlashEffect;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.sound.ModSounds;
import net.farzad.twisted_and_carved.common.util.ModDamageTypes;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class TwistedFalchionItem extends TwistedToolItem {

    public TwistedFalchionItem(float attackDamage, float attackSpeed, double attackRange, Settings settings) {
        super(applyToolSettings(settings, BlockTags.LEAVES, attackDamage, attackSpeed, attackRange));
    }

    public static Settings applyToolSettings(Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, double attackRange) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return settings.component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(new RegistryEntry[]{Blocks.COBWEB.getRegistryEntry()}), 15.0F), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(effectiveBlocks),1.5f)), 1.0F, 2, false)).attributeModifiers(createAttributeModifiers(attackDamage,attackSpeed,attackRange )).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).enchantable(15);
    }

    public static AttributeModifiersComponent createAttributeModifiers(float attackDamage, float attackSpeed, double attackRange) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (attackDamage), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of(TwistedAndCarved.MOD_ID, "base_attack_range"), attackRange, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    private void setBlood(ItemStack stack, int value) {
        stack.set(ModDataComponents.BLOOD_CHARGE, value);
    }

    private int getBlood(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.BLOOD_CHARGE, 0);
    }

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    private void createSlashDamage(World world, PlayerEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            Vec3d dir = user.raycast(2.5,1,false).getPos();
            Box box = new Box(dir.x,user.getY(),dir.z,dir.x + 1, dir.y + 1, dir.z + 1).expand(0.5).offset(new Vec3d(-0.5,-0.5,-0.5));
            List<LivingEntity> entities = serverWorld.getEntitiesByClass(LivingEntity.class,box,livingEntity -> livingEntity != user);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity && user instanceof PlayerEntity player) {
                    livingEntity.damage(serverWorld, entity.getDamageSources().create(ModDamageTypes.FALCHION_SLASH,user),5f);
                }
            }
        }
    }

    @Override
    public boolean isValidType(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type() == "falchion";
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (getBlood(stack) >= (100 / 3 )) {
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(new FalchionSlashEffect(user.getYaw()), user.getX(), user.getY() + 0.5, user.getZ(), 1, 0, 0, 0, 1);
                serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_MUD_HIT, user.getSoundCategory(), 2.0F, MathHelper.nextBetween(user.getRandom(),3.8f,3.5f));
                serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, user.getSoundCategory(), 2.0F, MathHelper.nextBetween(user.getRandom(),0.5f,0.7f));
                serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.SCYTHE_SWEEP_0, user.getSoundCategory(), 1.0F, MathHelper.nextBetween(user.getRandom(),0.7f,1f));
            }
            user.getItemCooldownManager().set(stack,20 * 2);
            createSlashDamage(world,user);
            user.swingHand(hand);
            setBlood(stack,getBlood(stack) - 100 / 3);
        }


        return super.use(world, user, hand);
    }

    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && TwistedWeaponUtil.getAbilityID(stack) == "bleeding") {
            int amount = player.getWorld().random.nextBetween(1, 3) * 5;
            if (!(getBlood(stack) + amount >= 100)) {
                setBlood(stack, getBlood(stack) + amount);
            } else {
                setBlood(stack, 100);
            }

        }

    }

}
