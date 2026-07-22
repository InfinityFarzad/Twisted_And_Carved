package net.farzad.twisted_and_carved.common;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.farzad.twisted_and_carved.common.config.TwistedAndCarvedConfigScreen;
import net.farzad.twisted_and_carved.common.init.*;
import net.farzad.twisted_and_carved.common.init.client.TCParticles;
import net.farzad.twisted_and_carved.common.util.TwistedToolPiecePlacer;
import net.farzad.twisted_and_carved.common.world.TCBiomes;
import net.farzad.twisted_and_carved.common.world.TCSurfaceRules;
import net.farzad.twisted_and_carved.common.world.TwistedForestRegions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class TwistedAndCarved implements ModInitializer, TerraBlenderApi {
    public static final String MOD_ID = "twisted_and_carved";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID,id);
    }

    @Override
    public void onInitialize() {
        TCItems.init();
        TCDataComponents.init();
        TCBlocks.init();
        TCParticles.init();
        TCCreativeGroupTabs.init();
        TCSounds.init();
        TCEntities.init();
        TCNetworking.init();
        TCBlockEntities.init();
        TwistedToolPiecePlacer.init();
        TCBiomes.init();
        MidnightConfig.init(MOD_ID, TwistedAndCarvedConfigScreen.class);

        // will be used later on, do not delete

/*        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            ItemStack weaponStack = damageSource.getWeaponItem();
            Entity attacker = damageSource.getEntity();
            Level world = entity.level();
            if (entity instanceof EnderMan && attacker instanceof Player && !weaponStack.isEmpty() && weaponStack.getItem() instanceof TwistedToolItem) {
                Containers.dropItemStack(world,entity.getX(),entity.getY(),entity.getZ(),new ItemStack(TCItems.TWISTED_FALCHION));
            }
        });*/

        FlattenableBlockRegistry.register(TCBlocks.FESTERING_ROOTS,Blocks.DIRT_PATH.defaultBlockState());
        TillableBlockRegistry.register(TCBlocks.FESTERING_ROOTS, HoeItem::onlyIfAirAbove, Blocks.FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE,7));
        StrippableBlockRegistry.register(TCBlocks.TWISTED_LOG, TCBlocks.STRIPPED_TWISTED_LOG);
        StrippableBlockRegistry.register(TCBlocks.TWISTED_WOOD, TCBlocks.STRIPPED_TWISTED_WOOD);
    }



    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new TwistedForestRegions(TwistedAndCarved.id("overworld"), 3));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD,TwistedAndCarved.MOD_ID, TCSurfaceRules.makeRules() );
    }


}