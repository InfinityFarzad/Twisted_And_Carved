package net.farzad.twisted_and_carved.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.client.render.entity.state.TwistedGreataxeEntityRenderstate;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;


@Environment(EnvType.CLIENT)
public class TwistedGreataxeEntityRenderer extends EntityRenderer<TwistedGreataxeEntity, TwistedGreataxeEntityRenderstate> {
    private final ItemModelResolver itemModelManager;
    private final float scale;

    public TwistedGreataxeEntityRenderer(EntityRendererProvider.Context ctx, float scale) {
        super(ctx);
        this.itemModelManager = ctx.getItemModelResolver();
        this.scale = scale;
    }

    public TwistedGreataxeEntityRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0F);
    }

    @Override
    public void submit(TwistedGreataxeEntityRenderstate renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        int rotDir = renderState.entity.shouldReturn ? -1 : 1;

        matrices.pushPose();
        matrices.scale(this.scale, this.scale, this.scale);
        matrices.mulPose(Axis.YP.rotationDegrees(renderState.entity.getViewYRot(renderState.tickDelta) + 180));
        if (!(renderState.entity.shakeTime > 0)) {
            matrices.mulPose(new Quaternionf().rotateX((float) -Math.toRadians(((renderState.entity.level().getGameTime() + renderState.tickDelta)) * 65) * rotDir));
        }

        renderState.itemRenderState.submit(matrices, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY,renderState.outlineColor);
        matrices.popPose();
        super.submit(renderState, matrices, queue,cameraState);
    }

    @Override
    public TwistedGreataxeEntityRenderstate createRenderState() {
        return new TwistedGreataxeEntityRenderstate();
    }

    @Override
    public void extractRenderState(TwistedGreataxeEntity entity, TwistedGreataxeEntityRenderstate state, float tickDelta) {
        itemModelManager.updateForNonLiving(state.itemRenderState, TCItems.TWISTED_GREATAXE.getDefaultInstance(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        state.entity = entity;
        state.stack = entity.getWeaponItem();
        state.tickDelta = tickDelta;
        super.extractRenderState(entity, state, tickDelta);
    }

}

