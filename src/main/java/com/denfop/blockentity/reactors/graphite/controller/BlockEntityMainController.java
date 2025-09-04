package com.denfop.blockentity.reactors.graphite.controller;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.reactors.*;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blockentity.reactors.graphite.*;
import com.denfop.blocks.FluidName;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGraphiteReactor;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryScheduleReactor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.screen.ScreenGraphiteController;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockEntityMainController extends BlockEntityMultiBlockBase implements IGraphiteReactor, IUpdatableTileEvent {

    public final EnumGraphiteReactors enumFluidReactors;
    public final Inventory reactorsElements;
    public final InventoryReactorModules<BlockEntityMainController> reactorsModules;
    public final InventoryScheduleReactor scheduleReactor;
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
    public LogicGraphiteReactor reactor;
    public EnumTypeSecurity security = EnumTypeSecurity.NONE;
    public int levelBlock = 0;
    public boolean stable_sensor;
    public boolean heat_sensor;
    public List<Fluids.InternalFluidTank> cells = new ArrayList<>();
    private List<IGraphiteController> listGraphiteController = new ArrayList<>();
    private List<IExchanger> listExchanger = new ArrayList<>();
    private List<ICapacitor> listCapacitor = new ArrayList<>();
    private List<ICooling> listCoolant = new ArrayList<>();

    public BlockEntityMainController(final MultiBlockStructure multiBlockStructure, EnumGraphiteReactors enumFluidReactors, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(multiBlockStructure, block, pos, state);
        this.enumFluidReactors = enumFluidReactors;
        this.reactorsModules = new InventoryReactorModules<>(this);
        this.reactorsElements = new Inventory(this, Inventory.TypeItemSlot.INPUT,
                enumFluidReactors.getHeight() * enumFluidReactors.getWidth()
        ) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (scheduleReactor.getAccepts().isEmpty()) {
                    if (stack.getItem() instanceof IReactorItem) {
                        IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                        return ((BlockEntityMainController) this.base).getLevelReactor() >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_EXCHANGER);
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
        this.scheduleReactor = new InventoryScheduleReactor(this, 3, enumFluidReactors.ordinal() + 1, enumFluidReactors.getWidth(),
                enumFluidReactors.getHeight()
        );
        this.rad = this.addComponent(new ComponentBaseEnergy(EnergyType.RADIATION, this, enumFluidReactors.getRadiation() * 100));
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.reactor_safety_doom.info"));
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
    public double getModuleExchanger() {
        return reactorsModules.getExchanger();
    }

    @Override
    public double getModuleComponentVent() {
        return reactorsModules.getComponentVent();
    }

    @Override
    public double getModuleCapacitor() {
        return reactorsModules.getCapacitor();
    }

    public LogicReactor getReactor() {
        if (this.reactor == null) {
            this.reactor = new LogicGraphiteReactor(this);
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
                this.reactor = new LogicGraphiteReactor(this);
            }
            try {
                this.reactor.setGeneration((int) DecoderHandler.decode(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.cells.clear();
            listCapacitor.clear();
            listExchanger.clear();
            listGraphiteController.clear();
            listCoolant.clear();
        }
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (this.levelBlock > 0) {
                setWork(!this.work);
            }
        } else if (var2 == 1) {
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
                switch (this.levelBlock) {
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
                    switch (this.levelBlock) {
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
            reactor = new LogicGraphiteReactor(this);
            reactor.temp_heat = this.heat;
            new PacketUpdateFieldTile(this, "reactor", this.reactor.getGeneration());
        } else {
            if (this.full) {

                if (this.typeWork == EnumTypeWork.WORK) {
                    this.energy.buffer.capacity = Math.max(this.output, this.energy.getDefaultCapacity());
                    if (this.getWorld().getGameTime() % 20 == 0 && this.work) {
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
                } else {
                    if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                        this.levelBlock++;
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
        if (heat < 0) {
            heat = 0;
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.pressure = customPacketBuffer.readInt();
        this.work = customPacketBuffer.readBoolean();
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
        this.levelBlock = customPacketBuffer.readInt();

        this.typeWork = EnumTypeWork.values()[customPacketBuffer.readInt()];

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
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.pressure);
        customPacketBuffer.writeBoolean(work);
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
        customPacketBuffer.writeInt(this.levelBlock);
        customPacketBuffer.writeInt(this.typeWork.ordinal());

        this.red_timer.writeBuffer(customPacketBuffer);
        this.yellow_timer.writeBuffer(customPacketBuffer);
        return customPacketBuffer;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.work = nbttagcompound.getBoolean("work");
        this.pressure = nbttagcompound.getInt("pressure");
        this.levelBlock = nbttagcompound.getInt("level");
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
        nbt.putInt("level", this.levelBlock);
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
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        return super.writeUpdatePacket();
    }

    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );

        for (int i = 0; i < pos1.size(); i++) {
            int k = i % 4;
            ITank tank = ((ITank) this.getWorld().getBlockEntity(pos1.get(i)));
            switch (k) {
                case 0:
                    tank.setFluid(net.minecraft.world.level.material.Fluids.WATER);
                    if (!tank.getTank().getFluid().isEmpty() && !tank.getTank().getFluid().getFluid().equals(net.minecraft.world.level.material.Fluids.WATER)) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    break;
                case 1:
                    tank.setFluid(FluidName.fluidcarbondioxide.getInstance().get());
                    if (!tank.getTank().getFluid().isEmpty() && !tank
                            .getTank()
                            .getFluid()
                            .getFluid()
                            .equals(FluidName.fluidcarbondioxide.getInstance().get())) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    break;
                case 2:
                    tank.setFluid(FluidName.fluidsteam.getInstance().get());
                    if (!tank.getTank().getFluid().isEmpty() && !tank
                            .getTank()
                            .getFluid()
                            .getFluid()
                            .equals(FluidName.fluidsteam.getInstance().get())) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    break;
                case 3:
                    tank.setFluid(FluidName.fluidoxygen.getInstance().get());
                    if (!tank.getTank().getFluid().isEmpty() && !tank
                            .getTank()
                            .getFluid()
                            .getFluid()
                            .equals(FluidName.fluidoxygen.getInstance().get())) {
                        tank.getTank().drain(tank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                    break;
            }
            cells.add(tank.getTank());
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IGraphiteController.class
                );
        int i = 0;
        for (BlockPos pos2 : pos1) {
            IGraphiteController recirculationPump = ((IGraphiteController) this.getWorld().getBlockEntity(pos2));
            recirculationPump.setIndex(i);
            this.listGraphiteController.add(recirculationPump);
            i++;
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICapacitor.class
                );
        for (BlockPos pos2 : pos1) {
            ICapacitor iInterCooler = ((ICapacitor) this.getWorld().getBlockEntity(pos2));
            this.listCapacitor.add(iInterCooler);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        for (BlockPos pos2 : pos1) {
            IExchanger compressor = ((IExchanger) this.getWorld().getBlockEntity(pos2));
            this.listExchanger.add(compressor);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICooling.class
                );
        for (BlockPos pos2 : pos1) {
            ICooling compressor = ((ICooling) this.getWorld().getBlockEntity(pos2));
            this.listCoolant.add(compressor);
        }

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        for (BlockPos pos2 : pos1) {
            ISocket socket = ((ISocket) this.getWorld().getBlockEntity(pos2));
            this.energy = socket.getEnergy();
        }


        reactor = new LogicGraphiteReactor(this);
        reactor.temp_heat = this.heat;
        new PacketUpdateFieldTile(this, "reactor", this.reactor.getGeneration());
        this.reactorsModules.load();
        if (isFull()) {
            if (this.typeWork == EnumTypeWork.LEVEL_INCREASE) {
                this.energy.onUnloaded();
                this.energy.setDirections(ModUtils.allFacings, ModUtils.noFacings);
                this.energy.delegate = null;
                this.energy.createDelegate();
                this.energy.onLoaded();
                switch (this.levelBlock) {
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
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGraphiteController((ContainerMenuGraphiteReactor) menu);
    }

    @Override
    public ContainerMenuGraphiteReactor getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuGraphiteReactor(this, entityPlayer);
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
        if (this.getStableMaxHeat() == 0) {
            this.setSecurity(EnumTypeSecurity.STABLE);
            this.setTime(EnumTypeSecurity.STABLE);
        } else if (this.heat < this.getStableMaxHeat()) {
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
    public Energy getEnergy() {
        return energy;
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

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
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
            level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);


        }
        if (this.level.dimension() == Level.OVERWORLD) {
            for (ChunkPos pos1 : chunkPosList) {
                if (!this.level.isClientSide) {
                    if (!pos1.equals(chunkPos)) {
                        new PacketUpdateRadiationValue(pos1, (int) (rad * 10), (ServerLevel) this.level);
                    } else {
                        new PacketUpdateRadiationValue(pos1, (int) (rad * 50), (ServerLevel) this.level);
                    }
                }
            }
        }
        new PacketExplosion(explosion, 25, false, false);
    }

    public EnumTypeWork getTypeWork() {
        return typeWork;
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
        return this.levelBlock;
    }

    @Override
    public int getMaxLevelReactor() {
        return enumFluidReactors.ordinal() + 1;
    }


    @Override
    public void increaseLevelReactor() {
        this.levelBlock++;
    }

    @Override
    public ComponentBaseEnergy getRadiation() {
        return this.rad;
    }


    @Override
    public FluidTank getWaterTank() {
        return this.cells.get(0);
    }

    @Override
    public FluidTank getCoalDioxideTank() {
        return this.cells.get(1);
    }

    @Override
    public ItemStack getGraphite(final int index) {
        return this.listGraphiteController.get(index).getGraphite();
    }

    @Override
    public boolean canWorkWithGraphite() {
        final List<Integer> list = this.reactor.getListIndexRod();
        for (int i : list) {
            if (this.getGraphite(i).isEmpty() && this.getFuelGraphite(i) <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getLevelGraphite(final int index) {
        return this.listGraphiteController.get(index).getLevelGraphite();
    }

    @Override
    public double getFuelGraphite(final int index) {
        return this.listGraphiteController.get(index).getFuelGraphite();
    }

    @Override
    public void consumeFuelGraphite(final int index, final double fuel) {
        this.listGraphiteController.get(index).consumeFuelGraphite(fuel);
    }

    @Override
    public void consumeGraphite(final int index) {
        this.listGraphiteController.get(index).consumeGraphite();
    }

    @Override
    public FluidTank getSteamTank() {
        return this.cells.get(2);
    }

    @Override
    public FluidTank getOxideTank() {
        return this.cells.get(3);
    }

    @Override
    public double workCoolant(double heat) {

        for (ICooling cooling : this.listCoolant) {
            heat = cooling.work(heat);
        }
        return heat;
    }

    @Override
    public double getMulHeatRod(final int x, final int y, final ItemStack stack) {
        double coef = 1;
        for (ICapacitor capacitor : this.listCapacitor) {
            coef *= capacitor.getPercent(x);
        }
        coef *= 1 + ((this.listGraphiteController.get(x).getLevelGraphite() - 1) / 55D);
        return coef;
    }

    @Override
    public double getMulHeat(final int x, final int y, final ItemStack stack) {
        double coef = 1;
        for (IExchanger exchanger : this.listExchanger) {
            coef *= exchanger.getPercent(x);
        }

        return coef;
    }

    @Override
    public double getMulOutput(final int x, final int y, final ItemStack stack) {

        IGraphiteController controller = this.listGraphiteController.get(x);
        double level = 1 + (controller.getLevelGraphite() - 1 / 4D) * 0.05;

        return 1 * level;
    }

    @Override
    public void updateDataReactor() {
        this.reactor = null;
    }

}
