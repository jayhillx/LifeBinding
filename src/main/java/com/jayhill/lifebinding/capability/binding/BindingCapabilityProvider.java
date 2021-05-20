package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class BindingCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

    private final IBoundCapability INSTANCE = BindingCapabilities.LIFE_BOUND_CAPABILITY.getDefaultInstance();
    private final LazyOptional<IBoundCapability> optional = LazyOptional.of(() -> INSTANCE);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == BindingCapabilities.LIFE_BOUND_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    public CompoundNBT serializeNBT() {
        return BindingCapabilities.LIFE_BOUND_CAPABILITY == null ? new CompoundNBT() : (CompoundNBT)BindingCapabilities.LIFE_BOUND_CAPABILITY.writeNBT(INSTANCE, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (BindingCapabilities.LIFE_BOUND_CAPABILITY != null) {
            BindingCapabilities.LIFE_BOUND_CAPABILITY.readNBT(INSTANCE, null, nbt);
        }
    }

}