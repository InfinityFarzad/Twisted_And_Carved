package net.farzad.twisted_and_carved.client.render.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class LostMerchantModel extends EntityModel<LivingEntityRenderState> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public LostMerchantModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.body = this.root.getChild("body");
		this.left_leg = this.root.getChild("left_leg");
		this.right_leg = this.root.getChild("right_leg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.0F, -1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 36).addBox(-4.05F, -3.95F, -3.95F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 36).addBox(-4.45F, -4.55F, -4.55F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.05F, -8.05F, -0.05F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -5.5F, -6.5F, 6.0F, 23.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(38, 0).addBox(-3.0F, -4.0F, -4.0F, 5.0F, 11.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(50, 21).addBox(-1.1F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.4F, 7.0F, 1.5F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(38, 21).addBox(-2.5F, 0.0F, -0.9F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -2.1F));
		return LayerDefinition.create(modelData, 128, 128);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);
		head.zRot = state.xRot * Mth.DEG_TO_RAD;
		head.yRot = state.yRot * Mth.DEG_TO_RAD;
		float limbSwingAmplitude = (float) Math.clamp(state.walkAnimationSpeed,-0.15,0.15);
		float limbSwingAnimationProgress = state.walkAnimationPos;
		left_leg.zRot = Mth.cos(limbSwingAnimationProgress * 0.2f + Mth.PI) * 1.4f * limbSwingAmplitude;
		right_leg.zRot = Mth.cos(limbSwingAnimationProgress * 0.2f) * 1.4f * limbSwingAmplitude;

	}
}