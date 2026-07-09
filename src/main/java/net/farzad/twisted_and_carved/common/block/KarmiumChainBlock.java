package net.farzad.twisted_and_carved.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class KarmiumChainBlock extends ChainBlock {

    private static final Map<Direction.Axis, VoxelShape> SHAPES_BY_AXIS;

    public KarmiumChainBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_BY_AXIS.get(state.getValue(AXIS));
    }

    static {
        SHAPES_BY_AXIS = Shapes.rotateAllAxis(Block.cube((double)7.0F, (double)7.0F, (double)16.0F));
    }
}
