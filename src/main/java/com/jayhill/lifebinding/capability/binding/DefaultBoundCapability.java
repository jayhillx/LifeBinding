package com.jayhill.lifebinding.capability.binding;

public class DefaultBoundCapability implements IBoundCapability {

    private boolean isBound = false;

    public boolean isBound() {
        return this.isBound;
    }

    public void setBound(boolean bound) {
        this.isBound = bound;
    }

    public void copyForRespawn(DefaultBoundCapability deadPlayer) {
        this.isBound = deadPlayer.isBound;
    }

}