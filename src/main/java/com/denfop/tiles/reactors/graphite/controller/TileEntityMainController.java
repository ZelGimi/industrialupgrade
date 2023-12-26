package com.denfop.tiles.reactors.graphite.controller;

import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.reactors.IReactorItem;
import com.denfop.api.reactors.ITypeRector;
import com.denfop.api.reactors.InvSlotReactorModules;
import com.denfop.api.reactors.LogicGraphiteReactor;
import com.denfop.api.reactors.LogicReactor;
import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.FluidName;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGraphiteReactor;
import com.denfop.gui.GuiGraphiteController;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.graphite.ICapacitor;
import com.denfop.tiles.reactors.graphite.ICooling;
import com.denfop.tiles.reactors.graphite.IExchanger;
import com.denfop.tiles.reactors.graphite.IGraphiteController;
import com.denfop.tiles.reactors.graphite.ISocket;
import com.denfop.tiles.reactors.graphite.ITank;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntityMainController extends TileMultiBlockBase implements IGraphiteReactor, IUpdatableTileEvent {

    public final EnumGraphiteReactors enumFluidReactors;
    public final InvSlot reactorsElements;
    private final ComponentBaseEnergy rad;
    public final InvSlotReactorModules<TileEntityMainController> reactorsModules;
    public Timer timer = new Timer(9999, 0, 0);
    public Timer red_timer = new Timer(0, 5, 0);
    public Timer yellow_timer = new Timer(0, 30, 0);
    public AdvEnergy energy;
    public EnumTypeWork typeWork = EnumTypeWork.WORK;

    public int pressure = 1;
    public boolean work = false;
    public double heat;
    public double output = 0;
    public LogicGraphiteReactor reactor;
    public EnumTypeSecurity security = EnumTypeSecurity.NONE;
    public int level = 0;
    private List<IGraphiteController> listGraphiteController = new ArrayList<>();
    private List<IExchanger> listExchanger = new ArrayList<>();
    private List<ICapacitor> listCapacitor = new ArrayList<>();
    private List<ICooling> listCoolant = new ArrayList<>();

    public TileEntityMainController(final MultiBlockStructure multiBlockStructure, EnumGraphiteReactors enumFluidReactors) {
        super(multiBlockStructure);
        this.enumFluidReactors = enumFluidReactors;
        this.reactorsModules = new InvSlotReactorModules<>(this);
        this.reactorsElements = new InvSlot(this, InvSlot.TypeItemSlot.INPUT,
                enumFluidReactors.getHeight() * enumFluidReactors.getWidth()
        ) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (stack.getItem() instanceof IReactorItem) {
                    IReactorItem iReactorItem = (IReactorItem) stack.getItem();
                    return ((TileEntityMainController) this.base).getLevelReactor() >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_EXCHANGER);
                } else {
                    return false;
                }
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                reactor = null;
            }
        };
        this.reactorsElements.setStackSizeLimit(1);
        this.rad = this.addComponent(new ComponentBaseEnergy(EnergyType.RADIATION, this, enumFluidReactors.getRadiation()));
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
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            if (this.level > 0) {
                setWork(!this.work);
            }
        }
        if (var2 == 1) {
            this.pressure++;
            this.pressure = Math.min(5, this.pressure);
        } else if (var2 == 2) {
            this.pressure--;
            this.pressure = Math.max(1, this.pressure);
        } else {
            if (this.typeWork == EnumTypeWork.WORK && this.getLevelReactor() < this.getMaxLevelReactor()) {
                this.typeWork = EnumTypeWork.LEVEL_INCREASE;
                this.energy.onUnloaded();
                this.energy.setDirections(ModUtils.allFacings, ModUtils.noFacings);
                this.energy.delegate = null;
                this.energy.createDelegate();
                this.energy.onLoaded();
                switch (this.level) {
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
                this.energy.storage = 0;
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote && this.full) {
            this.reactorsModules.load();
            if (this.typeWork == EnumTypeWork.LEVEL_INCREASE) {
                this.energy.onUnloaded();
                this.energy.setDirections(ModUtils.allFacings, ModUtils.noFacings);
                this.energy.delegate = null;
                this.energy.createDelegate();
                this.energy.onLoaded();
                switch (this.level) {
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
            ChunkPos chunkPos = this.getWorld().getChunkFromBlockCoords(this.pos).getPos();
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
        if (!this.getWorld().isRemote) {
            ChunkPos chunkPos = this.getWorld().getChunkFromBlockCoords(this.pos).getPos();
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
                    if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.work) {
                        try {
                            reactor.onTick();
                        } catch (Exception ignored) {
                        }
                        ;
                        if (this.rad.getEnergy() >= this.rad.getCapacity() * 0.5 && this.rad.getEnergy() < this.rad.getCapacity() * 0.75) {
                            this.setSecurity(EnumTypeSecurity.UNSTABLE);
                        }
                        workTimer();
                        if (!this.timer.canWork()) {
                            this.explode();
                            this.reactor = null;
                            this.updateFull();
                        } else if (!this.yellow_timer.canWork()) {
                            this.explode();
                            this.reactor = null;
                            this.updateFull();
                        } else if (!this.red_timer.canWork()) {
                            this.explode();
                            this.reactor = null;
                            this.updateFull();
                        }

                    }
                    if (this.work) {
                        this.energy.addEnergy(this.output);
                    }
                } else {
                    if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                        this.level++;
                        this.typeWork = EnumTypeWork.WORK;
                        this.energy.onUnloaded();
                        this.energy.setCapacity(this.energy.defaultCapacity);
                        this.energy.setDirections(ModUtils.noFacings, ModUtils.allFacings);
                        this.energy.delegate = null;
                        this.energy.createDelegate();
                        this.energy.onLoaded();
                        this.energy.storage = 0;
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
        this.level = customPacketBuffer.readInt();

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
        customPacketBuffer.writeInt(this.level);
        customPacketBuffer.writeInt(this.typeWork.ordinal());

        this.red_timer.writeBuffer(customPacketBuffer);
        this.yellow_timer.writeBuffer(customPacketBuffer);
        return customPacketBuffer;
    }


    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.work = nbttagcompound.getBoolean("work");
        this.pressure = nbttagcompound.getInteger("pressure");
        this.level = nbttagcompound.getInteger("level");
        this.timer.readNBT(nbttagcompound);
        this.red_timer.readNBT(nbttagcompound.getCompoundTag("red"));
        this.yellow_timer.readNBT(nbttagcompound.getCompoundTag("yellow"));
        this.heat = nbttagcompound.getDouble("heat");
        this.typeWork = EnumTypeWork.values()[nbttagcompound.getInteger("typeWork")];
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        final NBTTagCompound nbt = super.writeToNBT(nbttagcompound);
        nbt.setBoolean("work", this.work);
        nbt.setInteger("pressure", this.pressure);
        nbt.setInteger("level", this.level);
        this.timer.writeNBT(nbt);
        final NBTTagCompound nbt1 = new NBTTagCompound();
        this.red_timer.writeNBT(nbt1);
        nbt.setTag("red", nbt1);
        final NBTTagCompound nbt2 = new NBTTagCompound();
        this.yellow_timer.writeNBT(nbt2);
        nbt.setTag("yellow", nbt2);
        nbt.setDouble("heat", heat);
        nbt.setInteger("typeWork", this.typeWork.ordinal());
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

    public List<Fluids.InternalFluidTank> cells = new ArrayList<>();

    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );

        for (int i = 0; i < pos1.size(); i++) {
            int k = i % 4;
            ITank tank = ((ITank) this.getWorld().getTileEntity(pos1.get(i)));
            switch (k) {
                case 0:
                    tank.setFluid(FluidRegistry.WATER);

                    break;
                case 1:
                    tank.setFluid(FluidName.fluidco2.getInstance());
                    break;
                case 2:
                    tank.setFluid(FluidName.fluidsteam.getInstance());
                    break;
                case 3:
                    tank.setFluid(FluidName.fluidoxy.getInstance());
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
            IGraphiteController recirculationPump = ((IGraphiteController) this.getWorld().getTileEntity(pos2));
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
            ICapacitor iInterCooler = ((ICapacitor) this.getWorld().getTileEntity(pos2));
            this.listCapacitor.add(iInterCooler);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        for (BlockPos pos2 : pos1) {
            IExchanger compressor = ((IExchanger) this.getWorld().getTileEntity(pos2));
            this.listExchanger.add(compressor);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICooling.class
                );
        for (BlockPos pos2 : pos1) {
            ICooling compressor = ((ICooling) this.getWorld().getTileEntity(pos2));
            this.listCoolant.add(compressor);
        }

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        for (BlockPos pos2 : pos1) {
            ISocket socket = ((ISocket) this.getWorld().getTileEntity(pos2));
            this.energy = socket.getEnergy();
        }


        reactor = new LogicGraphiteReactor(this);
        reactor.temp_heat = this.heat;
        new PacketUpdateFieldTile(this, "reactor", this.reactor.getGeneration());

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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGraphiteController(getGuiContainer(var1));
    }

    @Override
    public ContainerGraphiteReactor getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerGraphiteReactor(this, entityPlayer);
    }

    @Override
    public double getHeat() {
        return this.heat;
    }

    @Override
    public void setUpdate() {
        this.reactor = null;
    }

    @Override
    public int getLevel() {
        return enumFluidReactors.ordinal();
    }

    @Override
    public void setHeat(final double var1) {
        this.heat = var1;
        if(this.heat > this.getMaxHeat())
            this.heat = this.getMaxHeat();
        if (this.heat < this.getStableMaxHeat()) {
            this.setSecurity(EnumTypeSecurity.STABLE);
            this.setTime(EnumTypeSecurity.STABLE);
        } else if (this.heat >= this.getStableMaxHeat() && this.heat <=
                this.getMaxHeat()) {
            this.setSecurity(EnumTypeSecurity.UNSTABLE);
            this.setTime(EnumTypeSecurity.UNSTABLE);
        } else {
            this.setSecurity(EnumTypeSecurity.ERROR);
            this.setTime(EnumTypeSecurity.ERROR);
        }
    }

    @Override
    public void setOutput(final double output) {
        this.output = output * this.reactorsModules.getGeneration();
    }

    @Override
    public void setRad(final double rad) {
        this.rad.addEnergy(rad  * this.reactorsModules.getRadiation());
        if (this.rad.getEnergy() >= this.rad.getCapacity()) {
            this.explode();
        }
    }

    @Override
    public int getMaxHeat() {
        return (int) (enumFluidReactors.getMaxHeat()  * this.reactorsModules.getStableHeat());
    }

    @Override
    public int getStableMaxHeat() {
        return (int) (enumFluidReactors.getMaxStable() * this.reactorsModules.getStableHeat());
    }

    @Override
    public AdvEnergy getEnergy() {
        return energy;
    }

    @Override
    public double getOutput() {
        return this.output;
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
        ChunkPos chunkPos = this.getWorld().getChunkFromBlockCoords(this.pos).getPos();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                chunkPosList.add(new ChunkPos(chunkPos.x + x, chunkPos.z + z));
            }
        }
        double rad = this.rad.getEnergy() / 9;


        Explosion explosion = new Explosion(this.world, null, this.getPos().getX() + weight, this.getPos().getY() + height,
                this.getPos().getZ() + length, 25, false, true
        );
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.getWorld(), explosion)) {
            return;
        }
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        if (this.world.provider.getDimension() == 0) {
            for (ChunkPos pos1 : chunkPosList) {
                if (!pos1.equals(chunkPos)) {
                    new PacketUpdateRadiationValue(pos1, (int) (rad * 10));
                } else {
                    new PacketUpdateRadiationValue(pos1, (int) (rad * 50));
                }
            }
        }
        new PacketExplosion(explosion, 25, false, true);
    }


    public EnumTypeWork getTypeWork() {
        return typeWork;
    }


    public ComponentBaseEnergy getRad() {
        return rad;
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
                    timer = new Timer(0, 5, 0);
                    break;
                case UNSTABLE:
                    timer = new Timer(0, 30, 0);
                    break;
            }
        }
    }

    @Override
    public void workTimer() {
        switch (this.security) {
            case UNSTABLE:
                this.yellow_timer.work();
                if (!this.red_timer.getMinute(5)) {
                    this.red_timer.rework();
                }
                break;
            case STABLE:
                this.timer.work();
                if (!this.yellow_timer.getMinute(30)) {
                    this.yellow_timer.rework();
                }
                if (!this.red_timer.getMinute(5)) {
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
        return this.level;
    }

    @Override
    public int getMaxLevelReactor() {
        return enumFluidReactors.ordinal() + 1;
    }


    @Override
    public void increaseLevelReactor() {
        this.level++;
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
