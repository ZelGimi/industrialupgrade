package com.denfop.blocks;


import com.denfop.api.tile.IMultiTileBlock;

import java.util.Objects;

public class State implements Comparable<State> {

    public final IMultiTileBlock teBlock;
    public final String state;

    State(IMultiTileBlock teBlock, String state) {
        this.teBlock = teBlock;
        this.state = state;
    }

    public static int compare(String x, String y) {
        return (Objects.equals(x, y)) ? 0 : (!x.isEmpty() ? 1 : -1);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        State that = (State) o;
        return Objects.equals(teBlock, that.teBlock) && state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teBlock, state);
    }

    public int compareTo(State o) {
        int ret = this.teBlock.getId() - o.teBlock.getId();
        return ret != 0 ? ret : compare(this.state, o.state);
    }


}
