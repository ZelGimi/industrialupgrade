package com.denfop.api.space.fakebody;

import net.minecraft.nbt.NBTTagCompound;

public class FakePlayer implements IFakePlayer {

    private final String name;
    private final NBTTagCompound tag;

    public FakePlayer(String name, NBTTagCompound tag) {
        this.name = name;
        this.tag = tag;
    }

    @Override
    public NBTTagCompound getTag() {
        return this.tag;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean matched(final String name) {
        return this.name.equals(name);
    }

    @Override
    public NBTTagCompound writeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", this.getName());
        tag.setTag("tag", this.getTag());
        return tag;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FakePlayer player = (FakePlayer) o;
        return matched(player.name);
    }


}
