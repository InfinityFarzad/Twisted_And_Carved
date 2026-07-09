package net.farzad.twisted_and_carved.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.farzad.twisted_and_carved.client.render.entity.model.LostMerchantModel;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

@Environment(value= EnvType.CLIENT)
public class LostMerchantEyeLayer<M extends LostMerchantModel>
        extends EyesLayer<LivingEntityRenderState, M> {

    private static final Identifier TEXTURE = TwistedAndCarved.id("textures/entity/lost_merchant_eyes.png");
    private static final RenderType EYES_LAYER = RenderTypes.eyes(TEXTURE);

    public LostMerchantEyeLayer(RenderLayerParent<LivingEntityRenderState, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderType renderType() {
        return EYES_LAYER;
    }
}
