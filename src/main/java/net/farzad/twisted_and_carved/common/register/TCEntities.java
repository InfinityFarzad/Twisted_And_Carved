package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.entity.LostMerchantEntity;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class TCEntities {

    private static final ResourceKey<EntityType<?>> TWISTED_GREATAXE_ENTITY_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, TwistedAndCarved.id("twisted_greataxe_entity"));

    private static final ResourceKey<EntityType<?>> TWISTED_SCYTHE_ENTITY_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, TwistedAndCarved.id("twisted_scythe_entity"));

    private static final ResourceKey<EntityType<?>> LOST_MERCHANT_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, TwistedAndCarved.id("lost_merchant"));


    public static final EntityType<LostMerchantEntity> LOST_MERCHANT_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            TwistedAndCarved.id("lost_merchant"),
            EntityType.Builder.<LostMerchantEntity>of(LostMerchantEntity::new, MobCategory.CREATURE)
                    .sized(0.5f,1.8f)
                    .eyeHeight(1.4f)
                    .clientTrackingRange(25)
                    .canSpawnFarFromPlayer()
                    .build(LOST_MERCHANT_KEY)
    );

    public static final EntityType<TwistedGreataxeEntity> TWISTED_GREATAXE_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            TwistedAndCarved.id("twisted_greataxe_entity"),
            EntityType.Builder.<TwistedGreataxeEntity>of(TwistedGreataxeEntity::new, MobCategory.MISC)
                    .sized(0.8f, 0.8f).build(TWISTED_GREATAXE_ENTITY_KEY));

    public static final EntityType<TwistedScytheEntity> TWISTED_SCYTHE_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            TwistedAndCarved.id("twisted_scythe_entity"),
            EntityType.Builder.<TwistedScytheEntity>of(TwistedScytheEntity::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f).build( TWISTED_SCYTHE_ENTITY_KEY));

    public static void initAttributes() {
        FabricDefaultAttributeRegistry.register(LOST_MERCHANT_ENTITY,LostMerchantEntity.createDefaultAttributes());
    }

    public static void init() {
        initAttributes();
    }
}
