package net.farzad.twisted_and_carved.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
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

    public void render(TwistedGreataxeEntityRenderstate itemEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(this.scale, this.scale, this.scale);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(itemEntityRenderState.entity.getLerpedYaw(itemEntityRenderState.tickDelta)));
        if (!itemEntityRenderState.entity.dealtDamage) {
            matrixStack.multiply(new Quaternionf().rotateX((float) Math.toRadians((itemEntityRenderState.entity.getWorld().getTime() + itemEntityRenderState.tickDelta) * 120)));
        } else {
            matrixStack.multiply(new Quaternionf().rotateX((float) Math.toRadians((itemEntityRenderState.entity.getWorld().getTime() + itemEntityRenderState.tickDelta) * 5)));
        }
        itemEntityRenderState.itemRenderState.render(matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(itemEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public TwistedGreataxeEntityRenderstate createRenderState() {
        return new TwistedGreataxeEntityRenderstate();
    }

    @Override
    public void updateRenderState(TwistedGreataxeEntity entity, TwistedGreataxeEntityRenderstate state, float tickDelta) {
        itemModelManager.updateForNonLivingEntity(state.itemRenderState, ModItems.TWISTED_GREATAXE.getDefaultStack(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        state.entity = entity;
        state.stack = entity.getWeaponStack();
        state.tickDelta = tickDelta;
        super.updateRenderState(entity, state, tickDelta);
    }

}

