package net.farzad.twisted_and_carved.common.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

import java.util.Optional;

public class TwistedToolPiecePlacer {

    public static void init() {

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key == LootTables.DESERT_PYRAMID_ARCHAEOLOGY) {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder
                        .with(ItemEntry.builder(TCItems.TWISTED_GLAIVE_PIECE)));
            }

            if (LootTables.JUNGLE_TEMPLE_CHEST.equals(key)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.25f))
                        .with(ItemEntry.builder(TCItems.TWISTED_FALCHION_PIECE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });
        TradeOfferHelper.registerWanderingTraderOffers(factories ->
                factories.addOffersToPool(TwistedAndCarved.id("scythe_trade"),(world, entity, random) -> new TradeOffer(
                        new TradedItem(Items.EMERALD,25),
                        Optional.of(new TradedItem(Items.WHEAT, 5)),
                        new ItemStack(TCItems.TWISTED_SCYTHE_PIECE),
                        1,20,0.075f)));
    }

}

