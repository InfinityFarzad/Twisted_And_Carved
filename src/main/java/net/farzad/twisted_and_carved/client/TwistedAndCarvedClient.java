package net.farzad.twisted_and_carved.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.hud.HudElementRegistryImpl;
import net.farzad.twisted_and_carved.client.particle.*;
import net.farzad.twisted_and_carved.client.properties.TwistedScytheGrapplingProperty;
import net.farzad.twisted_and_carved.client.render.entity.LostMerchentRenderer;
import net.farzad.twisted_and_carved.client.render.entity.SpiritForgeEntityRenderer;
import net.farzad.twisted_and_carved.client.render.entity.TwistedGreataxeEntityRenderer;
import net.farzad.twisted_and_carved.client.render.entity.TwistedScytheEntityRenderer;
import net.farzad.twisted_and_carved.client.render.hud.BloodBarHudRenderer;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.farzad.twisted_and_carved.common.init.client.TCModelLayers;
import net.farzad.twisted_and_carved.common.init.client.TCParticles;
import net.farzad.twisted_and_carved.common.item.TwistedItemPieceItem;
import net.farzad.twisted_and_carved.common.item.TwistedSpiritItem;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.init.*;
import net.farzad.twisted_and_carved.common.sound.WeaponEntitySoundInstance;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideSetterInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.AttackSweepParticle;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TwistedAndCarvedClient implements ClientModInitializer {
    private final BloodBarHudRenderer bloodBarHudRenderer = new BloodBarHudRenderer();

    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(TCParticles.TWISTED_SWEEP_ATTACK, AttackSweepParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.TWISTED_GLAIVE_SWEEP, TwistedGlaiveSweepParticle.Factory::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.TWISTED_LEAF_PARTICLE, FallingLeavesParticle.CherryProvider::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.PARRY_PARTICLE, ParryParticle.Factory::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.FALCHION_SLASH, DirectionalSlashParticle.FalchionSlashFactory::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.DASH_PARTICLE, DashParticle.Factory::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.COFFIN_SMOKE, CoffinSmokeParticle.CoffinSmokeParticleFactory::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.COFFIN_ASH, DotParticle.Factory::new);
        ParticleProviderRegistry.getInstance().register(TCParticles.HARVEST_SLASH, DirectionalSlashParticle.HarvestSlashFactory::new);

        EntityRendererRegistryImpl.register(TCEntities.LOST_MERCHANT_ENTITY, LostMerchentRenderer::new);
        EntityRendererRegistryImpl.register(TCEntities.TWISTED_GREATAXE_ENTITY, TwistedGreataxeEntityRenderer::new);
        EntityRendererRegistryImpl.register(TCEntities.TWISTED_SCYTHE_ENTITY, TwistedScytheEntityRenderer::new);
        BlockEntityRendererRegistryImpl.register(TCBlockEntities.SPIRIT_FORGE_ENTITY, SpiritForgeEntityRenderer::new);
        ConditionalItemModelProperties.ID_MAPPER.put(TwistedAndCarved.id("twisted_scythe_grappling"),TwistedScytheGrapplingProperty.CODEC);

        applyItemTooltips();

        HudElementRegistryImpl.attachElementAfter(Identifier.parse(VanillaHudElements.HOTBAR.getPath()),TwistedAndCarved.id("bloodbar_hud"),bloodBarHudRenderer);
        ClientTickEvents.END_CLIENT_TICK.register((client) -> this.bloodBarHudRenderer.tick());

        ClientPlayNetworking.registerGlobalReceiver(GreataxeSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedGreataxeEntity twistedGreataxe = (TwistedGreataxeEntity) context.player().level().getEntity(payload.entityID());
            if (twistedGreataxe != null && !twistedGreataxe.isRemoved()) {
                WeaponEntitySoundInstance instance = new WeaponEntitySoundInstance(SoundEvents.ELYTRA_FLYING,twistedGreataxe, SoundSource.AMBIENT);
                if (!context.client().getSoundManager().isActive(instance)) {
                    context.client().getSoundManager().play(instance);
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ScytheSoundLoopS2CPayload.ID, (payload, context) -> {
            TwistedScytheEntity twistedScytheEntity = (TwistedScytheEntity) context.player().level().getEntity(payload.entityID());
            if (twistedScytheEntity != null && !twistedScytheEntity.isRemoved()) {
                WeaponEntitySoundInstance instance = new WeaponEntitySoundInstance(SoundEvents.PLAYER_ATTACK_SWEEP,twistedScytheEntity, SoundSource.AMBIENT);
                context.client().getSoundManager().play(instance);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(RiptideModificationPayload.ID, (payload, context) -> {
            Player entity = ((Player)context.player().level().getEntity(payload.entityID()));
            if (entity != null) {
                ((TwistedRiptideSetterInterface)entity).twistedAndCarved$setRiptideStack(payload.stack());
            }
        });

        TCModelLayers.init();

    }

    private static void applyItemTooltips() {
        Minecraft client = Minecraft.getInstance() != null ? Minecraft.getInstance() : null;

        if (client != null) {

            ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
                String rightClick  = Component.translatable(Minecraft.getInstance().options.keyUse.saveString()).getString();
                boolean isShifting = Minecraft.getInstance().hasShiftDown();
                if (itemStack.has(TCDataComponents.TWISTED_SPIRIT)) {
                    if (itemStack.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY) != ItemStack.EMPTY) {
                        list.add(1,
                                Component.translatable(
                                        "tooltip.twisted_and_carved.twisted_spirit",
                                        Component.literal(itemStack.get(TCDataComponents.TWISTED_SPIRIT).getItemName().getString()).withStyle(ChatFormatting.GOLD)
                                ));
                    } else {
                        list.add(1,
                                Component.translatable(
                                        "tooltip.twisted_and_carved.twisted_spirit",
                                        Component.literal(". . .").withStyle(ChatFormatting.GOLD)));
                    }
                }
                if (itemStack.is(TCItems.TWISTED_GREATAXE)) {
                    if (isShifting) {
                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_greataxe_info"
                        ).withStyle(ChatFormatting.DARK_GRAY));
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "stride") {
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_dash",
                                    Component.literal("Attack").withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                            list.add(3,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_dash1",
                                    Component.literal(rightClick).withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                        } else if (TwistedWeaponUtil.getAbilityID(itemStack) == "tomahawk") {
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_tomahawk",
                                    Component.literal("Tomahawk").withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                        }
                    } else {
                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Component.literal("Shift").withStyle(ChatFormatting.GOLD)
                        ).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else if (itemStack.is(TCItems.TWISTED_GLAIVE)) {
                    if (isShifting) {

                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_glaive_info"
                        ).withStyle(ChatFormatting.DARK_GRAY));

                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "sweeping") {
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_sweep",
                                    Component.literal(rightClick).withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                        }

                    } else {
                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Component.literal("Shift").withStyle(ChatFormatting.GOLD)
                        ).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else if (itemStack.is(TCItems.TWISTED_SCYTHE)) {
                    if (isShifting) {

                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_greataxe_info"
                        ).withStyle(ChatFormatting.DARK_GRAY));
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "harvest") {
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_harvest",
                                    Component.literal(rightClick + " + Shift").withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                        }
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "grappling") {
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_grapple",
                                    Component.literal(rightClick).withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                        }

                    } else {
                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Component.literal("Shift").withStyle(ChatFormatting.GOLD)
                        ).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else if (itemStack.is(TCItems.TWISTED_FALCHION)) {
                    if (isShifting) {

                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_falchion_info"
                        ).withStyle(ChatFormatting.DARK_GRAY));
                        if (TwistedWeaponUtil.getAbilityID(itemStack) == "bleeding") {
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_bleeding",
                                    Component.literal("Attack").withStyle(ChatFormatting.GOLD)
                            ).withStyle(ChatFormatting.DARK_GRAY));
                            list.add(2,Component.translatable(
                                    "tooltip.twisted_and_carved.twisted_falchion_slash",
                                    Component.literal(rightClick).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.DARK_GRAY));
                        }

                    } else {
                        list.add(2,Component.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Component.literal("Shift").withStyle(ChatFormatting.GOLD)
                        ).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else if (itemStack.getItem() instanceof TwistedItemPieceItem) {
                    if (isShifting) {
                        list.add(Component.translatable("tooltip.twisted_and_carved.twisted_piece").withStyle(ChatFormatting.DARK_GRAY));
                    } else {
                        list.add(Component.translatable(
                                "tooltip.twisted_and_carved.twisted_info",
                                Component.literal("Shift").withStyle(ChatFormatting.GOLD)
                        ).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else if (itemStack.getItem() instanceof TwistedSpiritItem) {
                    list.add(Component.translatable(itemStack.getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type()).withStyle(ChatFormatting.GOLD));
                }
            });
        }
    }
}
