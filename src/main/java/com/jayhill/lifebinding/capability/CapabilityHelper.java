package com.jayhill.lifebinding.capability;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nonnull;

@SuppressWarnings("all")
public class CapabilityHelper {

    public static void setPlayerBound(@Nonnull ServerPlayerEntity player, boolean isBound) {
        player.getCapability(BindingCapabilities.LIFE_BOUND_CAPABILITY).orElse(BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance());
    }

}