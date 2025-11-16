package net.farzad.twisted_and_carved.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.block.entity.custom.CoffinBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
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
        if (!world.getBlockState(pos).get(OPEN) && player.getMainHandStack().isEmpty() && player.getOffHandStack().isEmpty()) {
            player.swingHand(Hand.MAIN_HAND);
            world.playSound(player,pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,1,1);
            if (player.isSneaking()) {
                world.setBlockState(pos,state.with(PUSH, true).with(OPEN,true));
            } else {
                world.setBlockState(pos,state.with(OPEN, true).with(PUSH,false));
            }
            if (world instanceof ServerWorld servre && ((CoffinBlockEntity)servre.getBlockEntity(pos)).getStack() != null) {
                CoffinBlockEntity coffin = ((CoffinBlockEntity) servre.getBlockEntity(pos));
                if (!coffin.getStack().isEmpty()) {
                    player.swingHand(Hand.MAIN_HAND);
                    ItemScatterer.spawn(world,pos.getX(),pos.getY(),pos.getZ(),coffin.getStack());
                    coffin.setStack(ItemStack.EMPTY);
                    world.playSound(player,pos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS,1,1);
                    return ActionResult.CONSUME;
                } else {
                    return ActionResult.PASS;
                }
            }
            return ActionResult.CONSUME;
        } else {
            if (!player.isSneaking()) {
                player.swingHand(Hand.MAIN_HAND);
                world.setBlockState(pos, state.with(PUSH, false).with(OPEN, false));
                world.playSound(player,pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,1,1);
            } else {

            }
            return ActionResult.CONSUME;
        }
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
