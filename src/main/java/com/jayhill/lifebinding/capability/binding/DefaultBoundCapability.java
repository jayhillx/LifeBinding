package com.jayhill.lifebinding.capability.binding;

import java.util.UUID;

public class DefaultBoundCapability implements IBoundCapability {

    private UUID uuid = null;
    private String name = null;

    private boolean isBound;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void copyForRespawn(IBoundCapability oldStore) {
        this.isBound = oldStore.isBound();
        this.uuid = oldStore.getUUID();
        this.name = oldStore.getName();
    }

}