package com.denfop.api.gasvein;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

import java.util.Objects;

public class GasVein implements IGasVein {

    private final ChunkPos chunk;
    boolean find;
    private TypeGas type;
    private int col;
    private int maxcol;

    public GasVein(TypeGas type, ChunkPos chunk) {
        this.type = type;
        this.chunk = chunk;
        this.col = 0;
        this.maxcol = 0;
    }

    public GasVein(NBTTagCompound tagCompound) {

        this.type = TypeGas.values()[tagCompound.getByte("type")];
        this.chunk = new ChunkPos(tagCompound.getShort("x"), tagCompound.getShort("z"));
        this.col = tagCompound.getInteger("col");
        this.maxcol = tagCompound.getInteger("maxcol");
        this.find = tagCompound.getBoolean("find");
        if (!this.find) {
            if (this.col != this.maxcol) {
                this.find = true;
            }
        }
    }


    public GasVein(CustomPacketBuffer is) {
        this.find = is.readBoolean();
        this.chunk = new ChunkPos(is.readShort(), is.readShort());
        this.type = TypeGas.values()[is.readByte()];
        this.col = is.readInt();
        this.maxcol = is.readInt();
        if (!this.find) {
            if (this.col != this.maxcol) {
                this.find = true;
            }
        }
    }

    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(32);
        customPacketBuffer.writeBoolean(find);
        customPacketBuffer.writeShort(chunk.x);
        customPacketBuffer.writeShort(chunk.z);
        customPacketBuffer.writeByte(this.type.ordinal());
        customPacketBuffer.writeInt(col);
        customPacketBuffer.writeInt(maxcol);
        return customPacketBuffer;
    }

    @Override
    public TypeGas getType() {
        return this.type;
    }

    @Override
    public void setType(final TypeGas type) {
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

    @Override
    public NBTTagCompound writeTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setBoolean("find", find);
        tagCompound.setShort("x", (short) chunk.x);
        tagCompound.setShort("z", (short) chunk.z);
        tagCompound.setByte("type", (byte) type.ordinal());
        tagCompound.setInteger("col", this.col);
        tagCompound.setInteger("maxcol", this.maxcol);

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
        GasVein vein = (GasVein) o;
        return vein.chunk.equals(this.chunk);

    }

    @Override
    public int hashCode() {
        return Objects.hash(chunk, find, type, col, maxcol);
    }


}
