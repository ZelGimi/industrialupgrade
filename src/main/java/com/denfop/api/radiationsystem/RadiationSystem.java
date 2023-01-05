package com.denfop.api.radiationsystem;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadiationSystem {

    public static RadiationSystem rad_system;
    public List<Radiation> radiationList = new ArrayList<>();
    Map<ChunkPos, Radiation> map = new HashMap<>();

    public RadiationSystem() {
        rad_system = this;
        FMLCommonHandler.instance().bus().register(new EventHandler());
    }

    public void work(EntityPlayer player) {
        if (player.getEntityWorld().provider.getDimension() != 0) {
            return;
        }
        ChunkPos pos = player.getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getPos();
        if (this.map.containsKey(pos)) {
            Radiation rad = this.map.get(pos);
            rad.process(player);
        }
    }

    public Map<ChunkPos, Radiation> getMap() {
        return map;
    }

    public List<Radiation> getRadiationList() {
        return radiationList;
    }

    public void addRadiation(NBTTagCompound tagCompound) {
        Radiation rad = new Radiation(tagCompound);
        if (!this.map.containsKey(rad.getPos())) {
            this.map.put(rad.getPos(), rad);
            this.radiationList.add(rad);
        }
    }

    public void addRadiation(Radiation radiation) {
        if (!this.map.containsKey(radiation.getPos())) {
            this.map.put(radiation.getPos(), radiation);
            this.radiationList.add(radiation);
            IUCore.network.get(true).initiateRadiation(radiation);
        } else {
            Radiation rad = this.map.get(radiation.getPos());
        }

    }

    public void update(EntityPlayer player) {
        IUCore.network.get(true).initiateRadiation(this.radiationList, player);
    }

}
