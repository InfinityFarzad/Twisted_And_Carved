package net.farzad.twisted_and_carved.common.sound;

import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;

public class WeaponEntitySoundInstance extends AbstractTickableSoundInstance {
    private final Projectile entity;
    private final float baseVolume;

    public WeaponEntitySoundInstance(SoundEvent soundEvent, Projectile entity, SoundSource soundCategory) {
        super(soundEvent, soundCategory, SoundInstance.createUnseededRandom());
        this.baseVolume = (float) (2f / entity.position().distanceTo(entity.getOwner().position()));
        this.volume = baseVolume;

        this.pitch = Mth.randomBetween(random,0.5f,0.7f);
        this.looping = true;
        this.setPositionToEntity();
        this.entity = entity;
    }

    @Override
    public void tick() {
        if (entity.isRemoved()) {
            this.stop();
        } else {
            this.setPositionToEntity();
            if (entity instanceof TwistedGreataxeEntity twistedGreataxe) {
                this.volume = twistedGreataxe.shouldStopPlayingSound() ? 0 : this.baseVolume;
            }
        }
    }

    private void setPositionToEntity() {
        if (entity != null) {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
        }
    }
}
