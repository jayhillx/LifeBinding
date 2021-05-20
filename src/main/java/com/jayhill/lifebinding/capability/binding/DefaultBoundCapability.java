package com.jayhill.lifebinding.capability.binding;

public class DefaultBoundCapability implements IBoundCapability {

    private String[] boundPlayer = new String[]{"none"};

    public void setBoundPlayer(String[] boundPlayer) {
        this.boundPlayer = boundPlayer;
    }

    public String[] getBoundPlayer() {
        return this.boundPlayer;
    }

    public void copyForRespawn(DefaultBoundCapability oldStore) {
        this.boundPlayer = oldStore.boundPlayer;
    }

}