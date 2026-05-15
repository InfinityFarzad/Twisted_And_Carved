package net.farzad.twisted_and_carved.common.block.custom;

import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * ALL RIGHTS RESERVED
 *
 * Powercyphe : originally coded the class
 * InfinityFarzad : modified class with permission for twisted and carved
 *
 */


public class KarmiumFence extends Block implements Segmented {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
    public static final BooleanProperty EAST = ConnectingBlock.EAST;
    public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
    public static final BooleanProperty WEST = ConnectingBlock.WEST;
    public static final BooleanProperty UP_FENCE = BooleanProperty.of("up_fence");
    public static final IntProperty FENCE_SEGMENTS = IntProperty.of("fence_segments",1,4);
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet().stream().collect(Util.toMap());
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesByState;


    public KarmiumFence(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(UP_FENCE, false).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(WATERLOGGED, false));
        this.shapesByState = Map.copyOf(this.stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), KarmiumFence::getShapeForState)));
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = VoxelShapes.empty();

        if (state.get(NORTH)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
        }

        if (state.get(SOUTH)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
        }

        if (state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
        }

        if (state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
        }

        return voxelShape.isEmpty() ? VoxelShapes.fullCube() : voxelShape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapesByState.get(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED, NORTH, EAST, SOUTH, WEST, UP_FENCE, FENCE_SEGMENTS);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (!context.getStack().isOf(state.getBlock().asItem())) return false;

        boolean bl = context.getPlayer() == null || !context.getPlayer().isSneaking();
        return canAdd(state) && bl;
    }

    @Override
    public IntProperty getAmountProperty() {
        return FENCE_SEGMENTS;
    }

    @Override
    public double getHeight() {
        return 1.0;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (world.getBlockState(pos.up()).isOf(ModBlocks.KARMIUM_FENCE)) {
            state = state.with(UP_FENCE,true);
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction side = ctx.getHorizontalPlayerFacing().getOpposite();
        BlockState state = this.getDefaultState();
        BlockState currentState = world.getBlockState(blockPos);
        int segmentCount = getSegment(currentState);

        if (side.getAxis() != Direction.Axis.Y ) {

            if (currentState.isOf(this) && !currentState.get(getFacingProperty(side.getOpposite()))) {
                if (world.getBlockState(ctx.getBlockPos().up()).getBlock() == ModBlocks.KARMIUM_FENCE) {
                    return currentState.with(getFacingProperty(side.getOpposite()), true).with(UP_FENCE,true).with(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
                } else {
                    return currentState.with(getFacingProperty(side.getOpposite()), true).with(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
                }
            }
        }
        if (currentState.isOf(this)) {
            for (Direction direction : DIRECTIONS) {
                if (direction.getAxis() != Direction.Axis.Y && !currentState.get(getFacingProperty(direction))) {
                    return currentState.with(getFacingProperty(direction), true).with(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
                }
            }
        }
        FluidState fluidState = world.getFluidState(blockPos);
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return state.with(getFacingProperty(side.getOpposite()), true).with(WATERLOGGED, bl).with(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
    }

    private int getSegment(BlockState state) {
        if (state.isOf(ModBlocks.KARMIUM_FENCE)) {
            return state.get(FENCE_SEGMENTS);
        }
        return 0;
    }

    public boolean canAdd(BlockState state) {
        if (!state.isOf(this)) {
            return false;
        }
        for (Direction direction : DIRECTIONS) {
            if (direction.getAxis() != Direction.Axis.Y && !state.get(getFacingProperty(direction))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 ->
                    state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
            case CLOCKWISE_90 ->
                    state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
            case FRONT_BACK -> state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    public static BooleanProperty getFacingProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }
}

