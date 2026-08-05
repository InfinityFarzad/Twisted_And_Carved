package net.farzad.twisted_and_carved.common.init;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class TCSounds {

    public static final SoundEvent SCYTHE_SWEEP = registerSound("scythe_sweep");
    public static final SoundEvent TWISTED_GLAIVE_SWEEP = registerSound("twisted_glaive_sweep");
    public static final SoundEvent PARRY = registerSound("parry");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = TwistedAndCarved.id(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void init() {
    }

}