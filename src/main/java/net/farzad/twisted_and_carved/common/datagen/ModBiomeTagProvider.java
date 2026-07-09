package net.farzad.twisted_and_carved.common.datagen;


import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.farzad.twisted_and_carved.common.world.TCBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagsProvider<Biome> {

    public ModBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.getOrCreateRawBuilder(ConventionalBiomeTags.IS_OVERWORLD).addOptionalElement(TCBiomes.TWISTED_FOREST.identifier());
        //this.getTagBuilder(TDTags.Biomes.TWISTED_FOREST).add(TCBiomes.TWISTED_FOREST.getValue());
    }
}
