package net.farzad.twisted_and_carved.common.sound;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent SCYTHE_SWEEP_0 = registerSound("scythe_sweep_0");
    public static final SoundEvent SCYTHE_SWEEP_1 = registerSound("scythe_sweep_1");
    public static final SoundEvent SCYTHE_SWEEP_2 = registerSound("scythe_sweep_2");
    public static final SoundEvent TWISTED_GLAIVE_SWEEP = registerSound("twisted_glaive_sweep");
    public static final SoundEvent PARRY = registerSound("parry");
    public static final SoundEvent GREATAXE_FLYING_SOUND = registerSound("greataxe_flying_sound");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(TwistedAndCarved.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void init() {
    }

}