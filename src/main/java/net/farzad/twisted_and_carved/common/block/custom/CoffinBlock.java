package net.farzad.twisted_and_carved.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.block.entity.custom.CoffinBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CoffinBlock extends BlockWithEntity {
    public static final MapCodec<CoffinBlock> CODEC = createCodec(CoffinBlock::new);
    public static final BooleanProperty OPEN;
    public static final BooleanProperty PUSH;
    public static final EnumProperty<Direction> FACING;

    public CoffinBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState().with(OPEN,false).with(PUSH,false));
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
        world.playSound(player,pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,1,1);

        if (!world.getBlockState(pos).get(OPEN)) {
            if (player.isSneaking()) {
                world.setBlockState(pos,state.with(PUSH, true).with(OPEN,true));
            } else {
                world.setBlockState(pos,state.with(OPEN, true).with(PUSH,false));
            }
            return ActionResult.CONSUME;
        } else {
            world.setBlockState(pos,state.with(PUSH, false).with(OPEN,false));
            return ActionResult.CONSUME;
        }
    }

    private static boolean DoItPush(World world, BlockPos pos) {
        return !(world.getBlockState(pos.east()).isAir() || world.getBlockState(pos.west()).isAir() || world.getBlockState(pos.north()).isAir() || world.getBlockState(pos.south()).isAir());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PUSH);
        builder.add(OPEN);
        builder.add(FACING);
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) {
            return Block.createCuboidShape(1,0,1,15,11,15);
        }
        return Block.createCuboidShape(1,0,1,15,14,15);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(PUSH, false).with(OPEN,false).with(FACING,ctx.getHorizontalPlayerFacing());
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
