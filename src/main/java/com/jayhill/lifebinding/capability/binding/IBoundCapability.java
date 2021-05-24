package com.jayhill.lifebinding.capability.binding;

import java.util.UUID;

public interface IBoundCapability {

    boolean isBound();

    void setBound(boolean bound);

    void setUUID(UUID var1);

    UUID getUUID();

    void copyForRespawn(IBoundCapability var1);

}