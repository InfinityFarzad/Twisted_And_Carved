package net.farzad.twisted_and_carved.common.block.entity;

import net.farzad.twisted_and_carved.common.init.TCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoffinBlockEntity extends BlockEntity implements RandomizableContainer, ContainerSingleItem.BlockContainerSingleItem {

    private ItemStack stack;
    @Nullable
    protected ResourceKey<LootTable> lootTableId;
    protected long lootTableSeed;

    public CoffinBlockEntity(BlockPos pos, BlockState state) {
        super(TCBlockEntities.COFFIN_BLOCK_ENTITY, pos, state);
        this.stack = ItemStack.EMPTY;
    }

    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.stack)));
    }

    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.stack = ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyOne();
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        if (!this.trySaveLootTable(view) && !this.stack.isEmpty()) {
            view.store("item", ItemStack.CODEC, this.stack);
        }
        super.saveAdditional(view);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        if (!this.tryLoadLootTable(view)) {
            this.stack = view.read("storedCoffinItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        } else {
            this.stack = ItemStack.EMPTY;
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return this.lootTableId;
    }

    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTableId = lootTable;
    }

    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public BlockEntity getContainerBlockEntity() {
        return this;
    }

    @Override
    public ItemStack getTheItem() {
        this.unpackLootTable(null);
        return this.stack;
    }

    @Override
    public void setTheItem(ItemStack stack) {
        this.unpackLootTable(null);
        this.stack = stack;
    }
}
