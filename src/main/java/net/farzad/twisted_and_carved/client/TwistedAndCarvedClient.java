package net.farzad.twisted_and_carved.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.client.particle.custom.FalchionSlashParticle;
import net.farzad.twisted_and_carved.client.particle.custom.ParryParticle;
import net.farzad.twisted_and_carved.client.particle.custom.TwistedGlaiveSweepParticle;
import net.farzad.twisted_and_carved.client.properties.TwistedScytheGrapplingProperty;
import net.farzad.twisted_and_carved.client.render.TwistedGreataxeEntityRenderer;
import net.farzad.twisted_and_carved.client.render.TwistedScytheEntityRenderer;
import net.farzad.twisted_and_carved.client.render.hud.BloodBarHudRenderer;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.block.entity.ModBlockEntities;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.sound.GreataxeSoundInstance;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.particle.LeavesParticle;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

import java.util.Objects;

public class TwistedAndCarvedClient implements ClientModInitializer {
    private final BloodBarHudRenderer bloodBarHudRenderer = new BloodBarHudRenderer();

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
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KARMIUM_CHAIN,RenderLayer.getCutout());
        //BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TWISTED_VINE,RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KARMIUM_FENCE,RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TWISTED_SAPLING, RenderLayer.getCutout());
        BooleanProperties.ID_MAPPER.put(Identifier.of(TwistedAndCarved.MOD_ID,"twisted_scythe_grappling"),TwistedScytheGrapplingProperty.CODEC);

        HudRenderCallback.EVENT.register(bloodBarHudRenderer);
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            this.bloodBarHudRenderer.tick();
        });

        ClientPlayNetworking.registerGlobalReceiver(GreataxeSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedGreataxeEntity twistedGreataxe = (TwistedGreataxeEntity) context.player().getWorld().getEntityById(payload.entityID());
            if (twistedGreataxe != null && !twistedGreataxe.isRemoved()) {
                GreataxeSoundInstance instance = new GreataxeSoundInstance(twistedGreataxe, SoundCategory.AMBIENT);
                context.client().getSoundManager().play(instance);
            }

        });

    }

}
