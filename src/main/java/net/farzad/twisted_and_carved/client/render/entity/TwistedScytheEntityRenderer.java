package net.farzad.twisted_and_carved.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Identifier;
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

    public void render(TwistedScytheEntityRenderstate scytheEntityRenderstate, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(this.scale, this.scale, this.scale);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(scytheEntityRenderstate.entity.getLerpedYaw(scytheEntityRenderstate.tickDelta)));
        if (!scytheEntityRenderstate.entity.isGripped) {
            matrixStack.multiply(new Quaternionf().rotateX((float) Math.toRadians((scytheEntityRenderstate.entity.getWorld().getTime() + scytheEntityRenderstate.tickDelta) * 150)));
        } else {
            matrixStack.multiply(new Quaternionf().rotateX((float) Math.toRadians((scytheEntityRenderstate.entity.getWorld().getTime() + scytheEntityRenderstate.tickDelta) * 0)));
        }
        scytheEntityRenderstate.itemRenderState.render(matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        matrixStack.push();
        renderChain(scytheEntityRenderstate,matrixStack,vertexConsumerProvider, i);
        matrixStack.pop();

        super.render(scytheEntityRenderstate, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public TwistedScytheEntityRenderstate createRenderState() {
        return new TwistedScytheEntityRenderstate();
    }

    @Override
    public void updateRenderState(TwistedScytheEntity entity, TwistedScytheEntityRenderstate state, float tickDelta) {
        itemModelManager.updateForNonLivingEntity(state.itemRenderState, ModItems.TWISTED_SCYTHE.getDefaultStack(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        state.entity = entity;
        state.stack = entity.getWeaponStack();
        state.tickDelta = tickDelta;
        super.updateRenderState(entity, state, tickDelta);
    }

    private static void renderChain(TwistedScytheEntityRenderstate scytheEntityRenderstate, MatrixStack stack, VertexConsumerProvider vertexConsumerProvider, int i) {

        TwistedScytheEntity scytheEntity = scytheEntityRenderstate.entity;

        if (scytheEntity.getOwner() instanceof LivingEntity livingOwner && livingOwner.isAlive()) {
            double dx = livingOwner.getX() - (scytheEntity.getX());
            double dy = livingOwner.getY() - scytheEntity.getY() + (livingOwner.getEyeHeight(livingOwner.getPose()) - 0.5);
            double dz = livingOwner.getZ() - (scytheEntity.getZ());
            float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            float textureRepeatInterval = 0.68f;
            float vMax = length / textureRepeatInterval;
            stack.push();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySmoothCutout(Identifier.of(TwistedAndCarved.MOD_ID,"textures/entity/scythe_chain.png")));

            Matrix4f model = stack.peek().getPositionMatrix();
            Vec3d dir = new Vec3d(dx, dy, dz).normalize();
            Vec3d renderFace = dir.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(0.32);
            MatrixStack.Entry entry = stack.peek().copy();

            vertexConsumer.vertex(model, (float) renderFace.x, (float) 0, (float) renderFace.z).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(model, (float) -renderFace.x, (float) 0, (float) -renderFace.z).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(model, (float) (dx - renderFace.x), (float) (dy), (float) (dz - renderFace.z)).color(255, 255, 255, 255).texture(1, vMax).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(model, (float) (dx + renderFace.x), (float) (dy), (float) (dz + renderFace.z)).color(255, 255, 255, 255).texture(0, vMax).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);

            vertexConsumer.vertex(model, (float) 0, (float) renderFace.y, (float) 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(model, (float) 0, (float) -renderFace.y, (float) 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(model, (float) 0, (float) (dy - renderFace.y), (float) 0).color(255, 255, 255, 255).texture(1, vMax).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(model, (float) 0, (float) (dy + renderFace.y), (float) 0).color(255, 255, 255, 255).texture(0, vMax).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(entry, 0, 1, 0);
            stack.pop();
        }
    }

}

