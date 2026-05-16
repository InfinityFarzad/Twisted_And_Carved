package net.farzad.twisted_and_carved.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.hud.HudElementRegistryImpl;
import net.farzad.twisted_and_carved.client.particle.*;
import net.farzad.twisted_and_carved.common.register.*;
import net.farzad.twisted_and_carved.client.properties.TwistedScytheGrapplingProperty;
import net.farzad.twisted_and_carved.client.render.entity.TwistedGreataxeEntityRenderer;
import net.farzad.twisted_and_carved.client.render.entity.TwistedScytheEntityRenderer;
import net.farzad.twisted_and_carved.client.render.hud.BloodBarHudRenderer;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.item.TwistedItemPieceItem;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.sound.WeaponEntitySoundInstance;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideSetterInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.LeavesParticle;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.BlockRenderLayer;
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
        ParticleFactoryRegistry.getInstance().register(TCParticles.TWISTED_SWEEP_ATTACK, SweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.TWISTED_GLAIVE_SWEEP, TwistedGlaiveSweepParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.TWISTED_LEAF_PARTICLE, LeavesParticle.CherryLeavesFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.PARRY_PARTICLE, ParryParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.FALCHION_SLASH, DirectionalSlashParticle.FalchionSlashFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.DASH_PARTICLE, DashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.COFFIN_SMOKE, CoffinSmokeParticle.CoffinSmokeParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.COFFIN_ASH, DotParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TCParticles.HARVEST_SLASH, DirectionalSlashParticle.HarvestSlashFactory::new);

        EntityRendererRegistryImpl.register(TCEntities.TWISTED_GREATAXE_ENTITY, TwistedGreataxeEntityRenderer::new);
        EntityRendererRegistryImpl.register(TCEntities.TWISTED_SCYTHE_ENTITY, TwistedScytheEntityRenderer::new);
        BlockRenderLayerMap.putBlocks(BlockRenderLayer.CUTOUT, TCBlocks.TWISTED_SAPLING, TCBlocks.KARMIUM_RAILING, TCBlocks.TWISTED_LEAVES, TCBlocks.KARMIUM_CHAIN);
        BooleanProperties.ID_MAPPER.put(Identifier.of(TwistedAndCarved.MOD_ID,"twisted_scythe_grappling"),TwistedScytheGrapplingProperty.CODEC);

        applyItemTooltips();


        HudElementRegistryImpl.attachElementAfter(Identifier.of(VanillaHudElements.HOTBAR.getPath()),TwistedAndCarved.id("bloodbar_hud"),bloodBarHudRenderer);
        ClientTickEvents.END_CLIENT_TICK.register((client) -> this.bloodBarHudRenderer.tick());

        ClientPlayNetworking.registerGlobalReceiver(GreataxeSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedGreataxeEntity twistedGreataxe = (TwistedGreataxeEntity) context.player().getEntityWorld().getEntityById(payload.entityID());
            if (twistedGreataxe != null && !twistedGreataxe.isRemoved()) {
                WeaponEntitySoundInstance instance = new WeaponEntitySoundInstance(SoundEvents.ITEM_ELYTRA_FLYING,twistedGreataxe, SoundCategory.AMBIENT);
                context.client().getSoundManager().play(instance);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ScytheSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedScytheEntity twistedScytheEntity = (TwistedScytheEntity) context.player().getEntityWorld().getEntityById(payload.entityID());
            if (twistedScytheEntity != null && !twistedScytheEntity.isRemoved()) {
                WeaponEntitySoundInstance instance = new WeaponEntitySoundInstance(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,twistedScytheEntity, SoundCategory.AMBIENT);
                context.client().getSoundManager().play(instance);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(RiptideModificationPayload.ID, (payload, context) -> {
            PlayerEntity entity = ((PlayerEntity)context.player().getEntityWorld().getEntityById(payload.entityID()));
            if (entity != null) {
                ((TwistedRiptideSetterInterface)entity).twistedAndCarved$setRiptideStack(payload.stack());
            }
        });

    }

    private static void applyItemTooltips() {
        MinecraftClient client = MinecraftClient.getInstance() != null ? MinecraftClient.getInstance() : null;

        if (client != null) {

            ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
                String rightClick  = Text.translatable(MinecraftClient.getInstance().options.useKey.getBoundKeyTranslationKey()).getString();
                boolean isShifting = MinecraftClient.getInstance().isShiftPressed();
                if (itemStack.contains(TCDataComponents.TWISTED_SPIRIT)) {
                    if (itemStack.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY) != ItemStack.EMPTY) {
                        list.add(1,
                                Text.translatable(
                                        "tooltip.twisted_and_carved.twisted_spirit",
                                        Text.literal(itemStack.get(TCDataComponents.TWISTED_SPIRIT).getItemName().getString()).formatted(Formatting.GOLD)
                                ));
                    } else {
                        list.add(1,
                                Text.translatable(
                                        "tooltip.twisted_and_carved.twisted_spirit",
                                        Text.literal(". . .").formatted(Formatting.GOLD)));
                    }
                }
                if (itemStack.isOf(TCItems.TWISTED_GREATAXE)) {
                    if (isShifting) {
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
                } else if (itemStack.isOf(TCItems.TWISTED_GLAIVE)) {
                    if (isShifting) {

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
                } else if (itemStack.isOf(TCItems.TWISTED_SCYTHE)) {
                    if (isShifting) {

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
                } else if (itemStack.isOf(TCItems.TWISTED_FALCHION)) {
                    if (isShifting) {

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
                    if (isShifting) {
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
