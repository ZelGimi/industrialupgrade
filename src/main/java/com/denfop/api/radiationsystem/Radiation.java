package com.denfop.api.radiationsystem;


import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateRadiation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.ChunkPos;

import java.util.Random;

public class Radiation {

    private final ChunkPos pos;
    private final Random rand = new Random();
    private double radiation;
    private EnumLevelRadiation level;
    private EnumCoefficient coef;

    public Radiation(ChunkPos pos) {
        this.radiation = 0;
        this.level = EnumLevelRadiation.LOW;
        this.coef = EnumCoefficient.NANO;
        this.pos = pos;
    }

    public Radiation(NBTTagCompound tagCompound) {
        this.radiation = tagCompound.getDouble("radiation");
        this.level = EnumLevelRadiation.values()[tagCompound.getByte("level")];
        this.coef = EnumCoefficient.values()[tagCompound.getByte("coef")];
        this.pos = new ChunkPos(tagCompound.getInteger("x"), tagCompound.getInteger("z"));
    }

    public Radiation(CustomPacketBuffer packetBuffer) {
        this.radiation = packetBuffer.readDouble();
        this.level = EnumLevelRadiation.values()[packetBuffer.readInt()];
        this.coef = EnumCoefficient.values()[packetBuffer.readInt()];
        this.pos = new ChunkPos(packetBuffer.readInt(), packetBuffer.readInt());
    }

    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeDouble(this.radiation);
        customPacketBuffer.writeInt(this.level.ordinal());
        customPacketBuffer.writeInt(this.coef.ordinal());
        customPacketBuffer.writeInt(this.pos.x);
        customPacketBuffer.writeInt(this.pos.z);
        return customPacketBuffer;
    }

    public ChunkPos getPos() {
        return pos;
    }

    public EnumCoefficient getCoef() {
        return coef;
    }

    public void setCoef(final EnumCoefficient coef) {
        this.coef = coef;
    }

    public EnumLevelRadiation getLevel() {
        return level;
    }

    public void setLevel(final EnumLevelRadiation level) {
        this.level = level;
    }

    public double getRadiation() {
        return radiation;
    }

    public void setRadiation(final double radiation) {
        this.radiation = radiation;
    }

    public boolean removeRadiationWithType(double radiation){
        boolean removed;

            if (this.level == EnumLevelRadiation.LOW) {
                if(this.radiation == 999 && radiation== 1000){
                    this.radiation = 0;
                    new PacketUpdateRadiation(this);
                    return true;
                }
              if(this.radiation < radiation){
                  return false;
              }
            }
            if (this.level == EnumLevelRadiation.DEFAULT) {
                radiation /= 10;
            }
            if (this.level == EnumLevelRadiation.MEDIUM) {
                radiation /= 100;
            }
            if (this.level == EnumLevelRadiation.HIGH) {
                radiation /= 1000;
            }
            if (this.level == EnumLevelRadiation.VERY_HIGH) {
                radiation /= 10000;
            }
            if (this.radiation - radiation == 0) {
                if(this.level != EnumLevelRadiation.LOW)
                this.radiation = 1000;
                else
                    this.radiation = 0;
                this.level = EnumLevelRadiation.values()[Math.max(
                        this.level.ordinal() - 1,
                        0
                )];
                this.coef = EnumCoefficient.values()[Math.max(this.coef.ordinal() - 1, 0)];
                removed = true;
            } else if (this.radiation - radiation < 0) {
                this.radiation = 1000 - (radiation - this.radiation);
                this.level = EnumLevelRadiation.values()[Math.max(
                        this.level.ordinal() - 1,
                        0
                )];
                this.coef = EnumCoefficient.values()[Math.max(this.coef.ordinal() - 1, 0)];
                removed = true;
            } else {
                this.radiation -= radiation;
                removed = true;
            }
        new PacketUpdateRadiation(this);
        return removed;

    }
    public void removeRadiation(double radiation){
        while (radiation > 0 && !(this.radiation == 0 && this.getLevel() == EnumLevelRadiation.LOW)) {
            if (this.radiation - radiation == 0) {
                radiation = 0;
                this.radiation = 1000;
                this.level = EnumLevelRadiation.values()[Math.max(
                        this.level.ordinal() - 1,
                        0
                )];
                this.coef = EnumCoefficient.values()[Math.max(this.coef.ordinal() - 1, 0)];
            } else if (this.radiation - radiation < 0) {
                this.radiation = 1000;
                radiation -= this.radiation;
                this.level = EnumLevelRadiation.values()[Math.max(
                        this.level.ordinal() - 1,
                        0
                )];
                this.coef = EnumCoefficient.values()[Math.max(this.coef.ordinal() - 1, 0)];
            } else {
                this.radiation -= radiation;
                radiation = 0;
            }
        }
    }

    public void addRadiation(double radiation) {
        while (radiation > 0) {

            if (this.level == EnumLevelRadiation.DEFAULT) {
                radiation /= 10;
            }
            if (this.level == EnumLevelRadiation.MEDIUM) {
                radiation /= 100;
            }
            if (this.level == EnumLevelRadiation.HIGH) {
                radiation /= 1000;
            }
            if (this.level == EnumLevelRadiation.VERY_HIGH) {
                radiation /= 10000;
            }
            if (this.radiation + radiation == 1000) {
                radiation = 0;
                this.level = EnumLevelRadiation.values()[Math.min(
                        this.level.ordinal() + 1,
                        EnumLevelRadiation.values().length - 1
                )];
                this.coef = EnumCoefficient.values()[Math.min(this.coef.ordinal() + 1, EnumCoefficient.values().length - 1)];
            } else if (this.radiation + radiation > 1000) {
                this.radiation = 0;
                radiation -= (1000 - this.radiation);
                this.level = EnumLevelRadiation.values()[Math.min(
                        this.level.ordinal() + 1,
                        EnumLevelRadiation.values().length - 1
                )];
                this.coef = EnumCoefficient.values()[Math.min(this.coef.ordinal() + 1, EnumCoefficient.values().length - 1)];
            } else {
                this.radiation += radiation;
            }
        }
    }

    public void process(EntityPlayer player) {
        boolean need = IHazmatLike.hasCompleteHazmat(player, this.level);
        if (!need) {
            switch (this.level) {
                case MEDIUM:
                    int num = rand.nextInt(4);
                    switch (num) {
                        case 0:
                            player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 0));
                            break;
                        case 1:
                            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
                            break;
                        case 2:
                            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 0));
                            break;
                        case 3:
                            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0));
                            break;
                    }
                    break;
                case HIGH:
                    num = rand.nextInt(4);
                    switch (num) {
                        case 0:
                            player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 0));
                            break;
                        case 1:
                            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
                            break;
                        case 2:
                            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 0));
                            break;
                        case 3:
                            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0));
                            break;
                    }
                    player.addPotionEffect(new PotionEffect(IUPotion.radiation, 200, 0));
                    break;
                case VERY_HIGH:
                    num = rand.nextInt(4);
                    switch (num) {
                        case 0:
                            player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 0));
                            break;
                        case 1:
                            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
                            break;
                        case 2:
                            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 0));
                            break;
                        case 3:
                            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0));
                            break;
                    }
                    player.addPotionEffect(new PotionEffect(IUPotion.radiation, 43200, 0));
                    player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 400, 0));
                    break;
                default:
                    break;
            }
        }
    }

    public NBTTagCompound writeCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("radiation", this.radiation);
        tag.setByte("level", (byte) this.level.ordinal());
        tag.setByte("coef", (byte) this.coef.ordinal());
        tag.setInteger("x", this.pos.x);
        tag.setInteger("z", this.pos.z);
        return tag;
    }

}
