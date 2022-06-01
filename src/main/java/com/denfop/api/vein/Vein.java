package com.denfop.api.vein;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

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
        this.meta = tagCompound.getInteger("meta");
        this.type = Type.getID(tagCompound.getInteger("id"));
        this.chunk = new ChunkPos(tagCompound.getInteger("x"), tagCompound.getInteger("z"));
        this.col = tagCompound.getInteger("col");
        this.maxcol = tagCompound.getInteger("maxcol");
        this.find = tagCompound.getBoolean("find");
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
        tagCompound.setInteger("meta", this.meta);
        tagCompound.setInteger("id", this.type.ordinal());
        tagCompound.setInteger("x", chunk.x);
        tagCompound.setInteger("z", chunk.z);
        tagCompound.setInteger("col", this.col);
        tagCompound.setInteger("maxcol", this.maxcol);
        tagCompound.setBoolean("find", this.find);
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
    public boolean get() {
        return this.find;
    }

    @Override
    public void setFind(final boolean find) {
        this.find = find;
    }


}
