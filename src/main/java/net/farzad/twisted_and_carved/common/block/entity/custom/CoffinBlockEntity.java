package net.farzad.twisted_and_carved.common.block.entity.custom;

import net.farzad.twisted_and_carved.common.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoffinBlockEntity extends BlockEntity implements LootableInventory, SingleStackInventory.SingleStackBlockEntityInventory {

    private ItemStack stack;
    @Nullable
    protected RegistryKey<LootTable> lootTableId;
    protected long lootTableSeed;

    public CoffinBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COFFIN_BLOCK_ENTITY, pos, state);
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
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (!this.writeLootTable(nbt) && !this.stack.isEmpty()) {
            nbt.put("item", ItemStack.CODEC, registries.getOps(NbtOps.INSTANCE), this.stack);
        }
        super.writeNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (!this.readLootTable(nbt)) {
            RegistryOps<NbtElement> registryOps = registries.getOps(NbtOps.INSTANCE);
            this.stack = (ItemStack)nbt.get("item", ItemStack.CODEC, registryOps).orElse(ItemStack.EMPTY);
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
