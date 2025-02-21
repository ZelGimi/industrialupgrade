package com.denfop.tiles.mechanism.water;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.IRotorUpgradeItem;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.water.upgrade.event.EventRotorItemLoad;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.api.windsystem.EnumRotorSide;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.EnumWindSide;
import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.api.windsystem.event.WindGeneratorEvent;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerBaseWaterGenerator;
import com.denfop.gui.GuiBaseWaterGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotWaterRotor;
import com.denfop.invslot.InvSlotWaterRotorBlades;
import com.denfop.items.ItemWindRod;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.DamageHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomeRiver;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileBaseWaterGenerator extends TileEntityInventory implements IWindMechanism, IType,
        IUpdatableTileEvent {


    public final Energy energy;
    public final InvSlotWaterRotorBlades slot_blades;
    private final EnumLevelGenerators levelGenerators;
    public InvSlotWaterRotor slot;
    public double generation = 0;
    public boolean need_repair;
    public int mind_wind;
    public boolean can_repair;
    public int mind_speed;
    public int timers;
    public double wind_speed;
    public EnumWindSide wind_side;
    public EnumTypeWind enumTypeWind;
    public int coefficient_power = 100;
    boolean space = false;
    private int tick;
    private boolean change_facing;
    private boolean min_level;
    private double addition_power;
    private double addition_efficient;
    private double addition_strength;
    private double coefficient;
    private EnumRotorSide rotorSide;
    private float speed;
    private float angle;
    private long lastcheck;
    private boolean work;
    private int time;
    private boolean can_work;
    private double biome;

    public TileBaseWaterGenerator(EnumLevelGenerators levelGenerators) {
        this.levelGenerators = levelGenerators;
        this.slot = new InvSlotWaterRotor(this);
        this.slot_blades = new InvSlotWaterRotorBlades(this);
        this.energy =
                this.addComponent(Energy.asBasicSource(this, 500000 * (levelGenerators.ordinal() + 1),
                        1
                ));
        this.change_facing = false;
        this.min_level = false;
        this.addition_power = 0;
        this.addition_efficient = 0;
        this.addition_strength = 0;
        this.tick = 0;
        this.biome = 1;
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.WATER_GENERATOR);
    }

    private int getDamageTimeFromWind() {
        switch (this.enumTypeWind) {
            case ONE:
                return 60;
            case TWO:
                return 55;
            case THREE:
                return 50;
            case FOUR:
                return 45;
            case FIVE:
                return 40;
            case SIX:
                return 35;
            case SEVEN:
                return 30;
            case EIGHT:
                return 25;
            case NINE:
                return 20;
            case TEN:
                return 10;
        }
        return 20;
    }

    private int getDamageRotor() {
        if (coefficient_power == 100) {
            return 1;
        }
        return (int) ((int) (this.getRotor().getLevel() * this.coefficient_power / 100D) * Math.pow(
                this.coefficient_power / 100D,
                this.getRotor().getLevel() - 1
        ));
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, slot);
            EncoderHandler.encode(packet, facing);
            EncoderHandler.encode(packet, generation);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, coefficient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            slot.readFromNbt(getNBTFromSlot(customPacketBuffer));
            facing = (byte) DecoderHandler.decode(customPacketBuffer);
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("wind.need_level") + this.levelGenerators.getMin() + " " + Localization.translate(
                "wind.need_level1") + this.levelGenerators.getMax() + " " + Localization.translate("wind.need_level2"));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
            speed = (float) DecoderHandler.decode(customPacketBuffer);
            slot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            rotorSide = EnumRotorSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            timers = (int) DecoderHandler.decode(customPacketBuffer);
            wind_speed = (double) DecoderHandler.decode(customPacketBuffer);
            wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            mind_wind = (int) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            enumTypeWind = EnumTypeWind.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            coefficient_power = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, coefficient);
            EncoderHandler.encode(packet, speed);
            EncoderHandler.encode(packet, slot);
            EncoderHandler.encode(packet, rotorSide);
            EncoderHandler.encode(packet, generation);
            EncoderHandler.encode(packet, timers);
            EncoderHandler.encode(packet, wind_speed);
            EncoderHandler.encode(packet, wind_side);
            EncoderHandler.encode(packet, mind_wind);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, enumTypeWind);
            EncoderHandler.encode(packet, coefficient_power);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (this.getRotor() != null && stack.getItem() instanceof ItemWindRod) {
            ItemStack rotor = this.slot.get();
            if (((ItemWindRod) stack.getItem()).getLevel(this.getRotor().getLevel(), stack.getItemDamage())) {

                if (rotor.getItemDamage() >= rotor.getMaxDamage() * 0.25) {
                    this.slot.damage((int) -(rotor.getMaxDamage() * 0.25), 0);
                    stack.shrink(1);
                    return true;
                }
            }
        }

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public boolean checkSpace() {
        int box = this.getRotorDiameter() / 2;
        if (box == 0) {
            return false;
        }
        BlockPos pos1 = pos.add(this.getFacing().getDirectionVec());
        switch (this.getFacing().getAxis()) {
            case Y:
                return false;
            case X:
                for (int z = pos1.getZ() - box; z <= pos1.getZ() + box; z++) {
                    for (int y = pos1.getY() - box; y <= pos1.getY() + box; y++) {
                        IBlockState state = this.world.getBlockState(new BlockPos(pos1.getX(), y, z));
                        if (state.getMaterial() != Material.WATER) {
                            return false;
                        }
                    }
                }
                return true;
            case Z:
                for (int x = pos1.getX() - box; x <= pos1.getX() + box; x++) {
                    for (int y = pos1.getY() - box; y <= pos1.getY() + box; y++) {
                        IBlockState state = this.world.getBlockState(new BlockPos(x, y, pos1.getZ()));
                        if (state.getMaterial() != Material.WATER) {
                            return false;
                        }
                    }
                }
                return true;
        }
        return false;
    }

    public boolean setFacingWrench(EnumFacing facing, EntityPlayer player) {
        boolean fac = super.setFacingWrench(facing, player);
        new PacketUpdateFieldTile(this, "facing", this.facing);
        return fac;
    }

    public void update_generator() {
        this.work = true;
        for (int i = this.pos.getX() - 4; i <= this.pos.getX() + 4; i++) {
            for (int j = this.pos.getY() - 4; j <= this.pos.getY() + 4; j++) {
                for (int k = this.pos.getZ() - 4; k <= this.pos.getZ() + 4; k++) {

                    if (pos.getX() == i && pos.getY() == j && pos.getZ() == k) {
                        continue;
                    }
                    final TileEntity tile = this.getWorld().getTileEntity(new BlockPos(i, j, k));
                    if (tile instanceof IWindMechanism) {
                        this.work = false;
                        ((IWindMechanism) tile).setWork(false);
                    }
                }
            }
        }
        new PacketUpdateFieldTile(this, "work", this.work);
    }
    @Override
    public void setWork(final boolean work) {
        this.work = work;
    }
    public void update_generator(BlockPos pos) {
        this.work = true;
        for (int i = this.pos.getX() - 4; i <= this.pos.getX() + 4; i++) {
            for (int j = this.pos.getY() - 4; j <= this.pos.getY() + 4; j++) {
                for (int k = this.pos.getZ() - 4; k <= this.pos.getZ() + 4; k++) {
                    if (this.pos.getX() == i && this.pos.getY() == j && this.pos.getZ() == k) {
                        continue;
                    }
                    if (pos.getX() == i && pos.getY() == j && pos.getZ() == k) {
                        continue;
                    }
                    final TileEntity tile = this.getWorld().getTileEntity(new BlockPos(i, j, k));
                    if (tile instanceof IWindMechanism) {
                        this.work = false;
                        ((IWindMechanism) tile).setWork(false);
                    }

                }
            }
        }
        new PacketUpdateFieldTile(this, "work", this.work);
    }

    @Override
    public EnumRotorSide getRotorSide() {
        return this.rotorSide;
    }

    @Override
    public void setRotorSide(final EnumRotorSide rotorSide) {
        this.rotorSide = rotorSide;
        new PacketUpdateFieldTile(this, "rotorSide", this.rotorSide);
    }

    @Override
    public double getCoefficient() {
        return this.coefficient;
    }

    @Override
    public void setCoefficient(final double coefficient) {
        this.coefficient = coefficient;
        new PacketUpdateFieldTile(this, "coefficient", this.coefficient);
    }

    @Override
    public IWindRotor getRotor() {
        return this.slot.isEmpty() ? null : (IWindRotor) this.slot.get().getItem();
    }

    @Override
    public ItemStack getItemStack() {
        return this.slot.get();
    }

    @Override
    public EnumLevelGenerators getLevelGenerator() {
        return this.levelGenerators;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.need_repair && this.getRotor() != null) {
            this.slot_blades.work();
        }
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.work || !can_work) {
            if (generation != 0) {
                generation = 0;
            }
            return;
        }

        if (this.can_repair) {
            if (this.time != 0) {
                if (this.world.provider.getWorldTime() % (this.time * 20L) == 0) {
                    this.slot.damage(-1, 0);

                }
            }
        }
        if (this.world.provider.getWorldTime() % 30 == 0) {
            if (this.getRotor() != null) {
                space = checkSpace();
                new PacketUpdateFieldTile(this, "space", this.space);
            } else {
                if (space) {
                    space = false;
                    new PacketUpdateFieldTile(this, "space", this.space);
                } else {
                    generation = 0;
                }
            }
        }
        this.tick++;
        if (this.tick >= 40) {
            this.tick = 40;
        }
        if (space && (this.getRotor() != null && ((ItemDamage) this.slot
                .get(0)
                .getItem()).getCustomDamage(this.slot.get(0)) > 0)) {
            if (!this.getActive()) {
                this.setActive(true);
            }
            if (this.getRotor() == null) {
                if (this.getActive()) {
                    this.setActive(false);
                }
                if (space) {
                    space = false;
                    new PacketUpdateFieldTile(this, "space", this.space);

                }
                return;
            }

            this.wind_speed = WindSystem.windSystem.getWind_Strength();

            generation =
                    WindSystem.windSystem.getPowerFromWaterRotor(
                            this.world,
                            this,
                            this.getItemStack()
                    ) * this.biome * this.coefficient_power / 100D;
            this.energy.addEnergy(generation);
            if (this.world.getWorldTime() % getDamageTimeFromWind() == 0) {
                this.slot.damage(this.getDamageRotor(), this.addition_strength);
            }
        } else {
            generation = 0;
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("speed")) {
            try {
                this.speed = (float) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot")) {
            this.slot.readFromNbt(getNBTFromSlot(is));
        }
        if (name.equals("space")) {
            try {
                this.space = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("coefficient")) {
            try {
                this.coefficient = (double) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("work")) {
            try {
                this.work = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("rotorSide")) {
            try {
                this.rotorSide = EnumRotorSide.values()[(int) DecoderHandler.decode(is)];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("change_facing")) {
            try {
                this.change_facing = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("min_level")) {
            try {
                this.min_level = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("mind_wind")) {
            try {
                this.mind_wind = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("wind_side")) {
            try {
                this.wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(is)];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("angle")) {
            try {
                this.angle = (float) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("generation")) {
            try {
                this.generation = (double) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("mind_speed")) {
            try {
                this.mind_speed = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.slot.isEmpty()) {
            MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(this.getWorld(),
                    (IRotorUpgradeItem) this.slot.get().getItem(), this.slot.get()
            ));
        }
        this.biome =
                (this.getWorld().getBiome(this.pos) instanceof BiomeOcean || this
                        .getWorld()
                        .getBiome(this.pos) instanceof BiomeRiver) ? 1 : 0.5;
        this.change();
        this.setRotorSide(WindSystem.windSystem.getRotorSide(this.getFacing()));
        MinecraftForge.EVENT_BUS.post(new WindGeneratorEvent(this, this.getWorld(), true));
        new PacketUpdateFieldTile(this, "speed", speed);
        new PacketUpdateFieldTile(this, "slot", slot);
        new PacketUpdateFieldTile(this, "space", space);
        new PacketUpdateFieldTile(this, "coefficient", coefficient);
        new PacketUpdateFieldTile(this, "wind_side", wind_side);
        new PacketUpdateFieldTile(this, "angle", angle);
        new PacketUpdateFieldTile(this, "mind_speed", mind_speed);
        new PacketUpdateFieldTile(this, "generation", generation);
        update_generator();
        if (this.getRotor() != null) {
            this.energy.setSourceTier(this.getRotor().getSourceTier());
        }
        this.can_work = this.getWorld().provider.hasSkyLight() &&
                !this.getWorld().provider.isNether();
        this.timers = WindSystem.windSystem.getTime();
        this.wind_side = WindSystem.windSystem.getWindSide();
        this.enumTypeWind = WindSystem.windSystem.getEnumTypeWind();
        if (!this.slot.get().isEmpty()) {
            if (DamageHandler.getDamage(this.slot.get()) <= DamageHandler.getMaxDamage(this.slot.get()) * 0.75) {
                this.need_repair = true;
            }
        }
    }

    @Override
    public void onUnloaded() {
        MinecraftForge.EVENT_BUS.post(new WindGeneratorEvent(this, this.getWorld(), false));
        super.onUnloaded();
    }

    @Override
    public void onBlockBreak(boolean w) {
        for (int i = this.pos.getX() - 4; i <= this.pos.getX() + 4; i++) {
            for (int j = this.pos.getY() - 4; j <= this.pos.getY() + 4; j++) {
                for (int k = this.pos.getZ() - 4; k <= this.pos.getZ() + 4; k++) {
                    final TileEntity tile = this.getWorld().getTileEntity(new BlockPos(i, j, k));
                    if (tile instanceof IWindMechanism) {
                        ((IWindMechanism) tile).update_generator(this.pos);
                    }
                }
            }
        }
        super.onBlockBreak(w);
    }

    public void setActive(boolean active) {
        if (active != this.getActive()) {
            new PacketUpdateFieldTile(this, "slot", this.slot);
        }

        super.setActive(active);
    }

    public double getGeneration() {
        return generation;
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("coef", this.coefficient_power);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.coefficient_power = nbtTagCompound.getInteger("coef");
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, speed);
            EncoderHandler.encode(packet, slot);
            EncoderHandler.encode(packet, space);
            EncoderHandler.encode(packet, coefficient);
            EncoderHandler.encode(packet, wind_side);
            EncoderHandler.encode(packet, angle);
            EncoderHandler.encode(packet, mind_speed);
            EncoderHandler.encode(packet, generation);
            EncoderHandler.encode(packet, coefficient_power);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            speed = (float) DecoderHandler.decode(customPacketBuffer);
            slot.readFromNbt(((InvSlot) DecoderHandler.decode(customPacketBuffer)).writeToNbt(new NBTTagCompound()));
            space = (boolean) DecoderHandler.decode(customPacketBuffer);
            coefficient = (double) DecoderHandler.decode(customPacketBuffer);
            wind_side = EnumWindSide.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            angle = (float) DecoderHandler.decode(customPacketBuffer);
            mind_speed = (int) DecoderHandler.decode(customPacketBuffer);
            generation = (double) DecoderHandler.decode(customPacketBuffer);
            coefficient_power = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public float getAngle() {
        if (this.getWorld().provider.getDimension() != 0) {
            return 0;
        }
        if (this.speed != 0.0F && this.work && (this.getRotor() != null && ((ItemDamage) this.slot
                .get(0)
                .getItem()).getCustomDamage(this.slot.get(0)) > 0)) {
            final long k = (System.currentTimeMillis() - this.lastcheck);
            if (this.mind_wind != 0) {
                this.angle += (float) ((float) k * WindSystem.windSystem.getSpeed(Math.min(
                        24.7 + this.mind_speed,
                        WindSystem.windSystem.getSpeedFromPower(this.getBlockPos(), this,
                                this.generation
                        )
                ) * this.getCoefficient()));
            } else {
                this.angle += (float) ((float) k * this.speed * this.getCoefficient());
            }
            this.angle %= 360.0F;
        }

        this.lastcheck = System.currentTimeMillis();
        return this.angle;
    }

    @Override
    public void setRotationSpeed(final float speed) {
        if (this.speed != speed) {
            this.speed = speed;
            new PacketUpdateFieldTile(this, "speed", speed);
        }
    }

    @Override
    public int getRotorDiameter() {
        return getRotor() != null ? getRotor().getDiameter(this.slot.get()) : 0;
    }

    @Override
    public ResourceLocation getRotorRenderTexture() {
        return getRotor() != null ? getRotor().getRotorRenderTexture(this.slot.get()) : null;
    }

    @Override
    public void change() {
        this.change_facing = false;
        this.min_level = false;
        this.addition_power = 0;
        this.addition_efficient = 0;
        this.addition_strength = 0;
        this.time = 0;
        this.mind_speed = 0;
        this.mind_wind = 0;
        if (this.getRotor() != null) {
            final List<RotorUpgradeItemInform> list = RotorUpgradeSystem.instance.getInformation(
                    this.getItemStack());
            this.change_facing = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.AUTO, list) != null;

            for (int i = 0; i < 3; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.addition_strength += modules == null ? 0 : modules.number * modules.upgrade.getCoef();
            }
            for (int i = 3; i < 6; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.addition_efficient += modules == null ? 0 : modules.number * modules.upgrade.getCoef();
            }
            for (int i = 6; i < 9; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.addition_power += modules == null ? 0 : modules.number * modules.upgrade.getCoef();
            }
            for (int i = 13; i < 16; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.time = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
            for (int i = 10; i < 13; i++) {
                final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                        i), list);
                this.mind_wind = modules == null ? 0 : (int) modules.upgrade.getCoef();
            }
            final RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(EnumInfoRotorUpgradeModules.getFromID(
                    16), list);
            this.biome = (this.getWorld().getBiome(this.pos) instanceof BiomeOcean || this
                    .getWorld()
                    .getBiome(this.pos) instanceof BiomeRiver) ? 1 : 0.5;
            if (modules != null) {
                this.biome = 1;
            }
        }

        new PacketUpdateFieldTile(this, "change_facing", this.change_facing);
        new PacketUpdateFieldTile(this, "min_level", this.min_level);
        new PacketUpdateFieldTile(this, "mind_wind", this.mind_wind);

        if (this.change_facing) {
            WindSystem.windSystem.getNewPositionOfMechanism(this);
        }
    }

    @Override
    public double getAdditionalCoefficient() {
        return this.addition_efficient;
    }

    @Override
    public double getAdditionalPower() {
        return this.addition_power;
    }

    @Override
    public boolean getAuto() {
        return this.change_facing;
    }

    @Override
    public boolean getMin() {
        return this.min_level;
    }

    @Override
    public boolean getSpace() {
        return this.space;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public boolean need_repair() {
        return this.need_repair;
    }

    @Override
    public boolean can_repair() {
        return this.can_repair;
    }

    @Override
    public int getMinWind() {
        return this.mind_wind;
    }

    @Override
    public int getMinWindSpeed() {
        return this.mind_speed;
    }

    @Override
    public ContainerBaseWaterGenerator getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerBaseWaterGenerator(this, entityPlayer);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiBaseWaterGenerator(getGuiContainer(entityPlayer));

    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i == 0) {
            if (this.tick >= 20) {
                WindSystem.windSystem.getNewFacing(this.getFacing(), this);
                this.tick = 0;
                if (this.getAuto()) {
                    WindSystem.windSystem.getNewPositionOfMechanism(this);
                }
            }
        } else {
            this.coefficient_power = (int) i;
            if (this.coefficient_power < 100) {
                coefficient_power = 100;
            }
            if (this.coefficient_power > 150) {
                coefficient_power = 150;
            }
        }
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
