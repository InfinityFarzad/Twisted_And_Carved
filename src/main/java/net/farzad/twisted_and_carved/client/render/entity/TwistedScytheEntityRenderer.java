package net.farzad.twisted_and_carved.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.client.render.entity.state.TwistedScytheEntityRenderstate;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;


@Environment(EnvType.CLIENT)
public class TwistedScytheEntityRenderer extends EntityRenderer<TwistedScytheEntity, TwistedScytheEntityRenderstate> {
    private final ItemModelResolver itemModelManager;
    private final float scale;

    public TwistedScytheEntityRenderer(EntityRendererProvider.Context ctx, float scale) {
        super(ctx);
        this.itemModelManager = ctx.getItemModelResolver();
        this.scale = scale;
    }

    public TwistedScytheEntityRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0F);
    }


    @Override
    public void submit(TwistedScytheEntityRenderstate renderState, PoseStack matrixStack, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrixStack.pushPose();
        matrixStack.scale(this.scale, this.scale, this.scale);
        matrixStack.mulPose(Axis.YP.rotationDegrees(renderState.entity.getYRot(renderState.tickDelta)));
        if (!renderState.entity.onGround()) {
            matrixStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(-renderState.scytheRot) * 15));
        }
        renderState.itemRenderState.submit(matrixStack, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY,renderState.outlineColor);
        matrixStack.popPose();
        matrixStack.pushPose();
        queue.submitCustomGeometry(matrixStack, RenderTypes.entityCutoutCull(TwistedAndCarved.id("textures/entity/scythe_chain.png")),(matricesEntry, vertexConsumer) -> renderChain(renderState,matricesEntry,vertexConsumer,renderState.lightCoords));
        matrixStack.popPose();

        super.submit(renderState, matrixStack, queue, cameraState);
    }

    @Override
    public TwistedScytheEntityRenderstate createRenderState() {
        return new TwistedScytheEntityRenderstate();
    }

    @Override
    public void extractRenderState(TwistedScytheEntity entity, TwistedScytheEntityRenderstate state, float tickDelta) {
        itemModelManager.updateForNonLiving(state.itemRenderState, TCItems.TWISTED_SCYTHE.getDefaultInstance(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        state.scytheRot = entity.getRot();
        state.entity = entity;
        state.stack = entity.getWeaponItem();
        state.tickDelta = tickDelta;
        super.extractRenderState(entity, state, tickDelta);
    }

    public static void renderChain(TwistedScytheEntityRenderstate scytheEntityRenderstate, PoseStack.Pose stackEntry, VertexConsumer vertexConsumer, int i) {
        TwistedScytheEntity scytheEntity = scytheEntityRenderstate.entity;
        PoseStack.Pose entry = stackEntry.copy();
        Matrix4f clientWorldPos = stackEntry.pose();
        float r = 0.68f;

        if (scytheEntity.getOwner() instanceof LivingEntity livingOwner && livingOwner.isAlive()) {
            double dx = livingOwner.getX() - (scytheEntity.getX());
            double dy = livingOwner.getY() - scytheEntity.getY() + (livingOwner.getEyeHeight(livingOwner.getPose()) - 0.5);
            double dz = livingOwner.getZ() - (scytheEntity.getZ());

            float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            float v = length / r;

            Vec3 dir = new Vec3(dx, dy, dz).normalize();
            Vec3 renderFace = dir.cross(new Vec3(0, 1, 0)).normalize().scale(0.32);

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

    private static void vertex(VertexConsumer vertexConsumer,Matrix4f mat4, PoseStack.Pose entry ,float x, float y, float z, float u, float v, int i) {
        vertexConsumer.addVertex(mat4, x, y, z).setUv(u, v).setLight(i).setColor(255, 255, 255, 255).setNormal(entry, 0, 1, 0).setOverlay(OverlayTexture.NO_OVERLAY);
    }

}

