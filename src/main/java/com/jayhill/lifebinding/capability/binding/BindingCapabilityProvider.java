package com.jayhill.lifebinding.capability.binding;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BindingCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DefaultBoundCapability bound = new DefaultBoundCapability();
    final LazyOptional<IBoundCapability> boundOptional = LazyOptional.of(() -> this.bound);

    public void invalidate() {
        this.boundOptional.invalidate();
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return BindingCapabilities.LIFE_BOUND_CAPABILITY.orEmpty(cap, this.boundOptional);
    }

    public CompoundNBT serializeNBT() {
        return BindingCapabilities.LIFE_BOUND_CAPABILITY == null ? new CompoundNBT() : (CompoundNBT)BindingCapabilities.LIFE_BOUND_CAPABILITY.writeNBT(this.bound, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (BindingCapabilities.LIFE_BOUND_CAPABILITY != null) {
            BindingCapabilities.LIFE_BOUND_CAPABILITY.readNBT(this.bound, null, nbt);
        }
    }

}