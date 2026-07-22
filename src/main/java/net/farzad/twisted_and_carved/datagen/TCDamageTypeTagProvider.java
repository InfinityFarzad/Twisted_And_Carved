package net.farzad.twisted_and_carved.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.farzad.twisted_and_carved.common.init.TCDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import java.util.concurrent.CompletableFuture;

public class TCDamageTypeTagProvider extends FabricTagsProvider<DamageType> {
    public TCDamageTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.builder(DamageTypeTags.BYPASSES_ARMOR).addOptional(TCDamageTypes.FALCHION_SLASH).addOptional(TCDamageTypes.TOMAHAWK_DAMAGE);
        this.builder(DamageTypeTags.BYPASSES_COOLDOWN).addOptional(TCDamageTypes.FALCHION_SLASH);
        this.builder(DamageTypeTags.IS_PROJECTILE).addOptional(TCDamageTypes.TOMAHAWK_DAMAGE);

    }
}
