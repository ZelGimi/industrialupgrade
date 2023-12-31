package com.denfop.tiles.reactors.gas.controller;

import com.denfop.Config;
import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.api.reactors.IReactorItem;
import com.denfop.api.reactors.ITypeRector;
import com.denfop.api.reactors.InvSlotReactorModules;
import com.denfop.api.reactors.LogicGasReactor;
import com.denfop.api.reactors.LogicReactor;
import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.FluidName;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGasMainController;
import com.denfop.container.ContainerWaterMainController;
import com.denfop.gui.GuiGasController;
import com.denfop.gui.GuiWaterMainController;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemComponentVent;
import com.denfop.items.reactors.ItemsFan;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.gas.ICell;
import com.denfop.tiles.reactors.gas.ICompressor;
import com.denfop.tiles.reactors.gas.ICoolant;
import com.denfop.tiles.reactors.gas.IInterCooler;
import com.denfop.tiles.reactors.gas.IRecirculationPump;
import com.denfop.tiles.reactors.gas.IRegenerator;
import com.denfop.tiles.reactors.gas.ISocket;
import com.denfop.tiles.reactors.water.ITank;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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
import java.util.Map;

public class TileEntityMainController extends TileMultiBlockBase implements IGasReactor, IUpdatableTileEvent {

    public final EnumGasReactors enumFluidReactors;
    public final InvSlot reactorsElements;
    private final ComponentBaseEnergy rad;
    public final InvSlotReactorModules<TileEntityMainController> reactorsModules;
    public Timer timer = new Timer(9999, 0, 0);
    public Timer red_timer = new Timer(0, 2, 30);
    public Timer yellow_timer = new Timer(0, 15, 0);
    public AdvEnergy energy;
    public EnumTypeWork typeWork = EnumTypeWork.WORK;

    public int pressure = 1;
    public boolean work = false;
    public double heat;
    public double output = 0;
    public LogicGasReactor reactor;
    public EnumTypeSecurity security = EnumTypeSecurity.NONE;
    public int level = 0;
    private List<IRecirculationPump> listReCirculationPump = new ArrayList<>();
    private List<IInterCooler> listInterCooler = new ArrayList<>();
    private List<ICompressor> listCompressor = new ArrayList<>();
    private List<ICoolant> coolantList = new ArrayList<>();
    private List<IRegenerator> regeneratorList = new ArrayList<>();

    public TileEntityMainController(final MultiBlockStructure multiBlockStructure, EnumGasReactors enumFluidReactors) {
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
                    return ((TileEntityMainController) this.base).getLevelReactor() >= iReactorItem.getLevel() && (iReactorItem.getType() != EnumTypeComponent.HEAT_SINK || (stack.getItem() instanceof ItemComponentVent));
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
            this.reactor = new LogicGasReactor(this);
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
                this.reactor = new LogicGasReactor(this);
            }
            try {
                this.reactor.setGeneration((int) DecoderHandler.decode(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ContainerGasMainController getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerGasMainController(this,entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGasController(getGuiContainer(var1));
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.cells.clear();
            listReCirculationPump.clear();
            listInterCooler.clear();
            listCompressor.clear();
            coolantList.clear();
            regeneratorList.clear();
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
        if (!this.getWorld().isRemote) {
            this.reactorsModules.load();
            try {
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
            }catch (Exception e){};
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
            reactor = new LogicGasReactor(this);
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
                        } else if (!this.yellow_timer.canWork()) {
                            this.explode();
                            this.reactor = null;
                        } else if (!this.red_timer.canWork()) {
                            this.explode();
                            this.reactor = null;
                        }

                    }
                    if(this.work)
                    this.energy.addEnergy(this.output);
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
        if(heat < 0)
            heat = 0;
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
                        ICell.class
                );

        for (int i = 0; i < pos1.size(); i++) {
            int k = i % 4;
            ICell tank = ((ICell) this.getWorld().getTileEntity(pos1.get(i)));
            switch (k) {
                case 0:
                    tank.setFluid(FluidName.fluidHelium.getInstance());

                    break;
                case 1:
                    tank.setFluid(FluidRegistry.WATER);
                    break;
                case 2:
                    tank.setFluid(FluidName.fluidhyd.getInstance());
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
                        IRecirculationPump.class
                );
        for (BlockPos pos2 : pos1) {
            IRecirculationPump recirculationPump = ((IRecirculationPump) this.getWorld().getTileEntity(pos2));
            this.listReCirculationPump.add(recirculationPump);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IInterCooler.class
                );
        for (BlockPos pos2 : pos1) {
            IInterCooler iInterCooler = ((IInterCooler) this.getWorld().getTileEntity(pos2));
            this.listInterCooler.add(iInterCooler);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICompressor.class
                );
        for (BlockPos pos2 : pos1) {
            ICompressor compressor = ((ICompressor) this.getWorld().getTileEntity(pos2));
            this.listCompressor.add(compressor);
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
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICoolant.class
                );
        for (BlockPos pos2 : pos1) {
            ICoolant coolant = ((ICoolant) this.getWorld().getTileEntity(pos2));
            this.coolantList.add(coolant);
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRegenerator.class
                );
        for (BlockPos pos2 : pos1) {
            IRegenerator regenerator = ((IRegenerator) this.getWorld().getTileEntity(pos2));
            this.regeneratorList.add(regenerator);
        }
        reactor = new LogicGasReactor(this);
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
        if(this.getStableMaxHeat() == 0){
            this.setSecurity(EnumTypeSecurity.STABLE);
            this.setTime(EnumTypeSecurity.STABLE);
        }
        else  if (this.heat < this.getStableMaxHeat()) {
            this.setSecurity(EnumTypeSecurity.STABLE);
            this.setTime(EnumTypeSecurity.STABLE);
        } else if (this.heat >= this.getStableMaxHeat() && this.heat <=
                this.getStableMaxHeat() +  (this.getMaxHeat() -  this.getStableMaxHeat()) * 0.75 ) {
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

        this.setFull(false);
        this.activate = false;
        Explosion explosion = new Explosion(this.world, null, this.getPos().getX() + weight, this.getPos().getY() + height,
                this.getPos().getZ() + length, 25, false, true
        );
        if(Config.explodeReactor) {
            if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.getWorld(), explosion)) {
                return;
            }
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }else{
            for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.getMultiBlockStucture().blockPosMap.entrySet()) {
                if(world.rand.nextInt(2) == 0){
                    continue;
                }
                BlockPos pos1;
                switch (EnumFacing.values()[facing]) {
                    case NORTH:
                        pos1 = pos.add(entry.getKey());
                        break;
                    case EAST:
                        pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                        break;
                    case WEST:
                        pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                        break;
                    case SOUTH:
                        pos1 = pos.add(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + facing);
                }
                world.setBlockToAir(pos1);

            }
        }
        if (this.world.provider.getDimension() == 0) {
            for (ChunkPos pos1 : chunkPosList) {
                if (!pos1.equals(chunkPos)) {
                    new PacketUpdateRadiationValue(pos1, (int) (rad * 10));
                } else {
                    new PacketUpdateRadiationValue(pos1, (int) (rad * 50));
                }
            }
        }
        if(Config.explodeReactor)
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
    public int getTemperatureRefrigerator() {
        double i = 0;
        for (ICoolant coolant : this.coolantList) {
            i += coolant.getLevelRefrigerator();
        }
        return (int) (i / this.coolantList.size());
    }

    @Override
    public FluidTank getHeliumTank() {
        return this.cells.get(0);
    }

    @Override
    public FluidTank getWaterTank(final int i) {
        return this.cells.get(1);
    }

    @Override
    public FluidTank getHydrogenTank(final int i) {
        return this.cells.get(2);
    }

    @Override
    public FluidTank getOxygenTank(final int i) {
        return this.cells.get(3);
    }

    @Override
    public int getPressure(final int i) {
        return listCompressor.get(i).getPressure();
    }

    @Override
    public int getLengthCompressors() {
        return listCompressor.size();
    }

    @Override
    public int getEnergyCompressor(final int i) {
        return listCompressor.get(i).getEnergy();
    }

    @Override
    public void addHeliumToRegenerate(final int col) {
        int temp = col;
        for (IRegenerator regenerator : this.regeneratorList) {
            int temp1 = regenerator.getMaxHelium() - regenerator.getHelium();
            temp1 = Math.min(temp1, temp);
            regenerator.addHelium(temp1);
            temp -= temp1;
            if (temp <= 0) {
                break;
            }
        }
    }

    @Override
    public int getCapacityHelium() {
        return this.cells.get(0).getCapacity();
    }

    @Override
    public int getHeliumToRegenerate() {
        int temp = 0;
        for (IRegenerator regenerator : this.regeneratorList) {
            temp+=regenerator.getHelium();
        }
        return temp;
    }

    @Override
    public int getEnergyFan(final int i) {
        return listInterCooler.get(i).getEnergy();
    }

    @Override
    public int getPowerFan(final int i) {
        return listInterCooler.get(i).getPower();
    }

    @Override
    public void damageFan(final int i) {
        ((ItemsFan) listInterCooler.get(i).getSlot().get().getItem()).applyCustomDamage(
                listInterCooler.get(i).getSlot().get(),
                -1,
                null
        );

    }

    @Override
    public int getLengthFan() {
        return this.listInterCooler.size();
    }

    @Override
    public boolean hasFan() {
        return !this.listInterCooler.isEmpty();
    }

    @Override
    public boolean hasPump() {
        return !listReCirculationPump.isEmpty();
    }

    @Override
    public int getLengthPump() {
        return listReCirculationPump.size();
    }

    @Override
    public int getPowerPump(final int i) {
        return listReCirculationPump.get(i).getPower();
    }

    @Override
    public int getEnergyPump(final int i) {
        return listReCirculationPump.get(i).getEnergy();
    }

    @Override
    public void damagePump(final int i) {
        ((ItemsPumps) listReCirculationPump.get(i).getSlot().get().getItem()).applyCustomDamage(listReCirculationPump
                .get(i)
                .getSlot()
                .get(), -1, null);
    }

}
