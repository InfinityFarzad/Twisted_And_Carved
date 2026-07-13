package net.farzad.twisted_and_carved.client.render.entity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpiritForgeRenderState extends BlockEntityRenderState {
    public List<ItemStack> inventory = List.of();
    public int count;
    public ItemStackRenderState[] itemStackRenderState = new ItemStackRenderState[7];
}
