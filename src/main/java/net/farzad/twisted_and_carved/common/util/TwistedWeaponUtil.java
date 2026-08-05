package net.farzad.twisted_and_carved.common.util;

import net.akws.chiseled_lib.common.util.NetworkingUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.init.TCDataComponents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;


public class TwistedWeaponUtil {

    public static String getAbilityID(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT,ItemStack.EMPTY).getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).id();
    }

    public static String getAbilityTYPE(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT,ItemStack.EMPTY).getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type();
    }

}
