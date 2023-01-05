package com.denfop.api.vein;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

import java.util.Objects;

public class Vein implements IVein {

    private final ChunkPos chunk;
    boolean find;
    private Type type;
    private int meta;
    private int col;
    private int maxcol;

    public Vein(Type type, int meta, ChunkPos chunk) {
        this.type = type;
        this.meta = meta;
        this.chunk = chunk;
        this.col = 0;
        this.maxcol = 0;

    }

    public Vein(NBTTagCompound tagCompound) {
    /*   this.meta = tagCompound.getInteger("meta");
        this.type = Type.getID(tagCompound.getInteger("id"));
        this.chunk = new ChunkPos(tagCompound.getInteger("x"), tagCompound.getInteger("z"));
        this.col = tagCompound.getInteger("col");
        this.maxcol = tagCompound.getInteger("maxcol");
        this.find = tagCompound.getBoolean("find");
*/

        int data = tagCompound.getInteger("data");
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
        this.col = tagCompound.getInteger("col");
        this.maxcol = tagCompound.getInteger("maxcol");
        this.find = find == 1;
        if (!this.find) {
            if (this.col != this.maxcol) {
                this.find = true;
            }
        }

    }

    @Override
    public int getMeta() {
        return this.meta;
    }

    @Override
    public void setMeta(final int meta) {
        this.meta = meta;
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

    @Override
    public boolean canMining() {
        return this.type == Type.EMPTY || (this.col == 0 && this.maxcol != 0);
    }

    @Override
    public NBTTagCompound writeTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();

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
        tagCompound.setInteger("data", m);
        tagCompound.setInteger("col", this.col);
        tagCompound.setInteger("maxcol", this.maxcol);

     /* tagCompound.setInteger("meta", this.meta);
       tagCompound.setInteger("id", this.type.ordinal());
      tagCompound.setInteger("x", chunk.x);
       tagCompound.setInteger("z", chunk.z);
     tagCompound.setInteger("col", this.col);
      tagCompound.setInteger("maxcol", this.maxcol);
        tagCompound.setBoolean("find", this.find);

      */
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
        Vein vein = (Vein) o;
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

    @Override
    public void setFind(final boolean find) {
        this.find = find;
    }


}
