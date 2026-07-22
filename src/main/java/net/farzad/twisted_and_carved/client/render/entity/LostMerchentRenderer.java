package net.farzad.twisted_and_carved.client.render.entity;

import net.farzad.twisted_and_carved.client.render.entity.feature.LostMerchantEyeLayer;
import net.farzad.twisted_and_carved.client.render.entity.model.LostMerchantModel;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.entity.LostMerchantEntity;
import net.farzad.twisted_and_carved.common.init.client.TCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class LostMerchentRenderer extends LivingEntityRenderer<LostMerchantEntity, LivingEntityRenderState, LostMerchantModel> {
    private static final Identifier TEXTURE = TwistedAndCarved.id("textures/entity/lost_merchant.png");


    public LostMerchentRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new LostMerchantModel(ctx.bakeLayer(TCModelLayers.LOST_MERCHANT)), 0.5f);
        this.addLayer(new LostMerchantEyeLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
