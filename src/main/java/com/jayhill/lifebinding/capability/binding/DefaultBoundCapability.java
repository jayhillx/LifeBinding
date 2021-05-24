package com.jayhill.lifebinding.capability.binding;

import java.util.UUID;

public class DefaultBoundCapability implements IBoundCapability {

    private UUID uuid = null;

    private boolean isBound = false;

    public boolean isBound() {
        return this.isBound;
    }

    public void setBound(boolean bound) {
        this.isBound = bound;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void copyForRespawn(IBoundCapability oldStore) {
        this.uuid = oldStore.getUUID();
    }

}