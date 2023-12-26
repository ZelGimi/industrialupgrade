package com.denfop.api.radiationsystem;

import com.denfop.api.reactors.IAdvReactor;
import com.denfop.network.packet.PacketRadiation;
import com.denfop.network.packet.PacketUpdateRadiation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadiationSystem {

    public static RadiationSystem rad_system;
    public List<Radiation> radiationList = new ArrayList<>();
    Map<ChunkPos, Radiation> map = new HashMap<>();

    Map<ChunkPos,  List<IAdvReactor>> iAdvReactorMap = new HashMap<>();

    public RadiationSystem() {
        rad_system = this;
        FMLCommonHandler.instance().bus().register(new EventHandler());
    }

    public Map<ChunkPos, List<IAdvReactor>> getAdvReactorMap() {
        return iAdvReactorMap;
    }

    public void work(EntityPlayer player) {
        if (player.getEntityWorld().provider.getDimension() != 0 || player.getEntityWorld().isRemote) {
            return;
        }
        ChunkPos pos = player.getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getPos();
        if (player.getEntityWorld().getWorldTime() % 200 == 0 && this.map.containsKey(pos)) {
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
        }

    }

    public void update(EntityPlayer player) {
        new PacketRadiation(this.radiationList, player);
    }

    public void addRadiationWihoutUpdate(Radiation radiation) {
        if (!this.map.containsKey(radiation.getPos())) {
            this.map.put(radiation.getPos(), radiation);
            this.radiationList.add(radiation);
        }
    }

    public void workDecay(final World world) {
        for (Radiation radiation : this.radiationList) {
            if (radiation.getRadiation() > 0) {
                switch (radiation.getLevel()) {
                    case LOW:
                        if (world.provider.getWorldTime() % 36000 == 0) {
                            if(radiation.getRadiation() > 1) {
                                radiation.removeRadiation(radiation.getRadiation() / 2);
                            }else{
                                radiation.removeRadiation(radiation.getRadiation());
                            }
                            new PacketUpdateRadiation(radiation);
                        }
                        break;
                    case DEFAULT:
                        if (world.provider.getWorldTime() % 18000 == 0) {
                            if(radiation.getRadiation() > 1) {
                                radiation.removeRadiation(radiation.getRadiation() / 2);
                            }else{
                                radiation.removeRadiation(radiation.getRadiation());
                            }
                            new PacketUpdateRadiation(radiation);
                        }
                        break;
                    case MEDIUM:
                        if (world.provider.getWorldTime() % 12000 == 0) {
                             if(radiation.getRadiation() > 1) {
                                radiation.removeRadiation(radiation.getRadiation() / 2);
                            }else{
                                radiation.removeRadiation(radiation.getRadiation());
                            }
                            new PacketUpdateRadiation(radiation);
                        }
                        break;
                    case HIGH:
                        if (world.provider.getWorldTime() % 6000 == 0) {
                            if(radiation.getRadiation() > 1) {
                                radiation.removeRadiation(radiation.getRadiation() / 2);
                            }else{
                                radiation.removeRadiation(radiation.getRadiation());
                            }
                            new PacketUpdateRadiation(radiation);
                        }
                        break;
                    case VERY_HIGH:
                        if (world.provider.getWorldTime() % 2400 == 0) {
                            if(radiation.getRadiation() > 1) {
                                radiation.removeRadiation(radiation.getRadiation() / 2);
                            }else{
                                radiation.removeRadiation(radiation.getRadiation());
                            }
                            new PacketUpdateRadiation(radiation);
                        }
                        break;
                }
            }
        }
    }

}
