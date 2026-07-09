package net.farzad.twisted_and_carved.common.block;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.block.entity.CoffinBlockEntity;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CoffinBlock extends BaseEntityBlock {
    public static final MapCodec<CoffinBlock> CODEC = simpleCodec(CoffinBlock::new);
    public static final BooleanProperty OPEN;
    public static final BooleanProperty PUSH;
    public static final BooleanProperty FIRST_OPENING = BooleanProperty.create("first_opening");
    public static final EnumProperty<Direction> FACING;

    public CoffinBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(getStateDefinition().any().setValue(FIRST_OPENING,true).setValue(OPEN,false).setValue(PUSH,false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoffinBlockEntity(pos,state);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.getBlockState(pos).getValue(OPEN) && player.getMainHandItem().isEmpty()) {
            if (world instanceof ServerLevel server && server.getBlockState(pos).getValue(FIRST_OPENING)) {
                if (world instanceof ServerLevel serverWorld) {
                    serverWorld.sendParticles(TCParticles.COFFIN_SMOKE,pos.getX() + 0.5,pos.getY() + 1.1,pos.getZ() + 0.5,14, 0,0,0,0.08);
                }
                world.setBlockAndUpdate(pos,state.setValue(FIRST_OPENING,false));
            }
            player.swing(InteractionHand.MAIN_HAND);
            world.playSound(player,pos, SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS,1,1);
            if (player.isShiftKeyDown() || world.getBlockState(pos.above()).isRedstoneConductor(world,pos)) {
                world.setBlockAndUpdate(pos,state.setValue(PUSH, true).setValue(OPEN,true).setValue(FIRST_OPENING,false));
            } else {
                world.setBlockAndUpdate(pos,state.setValue(OPEN, true).setValue(PUSH,false).setValue(FIRST_OPENING,false));
            }

            if (world instanceof ServerLevel server && ((CoffinBlockEntity)server.getBlockEntity(pos)).getTheItem() != null) {
                CoffinBlockEntity coffin = ((CoffinBlockEntity) server.getBlockEntity(pos));
                if (!coffin.getTheItem().isEmpty()) {
                    player.swing(InteractionHand.MAIN_HAND);
                    Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),coffin.getTheItem());
                    if (world instanceof ServerLevel) {
                        coffin.setTheItem(ItemStack.EMPTY);
                    }
                    world.playSound(player,pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS,1,1);
                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.FAIL;
                }
            }

            return InteractionResult.CONSUME;
        } else {
            if (!player.isShiftKeyDown() ) {
                if (player.getMainHandItem().isEmpty() || (!player.getMainHandItem().isEmpty() && player.getMainHandItem() != ((CoffinBlockEntity) world.getBlockEntity(pos)).getTheItem() && !((CoffinBlockEntity) world.getBlockEntity(pos)).getTheItem().isEmpty() && world.getBlockState(pos).getValue(OPEN)) ) {
                    player.swing(InteractionHand.MAIN_HAND);
                    world.setBlockAndUpdate(pos, state.setValue(PUSH, false).setValue(OPEN, false));
                    world.playSound(player,pos, SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS,1,1);
                    return InteractionResult.CONSUME;
                } else {
                    CoffinBlockEntity coffin = ((CoffinBlockEntity) world.getBlockEntity(pos));
                    if (coffin != null && coffin.isEmpty() && world.getBlockState(pos).getValue(OPEN)) {
                        if (world instanceof ServerLevel) {
                            coffin.setTheItem(player.getMainHandItem());
                            player.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
                        }
                        world.playSound(player,pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1,1);
                        return InteractionResult.CONSUME;
                    }
                }
            } else {
                CoffinBlockEntity coffinBlock = (CoffinBlockEntity) world.getBlockEntity(pos);
                if (coffinBlock != null && !coffinBlock.getTheItem().isEmpty() && world.getBlockState(pos).getValue(OPEN)) {
                    if (player.getMainHandItem().isEmpty()) {
                        if (world instanceof ServerLevel) {
                            player.setItemInHand(InteractionHand.MAIN_HAND, coffinBlock.getTheItem());
                            coffinBlock.setTheItem(ItemStack.EMPTY);
                        }
                        world.playSound(player,pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1,1);
                        return InteractionResult.CONSUME;
                    }
                } else {
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.FAIL;
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PUSH);
        builder.add(OPEN);
        builder.add(FACING);
        builder.add(FIRST_OPENING);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (state.getValue(OPEN)) {
            world.addParticle(TCParticles.COFFIN_ASH, x + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), y + Math.max(random.nextDouble() / 20,0.15) * (random.nextBoolean() ? 1 : -1), z + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), 0, 0, 0);
            world.addParticle(TCParticles.COFFIN_ASH, x + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), y + Math.max(random.nextDouble() / 20,0.15) * (random.nextBoolean() ? 1 : -1), z + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), 0, 0, 0);
            world.addParticle(TCParticles.COFFIN_ASH, x + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), y + Math.max(random.nextDouble() / 20,0.15) * (random.nextBoolean() ? 1 : -1), z + Math.max(random.nextDouble() / 2,0.28) * (random.nextBoolean() ? 1 : -1), 0, 0, 0);
        }

    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return Block.box(1,0,1,15,11,15);
        }
        return Block.box(1,0,1,15,14,15);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState)this.defaultBlockState().setValue(PUSH, false).setValue(OPEN,false).setValue(FACING,ctx.getHorizontalDirection()).setValue(FIRST_OPENING,true);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    static {
        OPEN = BlockStateProperties.OPEN;
        PUSH = BooleanProperty.create("push");
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
