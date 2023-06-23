package com.lifebinding.capability.data;

import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

public class BoundPlayerData {
    private UUID id;
    private String name;

    public BoundPlayerData(CompoundNBT nbt) {
        this.id = nbt.getUniqueId("Id");
        this.name = nbt.getString("Name");
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putUniqueId("Id", this.id);
        nbt.putString("Name", this.name);
        return nbt;
    }

}