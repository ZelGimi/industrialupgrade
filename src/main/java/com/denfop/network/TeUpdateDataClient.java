package com.denfop.network;

import ic2.core.block.TileEntityBlock;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class TeUpdateDataClient {

    private final List<TeUpdateDataClient.TeData> updates = new ArrayList<>();

    TeUpdateDataClient() {
    }

    public TeUpdateDataClient.TeData addTe(BlockPos pos, int fieldCount) {
        TeUpdateDataClient.TeData ret = new TeUpdateDataClient.TeData(pos, fieldCount);
        this.updates.add(ret);
        return ret;
    }

    public Collection<TeUpdateDataClient.TeData> getTes() {
        return this.updates;
    }

    static class FieldData {

        final String name;
        final Object value;
        Field field;

        private FieldData(String name, Object value) {
            this.name = name;
            this.value = value;
        }

    }

    static class TeData {

        final BlockPos pos;
        private final List<TeUpdateDataClient.FieldData> fields;
        Class<? extends TileEntityBlock> teClass;

        private TeData(BlockPos pos, int fieldCount) {
            this.pos = pos;
            this.fields = new ArrayList<>(fieldCount);
        }

        public void addField(String name, Object value) {
            this.fields.add(new TeUpdateDataClient.FieldData(name, value));
        }

        public Collection<TeUpdateDataClient.FieldData> getFields() {
            return this.fields;
        }

    }

}
