package net.farzad.twisted_and_carved.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class TwistedGreataxeEntityRenderstate extends EntityRenderState {
    public ItemStack stack;
    public float tickDelta;
    public TwistedGreataxeEntity entity;
    public ItemRenderState itemRenderState = new ItemRenderState();

    public TwistedGreataxeEntityRenderstate() {
    }
}
