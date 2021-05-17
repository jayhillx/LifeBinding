package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class BindingCapabilityProvider implements ICapabilitySerializable<ByteNBT> {

    private final IBoundCapability INSTANCE = BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance();
    private final LazyOptional<IBoundCapability> optional = LazyOptional.of(() -> INSTANCE);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == BindingCapabilities.LIFE_BOUND_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public ByteNBT serializeNBT() {
        return (ByteNBT) BindingCapabilities.LIFE_BOUND_CAPABILITY.getStorage().writeNBT(BindingCapabilities.LIFE_BOUND_CAPABILITY, INSTANCE, null);
    }

    @Override
    public void deserializeNBT(ByteNBT nbt) {
        BindingCapabilities.LIFE_BOUND_CAPABILITY.getStorage().readNBT(BindingCapabilities.LIFE_BOUND_CAPABILITY, INSTANCE, null, nbt);
    }

}