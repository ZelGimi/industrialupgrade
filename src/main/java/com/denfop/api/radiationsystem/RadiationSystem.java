package com.denfop.api.radiationsystem;

import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.network.packet.PacketRadiation;
import com.denfop.network.packet.PacketUpdateRadiation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RadiationSystem {

    public static RadiationSystem rad_system;
    public List<Radiation> radiationList = new LinkedList<>();
    Map<ChunkPos, Radiation> map = new HashMap<>();

    Map<ChunkPos, List<IAdvReactor>> iAdvReactorMap = new HashMap<>();

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

        if (player.getEntityWorld().getWorldTime() % 200 == 0) {
            ChunkPos pos = new ChunkPos(player.getPosition());
            Radiation rad = this.map.get(pos);
            if (rad != null) {

                if (!IHazmatLike.hasCompleteHazmat(player, rad.getLevel())) {
                    rad.process(player);
                }
            } else {
                final NBTTagCompound nbt = player.getEntityData();
                double radiation = nbt.getDouble("radiation");
                if (radiation >= 50) {
                    player.addPotionEffect(new PotionEffect(IUPotion.radiation, 43200, 0));
                    player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 400, 0));
                } else if (radiation >= 10) {
                    player.addPotionEffect(new PotionEffect(IUPotion.radiation, 1000, 0));
                    player.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0));
                } else if (radiation >= 1) {
                    player.addPotionEffect(new PotionEffect(IUPotion.radiation, 200, 0));
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 0));
                }
            }
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
        this.map.put(rad.getPos(), rad);
        this.radiationList.add(rad);
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

    public void clear() {
        this.map.clear();
        this.radiationList.clear();
    }

    public void addRadiationWihoutUpdate(Radiation radiation) {
        if (!this.map.containsKey(radiation.getPos())) {
            this.map.put(radiation.getPos(), radiation);
            this.radiationList.add(radiation);
        }
    }

    public void workDecay(final World world) {
        try {
            for (Radiation radiation : this.radiationList) {
                if (radiation.getRadiation() > 0) {
                    switch (radiation.getLevel()) {
                        case LOW:
                            if (world.provider.getWorldTime() % 36000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case DEFAULT:
                            if (world.provider.getWorldTime() % 18000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case MEDIUM:
                            if (world.provider.getWorldTime() % 12000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case HIGH:
                            if (world.provider.getWorldTime() % 6000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case VERY_HIGH:
                            if (world.provider.getWorldTime() % 2400 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

}
