package net.farzad.twisted_and_carved.common.block;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.block.entity.SpiritForgeEntity;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.CubeVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SpiritForgingTable extends BaseEntityBlock {
    public static final MapCodec<SpiritForgingTable> CODEC = simpleCodec(SpiritForgingTable::new);

    public SpiritForgingTable(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new SpiritForgeEntity(worldPosition,blockState);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, @NonNull Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        SpiritForgeEntity entity = (SpiritForgeEntity) level.getBlockEntity(pos);
        if (!itemStack.isEmpty()) {
            if (level instanceof ServerLevel serverLevel) {

                return entity.placeItem(serverLevel,itemStack,player,entity,pos,state);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BaseEntityBlock.box(0,0,0,16,13,16);
    }
}
