package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.util.interfaces.CustomAttackSoundInterface;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    public abstract ItemStack getWeaponItem();

    @Shadow
    protected abstract void playServerSideSound(SoundEvent sound);

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V", shift = At.Shift.AFTER))
    private void twisted_and_carved$playGreataxeSound(Entity entity, CallbackInfo ci) {
        ItemStack stack = this.getWeaponItem();
        if (stack.getItem() instanceof CustomAttackSoundInterface sfx) {
            if (sfx.getExtraAttackSound(stack) != null) {
                Player player = (Player)(Object)this;
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), sfx.getExtraAttackSound(stack), player.getSoundSource(), 1.0F, Mth.randomBetween(player.getRandom(),0.85f,1.0f));
            }
        }
    }
}
