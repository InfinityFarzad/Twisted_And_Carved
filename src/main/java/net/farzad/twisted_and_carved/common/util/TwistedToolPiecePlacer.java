package net.farzad.twisted_and_carved.common.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.farzad.twisted_and_carved.common.init.TCItems;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
    }

}

