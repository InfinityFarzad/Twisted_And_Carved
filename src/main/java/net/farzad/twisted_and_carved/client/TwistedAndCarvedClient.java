package net.farzad.twisted_and_carved.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.client.particle.custom.*;
import net.farzad.twisted_and_carved.client.properties.TwistedScytheGrapplingProperty;
import net.farzad.twisted_and_carved.client.render.entity.TwistedGreataxeEntityRenderer;
import net.farzad.twisted_and_carved.client.render.entity.TwistedScytheEntityRenderer;
import net.farzad.twisted_and_carved.client.render.hud.BloodBarHudRenderer;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.entity.custom.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.item.custom.TwistedItemPieceItem;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.sound.WeaponEntitySoundInstance;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideMixinInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.particle.LeavesParticle;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class TwistedAndCarvedClient implements ClientModInitializer {
    private final BloodBarHudRenderer bloodBarHudRenderer = new BloodBarHudRenderer();

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.TWISTED_SWEEP_ATTACK, SweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TWISTED_GLAIVE_SWEEP, TwistedGlaiveSweepParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TWISTED_LEAF_PARTICLE, LeavesParticle.CherryLeavesFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.PARRY_PARTICLE, ParryParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FALCHION_SLASH, DirectionalSlashParticle.FalchionSlashFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.DASH_PARTICLE, DashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.COFFIN_SMOKE, CoffinSmokeParticle.CoffinSmokeParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.COFFIN_ASH, DotParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.HARVEST_SLASH, DirectionalSlashParticle.HarvestSlashFactory::new);

        EntityRendererRegistry.register(ModEntities.TWISTED_GREATAXE_ENTITY, TwistedGreataxeEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TWISTED_SCYTHE_ENTITY, TwistedScytheEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),ModBlocks.TWISTED_SAPLING,ModBlocks.KARMIUM_FENCE,ModBlocks.TWISTED_LEAVES,ModBlocks.KARMIUM_CHAIN);
        BooleanProperties.ID_MAPPER.put(Identifier.of(TwistedAndCarved.MOD_ID,"twisted_scythe_grappling"),TwistedScytheGrapplingProperty.CODEC);

        applyItemTooltips();


        HudLayerRegistrationCallback.EVENT.register(bloodBarHudRenderer);
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            this.bloodBarHudRenderer.tick();
        });

        ClientPlayNetworking.registerGlobalReceiver(GreataxeSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedGreataxeEntity twistedGreataxe = (TwistedGreataxeEntity) context.player().getWorld().getEntityById(payload.entityID());
            if (twistedGreataxe != null && !twistedGreataxe.isRemoved()) {
                WeaponEntitySoundInstance instance = new WeaponEntitySoundInstance(SoundEvents.ITEM_ELYTRA_FLYING,twistedGreataxe, SoundCategory.AMBIENT);
                context.client().getSoundManager().play(instance);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ScytheSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedScytheEntity twistedScytheEntity = (TwistedScytheEntity) context.player().getWorld().getEntityById(payload.entityID());
            if (twistedScytheEntity != null && !twistedScytheEntity.isRemoved()) {
                WeaponEntitySoundInstance instance = new WeaponEntitySoundInstance(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,twistedScytheEntity, SoundCategory.AMBIENT);
                context.client().getSoundManager().play(instance);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(RiptideModificationPayload.ID, (payload, context) -> {
            PlayerEntity entity = ((PlayerEntity)context.player().getWorld().getEntityById(payload.entityID()));
            if (entity != null) {
                ((TwistedRiptideMixinInterface)entity).twistedAndCarved$setRiptideStack(payload.stack());
            }
        });

    }
    private static void applyItemTooltips() {
        MinecraftClient client = MinecraftClient.getInstance() != null ? MinecraftClient.getInstance() : null;

        if (client != null) {

            ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
                String rightClick  = Text.translatable(MinecraftClient.getInstance().options.useKey.getBoundKeyTranslationKey()).getString();

                if (itemStack.contains(ModDataComponents.TWISTED_SPIRIT)) {
                    if (itemStack.getOrDefault(ModDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY) != ItemStack.EMPTY) {
                        list.add(1,
                                Text.translatable(
                                        "tooltip.twisted_and_carved.twisted_spirit",
                                        Text.literal(itemStack.get(ModDataComponents.TWISTED_SPIRIT).getItemName().getString()).formatted(Formatting.GOLD)
                                ));
                    } else {
                        list.add(1,
                                Text.translatable(
                                        "tooltip.twisted_and_carved.twisted_spirit",
                                        Text.literal(". . .").formatted(Formatting.GOLD)));
                    }
                }
                if (itemStack.isOf(ModItems.TWISTED_GREATAXE)) {
                    if (Screen.hasShiftDown()) {
                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_greataxe_info"
                        ).formatted(Formatting.DARK_GRAY));
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "stride") {
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_dash",
                                    Text.literal("Attack").formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                            list.add(3,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_dash1",
                                    Text.literal(rightClick).formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                        } else if (TwistedWeaponUtil.getAbilityID(itemStack) == "tomahawk") {
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_tomahawk",
                                    Text.literal("Tomahawk").formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                        }
                    } else {
                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Text.literal("Shift").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }
                } else if (itemStack.isOf(ModItems.TWISTED_GLAIVE)) {
                    if (Screen.hasShiftDown()) {

                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_glaive_info"
                        ).formatted(Formatting.DARK_GRAY));

                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "sweeping") {
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_sweep",
                                    Text.literal(rightClick).formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                        }

                    } else {
                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Text.literal("Shift").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }
                } else if (itemStack.isOf(ModItems.TWISTED_SCYTHE)) {
                    if (Screen.hasShiftDown()) {

                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_greataxe_info"
                        ).formatted(Formatting.DARK_GRAY));
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "harvest") {
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_harvest",
                                    Text.literal(rightClick + " + Shift").formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                        }
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "grappling") {
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_grapple",
                                    Text.literal(rightClick).formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                        }

                    } else {
                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Text.literal("Shift").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }
                } else if (itemStack.isOf(ModItems.TWISTED_FALCHION)) {
                    if (Screen.hasShiftDown()) {

                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_falchion_info"
                        ).formatted(Formatting.DARK_GRAY));
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "bleeding") {
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_bleeding",
                                    Text.literal("Attack").formatted(Formatting.GOLD)
                            ).formatted(Formatting.DARK_GRAY));
                            list.add(2,Text.translatable(
                                    "tooltip.twisted_and_carved.twisted_falchion_slash",
                                    Text.literal(rightClick).formatted(Formatting.GOLD)).formatted(Formatting.DARK_GRAY));
                        }

                    } else {
                        list.add(2,Text.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Text.literal("Shift").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }
                } else if (itemStack.getItem() instanceof TwistedItemPieceItem) {
                    if (Screen.hasShiftDown()) {
                        list.add(Text.translatable("tooltip.twisted_and_carved.twisted_piece").formatted(Formatting.DARK_GRAY));
                    } else {
                        list.add(Text.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Text.literal("Shift").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }
                }
            });
        }
    }
}
