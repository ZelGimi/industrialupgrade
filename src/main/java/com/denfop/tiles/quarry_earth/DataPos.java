package com.denfop.tiles.quarry_earth;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class DataPos {

    public final BlockPos pos;
    public final IBlockState state;

    public DataPos(BlockPos pos, IBlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockState getState() {
        return state;
    }
    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", pos.getX());
        tag.setInteger("y", pos.getY());
        tag.setInteger("z", pos.getZ());

        String blockId = Block.REGISTRY.getNameForObject(state.getBlock()).toString();
        int meta = state.getBlock().getMetaFromState(state);
        tag.setString("block", blockId);
        tag.setInteger("meta", meta);

        return tag;
    }

    public static DataPos load(NBTTagCompound tag) {
        int x = tag.getInteger("x");
        int y = tag.getInteger("y");
        int z = tag.getInteger("z");

        Block block = Block.getBlockFromName(tag.getString("block"));
        int meta = tag.getInteger("meta");
        IBlockState state = block != null ? block.getStateFromMeta(meta) : Block.getBlockFromName("minecraft:air").getDefaultState();

        return new DataPos(new BlockPos(x, y, z), state);
    }
}
