package net.farzad.twisted_and_carved.common.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.Optional;

public class TwistedToolPiecePlacer {

    public static void init() {

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key == BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY) {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder
                        .add(LootItem.lootTableItem(TCItems.TWISTED_GLAIVE_PIECE)));
            }

            if (BuiltInLootTables.JUNGLE_TEMPLE.equals(key)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.25f))
                        .add(LootItem.lootTableItem(TCItems.TWISTED_FALCHION_PIECE))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }


        });
        TradeOfferHelper.registerWanderingTraderOffers(factories ->
                factories.addOffersToPool(TwistedAndCarved.id("scythe_trade"),(world, entity, random) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD,25),
                        Optional.of(new ItemCost(Items.WHEAT, 5)),
                        new ItemStack(TCItems.TWISTED_SCYTHE_PIECE),
                        1,20,0.075f)));
    }

}

