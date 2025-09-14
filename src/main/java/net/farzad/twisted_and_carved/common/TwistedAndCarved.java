package net.farzad.twisted_and_carved.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.enchantment.ModEnchantmentEffects;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.item.ModItemGroups;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.item.custom.TwistedItemPieceItem;
import net.farzad.twisted_and_carved.common.networking.ModNetworking;
import net.farzad.twisted_and_carved.common.sound.ModSounds;
import net.farzad.twisted_and_carved.common.util.TwistedToolPiecePlacer;
import net.farzad.twisted_and_carved.common.world.ModBiomes;
import net.farzad.twisted_and_carved.common.world.TwistedForestRegions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

import static net.farzad.twisted_and_carved.common.util.EnchantmentUtil.hasEnchantment;

public class TwistedAndCarved implements ModInitializer, TerraBlenderApi {
    public static final String MOD_ID = "twisted_and_carved";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.init();
        ModEnchantmentEffects.init();
        ModDataComponents.init();
        ModBlocks.init();
        ModParticles.init();
        ModItemGroups.init();
        ModSounds.init();
        ModEntities.init();
        ModNetworking.init();
        TwistedToolPiecePlacer.init();
        ModBiomes.init();
        applyItemTooltips();

        StrippableBlockRegistry.register(ModBlocks.TWISTED_LOG, ModBlocks.STRIPPED_TWISTED_LOG);
        StrippableBlockRegistry.register(ModBlocks.TWISTED_WOOD, ModBlocks.STRIPPED_TWISTED_WOOD);
    }

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new TwistedForestRegions(Identifier.of(MOD_ID, "overworld"), 2));
    }

    private static void applyItemTooltips() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            if (itemStack.isOf(ModItems.TWISTED_GREATAXE)) {
                if (Screen.hasShiftDown()) {
                    list.add(Text.translatable(
                            "tooltip.twisted_and_carved.twisted_greataxe_info"
                    ).formatted(Formatting.DARK_GRAY));
                    if (hasEnchantment(itemStack, ModEnchantmentEffects.STRIDE)) {
                        list.add(Text.translatable(
                                "tooltip.twisted_and_carved.twisted_dash",
                                Text.literal("Attack").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    } else if (hasEnchantment(itemStack, ModEnchantmentEffects.TOMAHAWK)) {
                        list.add(Text.translatable(
                                "tooltip.twisted_and_carved.twisted_tomahawk",
                                Text.literal("Tomahawk").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }
                } else {
                    list.add(Text.translatable(
                            "tooltip.twisted_and_carved.twisted_info",
                            Text.literal("Shift").formatted(Formatting.GOLD)
                    ).formatted(Formatting.DARK_GRAY));
                }
            } else if (itemStack.isOf(ModItems.TWISTED_GLAIVE)) {
                if (Screen.hasShiftDown()) {

                    list.add(Text.translatable(
                            "tooltip.twisted_and_carved.twisted_glaive_info"
                    ).formatted(Formatting.DARK_GRAY));
                    if (hasEnchantment(itemStack, ModEnchantmentEffects.SWEEPING)) {
                        list.add(Text.translatable(
                                "tooltip.twisted_and_carved.twisted_sweep",
                                Text.literal("RightClick").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }

                } else {
                    list.add(Text.translatable(
                            "tooltip.twisted_and_carved.twisted_info",
                            Text.literal("Shift").formatted(Formatting.GOLD)
                    ).formatted(Formatting.DARK_GRAY));
                }
            } else if (itemStack.isOf(ModItems.TWISTED_SCYTHE)) {
                if (Screen.hasShiftDown()) {

                    list.add(Text.translatable(
                            "tooltip.twisted_and_carved.twisted_greataxe_info"
                    ).formatted(Formatting.DARK_GRAY));
                    if (hasEnchantment(itemStack, ModEnchantmentEffects.HARVEST)) {
                        list.add(Text.translatable(
                                "tooltip.twisted_and_carved.twisted_harvest",
                                Text.literal("RightClick").formatted(Formatting.GOLD)
                        ).formatted(Formatting.DARK_GRAY));
                    }

                } else {
                    list.add(Text.translatable(
                            "tooltip.twisted_and_carved.twisted_info",
                            Text.literal("Shift").formatted(Formatting.GOLD)
                    ).formatted(Formatting.DARK_GRAY));
                }
            } else if (itemStack.isOf(ModItems.TWISTED_FALCHION)) {

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