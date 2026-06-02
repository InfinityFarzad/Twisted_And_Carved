package net.farzad.twisted_and_carved.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.farzad.twisted_and_carved.common.item.TwistedToolItem;
import net.farzad.twisted_and_carved.common.register.*;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.register.TCNetworking;
import net.farzad.twisted_and_carved.common.util.TwistedToolPiecePlacer;
import net.farzad.twisted_and_carved.common.world.TCBiomes;
import net.farzad.twisted_and_carved.common.world.TwistedForestRegions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
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

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            ItemStack weaponStack = damageSource.getWeaponStack();
            Entity attacker = damageSource.getAttacker();
            World world = entity.getEntityWorld();
            if (entity instanceof EndermanEntity && attacker instanceof PlayerEntity && !weaponStack.isEmpty() && weaponStack.getItem() instanceof TwistedToolItem) {
                ItemScatterer.spawn(world,entity.getX(),entity.getY(),entity.getZ(),new ItemStack(TCItems.TWISTED_FALCHION));
            }
        });

        StrippableBlockRegistry.register(TCBlocks.TWISTED_LOG, TCBlocks.STRIPPED_TWISTED_LOG);
        StrippableBlockRegistry.register(TCBlocks.TWISTED_WOOD, TCBlocks.STRIPPED_TWISTED_WOOD);
    }

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new TwistedForestRegions(Identifier.of(MOD_ID, "overworld"), 3));
    }


}