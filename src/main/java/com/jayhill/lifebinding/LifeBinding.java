package com.jayhill.lifebinding;

import com.jayhill.lifebinding.capability.binding.BindingCapabilities;
import com.jayhill.lifebinding.capability.network.PacketHandler;
import com.jayhill.lifebinding.events.AttachCapabilityEvent;
import com.jayhill.lifebinding.events.LifeBindingEvents;
import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("lifebinding")
public class LifeBinding {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "lifebinding";

    public static LifeBinding INSTANCE;

    public LifeBinding() {
        INSTANCE = this;

        PacketHandler packetHandler = new PacketHandler();
        packetHandler.registerMessages();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        LifeBindingPotion.EFFECTS.register(modEventBus);
        LifeBindingPotion.POTIONS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new LifeBindingEvents());
        MinecraftForge.EVENT_BUS.register(new AttachCapabilityEvent());
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

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}