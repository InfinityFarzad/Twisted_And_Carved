package net.farzad.twisted_and_carved.common.block.entity;

import net.farzad.twisted_and_carved.common.register.TCBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoffinBlockEntity extends BlockEntity implements LootableInventory, SingleStackInventory.SingleStackBlockEntityInventory {

    private ItemStack stack;
    @Nullable
    protected RegistryKey<LootTable> lootTableId;
    protected long lootTableSeed;

    public CoffinBlockEntity(BlockPos pos, BlockState state) {
        super(TCBlockEntities.COFFIN_BLOCK_ENTITY, pos, state);
        this.stack = ItemStack.EMPTY;
    }

    protected void addComponents(ComponentMap.Builder builder) {
        super.addComponents(builder);
        builder.add(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(List.of(this.stack)));
    }

    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.stack = ((ContainerComponent)components.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)).copyFirstStack();
    }

    @Override
    protected void writeData(WriteView view) {
        if (!this.writeLootTable(view) && !this.stack.isEmpty()) {
            view.put("item", ItemStack.CODEC, this.stack);
        }
        super.writeData(view);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        if (!this.readLootTable(view)) {
            this.stack = view.read("storedCoffinItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        } else {
            this.stack = ItemStack.EMPTY;
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return this.createComponentlessNbt(registries);
    }

    @Override
    public @Nullable RegistryKey<LootTable> getLootTable() {
        return this.lootTableId;
    }

    public void setLootTable(@Nullable RegistryKey<LootTable> lootTable) {
        this.lootTableId = lootTable;
    }

    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public BlockEntity asBlockEntity() {
        return this;
    }

    @Override
    public ItemStack getStack() {
        this.generateLoot(null);
        return this.stack;
    }

    @Override
    public void setStack(ItemStack stack) {
        this.generateLoot(null);
        this.stack = stack;
    }
}
