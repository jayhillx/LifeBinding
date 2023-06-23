package com.lifebinding.capability.binding;

import com.lifebinding.capability.data.PlayerData;

import java.util.*;

public interface IBoundPlayers {

    List<UUID> getDeathList();

    Map<UUID, PlayerData> getPlayers();

    PlayerData getInformationById(UUID id);

}