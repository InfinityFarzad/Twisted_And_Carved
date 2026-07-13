package net.farzad.twisted_and_carved.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.farzad.twisted_and_carved.client.render.entity.state.SpiritForgeRenderState;
import net.farzad.twisted_and_carved.common.block.entity.SpiritForgeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SpiritForgeEntityRenderer implements BlockEntityRenderer<SpiritForgeEntity, SpiritForgeRenderState> {

    private final ItemModelResolver modelManager;

    public SpiritForgeEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.modelManager = context.itemModelResolver();
    }

    @Override
    public SpiritForgeRenderState createRenderState() {
        return new SpiritForgeRenderState();
    }

    @Override
    public void submit(SpiritForgeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float time = Minecraft.getInstance().level.getDefaultClockTime();
        for (ItemStackRenderState stackRenderState : state.itemStackRenderState) {
            if (stackRenderState != null) {
                int index = Arrays.stream(state.itemStackRenderState).toList().indexOf(stackRenderState);
                poseStack.pushPose();
                poseStack.translate(0.5,2,0.5);
                if (index != 0) {
                    poseStack.translate(0,Mth.sin(time / 20.0+index* 5) / 20.0,0);
                    poseStack.translate(Mth.sin(index * 5),0,Mth.cos(index * 5));
                }
                poseStack.mulPose(Axis.YN.rotation((float) (time / 20.0)));
                stackRenderState.submit(poseStack,submitNodeCollector,state.lightCoords, OverlayTexture.NO_OVERLAY,0);
                poseStack.popPose();
            }
        }
    }

    @Override
    public void extractRenderState(SpiritForgeEntity blockEntity, SpiritForgeRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        List<ItemStack> stacks = blockEntity.getItems();
        for (int slot = 0; slot < stacks.size(); ++slot) {
            ItemStack itemStack = stacks.get(slot);
            if (itemStack.isEmpty()) continue;
            ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            this.modelManager.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.GROUND, blockEntity.getLevel(), null,slot);
            state.itemStackRenderState[slot] = itemStackRenderState;
        }
        state.inventory = blockEntity.getItems();
        state.count = blockEntity.getItems().size();
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    }
}
