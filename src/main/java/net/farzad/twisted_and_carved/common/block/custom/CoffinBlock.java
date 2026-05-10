package net.farzad.twisted_and_carved.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.block.entity.custom.CoffinBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CoffinBlock extends BlockWithEntity {
    public static final MapCodec<CoffinBlock> CODEC = createCodec(CoffinBlock::new);
    public static final BooleanProperty OPEN;
    public static final BooleanProperty PUSH;
    public static final BooleanProperty FIRST_OPENING = BooleanProperty.of("first_opening");
    public static final EnumProperty<Direction> FACING;

    public CoffinBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState().with(FIRST_OPENING,true).with(OPEN,false).with(PUSH,false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CoffinBlockEntity(pos,state);
    }

    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.getBlockState(pos).get(OPEN) && player.getMainHandStack().isEmpty()) {
            if (world instanceof ServerWorld server && server.getBlockState(pos).get(FIRST_OPENING)) {
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ModParticles.COFFIN_SMOKE,pos.getX() + 0.5,pos.getY() + 1.1,pos.getZ() + 0.5,14, 0,0,0,0.08);
                }
                world.setBlockState(pos,state.with(FIRST_OPENING,false));
            }
            player.swingHand(Hand.MAIN_HAND);
            world.playSound(player,pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,1,1);
            if (player.isSneaking() || world.getBlockState(pos.up()).isSolidBlock(world,pos)) {
                world.setBlockState(pos,state.with(PUSH, true).with(OPEN,true).with(FIRST_OPENING,false));
            } else {
                world.setBlockState(pos,state.with(OPEN, true).with(PUSH,false).with(FIRST_OPENING,false));
            }

            if (world instanceof ServerWorld server && ((CoffinBlockEntity)server.getBlockEntity(pos)).getStack() != null) {
                CoffinBlockEntity coffin = ((CoffinBlockEntity) server.getBlockEntity(pos));
                if (!coffin.getStack().isEmpty()) {
                    player.swingHand(Hand.MAIN_HAND);
                    ItemScatterer.spawn(world,pos.getX(),pos.getY(),pos.getZ(),coffin.getStack());
                    if (world instanceof ServerWorld) {
                        coffin.setStack(ItemStack.EMPTY);
                    }
                    world.playSound(player,pos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS,1,1);
                    return ActionResult.CONSUME;
                } else {
                    return ActionResult.FAIL;
                }
            }

            return ActionResult.CONSUME;
        } else {
            if (!player.isSneaking() ) {
                if (player.getMainHandStack().isEmpty() || (!player.getMainHandStack().isEmpty() && player.getMainHandStack() != ((CoffinBlockEntity) world.getBlockEntity(pos)).getStack() && !((CoffinBlockEntity) world.getBlockEntity(pos)).getStack().isEmpty() && world.getBlockState(pos).get(OPEN)) ) {
                    player.swingHand(Hand.MAIN_HAND);
                    world.setBlockState(pos, state.with(PUSH, false).with(OPEN, false));
                    world.playSound(player,pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,1,1);
                    return ActionResult.CONSUME;
                } else {
                    CoffinBlockEntity coffin = ((CoffinBlockEntity) world.getBlockEntity(pos));
                    if (coffin != null && coffin.isEmpty() && world.getBlockState(pos).get(OPEN)) {
                        if (world instanceof ServerWorld) {
                            coffin.setStack(player.getMainHandStack());
                            player.setStackInHand(Hand.MAIN_HAND,ItemStack.EMPTY);
                        }
                        world.playSound(player,pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,1,1);
                        return ActionResult.CONSUME;
                    }
                }
            } else {
                CoffinBlockEntity coffinBlock = (CoffinBlockEntity) world.getBlockEntity(pos);
                if (coffinBlock != null && !coffinBlock.getStack().isEmpty() && world.getBlockState(pos).get(OPEN)) {
                    if (player.getMainHandStack().isEmpty()) {
                        if (world instanceof ServerWorld) {
                            player.setStackInHand(Hand.MAIN_HAND, coffinBlock.getStack());
                            coffinBlock.setStack(ItemStack.EMPTY);
                        }
                        world.playSound(player,pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,1,1);
                        return ActionResult.CONSUME;
                    }
                } else {
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.FAIL;
        }

    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PUSH);
        builder.add(OPEN);
        builder.add(FACING);
        builder.add(FIRST_OPENING);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (state.get(OPEN)) {
            world.addParticleClient(ModParticles.COFFIN_ASH, x + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), y + Math.max(random.nextDouble() / 20,0.15) * (random.nextBoolean() ? 1 : -1), z + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), 0, 0, 0);
            world.addParticleClient(ModParticles.COFFIN_ASH, x + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), y + Math.max(random.nextDouble() / 20,0.15) * (random.nextBoolean() ? 1 : -1), z + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), 0, 0, 0);
            world.addParticleClient(ModParticles.COFFIN_ASH, x + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), y + Math.max(random.nextDouble() / 20,0.15) * (random.nextBoolean() ? 1 : -1), z + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), 0, 0, 0);
        }

    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) {
            return Block.createCuboidShape(1,0,1,15,11,15);
        }
        return Block.createCuboidShape(1,0,1,15,14,15);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(PUSH, false).with(OPEN,false).with(FACING,ctx.getHorizontalPlayerFacing()).with(FIRST_OPENING,true);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    static {
        OPEN = Properties.OPEN;
        PUSH = BooleanProperty.of("push");
        FACING = HorizontalFacingBlock.FACING;
    }
}
