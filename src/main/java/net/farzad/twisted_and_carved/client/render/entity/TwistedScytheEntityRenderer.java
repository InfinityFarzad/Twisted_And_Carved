package net.farzad.twisted_and_carved.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.client.render.entity.state.TwistedScytheEntityRenderstate;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;


@Environment(EnvType.CLIENT)
public class TwistedScytheEntityRenderer extends EntityRenderer<TwistedScytheEntity, TwistedScytheEntityRenderstate> {
    private final ItemModelManager itemModelManager;
    private final float scale;

    public TwistedScytheEntityRenderer(EntityRendererFactory.Context ctx, float scale) {
        super(ctx);
        this.itemModelManager = ctx.getItemModelManager();
        this.scale = scale;
    }

    public TwistedScytheEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F);
    }


    @Override
    public void render(TwistedScytheEntityRenderstate renderState, MatrixStack matrixStack, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrixStack.push();
        matrixStack.scale(this.scale, this.scale, this.scale);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(renderState.entity.getLerpedYaw(renderState.tickDelta)));
        if (!renderState.entity.isOnGround()) {
            matrixStack.multiply(new Quaternionf().rotateX((float) Math.toRadians(-renderState.scytheRot) * 15));
        }
        renderState.itemRenderState.render(matrixStack, queue, renderState.light, OverlayTexture.DEFAULT_UV,renderState.outlineColor);
        matrixStack.pop();
        matrixStack.push();
        queue.submitCustom(matrixStack, RenderLayers.entitySmoothCutout(TwistedAndCarved.id("textures/entity/scythe_chain.png")),(matricesEntry, vertexConsumer) -> renderChain(renderState,matricesEntry,vertexConsumer,renderState.light));
        matrixStack.pop();

        super.render(renderState, matrixStack, queue, cameraState);
    }

    @Override
    public TwistedScytheEntityRenderstate createRenderState() {
        return new TwistedScytheEntityRenderstate();
    }

    @Override
    public void updateRenderState(TwistedScytheEntity entity, TwistedScytheEntityRenderstate state, float tickDelta) {
        itemModelManager.updateForNonLivingEntity(state.itemRenderState, TCItems.TWISTED_SCYTHE.getDefaultStack(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        state.scytheRot = entity.getRot();
        state.entity = entity;
        state.stack = entity.getWeaponStack();
        state.tickDelta = tickDelta;
        super.updateRenderState(entity, state, tickDelta);
    }

    public static void renderChain(TwistedScytheEntityRenderstate scytheEntityRenderstate, MatrixStack.Entry stackEntry, VertexConsumer vertexConsumer, int i) {
        TwistedScytheEntity scytheEntity = scytheEntityRenderstate.entity;
        MatrixStack.Entry entry = stackEntry.copy();
        Matrix4f clientWorldPos = stackEntry.getPositionMatrix();
        float r = 0.68f;

        if (scytheEntity.getOwner() instanceof LivingEntity livingOwner && livingOwner.isAlive()) {
            double dx = livingOwner.getX() - (scytheEntity.getX());
            double dy = livingOwner.getY() - scytheEntity.getY() + (livingOwner.getEyeHeight(livingOwner.getPose()) - 0.5);
            double dz = livingOwner.getZ() - (scytheEntity.getZ());

            float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            float v = length / r;

            Vec3d dir = new Vec3d(dx, dy, dz).normalize();
            Vec3d renderFace = dir.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(0.32);

            vertex(vertexConsumer,clientWorldPos,entry, (float) renderFace.x,0.0f, (float) renderFace.z,0,0,i);
            vertex(vertexConsumer,clientWorldPos,entry, (float) -renderFace.x,0.0f, (float) -renderFace.z,1,0,i);
            vertex(vertexConsumer,clientWorldPos,entry, (float) (dx - renderFace.x), (float)dy, (float) (dz - renderFace.z),1,v,i);
            vertex(vertexConsumer,clientWorldPos,entry, (float) (dx + renderFace.x), (float)dy, (float) (dz + renderFace.z),0,v,i);

            vertex(vertexConsumer,clientWorldPos,entry, (float) 0, (float) renderFace.y, (float) 0,0, 0,i);
            vertex(vertexConsumer,clientWorldPos,entry, (float) 0, (float) -renderFace.y, (float) 0,1, 0,i);
            vertex(vertexConsumer,clientWorldPos,entry, (float) 0, (float) (dy - renderFace.y), (float) 0,1, v,i);
            vertex(vertexConsumer,clientWorldPos,entry, (float) 0, (float) (dy + renderFace.y), (float) 0,0, v,i);
        }
    }

    private static void vertex(VertexConsumer vertexConsumer,Matrix4f mat4, MatrixStack.Entry entry ,float x, float y, float z, float u, float v, int i) {
        vertexConsumer.vertex(mat4, x, y, z).texture(u, v).light(i).color(255, 255, 255, 255).normal(entry, 0, 1, 0).overlay(OverlayTexture.DEFAULT_UV);
    }

}

