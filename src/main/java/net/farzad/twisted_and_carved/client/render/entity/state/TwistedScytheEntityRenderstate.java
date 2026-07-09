package net.farzad.twisted_and_carved.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class TwistedScytheEntityRenderstate extends EntityRenderState {
    public ItemStack stack;
    public float tickDelta;
    public float scytheRot;
    public TwistedScytheEntity entity;
    public ItemStackRenderState itemRenderState = new ItemStackRenderState();

    public TwistedScytheEntityRenderstate() {
    }
}
