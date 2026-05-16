package net.farzad.twisted_and_carved.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;

public class KarmiumChainBlock extends ChainBlock {

    private static final Map<Direction.Axis, VoxelShape> SHAPES_BY_AXIS;

    public KarmiumChainBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_AXIS.get(state.get(AXIS));
    }

    static {
        SHAPES_BY_AXIS = VoxelShapes.createAxisShapeMap(Block.createCuboidShape((double)7.0F, (double)7.0F, (double)16.0F));
    }
}
