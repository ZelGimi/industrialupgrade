package com.denfop.api.pollution;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.INetworkObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public class ChunkLevel implements INetworkObject {

    private ChunkPos pos;
    private ChunkPos defaultPos;
    private LevelPollution levelPollution;
    private double pollution;

    public ChunkLevel(ChunkPos pos, LevelPollution levelPollution, double pollution) {
        this.pos = pos;
        this.defaultPos = pos;
        this.levelPollution = levelPollution;
        this.pollution = pollution;
    }

    public ChunkLevel(CompoundTag tagCompound) {
        this.pollution = tagCompound.getDouble("pollution");
        this.levelPollution = LevelPollution.values()[tagCompound.getByte("level")];
        this.pos = new ChunkPos(tagCompound.getInt("x"), tagCompound.getInt("z"));
        this.defaultPos = new ChunkPos(tagCompound.getInt("x1"), tagCompound.getInt("z1"));

    }

    public ChunkLevel(CustomPacketBuffer packetBuffer) {
        this.pollution = packetBuffer.readDouble();
        this.levelPollution = LevelPollution.values()[packetBuffer.readInt()];
        this.pos = new ChunkPos(packetBuffer.readInt(), packetBuffer.readInt());
        this.defaultPos = new ChunkPos(packetBuffer.readInt(), packetBuffer.readInt());
    }

    public CompoundTag writeCompound() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("pollution", this.pollution);
        tag.putByte("level", (byte) this.levelPollution.ordinal());
        tag.putInt("x", this.pos.x);
        tag.putInt("z", this.pos.z);
        tag.putInt("x1", this.defaultPos.x);
        tag.putInt("z1", this.defaultPos.z);
        return tag;
    }

    public CustomPacketBuffer writePacket(CustomPacketBuffer o) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(o.registryAccess());
        customPacketBuffer.writeDouble(this.pollution);
        customPacketBuffer.writeInt(this.levelPollution.ordinal());
        customPacketBuffer.writeInt(this.pos.x);
        customPacketBuffer.writeInt(this.pos.z);
        customPacketBuffer.writeInt(this.defaultPos.x);
        customPacketBuffer.writeInt(this.defaultPos.z);
        return customPacketBuffer;
    }

    public ChunkPos getDefaultPos() {
        return defaultPos;
    }

    public void setDefaultPos(final ChunkPos defaultPos) {
        this.defaultPos = defaultPos;
    }

    public ChunkPos getPos() {
        return pos;
    }

    public void setPos(final ChunkPos pos) {
        this.pos = pos;
    }

    public LevelPollution getLevelPollution() {
        return levelPollution;
    }

    public void setLevelPollution(final LevelPollution levelPollution) {
        this.levelPollution = levelPollution;
    }

    public double getPollution() {
        return pollution;
    }

    public void setPollution(final double pollution) {
        this.pollution = pollution;
    }

    public void addChunkPos(int x, int z) {
        this.pos = new ChunkPos(this.pos.x + x, this.pos.z + z);
    }


    public boolean removePollution(double pollution) {
        boolean removed;

        if (this.levelPollution == LevelPollution.VERY_LOW) {
            if (this.pollution == 0)
                return false;
            if (this.pollution == 124 && pollution == 125) {
                this.pollution = 0;
                return true;
            }
            if (this.pollution < pollution) {
                this.pollution = 0;
                return false;
            }
        }
        if (this.levelPollution == LevelPollution.LOW) {
            pollution /= 10;
        }
        if (this.levelPollution == LevelPollution.MEDIUM) {
            pollution /= 100;
        }
        if (this.levelPollution == LevelPollution.HIGH) {
            pollution /= 1000;
        }
        if (this.levelPollution == LevelPollution.VERY_HIGH) {
            pollution /= 10000;
        }
        if (this.pollution - pollution == 0) {
            if (this.levelPollution != LevelPollution.VERY_LOW) {
                this.pollution = 125;
            } else {
                this.pollution = 0;
            }
            this.levelPollution = LevelPollution.values()[Math.max(
                    this.levelPollution.ordinal() - 1,
                    0
            )];
            removed = true;
        } else if (this.pollution - pollution < 0) {
            this.pollution = 125 - (pollution - this.pollution);
            this.levelPollution = LevelPollution.values()[Math.max(
                    this.levelPollution.ordinal() - 1,
                    0
            )];
            removed = true;
        } else {
            this.pollution -= pollution;
            pollution = 0;
            removed = true;
        }
        return removed;

    }

    public void addPollution(double pollution) {
        while (pollution > 0) {

            if (this.levelPollution == LevelPollution.LOW) {
                pollution /= 10;
            }
            if (this.levelPollution == LevelPollution.MEDIUM) {
                pollution /= 100;
            }
            if (this.levelPollution == LevelPollution.HIGH) {
                pollution /= 1000;
            }
            if (this.levelPollution == LevelPollution.VERY_HIGH) {
                pollution /= 10000;
            }
            if (this.pollution + pollution == 125) {
                pollution = 0;
                this.levelPollution = LevelPollution.values()[Math.min(
                        this.levelPollution.ordinal() + 1,
                        LevelPollution.values().length - 1
                )];
            } else if (this.pollution + pollution > 125) {
                this.pollution = 0;
                pollution -= (125 - this.pollution);
                this.levelPollution = LevelPollution.values()[Math.min(
                        this.levelPollution.ordinal() + 1,
                        LevelPollution.values().length - 1
                )];
            } else {
                this.pollution += pollution;
                pollution = 0;
            }
        }
    }

}
