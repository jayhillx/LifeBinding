package com.jayhill.lifebinding;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.binding.BindingEvents;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lifebinding")
public class LifeBinding {
    public static final String MOD_ID = "lifebinding";

    public LifeBinding() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        LifeBindingPotion.POTIONS.register(modEventBus);
        LifeBindingPotion.EFFECTS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new BindingEvents());
        MinecraftForge.EVENT_BUS.register(this);
    }

    /** Registers recipes & capabilities. */
    private void setup(final FMLCommonSetupEvent event) {
        LifeBindingPotion.addPotionRecipes();
        BindingCapabilities.registerCapabilities();
    }

    public static final ItemGroup TAB = new ItemGroup("life_binding") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.BONE);
        }
    };

}