package com.jayhill.lifebinding.events;

import com.jayhill.lifebinding.potions.LifeBindingPotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LifeBindingEvents {

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            for (ServerPlayerEntity serverPlayerEntity : player.getServer().getPlayerList().getPlayers()) {
                if (serverPlayerEntity.getGameProfile().getId() != player.getGameProfile().getId()) {

                    serverPlayerEntity.addPotionEffect(new EffectInstance(LifeBindingPotion.LIFE_DAMAGING_EFFECT.get(), 70000, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        PlayerEntity player = (PlayerEntity)event.player.getEntity();
        if (event.phase == Phase.START && event.side.isServer()) {

        }
    }

}