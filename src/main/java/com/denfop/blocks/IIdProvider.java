package com.denfop.blocks;

public interface IIdProvider extends ic2.core.block.state.IIdProvider {

    String getName();

    int getId();

    default int getColor() {
        return 16777215;
    }

    default String getModelName() {
        return this.getName();
    }

}
