package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.UUID;

public class BindingCapabilities {
    @CapabilityInject(IBoundCapability.class)
    public static Capability<IBoundCapability> LIFE_BOUND_CAPABILITY = null;

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IBoundCapability.class, new Storage(), DefaultBoundCapability::new);
    }

    public static class Storage implements Capability.IStorage<IBoundCapability> {

        public INBT writeNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putUniqueId("boundTo", instance.getUUID());
            return tag;
        }

        public void readNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side, INBT nbt) {
            UUID boundTo = ((CompoundNBT)nbt).getUniqueId("boundTo");
            instance.setUUID(boundTo);
        }
    }

}