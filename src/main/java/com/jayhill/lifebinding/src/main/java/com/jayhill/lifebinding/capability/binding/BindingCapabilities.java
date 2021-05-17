package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class BindingCapabilities {

    @CapabilityInject(IBoundCapability.class)
    public static final Capability<IBoundCapability> LIFE_BOUND_CAPABILITY = null;

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IBoundCapability.class, new Storage(), DefaultBoundCapability::new);
    }

    public static class Storage implements Capability.IStorage<IBoundCapability> {

        @Nullable
        public INBT writeNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side) {
            return ByteNBT.valueOf(instance.isBound());
        }

        public void readNBT(Capability<IBoundCapability> capability, IBoundCapability instance, Direction side, INBT nbt) {
            if (nbt.getId() != Constants.NBT.TAG_BYTE) {
                return;
            }
            instance.setBound(((ByteNBT)nbt).getByte() == 1);
        }
    }

}