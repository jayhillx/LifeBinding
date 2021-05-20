package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.Arrays;

public class BindingCapabilities {

    @CapabilityInject(IBoundCapability.class)
    public static final Capability<IBoundCapability> LIFE_BOUND_CAPABILITY = null;

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IBoundCapability.class, new Storage(), DefaultBoundCapability::new);
    }

    public static class Storage implements Capability.IStorage<IBoundCapability> {

        @Nullable
        public INBT writeNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("boundTo", Arrays.toString(instance.getBoundPlayer()));
            return tag;
        }

        public void readNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side, INBT nbt) {
            String cause = ((CompoundNBT)nbt).getString("boundTo");
            String[] stringArray = cause.split(", ");
            instance.setBoundPlayer(stringArray);
        }
    }

}