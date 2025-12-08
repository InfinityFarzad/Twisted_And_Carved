package net.farzad.twisted_and_carved.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class KarmiumFence extends MultifaceBlock {
    public static final MapCodec<KarmiumFence> CODEC = createCodec(KarmiumFence::new);
    public static final BooleanProperty UP_FENCE = BooleanProperty.of("up_fence");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;
    protected static final Direction[] DIRECTIONS = Direction.values();;


    public KarmiumFence(Settings settings) {
        super(settings);
        this.setDefaultState(withAllDirections(this.getStateManager()));
    }

    @Override
    protected MapCodec<KarmiumFence> getCodec() {
        return CODEC;
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        return (BlockState) Arrays.stream(ctx.getPlacementDirections()).map((direction) -> this.withDirection(blockState, world, blockPos, ctx.getHorizontalPlayerFacing().getOpposite())).filter(Objects::nonNull).findFirst().orElse(null);
    }

    private static BlockState withAllDirections(StateManager<Block, BlockState> stateManager) {
        BlockState blockState = (BlockState)((BlockState)stateManager.getDefaultState()).with(WATERLOGGED, false).with(UP_FENCE,false);

        for(BooleanProperty booleanProperty : FACING_PROPERTIES.values()) {
            blockState = (BlockState)blockState.withIfExists(booleanProperty, false);
        }

        return blockState;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP_FENCE);
        super.appendProperties(builder);
    }
}

