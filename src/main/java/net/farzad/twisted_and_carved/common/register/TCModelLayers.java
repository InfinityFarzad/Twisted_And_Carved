package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.farzad.twisted_and_carved.client.render.entity.model.LostMerchantModel;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class TCModelLayers {
    public static final ModelLayerLocation LOST_MERCHANT = create("lost_merchant");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(TwistedAndCarved.id(name), "main");
    }

    private static void initModels() {
        ModelLayerRegistry.registerModelLayer(LOST_MERCHANT, LostMerchantModel::getTexturedModelData);
    }

    public static void init() {
        initModels();
    }
}
