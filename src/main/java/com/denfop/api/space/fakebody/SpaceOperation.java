package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBody;
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

    public SpaceOperation(IBody fakeBody, EnumOperation operation, boolean auto) {
        this.body = fakeBody;
        this.operation = operation;
        this.auto = auto;
    }

    public SpaceOperation(IBody body, NBTTagCompound tag) {
        this.body = body;
        this.operation = EnumOperation.getID(tag.getByte("id"));
        this.auto = tag.getBoolean("auto");
    }

    public boolean getAuto() {
        return this.auto;
    }

    public void setAuto(boolean set) {
        this.auto = set;
    }

    public NBTTagCompound writeTag(NBTTagCompound nbt) {
        nbt.setByte("id", (byte) this.operation.ordinal());
        nbt.setBoolean("auto", this.auto);
        return nbt;
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
