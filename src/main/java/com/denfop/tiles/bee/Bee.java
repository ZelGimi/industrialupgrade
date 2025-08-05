package com.denfop.tiles.bee;

import com.denfop.api.bee.IBee;
import com.denfop.world.WorldBaseGen;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class Bee {

    public static final Codec<Bee> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("id").forGetter(Bee::getId),
            Codec.INT.fieldOf("maxLife").forGetter(Bee::getMaxLife),
            Codec.INT.fieldOf("birthTick").forGetter(Bee::getBirthTick),
            Codec.DOUBLE.fieldOf("jelly").forGetter(Bee::getJelly),
            Codec.DOUBLE.fieldOf("food").forGetter(Bee::getFood),
            Codec.BOOL.fieldOf("ill").forGetter(Bee::isIll),
            Codec.INT.fieldOf("tick").forGetter(Bee::getTick),
            Codec.STRING.xmap(EnumTypeBee::valueOf, Enum::name).fieldOf("typeBee").forGetter(Bee::getTypeBee),
            Codec.STRING.xmap(EnumTypeLife::valueOf, Enum::name).fieldOf("type").forGetter(Bee::getType),
            Codec.BOOL.fieldOf("isDead").forGetter(Bee::isDead),
            Codec.BOOL.fieldOf("wasBirth").forGetter(Bee::isWasBirth)
    ).apply(instance, Bee::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Bee> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeLong(value.getId());
                buf.writeInt(value.getMaxLife());
                buf.writeInt(value.getBirthTick());
                buf.writeDouble(value.getJelly());
                buf.writeDouble(value.getFood());
                buf.writeBoolean(value.isIll());
                buf.writeInt(value.getTick());
                buf.writeUtf(value.getTypeBee().name());
                buf.writeUtf(value.getType().name());
                buf.writeBoolean(value.isDead());
                buf.writeBoolean(value.isWasBirth());
            },
            buf -> new Bee(
                    buf.readLong(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readBoolean(),
                    buf.readInt(),
                    EnumTypeBee.valueOf(buf.readUtf()),
                    EnumTypeLife.valueOf(buf.readUtf()),
                    buf.readBoolean(),
                    buf.readBoolean()
            )
    );
    final long id;
    private final int maxLife;
    private final int birthTick;
    private double jelly;
    private double food;
    private boolean ill;
    private int tick;
    private EnumTypeBee typeBee;
    private EnumTypeLife type;
    private boolean isDead;
    private boolean wasBirth = false;

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

    public Bee(long id, int maxLife, int birthTick, double jelly, double food, boolean isIll, int tick, EnumTypeBee enumTypeBee, EnumTypeLife enumTypeLife, boolean isDead, boolean isWasBirth) {
        this.maxLife = maxLife;
        this.typeBee = enumTypeBee;
        this.type = enumTypeLife;
        this.id = id;
        this.birthTick = birthTick;
        this.tick = tick;
        this.wasBirth = isWasBirth;
        this.jelly = jelly;
        this.ill = isIll;
        this.isDead = isDead;
        this.food = food;
    }

    public Bee(CompoundTag tagCompound) {
        this.maxLife = tagCompound.getShort("maxLife");
        this.ill = tagCompound.getBoolean("ill");
        this.typeBee = EnumTypeBee.values()[tagCompound.getByte("typeBee")];
        this.type = EnumTypeLife.values()[tagCompound.getByte("type")];
        this.id = WorldBaseGen.random.nextLong();
        this.birthTick = tagCompound.getShort("birthTick");
        this.tick = tagCompound.getShort("tick");
        this.jelly = tagCompound.getShort("jelly") / 100D;
        this.food = tagCompound.getShort("food") / 100D;
    }

    public long getId() {
        return id;
    }

    public int getBirthTick() {
        return birthTick;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bee bee = (Bee) o;
        return id == bee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public CompoundTag writeToNBT() {
        final CompoundTag tagCompound = new CompoundTag();
        tagCompound.putShort("maxLife", (short) this.maxLife);
        tagCompound.putByte("type", (byte) this.type.ordinal());
        tagCompound.putByte("typeBee", (byte) this.typeBee.ordinal());
        tagCompound.putBoolean("ill", ill);
        tagCompound.putShort("birthTick", (short) birthTick);
        tagCompound.putShort("tick", (short) tick);
        tagCompound.putShort("jelly", (short) (this.jelly * 100));
        tagCompound.putShort("food", (short) (this.food * 100));

        return tagCompound;
    }

    public boolean isWasBirth() {
        return wasBirth;
    }

    public boolean isChild() {
        return this.tick < this.birthTick;
    }

    public void addTick(int tick, double lifeGenome) {
        this.tick += tick;
        if (!this.isDead) {
            this.isDead = this.tick > this.maxLife * lifeGenome;
        }
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

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean b) {
        this.isDead = b;
    }

    public boolean isIll() {
        return ill;
    }

    public void setIll(final boolean ill) {
        this.ill = ill;
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

    public void setType(final EnumTypeLife type) {
        this.type = type;
    }

    public EnumTypeBee getTypeBee() {
        return typeBee;
    }

    public void setTypeBee(final EnumTypeBee typeBee) {
        this.typeBee = typeBee;
    }

}
