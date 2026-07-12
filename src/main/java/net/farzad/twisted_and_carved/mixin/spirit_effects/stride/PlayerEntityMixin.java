package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.register.TCNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(method = "startAutoSpinAttack", at = @At(value = "TAIL"))
    private void twisted_and_carved$syncRiptideStack(int activationTicks, float dmg, ItemStack itemStackUsed, CallbackInfo ci) {
        Player player = (Player) (Object)this;
        Level level = player.level();

        if (level instanceof ServerLevel serverLevel) {
            TCNetworking.sendPacketToAllClients(serverLevel,new RiptideModificationPayload(player.getId(),itemStackUsed));
        }
    }
}
