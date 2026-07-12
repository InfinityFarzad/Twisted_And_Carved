package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import net.farzad.twisted_and_carved.common.util.interfaces.StrideRenderStateAddon;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class PlayerEntityRenderStateMixin implements StrideRenderStateAddon {

    @Unique
    ItemStack riptideStack;

    @Override
    public void twistedAndCarved$setRiptideStack(ItemStack stack) {
        this.riptideStack = stack;
    }

    @Override
    public ItemStack twistedAndCarved$getRiptideStack() {
        return riptideStack != null ? riptideStack : ItemStack.EMPTY;
    }
}
