package com.lifebinding.capability.binding;

import com.lifebinding.capability.data.PlayerData;
import net.minecraft.nbt.CompoundNBT;

import java.util.*;

public class StoredBoundPlayers implements IBoundPlayers {

    private final Map<UUID, PlayerData> byId = new HashMap<>();
    private final List<UUID> deathList = new ArrayList<>();

    public List<UUID> getDeathList() {
        return this.deathList;
    }

    public Map<UUID, PlayerData> getPlayers() {
        return this.byId;
    }

    /**
     * @return new data if the uuid given is not in the map.
     */
    public PlayerData getInformationById(UUID id) {
        return this.byId.computeIfAbsent(id, (orNewId) -> new PlayerData(new CompoundNBT()));
    }

}