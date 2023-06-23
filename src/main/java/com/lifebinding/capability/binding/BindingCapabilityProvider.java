package com.lifebinding.capability.binding;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BindingCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final StoredBoundPlayers players = new StoredBoundPlayers();
    final LazyOptional<IBoundPlayers> playersOptional = LazyOptional.of(() -> players);

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return BindingCapability.BOUND.orEmpty(cap, this.playersOptional);
    }

    public CompoundNBT serializeNBT() {
        return BindingCapability.BOUND == null ? new CompoundNBT() : (CompoundNBT) BindingCapability.BOUND.writeNBT(players, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (BindingCapability.BOUND != null) {
            BindingCapability.BOUND.readNBT(players, null, nbt);
        }
    }

}