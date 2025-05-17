package com.denfop.api.radiationsystem;

import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.network.packet.PacketRadiation;
import com.denfop.network.packet.PacketUpdateRadiation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadiationSystem {

    public static RadiationSystem rad_system;
    public List<Radiation> radiationList = new ArrayList<>();
    Map<ChunkPos, Radiation> map = new HashMap<>();

    Map<ChunkPos, List<IAdvReactor>> iAdvReactorMap = new HashMap<>();

    public RadiationSystem() {
        rad_system = this;
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public Map<ChunkPos, List<IAdvReactor>> getAdvReactorMap() {
        return iAdvReactorMap;
    }

    public void work(Player player) {
        if (player.getLevel().dimension() != Level.OVERWORLD || player.getLevel().isClientSide) {
            return;
        }

        if (player.getLevel().getGameTime() % 200 == 0) {
            ChunkPos pos = new ChunkPos(new BlockPos(player.getX(), player.getY(), player.getZ()));
            Radiation rad = this.map.get(pos);
            if (rad != null) {

                if (!IHazmatLike.hasCompleteHazmat(player, rad.getLevel())) {
                    rad.process(player);
                }
            } else {
                double radiation = player.getPersistentData().getDouble("radiation");
                if (radiation >= 50) {
                    player.addEffect(new MobEffectInstance(IUPotion.radiation, 43200, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 0));
                } else if (radiation >= 10) {
                    player.addEffect(new MobEffectInstance(IUPotion.radiation, 1000, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                } else if (radiation >= 1) {
                    player.addEffect(new MobEffectInstance(IUPotion.radiation, 200, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
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

    public void addRadiation(CompoundTag tagCompound) {
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

    public void update(Player player) {
        new PacketRadiation(this.radiationList, player);
    }

    public void addRadiationWihoutUpdate(Radiation radiation) {
        if (!this.map.containsKey(radiation.getPos())) {
            this.map.put(radiation.getPos(), radiation);
            this.radiationList.add(radiation);
        }
    }

    public void workDecay(final Level world) {
        try {
            for (Radiation radiation : this.radiationList) {
                if (radiation.getRadiation() > 0) {
                    switch (radiation.getLevel()) {
                        case LOW:
                            if (world.getGameTime() % 36000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case DEFAULT:
                            if (world.getGameTime() % 18000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case MEDIUM:
                            if (world.getGameTime() % 12000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case HIGH:
                            if (world.getGameTime() % 6000 == 0) {
                                if (radiation.getRadiation() > 1) {
                                    radiation.removeRadiation(radiation.getRadiation() / 2);
                                } else {
                                    radiation.removeRadiation(radiation.getRadiation());
                                }
                                new PacketUpdateRadiation(radiation);
                            }
                            break;
                        case VERY_HIGH:
                            if (world.getGameTime() % 2400 == 0) {
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

    public void clear() {
        this.map.clear();
        this.radiationList.clear();
    }
}
