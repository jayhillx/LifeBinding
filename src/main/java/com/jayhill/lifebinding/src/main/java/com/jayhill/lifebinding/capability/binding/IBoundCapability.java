package com.jayhill.lifebinding.capability.binding;

public interface IBoundCapability {

    boolean isBound();

    void setBound(boolean bound);

    void copyForRespawn(DefaultBoundCapability var1);

}