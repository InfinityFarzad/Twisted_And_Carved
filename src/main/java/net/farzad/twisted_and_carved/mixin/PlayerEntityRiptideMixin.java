package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideMixinInterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityRiptideMixin implements TwistedRiptideMixinInterface {

    @Unique
    ItemStack RStack;

    @Override
    public void twistedAndCarved$setRiptideStack(ItemStack stack) {
        RStack = stack;
    }

    @Override
    public ItemStack twistedAndCarved$getRiptideStack() {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (this.RStack != null) {
            return this.RStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

}
