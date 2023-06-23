package com.lifebinding.effect;

import com.lifebinding.capability.binding.BindingCapability;
import com.lifebinding.capability.data.PlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class BindingEffect extends Effect {

    public BindingEffect() {
        super(EffectType.HARMFUL, 13315412);
    }

    public void performEffect(LivingEntity entity, int amplifier) {
        for (Entity e : entity.world.getEntitiesWithinAABBExcludingEntity(entity, entity.getBoundingBox().grow(2.0D))) {
            if (e instanceof PlayerEntity) {
                PlayerEntity nearbyPlayer = (PlayerEntity)e;

                if (nearbyPlayer.isPotionActive(this)) {
                    Set<PlayerEntity> list = new HashSet<>(2);

                    list.add(nearbyPlayer);
                    list.add((PlayerEntity)entity);

                    if (entity.getServer() != null) {
                        entity.getServer().getWorld(DimensionType.OVERWORLD).getCapability(BindingCapability.BOUND).ifPresent((bound) -> {
                            bound.getPlayers().computeIfAbsent(entity.getUniqueID(), (orNewId) -> {
                                PlayerData data = new PlayerData(new CompoundNBT());
                                data.setId(entity.getUniqueID().toString());
                                data.setName(entity.getName().getString());

                                data.getBoundPlayer().setName(nearbyPlayer.getName().getString());
                                data.getBoundPlayer().setId(nearbyPlayer.getUniqueID());
                                return data;
                            });

                            bound.getPlayers().computeIfAbsent(nearbyPlayer.getUniqueID(), (orNewId) -> {
                                PlayerData data = new PlayerData(new CompoundNBT());
                                data.setId(nearbyPlayer.getUniqueID().toString());
                                data.setName(nearbyPlayer.getName().getString());

                                data.getBoundPlayer().setName(entity.getName().getString());
                                data.getBoundPlayer().setId(entity.getUniqueID());
                                return data;
                            });

                            for (PlayerEntity players : list) {
                                players.removePotionEffect(this);

                                if (!players.world.isRemote) {
                                    players.world.playSound(null, players.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.5F, 1.0F);

                                    ((ServerWorld)players.world).spawnParticle(ParticleTypes.EXPLOSION, players.posX, players.posY, players.posZ, 24, 2.0D, 2.0D, 2.0D, 0.0D);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}