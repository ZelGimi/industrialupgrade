package com.denfop.tiles.bee;

import com.denfop.api.bee.IBee;
import com.denfop.world.WorldBaseGen;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class Bee {

    private final int maxLife;
    private final int birthTick;
    final long id;
    private double jelly;
    private double food;
    private boolean ill;
    private int tick;
    private EnumTypeBee typeBee;
    private EnumTypeLife type;
    private boolean isDead;
    private  boolean wasBirth = false;

    public Bee(EnumTypeBee typeBee, IBee bee, EnumTypeLife type, double food, final int tick) {
        this.maxLife = bee.getTickLifecycles();
        this.typeBee = typeBee;
        this.type = type;
        this.id = WorldBaseGen.random.nextLong();
        this.birthTick = bee.getTickBirthRate();
        this.tick = tick;
        this.wasBirth = isChild();
        this.jelly = 0;
        this.ill = false;
        this.food = food;
    }

    public Bee(NBTTagCompound tagCompound) {
        this.maxLife = tagCompound.getShort("maxLife");
        this.ill = tagCompound.getBoolean("ill");
        this.typeBee =  EnumTypeBee.values()[tagCompound.getByte("typeBee")];
        this.type = EnumTypeLife.values()[tagCompound.getByte("type")];
        this.id = WorldBaseGen.random.nextLong();
        this.birthTick = tagCompound.getShort("birthTick");
        this.tick = tagCompound.getShort("tick");
        this.jelly = tagCompound.getShort("jelly") / 100D;
        this.food = tagCompound.getShort("food") / 100D;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bee bee = (Bee) o;
        return id == bee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setTypeBee(final EnumTypeBee typeBee) {
        this.typeBee = typeBee;
    }

    public void setType(final EnumTypeLife type) {
        this.type = type;
    }

    public NBTTagCompound writeToNBT() {
        final NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort("maxLife", (short) this.maxLife);
        tagCompound.setByte("type", (byte) this.type.ordinal());
        tagCompound.setByte("typeBee", (byte) this.typeBee.ordinal());
        tagCompound.setBoolean("ill", ill);
        tagCompound.setShort("birthTick", (short) birthTick);
        tagCompound.setShort("tick", (short) tick);
        tagCompound.setShort("jelly", (short) (this.jelly * 100));
        tagCompound.setShort("food", (short) (this.food * 100));

        return tagCompound;
    }

    public boolean isChild() {
        return this.tick < this.birthTick;
    }

    public void addTick(int tick, double lifeGenome) {
        this.tick += tick;
        if (!this.isDead)
        this.isDead = this.tick > this.maxLife * lifeGenome;
    }

    public void addFood(double food) {
        this.food += food;
    }

    public void addJelly(double jelly) {
        this.jelly += jelly;
    }

    public void removeFood() {
        this.food -= (0.25 + 0.25 * (isIll() ? 1 : 0));
        if (this.food <= 0) {
            this.food = 0;
            this.isDead = true;
        }
    }

    public void removeJelly() {
        this.jelly -= (0.1 + 0.1 * (isIll() ? 1 : 0));
        if (this.jelly <= 0) {
            this.jelly = 0;
            this.isDead = true;
        }
    }

    public double getJelly() {
        return jelly;
    }

    public double getFood() {
        return food;
    }

    public void setIll(final boolean ill) {
        this.ill = ill;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isIll() {
        return ill;
    }

    public int getTick() {
        return tick;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public EnumTypeLife getType() {
        return type;
    }

    public EnumTypeBee getTypeBee() {
        return typeBee;
    }

    public void setDead(boolean b) {
        this.isDead = b;
    }

}
