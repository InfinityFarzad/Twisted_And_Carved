package net.farzad.twisted_and_carved.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.farzad.twisted_and_carved.client.particle.ModParticles;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.block.entity.ModBlockEntities;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.entity.ModEntities;
import net.farzad.twisted_and_carved.common.item.ModItemGroups;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.item.custom.TwistedItemPieceItem;
import net.farzad.twisted_and_carved.common.networking.ModNetworking;
import net.farzad.twisted_and_carved.common.sound.ModSounds;
import net.farzad.twisted_and_carved.common.util.TwistedToolPiecePlacer;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.world.ModBiomes;
import net.farzad.twisted_and_carved.common.world.TwistedForestRegions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

import java.util.ArrayList;
import java.util.List;

public class TwistedAndCarved implements ModInitializer, TerraBlenderApi {
    public static final String MOD_ID = "twisted_and_carved";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String id) {
        return Identifier.of(MOD_ID,id);
    }

    @Override
    public void onInitialize() {
        ModItems.init();
        ModDataComponents.init();
        ModBlocks.init();
        ModParticles.init();
        ModItemGroups.init();
        ModSounds.init();
        ModEntities.init();
        ModNetworking.init();
        ModBlockEntities.init();
        TwistedToolPiecePlacer.init();
        ModBiomes.init();

        // ServerLivingEntityEvents.AFTER_DAMAGE

        StrippableBlockRegistry.register(ModBlocks.TWISTED_LOG, ModBlocks.STRIPPED_TWISTED_LOG);
        StrippableBlockRegistry.register(ModBlocks.TWISTED_WOOD, ModBlocks.STRIPPED_TWISTED_WOOD);
    }

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new TwistedForestRegions(Identifier.of(MOD_ID, "overworld"), 2));
    }


}