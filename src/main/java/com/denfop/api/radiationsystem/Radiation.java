package com.denfop.api.radiationsystem;


import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketRadiationUpdateValue;
import com.denfop.network.packet.PacketUpdateRadiation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Random;

public class Radiation {

    private final ChunkPos pos;
    private static final Random rand = new Random();
    private double radiation;
    private EnumLevelRadiation level;
    private EnumCoefficient coef;

    public Radiation(ChunkPos pos) {
        this.radiation = 0;
        this.level = EnumLevelRadiation.LOW;
        this.coef = EnumCoefficient.NANO;
        this.pos = pos;
    }

    public Radiation(CompoundTag tagCompound) {
        this.radiation = tagCompound.getDouble("radiation");
        this.level = EnumLevelRadiation.values()[tagCompound.getByte("level")];
        this.coef = EnumCoefficient.values()[tagCompound.getByte("coef")];
        this.pos = new ChunkPos(tagCompound.getInt("x"), tagCompound.getInt("z"));
    }

    public Radiation(CustomPacketBuffer packetBuffer) {
        this.radiation = packetBuffer.readShort() / 32.5D;
        byte levelAndCoef = packetBuffer.readByte();
        this.level = EnumLevelRadiation.values()[levelAndCoef & 0x07];
        this.coef = EnumCoefficient.values()[(levelAndCoef >> 3) & 0x07];
        this.pos = new ChunkPos(packetBuffer.readShort(), packetBuffer.readShort());
    }

    public CustomPacketBuffer writePacket(CustomPacketBuffer os) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(os.registryAccess());
        customPacketBuffer.writeShort((short) (this.radiation * 32.5));
        byte levelAndCoef = (byte) ((this.level.ordinal() & 0x07) | ((this.coef.ordinal() & 0x07) << 3));
        customPacketBuffer.writeByte(levelAndCoef);
        customPacketBuffer.writeShort((short) this.pos.x);
        customPacketBuffer.writeShort((short) this.pos.z);
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

    public boolean removeRadiationWithType(double radiation, Level level) {
        boolean removed;

        if (this.level == EnumLevelRadiation.LOW) {
            if (this.radiation == 999 && radiation == 1000) {
                this.radiation = 0;
                new PacketUpdateRadiation(this, (ServerLevel) level);
                return true;
            }
            if (this.radiation < radiation) {
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
            if (this.level != EnumLevelRadiation.LOW) {
                this.radiation = 1000;
            } else {
                this.radiation = 0;
            }
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
            radiation = 0;
            removed = true;
        }
        new PacketUpdateRadiation(this, (ServerLevel) level);
        return removed;

    }

    public void removeRadiation(double radiation) {
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
                radiation = 0;
            }
        }
    }

    public void process(Player player) {
        boolean need = IHazmatLike.hasCompleteHazmat(player, this.level);
        final CompoundTag nbt = player.getPersistentData();
        double radiation = nbt.getDouble("radiation");
        if (!need) {
            switch (this.level) {
                case MEDIUM:
                    int num = rand.nextInt(4);
                    nbt.putDouble("radiation", radiation + 0.02f);
                    new PacketRadiationUpdateValue(player, radiation + 0.02);
                    switch (num) {
                        case 0:
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
                            break;
                        case 1:
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
                            break;
                        case 2:
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
                            break;
                        case 3:
                            player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                            break;
                    }
                    break;
                case HIGH:
                    num = rand.nextInt(4);
                    nbt.putDouble("radiation", radiation + 0.02f);
                    new PacketRadiationUpdateValue(player, radiation + 0.2);
                    switch (num) {
                        case 0:
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
                            break;
                        case 1:
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
                            break;
                        case 2:
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
                            break;
                        case 3:
                            player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                            break;
                    }
                    player.addEffect(new MobEffectInstance(IUPotion.rad, 200, 0));
                    break;
                case VERY_HIGH:
                    num = rand.nextInt(4);
                    nbt.putDouble("radiation", radiation + 0.02f);
                    new PacketRadiationUpdateValue(player, radiation + 2);
                    switch (num) {
                        case 0:
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
                            break;
                        case 1:
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
                            break;
                        case 2:
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
                            break;
                        case 3:
                            player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                            break;
                    }
                    player.addEffect(new MobEffectInstance(IUPotion.rad, 43200, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 0));
                    break;
                default:
                    break;
            }
        }
    }

    public CompoundTag writeCompound() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("radiation", this.radiation);
        tag.putByte("level", (byte) this.level.ordinal());
        tag.putByte("coef", (byte) this.coef.ordinal());
        tag.putInt("x", this.pos.x);
        tag.putInt("z", this.pos.z);
        return tag;
    }

}
