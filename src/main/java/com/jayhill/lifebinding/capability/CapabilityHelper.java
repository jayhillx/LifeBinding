package com.jayhill.lifebinding.capability;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

@SuppressWarnings("all")
public class CapabilityHelper {

    public static void setPlayerBound(ServerPlayerEntity player, boolean bound) {
        player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance()).setBound(bound);
    }

    public static boolean getPlayerBound(PlayerEntity player) {
        return player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance()).isBound();
    }

}