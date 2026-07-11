package net.farzad.twisted_and_carved.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class TwistedScytheEntityRenderstate extends EntityRenderState {
    public ItemStack stack;
    public float tickDelta;
    public float scytheRot;
    public boolean grounded;
    public float yRot;
    public float xRot;
    public Vec3 ownerPos = new Vec3(0,0,0);
    public boolean ownerLiving;
    public float ownerEyeHeight = 0f;
   public ItemStackRenderState itemRenderState = new ItemStackRenderState();

    public TwistedScytheEntityRenderstate() {
    }
}
