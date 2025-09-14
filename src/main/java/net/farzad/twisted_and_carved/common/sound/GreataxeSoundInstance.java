package net.farzad.twisted_and_carved.common.sound;

import net.farzad.twisted_and_carved.common.entity.custom.TwistedGreataxeEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class GreataxeSoundInstance extends MovingSoundInstance {
    private final TwistedGreataxeEntity twistedGreataxe;

    public GreataxeSoundInstance(TwistedGreataxeEntity twistedGreataxe, SoundCategory soundCategory) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, soundCategory, SoundInstance.createRandom());
        this.volume = (float) (0.65f / twistedGreataxe.getPos().distanceTo(twistedGreataxe.getOwner().getPos()));
        this.pitch = 1.0f + random.nextBetween(-2, 2);
        this.repeat = true;
        this.setPositionToEntity();
        this.twistedGreataxe = twistedGreataxe;
    }

    @Override
    public void tick() {
        if (twistedGreataxe.isRemoved()) {
            this.setDone();
        } else {
            this.setPositionToEntity();
        }

    }

    private void setPositionToEntity() {
        if (twistedGreataxe != null) {
            this.x = this.twistedGreataxe.getX();
            this.y = this.twistedGreataxe.getY();
            this.z = this.twistedGreataxe.getZ();
        }
    }
}
