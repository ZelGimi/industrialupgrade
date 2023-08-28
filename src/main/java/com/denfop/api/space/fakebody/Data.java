package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBody;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class Data implements IData {

    private final FakePlayer player;
    private final IBody body;
    private double percent;

    public Data(FakePlayer player, IBody body) {
        this.player = player;
        this.body = body;
        this.init(body);
    }

    private void init(final IBody body) {
        final NBTTagCompound nbt = this.player.getTag();
        if (nbt.hasKey("space")) {
            final NBTTagCompound nbt1 = nbt.getCompoundTag("space");
            this.percent = nbt1.getDouble(body.getName().toLowerCase());

        } else {
            final NBTTagCompound nbt1 = ModUtils.nbt();
            this.percent = 0;
            nbt1.setDouble(body.getName().toLowerCase(), 0);
            nbt.setTag("space", nbt1);

        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Data data = (Data) o;
        return Objects.equals(player, data.player) && Objects.equals(body, data.body);
    }


    @Override
    public double getPercent() {
        return this.percent;
    }

    @Override
    public void addInformation() {
        final NBTTagCompound nbt = this.player.getTag();
        final NBTTagCompound nbt1 = nbt.getCompoundTag("space");
        nbt1.setDouble(this.body.getName().toLowerCase(), Math.min(this.percent + 1, 100));


    }

    @Override
    public void setInformation(final double information) {
        final NBTTagCompound nbt = this.player.getTag();
        final NBTTagCompound nbt1 = nbt.getCompoundTag("space");
        nbt1.setDouble(this.body.getName().toLowerCase(), Math.min(information, 100));

    }

    @Override
    public void addInformation(final double information) {
        final NBTTagCompound nbt = this.player.getTag();
        final NBTTagCompound nbt1 = nbt.getCompoundTag("space");
        nbt1.setDouble(this.body.getName().toLowerCase(), Math.min(this.percent + information, 100));

    }


}
