package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.farzad.twisted_and_carved.common.entity.TwistedScytheEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class TCEntities {

    private static final RegistryKey<EntityType<?>> TWISTED_GREATAXE_ENTITY_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "twisted_greataxe_entity"));

    private static final RegistryKey<EntityType<?>> TWISTED_SCYTHE_ENTITY_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TwistedAndCarved.MOD_ID, "twisted_scythe_entity"));



    public static final EntityType<TwistedGreataxeEntity> TWISTED_GREATAXE_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID, "twisted_greataxe_entity"),
            EntityType.Builder.<TwistedGreataxeEntity>create(TwistedGreataxeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f).build(TWISTED_GREATAXE_ENTITY_KEY));

    public static final EntityType<TwistedScytheEntity> TWISTED_SCYTHE_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID, "twisted_scythe_entity"),
            EntityType.Builder.<TwistedScytheEntity>create(TwistedScytheEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f).build( TWISTED_SCYTHE_ENTITY_KEY));

    public static void init() {
    }
}
