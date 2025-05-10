package com.denfop.api.space.fakebody;

import net.minecraft.nbt.CompoundTag;

public class FakePlayer implements IFakePlayer {

    private final String name;
    private final CompoundTag tag;

    public FakePlayer(String name, CompoundTag tag) {
        this.name = name;
        this.tag = tag;
    }

    public FakePlayer(CompoundTag tag) {
        this.name = tag.getString("name");
        this.tag = tag;
    }

    @Override
    public CompoundTag getTag() {
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
    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.getName());
        tag.put("tag", this.getTag());
        return tag;
    }

    @Override
    public CompoundTag writeNBT(CompoundTag nbtTagCompound) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.getName());
        tag.put("tag", this.getTag());
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
