package com.jayhill.lifebinding;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.events.BindingEvents;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.Entity;
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

        LifeBindingPotion.EFFECTS.register(modEventBus);
        LifeBindingPotion.POTIONS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new BindingEvents());
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        LifeBindingPotion.addPotionRecipes();

        BindingCapabilities.registerCapabilities();
    }

    public static final ItemGroup TAB = new ItemGroup("lifebindingTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.BONE);
        }
    };

}