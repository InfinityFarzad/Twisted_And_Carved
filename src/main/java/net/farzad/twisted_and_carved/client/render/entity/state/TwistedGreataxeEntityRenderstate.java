package net.farzad.twisted_and_carved.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class TwistedGreataxeEntityRenderstate extends EntityRenderState {
    public ItemStack stack;
    public float tickDelta;
    public TwistedGreataxeEntity entity;
    public ItemStackRenderState itemRenderState = new ItemStackRenderState();

    public TwistedGreataxeEntityRenderstate() {
    }
}
