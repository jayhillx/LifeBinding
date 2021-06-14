package com.jayhill.lifebinding.capability.binding;

import java.util.UUID;

public interface IBoundCapability {

    boolean isBound();

    void setBound(boolean bound);

    void setUUID(UUID uuid);

    void setName(String name);

    UUID getUUID();

    String getName();

    void copyForRespawn(IBoundCapability var1);

}