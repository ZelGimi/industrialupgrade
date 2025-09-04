package com.denfop.api.vein.common;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

import java.util.Objects;

public class VeinBase implements Vein {

    private final ChunkPos chunk;
    boolean find;
    private boolean oldMineral;
    private Type type;
    private int meta;
    private int col;
    private int maxcol;

    public VeinBase(Type type, int meta, ChunkPos chunk) {
        this.type = type;
        this.meta = meta;
        this.chunk = chunk;
        this.col = 0;
        this.maxcol = 0;
        oldMineral = false;
    }

    public VeinBase(CompoundTag tagCompound) {
        int data = tagCompound.getInt("data");
        int z = data & 2047;
        data = data >> 11;
        int x = data & 2047;
        data = data >> 11;
        int sign = data & 1;
        if (sign == 0) {
            z *= -1;
        }
        data = data >> 1;
        int sign1 = data & 1;
        if (sign1 == 0) {
            x *= -1;
        }
        data = data >> 1;
        int find = data & 1;
        data = data >> 1;
        int type = data & 3;
        data = data >> 2;
        this.meta = data & 15;
        this.type = Type.getID(type);
        this.chunk = new ChunkPos(x, z);
        this.col = tagCompound.getInt("col");
        this.maxcol = tagCompound.getInt("maxcol");
        this.find = find == 1;
        if (!this.find) {
            if (this.col != this.maxcol) {
                this.find = true;
            }
        }
        int data1 = tagCompound.getInt("data1");
        this.oldMineral = data1 == 0;
    }


    public VeinBase(CustomPacketBuffer is) {
        int data = is.readInt();
        int z = data & 2047;
        data = data >> 11;
        int x = data & 2047;
        data = data >> 11;
        int sign = data & 1;
        if (sign == 0) {
            z *= -1;
        }
        data = data >> 1;
        int sign1 = data & 1;
        if (sign1 == 0) {
            x *= -1;
        }
        data = data >> 1;
        int find = data & 1;
        data = data >> 1;
        int type = data & 3;
        data = data >> 2;
        this.meta = data & 15;
        this.type = Type.getID(type);
        this.chunk = new ChunkPos(x, z);
        this.col = is.readInt();
        this.maxcol = is.readInt();
        this.find = find == 1;
        if (!this.find) {
            if (this.col != this.maxcol) {
                this.find = true;
            }
        }
        int data2 = is.readInt();
        this.oldMineral = data2 == 0;
    }

    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(32);

        int m = 0;
        m += this.meta;
        m = (m << 2) + this.type.ordinal();
        m = (m << 1) + (this.find ? 1 : 0);
        boolean k = chunk.x >= 0;
        boolean k1 = chunk.z >= 0;
        m = (m << 1) + (k ? 1 : 0);
        m = (m << 1) + (k1 ? 1 : 0);
        int x = Math.min(Math.abs(chunk.x), 2047);
        int z = Math.min(Math.abs(chunk.z), 2047);
        m = (m << 11) + x;
        m = (m << 11) + z;
        customPacketBuffer.writeInt(m);
        customPacketBuffer.writeInt(col);
        customPacketBuffer.writeInt(maxcol);
        customPacketBuffer.writeInt(this.oldMineral ? 0 : 1);
        return customPacketBuffer;
    }

    @Override
    public int getMeta() {
        return this.meta;
    }

    @Override
    public void setMeta(final int meta) {
        if (oldMineral) {
            this.meta = meta;
        } else {
            this.meta = meta - 16;
        }
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public void setType(final Type type) {
        this.type = type;
    }

    @Override
    public ChunkPos getChunk() {
        return this.chunk;
    }

    @Override
    public int getCol() {
        return this.col;
    }

    @Override
    public void setCol(final int col) {
        this.col = col;
    }

    @Override
    public int getMaxCol() {
        return this.maxcol;
    }

    @Override
    public void setMaxCol(int maxcol) {
        this.maxcol = maxcol;
    }

    @Override
    public void removeCol(int col) {
        assert this.col - col >= 0;
        this.col -= col;

    }

    public boolean isFind() {
        return find;
    }

    @Override
    public void setFind(final boolean find) {
        this.find = find;
    }

    public boolean isOldMineral() {
        return oldMineral;
    }

    public void setOldMineral(final boolean oldMineral) {
        this.oldMineral = oldMineral;
    }

    @Override
    public boolean canMining() {
        return this.type == Type.EMPTY || (this.col == 0 && this.maxcol != 0);
    }

    @Override
    public CompoundTag writeTag() {
        CompoundTag tagCompound = new CompoundTag();

        int m = 0;
        m += this.meta;
        m = (m << 2) + this.type.ordinal();
        m = (m << 1) + (this.find ? 1 : 0);
        boolean k = chunk.x >= 0;
        boolean k1 = chunk.z >= 0;
        m = (m << 1) + (k ? 1 : 0);
        m = (m << 1) + (k1 ? 1 : 0);
        int x = Math.min(Math.abs(chunk.x), 2047);
        int z = Math.min(Math.abs(chunk.z), 2047);
        m = (m << 11) + x;
        m = (m << 11) + z;
        tagCompound.putInt("data", m);
        tagCompound.putInt("col", this.col);
        tagCompound.putInt("maxcol", this.maxcol);
        tagCompound.putInt("data1", this.oldMineral ? 0 : 1);

        return tagCompound;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VeinBase vein = (VeinBase) o;
        return vein.chunk.equals(this.chunk);

    }

    @Override
    public int hashCode() {
        return Objects.hash(chunk, find, type, meta, col, maxcol);
    }

    @Override
    public boolean get() {
        return this.find;
    }


}
