package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;
import java.util.UUID;

public class Data implements IData {

    private final UUID player;
    private final IBody body;
    private double percent;

    public Data(UUID player, IBody body) {
        this.player = player;
        this.body = body;
    }

    public Data(NBTTagCompound nbtTagCompound) {
        this.player = nbtTagCompound.getUniqueId("uuid");
        this.body = SpaceNet.instance.getBodyFromName(nbtTagCompound.getString("nameBody"));
        this.percent = nbtTagCompound.getDouble("percent");
    }

    public IBody getBody() {
        return body;
    }

    public NBTTagCompound writeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setDouble("percent", percent);
        nbtTagCompound.setUniqueId("uuid", player);
        nbtTagCompound.setString("nameBody", body.getName());
        return nbtTagCompound;
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
        this.percent += 1;
        if (percent > 100) {
            percent = 100;
        }
    }

    @Override
    public void setInformation(final double information) {
        this.percent = information;
        if (percent > 100) {
            percent = 100;
        }
    }

    @Override
    public void addInformation(final double information) {
        this.percent += information;
        if (percent > 100) {
            percent = 100;
        }
    }


}
