package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.farzad.twisted_and_carved.common.world.TCBiomes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {

    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getTagBuilder(ConventionalBiomeTags.IS_OVERWORLD).addOptional(TCBiomes.TWISTED_FOREST.getValue());
        this.getTagBuilder(TDTags.Biomes.THICK_FOG).addOptional(TCBiomes.TWISTED_FOREST.getValue());
        this.getTagBuilder(TDTags.Biomes.TWISTED_FOREST).add(TCBiomes.TWISTED_FOREST.getValue());
    }
}
