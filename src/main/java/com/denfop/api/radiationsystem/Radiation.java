package com.denfop.api.radiationsystem;


import com.denfop.IUPotion;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.ChunkPos;

import java.util.Random;

public class Radiation {

    private final ChunkPos pos;
    private final Random rand = new Random();
    private int radiation;
    private EnumLevelRadiation level;
    private EnumCoefficient coef;

    public Radiation(ChunkPos pos) {
        this.radiation = 2;
        this.level = EnumLevelRadiation.LOW;
        this.coef = EnumCoefficient.MICRO;
        this.pos = pos;
    }

    public Radiation(NBTTagCompound tagCompound) {
        this.radiation = tagCompound.getInteger("radiation");
        this.level = EnumLevelRadiation.values()[tagCompound.getByte("level")];
        this.coef = EnumCoefficient.values()[tagCompound.getByte("coef")];
        this.pos = new ChunkPos(tagCompound.getInteger("x"), tagCompound.getInteger("z"));
    }
    public Radiation(CustomPacketBuffer packetBuffer) {
        this.radiation = packetBuffer.readInt();
        this.level = EnumLevelRadiation.values()[packetBuffer.readInt()];
        this.coef = EnumCoefficient.values()[packetBuffer.readInt()];
        this.pos = new ChunkPos(packetBuffer.readInt(), packetBuffer.readInt());
    }
    public CustomPacketBuffer writePacket(){
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeInt(this.radiation);
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

    public int getRadiation() {
        return radiation;
    }

    public void setRadiation(final int radiation) {
        this.radiation = radiation;
    }

    public void addRadiation(int radiation) {
        this.radiation += radiation;
        while (this.radiation >= 1000) {
            this.radiation /= 1000;
            this.level = EnumLevelRadiation.values()[Math.min(this.level.ordinal() + 1, EnumLevelRadiation.values().length)];
            this.coef = EnumCoefficient.values()[Math.min(this.coef.ordinal() + 1, EnumCoefficient.values().length)];

        }

    }

    public void process(EntityPlayer player) {
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
                player.addPotionEffect(new PotionEffect(IUPotion.radiation, 200, 0));
                break;
            case VERY_HIGH:
                player.addPotionEffect(new PotionEffect(IUPotion.radiation, 432000, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 400, 0));
                break;
            default:
                break;
        }
    }

    public NBTTagCompound writeCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("radiation", this.radiation);
        tag.setByte("level", (byte) this.level.ordinal());
        tag.setByte("coef", (byte) this.coef.ordinal());
        tag.setInteger("x", this.pos.x);
        tag.setInteger("z", this.pos.z);
        return tag;
    }

}
