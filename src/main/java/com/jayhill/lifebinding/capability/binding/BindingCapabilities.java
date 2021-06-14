package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class BindingCapabilities {
    @CapabilityInject(IBoundCapability.class)
    public static Capability<IBoundCapability> LIFE_BOUND_CAPABILITY = null;

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IBoundCapability.class, new Storage(), DefaultBoundCapability::new);
    }

    public static class Storage implements Capability.IStorage<IBoundCapability> {

        public INBT writeNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            if (instance.getUUID() != null) {
                tag.putUniqueId("boundUUID", instance.getUUID());
                tag.putString("boundName", instance.getName());
                tag.putBoolean("isBound", instance.isBound());
            }

            return tag;
        }

        public void readNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            if (tag.contains("boundUUID")) {
                instance.setUUID(tag.getUniqueId("boundUUID"));
            } if (tag.contains("boundName")) {
                instance.setName(tag.getString("boundName"));
            } if (tag.contains("isBound")) {
                instance.setBound(tag.getBoolean("isBound"));
            }
        }
    }

}