package net.farzad.twisted_and_carved.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.farzad.twisted_and_carved.common.init.TCConfiguredFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class TCDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TCModelProvider::new);
        pack.addProvider(TCBlockTagProvider::new);
        pack.addProvider(TCLootTableProvider::new);
        pack.addProvider(TCItemTagProvider::new);
        pack.addProvider(TCRegistryDataGenerator::new);
        pack.addProvider(TCBiomeTagProvider::new);
        pack.addProvider(TCDamageTypeTagProvider::new);
        pack.addProvider(TCRecipeProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, TCConfiguredFeatures::configure);
    }
}
