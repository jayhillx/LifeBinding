package com.lifebinding.capability.data;

import net.minecraft.nbt.CompoundNBT;

/**
 * Data class used to divide up players and their information.
 */
public class PlayerData {
    private String id;
    private String name;
    private final BoundPlayerData data;

    public PlayerData(CompoundNBT nbt) {
        this.id = nbt.getString("Id");
        this.name = nbt.getString("Name");
        this.data = new BoundPlayerData(nbt.getCompound("BoundPlayer"));
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BoundPlayerData getBoundPlayer() {
        return this.data;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putString("Id", this.id);
        nbt.putString("Name", this.name);
        nbt.put("BoundPlayer", this.data.write(new CompoundNBT()));
        return nbt;
    }

}