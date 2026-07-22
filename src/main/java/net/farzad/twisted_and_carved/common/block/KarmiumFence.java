package net.farzad.twisted_and_carved.common.block;

import net.farzad.twisted_and_carved.common.init.TCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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


public class KarmiumFence extends Block implements SegmentableBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP_FENCE = BooleanProperty.create("up_fence");
    public static final IntegerProperty FENCE_SEGMENTS = IntegerProperty.create("fence_segments",1,4);
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().collect(Util.toMap());
    private static final VoxelShape EAST_SHAPE = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape NORTH_SHAPE = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesByState;


    public KarmiumFence(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP_FENCE, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
        this.shapesByState = Map.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), KarmiumFence::getShapeForState)));
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = Shapes.empty();

        if (state.getValue(NORTH)) {
            voxelShape = Shapes.or(voxelShape, SOUTH_SHAPE);
        }

        if (state.getValue(SOUTH)) {
            voxelShape = Shapes.or(voxelShape, NORTH_SHAPE);
        }

        if (state.getValue(EAST)) {
            voxelShape = Shapes.or(voxelShape, WEST_SHAPE);
        }

        if (state.getValue(WEST)) {
            voxelShape = Shapes.or(voxelShape, EAST_SHAPE);
        }

        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.shapesByState.get(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, NORTH, EAST, SOUTH, WEST, UP_FENCE, FENCE_SEGMENTS);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (!context.getItemInHand().is(state.getBlock().asItem())) return false;

        boolean bl = context.getPlayer() == null || !context.getPlayer().isShiftKeyDown();
        return canAdd(state) && bl;
    }

    @Override
    public IntegerProperty getSegmentAmountProperty() {
        return FENCE_SEGMENTS;
    }

    @Override
    public double getShapeHeight() {
        return 1.0;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        if (world.getBlockState(pos.above()).is(TCBlocks.KARMIUM_RAILING)) {
            state = state.setValue(UP_FENCE,true);
        }
        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction side = ctx.getHorizontalDirection().getOpposite();
        BlockState state = this.defaultBlockState();
        BlockState currentState = world.getBlockState(blockPos);
        int segmentCount = getSegment(currentState);

        if (side.getAxis() != Direction.Axis.Y ) {

            if (currentState.is(this) && !currentState.getValue(getFacingProperty(side.getOpposite()))) {
                if (world.getBlockState(ctx.getClickedPos().above()).getBlock() == TCBlocks.KARMIUM_RAILING) {
                    return currentState.setValue(getFacingProperty(side.getOpposite()), true).setValue(UP_FENCE,true).setValue(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
                } else {
                    return currentState.setValue(getFacingProperty(side.getOpposite()), true).setValue(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
                }
            }
        }
        if (currentState.is(this)) {
            for (Direction direction : UPDATE_SHAPE_ORDER) {
                if (direction.getAxis() != Direction.Axis.Y && !currentState.getValue(getFacingProperty(direction))) {
                    return currentState.setValue(getFacingProperty(direction), true).setValue(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
                }
            }
        }
        FluidState fluidState = world.getFluidState(blockPos);
        boolean bl = fluidState.getType() == Fluids.WATER;
        return state.setValue(getFacingProperty(side.getOpposite()), true).setValue(WATERLOGGED, bl).setValue(FENCE_SEGMENTS,segmentCount < 4 ? segmentCount + 1 : segmentCount);
    }

    private int getSegment(BlockState state) {
        if (state.is(TCBlocks.KARMIUM_RAILING)) {
            return state.getValue(FENCE_SEGMENTS);
        }
        return 0;
    }

    public boolean canAdd(BlockState state) {
        if (!state.is(this)) {
            return false;
        }
        for (Direction direction : UPDATE_SHAPE_ORDER) {
            if (direction.getAxis() != Direction.Axis.Y && !state.getValue(getFacingProperty(direction))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    public static BooleanProperty getFacingProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }
}

