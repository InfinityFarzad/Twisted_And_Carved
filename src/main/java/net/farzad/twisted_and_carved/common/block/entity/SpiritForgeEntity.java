package net.farzad.twisted_and_carved.common.block.entity;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.init.TCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ListBackedContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


public class SpiritForgeEntity extends BlockEntity implements ListBackedContainer {

    //public List<>
    private NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_ITEM_COUNT,ItemStack.EMPTY);
    public static int MAX_ITEM_COUNT = 7;

    public SpiritForgeEntity(BlockPos worldPosition, BlockState blockState) {
        super(TCBlockEntities.SPIRIT_FORGE_ENTITY, worldPosition, blockState);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.inventory);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.inventory));
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ContainerHelper.saveAllItems(view,this.inventory);

    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        ContainerHelper.loadAllItems(view,this.inventory);
    }

    public void update() {
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        this.setChanged();
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard("Items");
    }


    public InteractionResult placeItem(ServerLevel level,ItemStack stack, Player source, SpiritForgeEntity entity, BlockPos pos, BlockState state) {

        for (int slot = 0; slot < SpiritForgeEntity.MAX_ITEM_COUNT; slot++){
            if (entity.getItem(slot).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    entity.setItem(slot,stack.consumeAndReturn(stack.getCount(),source));
                    this.updateInventory(entity,serverLevel,source);
                }
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    private void updateInventory(BlockEntity entity, ServerLevel serverLevel, Player player) {
        serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(player, this.getBlockState()));
        this.update();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        Containers.dropContents(level,pos,getItems());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), TwistedAndCarved.LOGGER);){
            TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
            ContainerHelper.saveAllItems(output, this.inventory, true);
            CompoundTag compoundTag = output.buildResult();
            return compoundTag;
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this,player);
    }
}
