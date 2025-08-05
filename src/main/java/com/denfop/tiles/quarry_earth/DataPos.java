package com.denfop.tiles.quarry_earth;


import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

public class DataPos {

    public final BlockPos pos;
    public final BlockState state;

    public DataPos(BlockPos pos, BlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        tag.putString("block", ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString());

        CompoundTag props = new CompoundTag();
        for (Property<?> property : state.getProperties()) {
            String name = property.getName();
            props.putString(name, getName(state, property));
        }
        tag.put("properties", props);
        return tag;
    }

    public static DataPos load(CompoundTag tag) {
        BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        var block = ForgeRegistries.BLOCKS.getValue(new net.minecraft.resources.ResourceLocation(tag.getString("block")));
        BlockState state = block.defaultBlockState();

        CompoundTag props = tag.getCompound("properties");
        for (String key : props.getAllKeys()) {
            Property<?> property = state.getBlock().getStateDefinition().getProperty(key);
            if (property != null) {
                state = setValue(state, property, props.getString(key));
            }
        }
        return new DataPos(pos, state);
    }


    private static <T extends Comparable<T>> String getName(BlockState state, Property<T> property) {
        T value = state.getValue(property);
        return property.getName(value);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState setValue(BlockState state, Property<T> property, String name) {
        return property.getValue(name).map(v -> state.setValue(property, v)).orElse(state);
    }
}
