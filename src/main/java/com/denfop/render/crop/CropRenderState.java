package com.denfop.render.crop;

import com.denfop.api.agriculture.ICrop;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CropRenderState implements Comparable<CropRenderState> {

    public final ICrop crop;
    private final boolean doubleStick;
    private boolean needTwoTexture;

    public CropRenderState(ICrop crop, boolean doubleStick, boolean needTwoTexture) {
        this.crop = crop;
        this.doubleStick = doubleStick;
        this.needTwoTexture = needTwoTexture;
    }

    public boolean isNeedTwoTexture() {
        return needTwoTexture;
    }

    public void setNeedTwoTexture(final boolean needTwoTexture) {
        this.needTwoTexture = needTwoTexture;
    }

    public ICrop getCrop() {
        return crop;
    }

    public boolean isDoubleStick() {
        return doubleStick;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CropRenderState that = (CropRenderState) o;
        if (doubleStick && that.doubleStick) {
            return true;
        }


        return ((crop != null && that.crop != null && (crop.getId() == this.crop.getId() && crop.getStage() == that.crop.getMaxStage())) || (crop == null && that.crop == null));
    }

    @Override
    public int hashCode() {
        return Objects.hash(crop, doubleStick);
    }


    @Override
    public int compareTo(@NotNull final CropRenderState o) {
        return o.equals(this) ? 0 : -1;
    }

}
