package com.jayhill.lifebinding.capability;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.network.NetworkHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nonnull;

@SuppressWarnings("all")
public class CapabilityHelper {

    public static void setPlayerBound(@Nonnull ServerPlayerEntity player, boolean bound) {
        player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance()).setBound(bound);
        NetworkHelper.updatePlayerBound(player, bound);
    }

    public static boolean getPlayerBound(@Nonnull PlayerEntity player) {
        return player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance()).isBound();
    }

}