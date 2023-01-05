package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import net.minecraft.nbt.NBTTagCompound;

public class SpaceOperation {

    private final IBody body;
    private EnumOperation operation;
    private boolean auto;

    public SpaceOperation(IBody fakeBody, EnumOperation operation) {
        this.body = fakeBody;
        this.operation = operation;
        this.auto = false;
    }

    public SpaceOperation(NBTTagCompound tag) {
        this.body = SpaceNet.instance.getBodyFromName(tag.getString("body"));
        this.operation = EnumOperation.getID(tag.getInteger("id"));
        this.auto = tag.getBoolean("auto");
    }

    public boolean getAuto() {
        return this.auto;
    }

    public void setAuto(boolean set) {
        this.auto = set;
    }

    public NBTTagCompound writeTag(NBTTagCompound tag) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("body", this.body.getName());
        nbt.setInteger("id", this.operation.ordinal());
        nbt.setBoolean("auto", this.auto);
        tag.setTag(this.body.getName(), nbt);
        return tag;
    }

    public IBody getBody() {
        return this.body;
    }

    public EnumOperation getOperation() {
        return this.operation;
    }

    public void setOperation(EnumOperation operation) {
        this.operation = operation;
    }


}
