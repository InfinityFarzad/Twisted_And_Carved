package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements TwistedRiptideRenderState {

    @Unique
    PlayerEntity player;

    @Override
    public void twistedAndCarved$setPlayer(PlayerEntity Iplayer) {
        this.player = Iplayer;
    }

    @Override
    public PlayerEntity twistedAndCarved$getPlayer() {
        return player;
    }
}
