package com.denfop.tiles.reactors.water.controller;

import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.reactors.*;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerLevelFuel;
import com.denfop.container.ContainerWaterMainController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiLevelFuel;
import com.denfop.gui.GuiWaterMainController;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotScheduleReactor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.water.*;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TileEntityMainController extends TileMultiBlockBase implements IFluidReactor, IUpdatableTileEvent {

    public final EnumFluidReactors enumFluidReactors;
    public final InvSlot reactorsElements;
    public final InvSlotReactorModules<TileEntityMainController> reactorsModules;
    public final InvSlotScheduleReactor scheduleReactor;
    private final ComponentBaseEnergy rad;
    public Timer timer = new Timer(9999, 0, 0);
    public Timer red_timer = new Timer(0, 2, 30);
    public Timer yellow_timer = new Timer(0, 15, 0);
    public Energy energy;
    public EnumTypeWork typeWork = EnumTypeWork.WORK;
    public int pressure = 1;
    public boolean work = false;
    public double heat;
    public double output = 0;
    public LogicFluidReactor reactor;
    public EnumTypeSecurity security = EnumTypeSecurity.NONE;
    public int blockLevel = 0;
    public boolean heat_sensor;
    public boolean stable_sensor;

    List<Fluids.InternalFluidTank> fluidTankInputList = new ArrayList<>();
    List<Fluids.InternalFluidTank> fluidTankOutputList = new ArrayList<>();
    List<Fluids.InternalFluidTank> fluidTankCoolantList = new ArrayList<>();
    List<Fluids.InternalFluidTank> fluidTankHotCoolantList = new ArrayList<>();
    List<ISecurity> securities = new ArrayList<>();

    public TileEntityMainController(final MultiBlockStructure multiBlockStructure, EnumFluidReactors enumFluidReactors, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(multiBlockStructure, block, pos, state);
        this.enumFluidReactors = enumFluidReactors;

        this.reactorsElements = new InvSlot(this, InvSlot.TypeItemSlot.INPUT,
                enumFluidReactors.getHeight() * enumFluidReactors.getWidth()
        ) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (scheduleReactor.getAccepts().isEmpty()) {
                    if (stack.getItem() instanceof IReactorItem) {
                        IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                        return ((TileEntityMainController) this.base).getLevelReactor() >= iReactorItem.getLevel();
                    } else {
                        return false;
                    }
                } else {
                    ItemStack stack1 = scheduleReactor.getAccepts().get(index);
                    if (stack1.isEmpty()) {
                        return false;
                    }
                    return stack1.is(stack.getItem());
                }
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                reactor = null;
                return content;
            }
        };

        this.reactorsElements.setStackSizeLimit(1);
        this.reactorsModules = new InvSlotReactorModules<>(this);
        this.scheduleReactor = new InvSlotScheduleReactor(this, 1, enumFluidReactors.ordinal() + 1, enumFluidReactors.getWidth(),
                enumFluidReactors.getHeight()
        );
        this.rad = this.addComponent(new ComponentBaseEnergy(EnergyType.RADIATION, this, enumFluidReactors.getRadiation() * 100));
    }
    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.reactor_safety_doom.info"));
    }
    public LogicReactor getReactor() {
        if (this.reactor == null) {
            this.reactor = new LogicFluidReactor(this);
        }
        return reactor;
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("pressure")) {
            try {
                this.pressure = (int) DecoderHandler.decode(is);
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
        if (name.equals("reactor")) {
            if (this.reactor == null) {
                this.reactor = new LogicFluidReactor(this);
            }
            try {
                this.reactor.setGeneration((int) DecoderHandler.decode(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ContainerWaterMainController getGuiContainer(final Player entityPlayer) {
        return new ContainerWaterMainController(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        if (menu instanceof ContainerLevelFuel)
            return new GuiLevelFuel((ContainerLevelFuel) menu);
        return new GuiWaterMainController((ContainerWaterMainController) menu);
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            fluidTankInputList.clear();
            fluidTankOutputList.clear();
            fluidTankCoolantList.clear();
            fluidTankHotCoolantList.clear();
            final List<BlockPos> pos1 = this
                    .getMultiBlockStucture()
                    .getPosFromClass(this.getFacing(), this.getBlockPos(),
                            IInput.class
                    );
            for (BlockPos pos : pos1) {
                BlockEntity tile = this.getWorld().getBlockEntity(pos);
                if (tile instanceof IInput) {
                    ((IInput) tile).clearList();
                }
            }
            List<BlockPos> pos2 = this
                    .getMultiBlockStucture()
                    .getPosFromClass(this.getFacing(), this.getBlockPos(),
                            IOutput.class
                    );
            for (BlockPos pos : pos2) {
                BlockEntity tile = this.getWorld().getBlockEntity(pos);
                if (tile instanceof IOutput) {
                    ((IOutput) tile).clearList();
                }
            }
            pos2 = this
                    .getMultiBlockStucture()
                    .getPosFromClass(this.getFacing(), this.getBlockPos(),
                            ILevelFuel.class
                    );
            for (BlockPos pos3 : pos2) {
                BlockEntity tile = this.getWorld().getBlockEntity(pos3);
                if (tile instanceof ILevelFuel) {
                    ((ILevelFuel) tile).setMainMultiElement(null);
                }

            }
        }
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (this.blockLevel > 0) {
                setWork(!this.work);
            }
        }
        if (var2 == 1) {
            this.pressure++;
            this.pressure = Math.min(5, this.pressure);
        } else if (var2 == 2) {
            this.pressure--;
            this.pressure = Math.max(1, this.pressure);

        } else if (var2 == -1) {
            if (!this.stable_sensor) {
                this.heat_sensor = !this.heat_sensor;
            }

        } else if (var2 == -2) {
            if (!this.heat_sensor) {
                this.stable_sensor = !this.stable_sensor;
            }

        } else {
            if (this.typeWork == EnumTypeWork.WORK && this.getLevelReactor() < this.getMaxLevelReactor()) {
                this.typeWork = EnumTypeWork.LEVEL_INCREASE;
                this.energy.onUnloaded();
                this.energy.setDirections(ModUtils.allFacings, ModUtils.noFacings);
                this.energy.delegate = null;
                this.energy.createDelegate();
                this.energy.onLoaded();
                switch (this.blockLevel) {
                    case 0:
                        this.energy.setCapacity(4000000);
                        break;
                    case 1:
                        this.energy.setCapacity(50000000);
                        break;
                    case 2:
                        this.energy.setCapacity(200000000);
                        break;
                    case 3:
                        this.energy.setCapacity(500000000);
                        break;

                }
            } else if (this.typeWork == EnumTypeWork.LEVEL_INCREASE && this.getLevelReactor() < this.getMaxLevelReactor()) {
                this.typeWork = EnumTypeWork.WORK;
                this.energy.onUnloaded();
                this.energy.setDirections(ModUtils.noFacings, ModUtils.allFacings);
                this.energy.delegate = null;
                this.energy.createDelegate();
                this.energy.onLoaded();
                this.energy.setCapacity(this.energy.defaultCapacity);
                this.energy.buffer.storage = 0;
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            scheduleReactor.update();
            this.reactorsModules.load();
            try {
                if (this.typeWork == EnumTypeWork.LEVEL_INCREASE) {
                    this.energy.onUnloaded();
                    this.energy.setDirections(ModUtils.allFacings, ModUtils.noFacings);
                    this.energy.delegate = null;
                    this.energy.createDelegate();
                    this.energy.onLoaded();
                    switch (this.blockLevel) {
                        case 0:
                            this.energy.setCapacity(4000000);
                            break;
                        case 1:
                            this.energy.setCapacity(50000000);
                            break;
                        case 2:
                            this.energy.setCapacity(200000000);
                            break;
                        case 3:
                            this.energy.setCapacity(500000000);
                            break;

                    }

                }
            } catch (Exception ignored) {
            }
            ;
            ChunkPos chunkPos = this.getWorld().getChunkAt(this.pos).getPos();
            List<IAdvReactor> list = RadiationSystem.rad_system.getAdvReactorMap().computeIfAbsent(
                    chunkPos,
                    k -> new ArrayList<>()
            );
            list.add(this);
        }
    }


    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();

    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (!this.getWorld().isClientSide) {
            ChunkPos chunkPos = this.getWorld().getChunkAt(this.pos).getPos();
            List<IAdvReactor> list = RadiationSystem.rad_system.getAdvReactorMap().computeIfAbsent(
                    chunkPos,
                    k -> new ArrayList<>()
            );
            list.remove(this);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (reactor == null) {
            reactor = new LogicFluidReactor(this);
            reactor.temp_heat = this.heat;
            new PacketUpdateFieldTile(this, "reactor", this.reactor.getGeneration());
        } else {
            if (this.full) {
                if (this.typeWork == EnumTypeWork.WORK) {
                    this.energy.buffer.capacity = Math.max(this.output, this.energy.getDefaultCapacity());
                    if (this.work) {
                        if (this.getWorld().getGameTime() % 20 == 0) {
                            reactor.onTick();
                            if (this.rad.getEnergy() >= this.rad.getCapacity() * 0.5 && this.rad.getEnergy() < this.rad.getCapacity() * 0.75) {
                                this.setSecurity(EnumTypeSecurity.UNSTABLE);
                            }
                            workTimer();
                            if (!this.timer.canWork()) {
                                this.explode();
                                this.reactor = null;
                            } else if (!this.yellow_timer.canWork()) {
                                this.explode();
                                this.reactor = null;
                            } else if (!this.red_timer.canWork()) {
                                this.explode();
                                this.reactor = null;
                            } else if (this.reactor != null && this.getHeat() >= this.getMaxHeat() && this.reactor.getMaxHeat() >= this.getMaxHeat() * 1.5) {
                                this.explode();
                                this.reactor = null;
                            }

                        }
                        if (this.work && this.reactor != null) {
                            this.energy.setSourceTier(EnergyNetGlobal.instance.getTierFromPower(output));
                            this.energy.addEnergy(this.output);
                        }
                    }
                } else {
                    if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                        this.blockLevel++;
                        this.typeWork = EnumTypeWork.WORK;
                        this.energy.onUnloaded();
                        this.energy.setCapacity(this.energy.defaultCapacity);
                        this.energy.setDirections(ModUtils.noFacings, ModUtils.allFacings);
                        this.energy.delegate = null;
                        this.energy.createDelegate();
                        this.energy.onLoaded();
                        this.energy.buffer.storage = 0;
                    }
                }

            }
        }
    }

    public Timer getYellow_timer() {
        return yellow_timer;
    }

    public Timer getRed_timer() {
        return red_timer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.pressure = customPacketBuffer.readInt();
        this.work = customPacketBuffer.readBoolean();
        this.stable_sensor = customPacketBuffer.readBoolean();
        this.heat_sensor = customPacketBuffer.readBoolean();
        boolean socket = customPacketBuffer.readBoolean();
        if (socket) {
            try {
                this.energy.onNetworkUpdate(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.security = EnumTypeSecurity.values()[customPacketBuffer.readInt()];
        this.heat = customPacketBuffer.readDouble();
        this.output = customPacketBuffer.readDouble();
        try {
            this.timer.readBuffer(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.blockLevel = customPacketBuffer.readInt();

        this.typeWork = EnumTypeWork.values()[customPacketBuffer.readInt()];

        for (Fluids.InternalFluidTank fluidTank : this.fluidTankCoolantList) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
                if (fluidTank1 != null) {
                    fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankInputList) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
                if (fluidTank1 != null) {
                    fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankOutputList) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
                if (fluidTank1 != null) {
                    fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankHotCoolantList) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
                if (fluidTank1 != null) {
                    fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.red_timer.readBuffer(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.yellow_timer.readBuffer(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.pressure);
        customPacketBuffer.writeBoolean(work);
        customPacketBuffer.writeBoolean(stable_sensor);
        customPacketBuffer.writeBoolean(heat_sensor);
        customPacketBuffer.writeBoolean(this.energy != null);
        if (energy != null) {
            try {
                EncoderHandler.encode(customPacketBuffer, this.energy, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        customPacketBuffer.writeInt(this.security.ordinal());
        customPacketBuffer.writeDouble(this.heat);
        customPacketBuffer.writeDouble(this.output);
        this.timer.writeBuffer(customPacketBuffer);
        customPacketBuffer.writeInt(this.blockLevel);
        customPacketBuffer.writeInt(this.typeWork.ordinal());
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankCoolantList) {
            try {
                EncoderHandler.encode(customPacketBuffer, (FluidTank) fluidTank);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankInputList) {
            try {
                EncoderHandler.encode(customPacketBuffer, (FluidTank) fluidTank);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankOutputList) {
            try {
                EncoderHandler.encode(customPacketBuffer, (FluidTank) fluidTank);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Fluids.InternalFluidTank fluidTank : this.fluidTankHotCoolantList) {
            try {
                EncoderHandler.encode(customPacketBuffer, (FluidTank) fluidTank);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.red_timer.writeBuffer(customPacketBuffer);
        this.yellow_timer.writeBuffer(customPacketBuffer);
        return customPacketBuffer;
    }


    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.work = nbttagcompound.getBoolean("work");
        this.pressure = nbttagcompound.getInt("pressure");
        this.blockLevel = nbttagcompound.getInt("level");
        this.timer.readNBT(nbttagcompound);
        this.red_timer.readNBT(nbttagcompound.getCompound("red"));
        this.yellow_timer.readNBT(nbttagcompound.getCompound("yellow"));
        this.heat = nbttagcompound.getDouble("heat");
        this.typeWork = EnumTypeWork.values()[nbttagcompound.getInt("typeWork")];
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        final CompoundTag nbt = super.writeToNBT(nbttagcompound);
        nbt.putBoolean("work", this.work);
        nbt.putInt("pressure", this.pressure);
        nbt.putInt("level", this.blockLevel);
        this.timer.writeNBT(nbt);
        final CompoundTag nbt1 = new CompoundTag();
        this.red_timer.writeNBT(nbt1);
        nbt.put("red", nbt1);
        final CompoundTag nbt2 = new CompoundTag();
        this.yellow_timer.writeNBT(nbt2);
        nbt.put("yellow", nbt2);
        nbt.putDouble("heat", heat);
        nbt.putInt("typeWork", this.typeWork.ordinal());
        return nbt;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
    }

    @Override
    public CustomPacketBuffer writePacket() {
        return super.writePacket();
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer packetBuffer) {
        super.readUpdatePacket(packetBuffer);
        try {
            this.red_timer.readBuffer(packetBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.yellow_timer.readBuffer(packetBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        CustomPacketBuffer customPacketBuffer = super.writeUpdatePacket();
        this.red_timer.writeBuffer(customPacketBuffer);
        this.yellow_timer.writeBuffer(customPacketBuffer);
        return customPacketBuffer;
    }

    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        List<Fluids> inputs = new ArrayList<>();
        List<Fluids> outputs = new ArrayList<>();
        for (int i = 0; i < pos1.size(); i++) {
            int k = i % 4;
            ITank tank = ((ITank) this.getWorld().getBlockEntity(pos1.get(i)));
            switch (k) {
                case 0:
                    fluidTankInputList.add(tank.getTank());
                    tank.setFluid(net.minecraft.world.level.material.Fluids.WATER);
                    if (!tank.getTank().getFluid().isEmpty() && !tank.getTank().getFluid().getFluid().equals(net.minecraft.world.level.material.Fluids.WATER)) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    inputs.add(tank.getFluids());
                    break;
                case 1:
                    fluidTankOutputList.add(tank.getTank());
                    tank.setFluid(FluidName.fluidsteam.getInstance().get());
                    if (!tank.getTank().getFluid().isEmpty() && !tank
                            .getTank()
                            .getFluid()
                            .getFluid()
                            .equals(FluidName.fluidsteam.getInstance().get())) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    outputs.add(tank.getFluids());
                    break;
                case 2:
                    fluidTankCoolantList.add(tank.getTank());
                    tank.setFluid(FluidName.fluidcoolant.getInstance().get());
                    if (!tank.getTank().getFluid().isEmpty() && !tank
                            .getTank()
                            .getFluid()
                            .getFluid()
                            .equals(FluidName.fluidcoolant.getInstance().get())) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    inputs.add(tank.getFluids());
                    break;
                case 3:
                    fluidTankHotCoolantList.add(tank.getTank());
                    tank.setFluid(FluidName.fluidhot_coolant.getInstance().get());
                    if (!tank.getTank().getFluid().isEmpty() && !tank
                            .getTank()
                            .getFluid()
                            .getFluid()
                            .equals(FluidName.fluidhot_coolant.getInstance().get())) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    outputs.add(tank.getFluids());
                    break;
            }
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IInput.class
                );
        for (BlockPos pos2 : pos1) {
            IInput input = (IInput) this.getWorld().getBlockEntity(pos2);
            inputs.forEach(input1 -> input.addFluids(input1));

        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IOutput.class
                );
        for (BlockPos pos2 : pos1) {
            IOutput output = (IOutput) this.getWorld().getBlockEntity(pos2);
            outputs.forEach(output1 -> output.addFluids(output1));

        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        for (BlockPos pos2 : pos1) {
            ISocket output = (ISocket) this.getWorld().getBlockEntity(pos2);
            this.energy = output.getEnergy();
        }
        if (!this.getWorld().isClientSide) {
            reactor = new LogicFluidReactor(this);
            reactor.temp_heat = this.heat;
            new PacketUpdateFieldTile(this, "reactor", this.reactor.getGeneration());
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISecurity.class
                );
        for (BlockPos pos2 : pos1) {
            ISecurity security = (ISecurity) this.getWorld().getBlockEntity(pos2);
            securities.add(security);

        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ILevelFuel.class
                );
        for (BlockPos pos2 : pos1) {
            ILevelFuel levelFuel = (ILevelFuel) this.getWorld().getBlockEntity(pos2);
            levelFuel.setMainMultiElement(this);

        }
        if (!this.getWorld().isClientSide) {
            this.reactorsModules.load();
        }
        if (this.isFull()) {
            if (this.typeWork == EnumTypeWork.LEVEL_INCREASE) {
                this.energy.onUnloaded();
                this.energy.setDirections(ModUtils.allFacings, ModUtils.noFacings);
                this.energy.delegate = null;
                this.energy.createDelegate();
                this.energy.onLoaded();
                switch (this.blockLevel) {
                    case 0:
                        this.energy.setCapacity(4000000);
                        break;
                    case 1:
                        this.energy.setCapacity(50000000);
                        break;
                    case 2:
                        this.energy.setCapacity(200000000);
                        break;
                    case 3:
                        this.energy.setCapacity(500000000);
                        break;

                }

            }
        }


    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public boolean isWork() {
        return this.work;
    }

    @Override
    public void setWork(final boolean work) {
        this.work = work;
        if (this.work) {
            this.setSecurity(EnumTypeSecurity.STABLE);
        } else {
            this.setSecurity(EnumTypeSecurity.NONE);
        }
    }

    @Override
    public double getHeat() {
        return this.heat;
    }

    @Override
    public void setHeat(final double var1) {
        this.heat = var1;
        if (this.heat > this.getMaxHeat()) {
            this.heat = this.getMaxHeat();
        }
        if (this.heat < this.getStableMaxHeat()) {
            this.setSecurity(EnumTypeSecurity.STABLE);
            this.setTime(EnumTypeSecurity.STABLE);
        } else if (this.heat >= this.getStableMaxHeat() && this.heat <=
                this.getStableMaxHeat() + (this.getMaxHeat() - this.getStableMaxHeat()) * 0.75) {
            this.setSecurity(EnumTypeSecurity.UNSTABLE);
            this.setTime(EnumTypeSecurity.UNSTABLE);
        } else {
            this.setSecurity(EnumTypeSecurity.ERROR);
            this.setTime(EnumTypeSecurity.ERROR);
        }
    }

    @Override
    public void setUpdate() {
        this.reactor = null;
    }

    @Override
    public int getBlockLevel() {
        return enumFluidReactors.ordinal();
    }

    @Override
    public int getMaxHeat() {
        return (int) (enumFluidReactors.getMaxHeat() * this.reactorsModules.getStableHeat());
    }

    @Override
    public int getStableMaxHeat() {
        return (int) (enumFluidReactors.getMaxStable() * this.reactorsModules.getStableHeat());
    }

    @Override
    public double getOutput() {
        return this.output;
    }

    @Override
    public void setOutput(final double output) {
        this.output = output * this.reactorsModules.getGeneration();
    }

    @Override
    public ItemStack getItemAt(final int x, final int y) {
        return this.reactorsElements.get(y * this.getWidth() + x);
    }

    @Override
    public void setItemAt(final int x, final int y) {
        this.reactorsElements.set(y * this.getWidth() + x, ItemStack.EMPTY);
    }

    @Override
    public void explode() {
        int weight = (this.getMultiBlockStucture().maxWeight + this.getMultiBlockStucture().minWeight);
        int height = (this.getMultiBlockStucture().maxHeight + this.getMultiBlockStucture().minHeight);
        int length = (this.getMultiBlockStucture().maxLength + this.getMultiBlockStucture().maxLength);
        List<ChunkPos> chunkPosList = new ArrayList<>();
        ChunkPos chunkPos = this.getWorld().getChunkAt(this.pos).getPos();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                chunkPosList.add(new ChunkPos(chunkPos.x + x, chunkPos.z + z));
            }
        }
        double rad = this.rad.getEnergy() / 9;

        this.setFull(false);
        this.activate = false;
        Explosion explosion = new Explosion(this.level, null, this.getPos().getX() + weight, this.getPos().getY() + height,
                this.getPos().getZ() + length, 25, false, Explosion.BlockInteraction.KEEP
        );

        level.setBlock(pos, Blocks.AIR.defaultBlockState(),11);
        for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.getMultiBlockStucture().blockPosMap.entrySet()) {
            if (level.random.nextInt(2) == 0) {
                continue;
            }
            BlockPos pos1;
            switch (Direction.values()[facing]) {
                case NORTH:
                    pos1 = pos.offset(entry.getKey());
                    break;
                case EAST:
                    pos1 = pos.offset(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                    break;
                case WEST:
                    pos1 = pos.offset(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.offset(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            level.setBlock(pos1,Blocks.AIR.defaultBlockState(),11);

        }

        if (this.level.dimension() == Level.OVERWORLD) {
            for (ChunkPos pos1 : chunkPosList) {
                if (!pos1.equals(chunkPos)) {
                    new PacketUpdateRadiationValue(pos1, (int) (rad * 10));
                } else {
                    new PacketUpdateRadiationValue(pos1, (int) (rad * 50));
                }
            }
        }
        new PacketExplosion(explosion, 25, false, false);
    }

    public List<Fluids.InternalFluidTank> getFluidTankCoolantList() {
        return fluidTankCoolantList;
    }

    public EnumTypeWork getTypeWork() {
        return typeWork;
    }

    public List<Fluids.InternalFluidTank> getFluidTankHotCoolantList() {
        return fluidTankHotCoolantList;
    }

    public List<Fluids.InternalFluidTank> getFluidTankInputList() {
        return fluidTankInputList;
    }

    public List<Fluids.InternalFluidTank> getFluidTankOutputList() {
        return fluidTankOutputList;
    }

    public ComponentBaseEnergy getRad() {
        return rad;
    }

    @Override
    public void setRad(final double rad) {
        this.rad.addEnergy(rad * this.reactorsModules.getRadiation());
        if (this.rad.getEnergy() >= this.rad.getCapacity()) {
            this.explode();
        }
    }

    @Override
    public ITypeRector getTypeRector() {
        return ITypeRector.FLUID;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void setTime(final EnumTypeSecurity enumTypeSecurity) {
        if (this.security == enumTypeSecurity) {
            switch (enumTypeSecurity) {
                case STABLE:
                    timer = new Timer(9999, 0, 0);
                    break;
                case ERROR:
                    timer = new Timer(0, 2, 30);
                    break;
                case UNSTABLE:
                    timer = new Timer(0, 15, 0);
                    break;
            }
        }
    }

    @Override
    public void workTimer() {
        switch (this.security) {
            case UNSTABLE:
                this.yellow_timer.work();
                if (!this.red_timer.getMinute(3)) {
                    this.red_timer.rework();
                }
                break;
            case STABLE:
                this.timer.work();
                if (!this.yellow_timer.getMinute(15)) {
                    this.yellow_timer.rework();
                }
                if (!this.red_timer.getMinute(3)) {
                    this.red_timer.rework();
                }
                break;
            case ERROR:
                this.red_timer.work();
                break;
        }
    }

    @Override
    public EnumTypeSecurity getSecurity() {
        return this.security;
    }

    @Override
    public void setSecurity(final EnumTypeSecurity enumTypeSecurity) {
        if (this.security != enumTypeSecurity) {
            this.security = enumTypeSecurity;
            new PacketUpdateFieldTile(this, "security", this.security);
            securities.forEach(security -> {
                security.setActive(enumTypeSecurity.name().toLowerCase());
                security.setSecurity(enumTypeSecurity);
            });
        }
    }

    @Override
    public int getWidth() {
        return enumFluidReactors.getWidth();
    }

    @Override
    public int getHeight() {
        return enumFluidReactors.getHeight();
    }

    @Override
    public int getLevelReactor() {
        return this.blockLevel;
    }

    @Override
    public int getMaxLevelReactor() {
        return enumFluidReactors.ordinal() + 1;
    }


    @Override
    public void increaseLevelReactor() {
        this.blockLevel++;
    }

    @Override
    public ComponentBaseEnergy getRadiation() {
        return this.rad;
    }

    @Override
    public double getModuleStableHeat() {
        return reactorsModules.getStableHeat();
    }

    @Override
    public double getModuleRadiation() {
        return reactorsModules.getRadiation();
    }

    @Override
    public double getModuleGeneration() {
        return reactorsModules.getGeneration();
    }

    @Override
    public double getModuleVent() {
        return reactorsModules.getVent();
    }

    @Override
    public double getModuleComponentVent() {
        return reactorsModules.getComponentVent();
    }

    @Override
    public double getModuleCapacitor() {
        return reactorsModules.getCapacitor();
    }

    @Override
    public double getModuleExchanger() {
        return reactorsModules.getExchanger();
    }

    @Override
    public FluidTank getInputTank() {
        for (Fluids.InternalFluidTank tank : this.fluidTankInputList) {
            if (tank.getFluidAmount() >= 5 * this.getPressure()) {
                return tank;
            }
        }
        return this.fluidTankInputList.get(0);
    }

    @Override
    public FluidTank getCoolantTank() {
        for (Fluids.InternalFluidTank tank : this.fluidTankCoolantList) {
            if (tank.getFluidAmount() >= 5) {
                return tank;
            }
        }
        return this.fluidTankCoolantList.get(0);
    }

    @Override
    public FluidTank getHotCoolantTank() {
        for (Fluids.InternalFluidTank tank : this.fluidTankHotCoolantList) {
            if (tank.getFluidAmount() >= 2) {
                return tank;
            }
        }
        return this.fluidTankHotCoolantList.get(0);
    }

    @Override
    public FluidTank getOutputTank() {
        for (Fluids.InternalFluidTank tank : this.fluidTankOutputList) {
            if (tank.getFluidAmount() >= 2 * this.getPressure()) {
                return tank;
            }
        }
        return this.fluidTankOutputList.get(0);
    }

    @Override
    public int getPressure() {
        return this.pressure;
    }

    @Override
    public void addPressure(int pressure) {
        this.pressure += pressure;
    }

    @Override
    public void removePressure(int pressure) {
        this.pressure -= pressure;
    }

}
