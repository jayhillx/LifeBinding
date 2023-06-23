package com.lifebinding;

import com.lifebinding.capability.BindingCapabilities;
import com.lifebinding.event.BindingEvents;
import com.lifebinding.init.BindingEffects;
import com.lifebinding.init.BindingPotions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lifebinding")
public class LifeBinding {
    public static final String id = "lifebinding";

    public static ResourceLocation location(String path) {
        return new ResourceLocation(id, path);
    }

    public LifeBinding() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        BindingEffects.EFFECTS.register(bus);
        BindingPotions.POTIONS.register(bus);

        MinecraftForge.EVENT_BUS.register(new BindingCapabilities());
        MinecraftForge.EVENT_BUS.register(new BindingEvents());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        BindingCapabilities.registerCapabilities();
        BindingPotions.registerRecipes();
    }

}