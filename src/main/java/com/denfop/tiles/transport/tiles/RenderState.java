package com.denfop.tiles.transport.tiles;

import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderState implements Comparable<RenderState> {

    public final ResourceLocation resourceLocation;
    public final int connectivity;
    private final ICableItem type;

    public RenderState(ResourceLocation resourceLocation, int connectivity, ICableItem type) {
        this.resourceLocation = resourceLocation;
        this.connectivity = connectivity;
        this.type = type;
    }

    public ICableItem getType() {
        return type;
    }

    public int hashCode() {
        int ret = this.resourceLocation.hashCode();
        ret = ret * 31 + this.connectivity;
        return ret;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof RenderState)) {
            return false;
        } else {
            RenderState o = (RenderState) obj;
            return o.resourceLocation == this.resourceLocation && o.connectivity == this.connectivity;
        }
    }

    public String toString() {
        return "RenderState<" + this.resourceLocation + ", " + this.connectivity + '>';
    }

    @Override
    public int compareTo(@NotNull final RenderState o) {
        return o.equals(this) ? 0 : -1;
    }

}
