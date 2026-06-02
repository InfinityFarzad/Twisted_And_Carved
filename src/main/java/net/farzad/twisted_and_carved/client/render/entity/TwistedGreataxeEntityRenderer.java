package net.farzad.twisted_and_carved.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.client.render.entity.state.TwistedGreataxeEntityRenderstate;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;


@Environment(EnvType.CLIENT)
public class TwistedGreataxeEntityRenderer extends EntityRenderer<TwistedGreataxeEntity, TwistedGreataxeEntityRenderstate> {
    private final ItemModelManager itemModelManager;
    private final float scale;

    public TwistedGreataxeEntityRenderer(EntityRendererFactory.Context ctx, float scale) {
        super(ctx);
        this.itemModelManager = ctx.getItemModelManager();
        this.scale = scale;
    }

    public TwistedGreataxeEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F);
    }

    @Override
    public void render(TwistedGreataxeEntityRenderstate renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        int rotDir = renderState.entity.shouldReturn ? -1 : 1;

        matrices.push();
        matrices.scale(this.scale, this.scale, this.scale);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(renderState.entity.getYaw(renderState.tickDelta) + 180));
        if (!(renderState.entity.shake > 0)) {
            matrices.multiply(new Quaternionf().rotateX((float) -Math.toRadians(((renderState.entity.getEntityWorld().getTime() + renderState.tickDelta)) * 65) * rotDir));
        }

        renderState.itemRenderState.render(matrices, queue, renderState.light, OverlayTexture.DEFAULT_UV,renderState.outlineColor);
        matrices.pop();
        super.render(renderState, matrices, queue,cameraState);
    }

    @Override
    public TwistedGreataxeEntityRenderstate createRenderState() {
        return new TwistedGreataxeEntityRenderstate();
    }

    @Override
    public void updateRenderState(TwistedGreataxeEntity entity, TwistedGreataxeEntityRenderstate state, float tickDelta) {
        itemModelManager.updateForNonLivingEntity(state.itemRenderState, TCItems.TWISTED_GREATAXE.getDefaultStack(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        state.entity = entity;
        state.stack = entity.getWeaponStack();
        state.tickDelta = tickDelta;
        super.updateRenderState(entity, state, tickDelta);
    }

}

