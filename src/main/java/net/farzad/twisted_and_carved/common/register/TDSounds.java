package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class TDSounds {

    public static final SoundEvent SCYTHE_SWEEP_0 = registerSound("scythe_sweep_0");
    public static final SoundEvent SCYTHE_SWEEP_1 = registerSound("scythe_sweep_1");
    public static final SoundEvent SCYTHE_SWEEP_2 = registerSound("scythe_sweep_2");
    public static final SoundEvent TWISTED_GLAIVE_SWEEP = registerSound("twisted_glaive_sweep");
    public static final SoundEvent PARRY = registerSound("parry");
    public static final SoundEvent GREATAXE_FLYING_SOUND = registerSound("greataxe_flying_sound");
    public static final SoundEvent SCYTHE_FLYING_SOUND = registerSound("scythe_flying_sound");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void init() {
    }

}