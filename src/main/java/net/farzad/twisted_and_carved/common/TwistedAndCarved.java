package net.farzad.twisted_and_carved.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.farzad.twisted_and_carved.common.register.*;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.register.TCNetworking;
import net.farzad.twisted_and_carved.common.util.TwistedToolPiecePlacer;
import net.farzad.twisted_and_carved.common.util.interfaces.AttackChargableItemInterface;
import net.farzad.twisted_and_carved.common.util.interfaces.CritInterface;
import net.farzad.twisted_and_carved.common.world.TCBiomes;
import net.farzad.twisted_and_carved.common.world.TwistedForestRegions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class TwistedAndCarved implements ModInitializer, TerraBlenderApi {
    public static final String MOD_ID = "twisted_and_carved";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String id) {
        return Identifier.of(MOD_ID,id);
    }

    @Override
    public void onInitialize() {
        TCItems.init();
        TCDataComponents.init();
        TCBlocks.init();
        TCParticles.init();
        TCItemGroups.init();
        TDSounds.init();
        TCEntities.init();
        TCNetworking.init();
        TCBlockEntities.init();
        TwistedToolPiecePlacer.init();
        TCBiomes.init();

        StrippableBlockRegistry.register(TCBlocks.TWISTED_LOG, TCBlocks.STRIPPED_TWISTED_LOG);
        StrippableBlockRegistry.register(TCBlocks.TWISTED_WOOD, TCBlocks.STRIPPED_TWISTED_WOOD);
    }

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new TwistedForestRegions(Identifier.of(MOD_ID, "overworld"), 3));
    }


}