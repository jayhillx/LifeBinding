package com.lifebinding.capability.binding;

import com.lifebinding.capability.data.PlayerData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.UUID;

public class BindingCapability {
    @CapabilityInject(IBoundPlayers.class)
    public static Capability<IBoundPlayers> BOUND;

    public static class Storage implements Capability.IStorage<IBoundPlayers> {
        public INBT writeNBT(Capability<IBoundPlayers> capability, IBoundPlayers bound, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            ListNBT list = new ListNBT();
            for (PlayerData data : bound.getPlayers().values()) {
                list.add(data.write(new CompoundNBT()));
            }
            nbt.put("BoundEntities", list);

            ListNBT list2 = new ListNBT();
            for (UUID id : bound.getDeathList()) {
                list2.add(new StringNBT(id.toString()));
            }
            nbt.put("OfflineDeaths", list2);
            return nbt;
        }

        public void readNBT(Capability<IBoundPlayers> capability, IBoundPlayers bound, Direction side, INBT inbt) {
            CompoundNBT nbt = ((CompoundNBT)inbt);

            ListNBT list = nbt.getList("BoundEntities", 10);
            for (int i = 0; i < list.size(); ++i) {
                PlayerData data = new PlayerData(list.getCompound(i));
                bound.getPlayers().put(UUID.fromString(data.getId()), data);
            }

            ListNBT list2 = nbt.getList("OfflineDeaths", 8);
            for (int i = 0; i < list2.size(); ++i) {
                bound.getDeathList().add(UUID.fromString(list2.getString(i)));
            }
        }
    }
    
}