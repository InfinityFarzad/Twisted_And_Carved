package net.farzad.twisted_and_carved.common.sound;

import net.farzad.twisted_and_carved.common.entity.TwistedGreataxeEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class WeaponEntitySoundInstance extends MovingSoundInstance {
    private final ProjectileEntity entity;
    private final float baseVolume;

    public WeaponEntitySoundInstance(SoundEvent soundEvent, ProjectileEntity entity, SoundCategory soundCategory) {
        super(soundEvent, soundCategory, SoundInstance.createRandom());
        this.baseVolume = (float) (2f / entity.getEntityPos().distanceTo(entity.getOwner().getEntityPos()));
        this.volume = baseVolume;

        this.pitch = MathHelper.nextBetween(random,0.5f,0.7f);
        this.repeat = true;
        this.setPositionToEntity();
        this.entity = entity;
    }

    @Override
    public void tick() {
        if (entity.isRemoved()) {
            this.setDone();
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
