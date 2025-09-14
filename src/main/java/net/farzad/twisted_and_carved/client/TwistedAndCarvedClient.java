package net.farzad.twisted_and_carved.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.client.particle.custom.FalchionSlashEffect;
import net.farzad.twisted_and_carved.client.particle.custom.FalchionSlashParticle;
import net.farzad.twisted_and_carved.client.particle.custom.ParryParticle;
import net.farzad.twisted_and_carved.client.particle.custom.TwistedGlaiveSweepParticle;
import net.farzad.twisted_and_carved.client.properties.TwistedScytheGrapplingProperty;
import net.farzad.twisted_and_carved.client.render.TwistedGreataxeEntityRenderer;
import net.farzad.twisted_and_carved.client.render.TwistedScytheEntityRenderer;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.enchantment.ModEnchantmentEffects;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.sound.GreataxeSoundInstance;
import net.farzad.twisted_and_carved.common.util.EnchantmentUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.LeavesParticle;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.property.bool.FishingRodCastProperty;
import net.minecraft.client.render.item.property.bool.SelectedProperty;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TwistedAndCarvedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.TWISTED_SWEEP_ATTACK, SweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TWISTED_GLAIVE_SWEEP, TwistedGlaiveSweepParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TWISTED_LEAF_PARTICLE, LeavesParticle.CherryLeavesFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.PARRY_PARTICLE, ParryParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FALCHION_SLASH, provider -> new FalchionSlashParticle.Factory(provider));
        EntityRendererRegistry.register(ModEntities.TWISTED_GREATAXE_ENTITY, TwistedGreataxeEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TWISTED_SCYTHE_ENTITY, TwistedScytheEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TWISTED_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TWISTED_SAPLING, RenderLayer.getCutout());
        BooleanProperties.ID_MAPPER.put(Identifier.of(TwistedAndCarved.MOD_ID,"twisted_scythe_grappling"),TwistedScytheGrapplingProperty.CODEC);

        HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
            Matrix4f transformationMatrix = drawContext.getMatrices().peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            MinecraftClient client = MinecraftClient.getInstance();

            assert client.player != null;
            ItemStack stack = getStack(client.player);


            Identifier texture = Identifier.of(TwistedAndCarved.MOD_ID, "textures/gui/blood_bar_" + getBloodChargeOverlay(stack) + ".png");
            if (stack.isOf(ModItems.TWISTED_FALCHION) && EnchantmentUtil.hasEnchantment(stack, ModEnchantmentEffects.BLEEDING)) {

                drawContext.drawTexture(RenderLayer::getGuiTextured, texture, client.getWindow().getScaledWidth() / 2 + 120, client.getWindow().getScaledHeight() - 28, 0, 0, 64, 32, 64, 32);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(GreataxeSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedGreataxeEntity twistedGreataxe = (TwistedGreataxeEntity) context.player().getWorld().getEntityById(payload.entityID());
            if (twistedGreataxe != null && !twistedGreataxe.isRemoved()) {
                GreataxeSoundInstance instance = new GreataxeSoundInstance(twistedGreataxe, SoundCategory.AMBIENT);
                context.client().getSoundManager().play(instance);
            }

        });

    }

    private ItemStack getStack(PlayerEntity player) {
        if (player.getOffHandStack().isOf(ModItems.TWISTED_FALCHION)) {
            return player.getOffHandStack();
        } else if (player.getMainHandStack().isOf(ModItems.TWISTED_FALCHION)) {
            return player.getMainHandStack();
        } else {
            return player.getMainHandStack();
        }

    }

    private int getBloodChargeOverlay(ItemStack stack) {
        int comp = stack.getOrDefault(ModDataComponents.BLOOD_CHARGE, 0);
        if (comp <= 25 && comp > 0) {
            return 0;
        } else if (comp <= 50 && comp > 25) {
            return 1;
        } else if (comp <= 75 && comp > 50) {
            return 2;
        } else if (comp <= 100 && comp > 75) {
            return 3;
        } else {
            return 0;
        }
    }
}
