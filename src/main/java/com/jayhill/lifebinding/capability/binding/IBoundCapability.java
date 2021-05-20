package com.jayhill.lifebinding.capability.binding;

public interface IBoundCapability {

    void setBoundPlayer(String[] var1);

    String[] getBoundPlayer();

    void copyForRespawn(DefaultBoundCapability var1);

}