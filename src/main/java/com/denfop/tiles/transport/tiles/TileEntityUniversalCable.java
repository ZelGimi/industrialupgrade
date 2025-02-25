package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.space.fakebody.EnumOperation;
import com.denfop.api.sytem.EnergyEvent;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.sytem.EnumTypeEvent;
import com.denfop.api.sytem.IAcceptor;
import com.denfop.api.sytem.IConductor;
import com.denfop.api.sytem.IEmitter;
import com.denfop.api.sytem.ITile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUniversalCable;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.tiles.transport.types.UniversalType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TileEntityUniversalCable extends TileEntityMultiCable implements IEnergyConductor, IHeatConductor, ICoolConductor,
        IConductor {


    public boolean addedToEnergyNet;
    protected UniversalType cableType;
    private boolean needUpdate;
    private ChunkPos chunkPos;
    EnumTypeOperation enumTypeOperation = null;
    private boolean heat;
    private boolean quantum;
    private boolean experience;
    private boolean solarium;
    private boolean radiation;
    private boolean cold;

    public TileEntityUniversalCable(UniversalType cableType) {
        super(cableType);
        this.cableType = cableType;
    }

    public TileEntityUniversalCable() {
        super(UniversalType.glass);
        this.cableType = UniversalType.glass;
        this.connectivity = 0;
        this.addedToEnergyNet = false;

    }

    public static TileEntityUniversalCable delegate(UniversalType cableType) {
        return new TileEntityUniversalCable(cableType);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockUniversalCable.universal_cable;
    }

    public BlockTileEntity getBlock() {
        return IUItem.universalcableblock;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    @Override
    public InfoCable getCable() {
        return cable;
    }

    private InfoCable cable;

    @Override
    public void setCable(final InfoCable cable) {
        this.cable = cable;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = UniversalType.values[nbt.getByte("cableType") & 255];
        heat = nbt.getBoolean("Heat");
        quantum = nbt.getBoolean("Quantum");
        experience = nbt.getBoolean("Experience");
        solarium = nbt.getBoolean("Solarium");
        radiation = nbt.getBoolean("Radiation");
        cold = nbt.getBoolean("Cold");
    }

    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();

    @Override
    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public void AddTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            if(!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
            updateConnect = true;
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        nbt.setBoolean("Heat", heat);
        nbt.setBoolean("Quantum", quantum);
        nbt.setBoolean("Experience", experience);
        nbt.setBoolean("Solarium", solarium);
        nbt.setBoolean("Radiation", radiation);
        nbt.setBoolean("Cold", cold);
        return nbt;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));

        if (heat) {
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
        }
        if (cold) {
            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
        }
        if (quantum) {
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.QUANTUM, this));
        }
        if (experience) {
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.EXPERIENCE, this));
        }
        if (solarium) {
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.SOLARIUM, this));
        }
        if (radiation) {
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.RADIATION, this));
        }
        this.needUpdate = true;
    }

    Map<EnumFacing, IHeatTile> energyHeatConductorMap = new HashMap<>();

    @Override
    public void AddHeatTile(final IHeatTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
            if (!this.energyHeatConductorMap.containsKey(dir)) {
                this.energyHeatConductorMap.put(dir, tile);
                validHeatReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            updateConnect = true;
        }
    }

    private com.denfop.api.heat.InfoCable typeHeatCable;

    @Override
    public com.denfop.api.heat.InfoCable getHeatCable() {
        return typeHeatCable;
    }

    @Override
    public void setHeatCable(final com.denfop.api.heat.InfoCable cable) {
        typeHeatCable = cable;
    }


    boolean updateConnect = false;

    @Override
    public void RemoveHeatTile(final IHeatTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
            this.energyHeatConductorMap.remove(dir);
            final Iterator<InfoTile<IHeatTile>> iter = validHeatReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IHeatTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public Map<EnumFacing, IHeatTile> getHeatTiles() {
        return energyHeatConductorMap;
    }

    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();

    @Override
    public List<InfoTile<IHeatTile>> getHeatValidReceivers() {
        return validHeatReceivers;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            this.energyTypeConductorMap.clear();
            validTypeReceivers.clear();
            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();
            this.validHeatReceivers.clear();
            this.energyHeatConductorMap.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));

            if (heat) {
                MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            }
            if (cold) {
                MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            }
            if (quantum) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.QUANTUM, this));
            }
            if (experience) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.EXPERIENCE, this));
            }
            if (solarium) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.SOLARIUM, this));
            }
            if (radiation) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.RADIATION, this));
            }
            this.needUpdate = false;
            this.updateConnectivity();
        }
        if (enumTypeOperation != null) {
            switch (enumTypeOperation) {
                case HEAT:
                    this.heat = true;
                    MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
                    break;
                case COLD:
                    this.cold = true;
                    MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
                    break;
                case QUANTUM:
                    this.quantum = true;
                    MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.QUANTUM, this));
                    break;
                case EXPERIENCE:
                    this.experience = true;
                    MinecraftForge.EVENT_BUS.post(new EnergyEvent(
                            this.getWorld(),
                            EnumTypeEvent.LOAD,
                            EnergyType.EXPERIENCE,
                            this
                    ));
                    break;
                case SOLARIUM:
                    this.solarium = true;
                    MinecraftForge.EVENT_BUS.post(new EnergyEvent(
                            this.getWorld(),
                            EnumTypeEvent.LOAD,
                            EnergyType.SOLARIUM,
                            this
                    ));
                    break;
                case RADIATION:
                    this.radiation = true;

                    break;
            }
            enumTypeOperation = null;
        }

        if (updateConnect) {
            updateConnect = false;
            this.updateConnectivity();
        }
    }

    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();

    private long id;

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public Map<EnumFacing, IEnergyTile> getTiles() {
        return energyConductorMap;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote && !addedToEnergyNet) {

            this.energyConductorMap.clear();
            validReceivers.clear();
            this.energyTypeConductorMap.clear();
            validTypeReceivers.clear();
            this.energyCoolConductorMap.clear();
            validColdReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));
            if (heat) {
                MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            }
            if (cold) {
                MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            }
            if (quantum) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.QUANTUM, this));
            }
            if (experience) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.EXPERIENCE, this));
            }
            if (solarium) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.SOLARIUM, this));
            }
            if (radiation) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.RADIATION, this));
            }
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

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
        if (stack.getItem() == IUItem.qcable && !quantum){
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.QUANTUM;
            return true;
        }
        if (stack.getItem() == IUItem.scable && !solarium){
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.SOLARIUM;
            return true;
        }
        if (stack.getItem() == IUItem.radcable_item && !radiation){
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.RADIATION;
            return true;
        }
        if (stack.getItem() == IUItem.expcable && !experience){
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.EXPERIENCE;
            return true;
        }
        if (stack.getItem() == IUItem.coolpipes && stack.getItemDamage() == 4 && !cold){
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.COLD;
            return true;
        }
        if (stack.getItem() == IUItem.pipes && stack.getItemDamage() == 4 && !heat){
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.HEAT;
            return true;
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            if (heat) {
                MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            }
            if (cold) {
                MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
            }
            if (quantum) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.QUANTUM, this));
            }
            if (experience) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(
                        this.getWorld(),
                        EnumTypeEvent.UNLOAD,
                        EnergyType.EXPERIENCE,
                        this
                ));
            }
            if (solarium) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.SOLARIUM, this));
            }
            if (radiation) {
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.RADIATION, this));
            }

            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> stacks =  super.getSelfDrops(fortune, wrench);
        if (quantum)
            stacks.add(new ItemStack(IUItem.qcable));
        if (solarium)
            stacks.add(new ItemStack(IUItem.scable));
        if (radiation)
            stacks.add(new ItemStack(IUItem.radcable_item));
        if (experience)
            stacks.add(new ItemStack(IUItem.expcable));
        if (heat)
            stacks.add(new ItemStack(IUItem.pipes,1,4));
        if (cold)
            stacks.add(new ItemStack(IUItem.coolpipes,1,4));
        return stacks;
    }

    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.universal_cable, 1, this.cableType.ordinal());
    }


    public void updateConnectivity() {
        World world = this.getWorld();
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            if (getBlackList().contains(dir)) {
                continue;
            }
            Object tile = energyConductorMap.get(dir);
            if (tile != null) {
                if ((tile instanceof IEnergyAcceptor && ((IEnergyAcceptor) tile).acceptsEnergyFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof IEnergyEmitter && ((IEnergyEmitter) tile).emitsEnergyTo(
                        this,
                        dir.getOpposite()
                )) && this.canInteractWith()) {
                    newConnectivity = (byte) (newConnectivity + 1);

                }
            } else {
                Map<EnumFacing, ITile> map = this.energyTypeConductorMap.computeIfAbsent(
                        EnergyType.SOLARIUM,
                        k -> new HashMap<>()
                );
                tile = map.get(dir);

                if (tile != null) {
                    if ((tile instanceof IAcceptor && ((IAcceptor) tile).acceptsFrom(
                            this,
                            dir.getOpposite()
                    ) || tile instanceof IEmitter && ((IEmitter) tile).emitsTo(
                            this,
                            dir.getOpposite()
                    ))) {
                        newConnectivity = (byte) (newConnectivity + 1);

                    }
                } else {
                    tile = energyHeatConductorMap.get(dir);
                    if (tile != null) {
                        if ((tile instanceof IHeatAcceptor && ((IHeatAcceptor) tile).acceptsHeatFrom(
                                this,
                                dir.getOpposite()
                        ) || tile instanceof IHeatEmitter && ((IHeatEmitter) tile).emitsHeatTo(
                                this,
                                dir.getOpposite()
                        )) && this.canInteractWith()) {
                            newConnectivity = (byte) (newConnectivity + 1);

                        }
                    } else {
                        tile = this.energyCoolConductorMap.get(dir);
                        if (tile != null) {
                            if ((tile instanceof ICoolAcceptor && ((ICoolAcceptor) tile).acceptsCoolFrom(
                                    this,
                                    dir.getOpposite()
                            ) || tile instanceof ICoolEmitter && ((ICoolEmitter) tile).emitsCoolTo(
                                    this,
                                    dir.getOpposite()
                            )) && this.canInteractWith()) {
                                newConnectivity = (byte) (newConnectivity + 1);

                            }
                        } else {
                            map = this.energyTypeConductorMap.computeIfAbsent(
                                    EnergyType.QUANTUM,
                                    k -> new HashMap<>()
                            );
                            tile = map.get(dir);

                            if (tile != null) {
                                if ((tile instanceof IAcceptor && ((IAcceptor) tile).acceptsFrom(
                                        this,
                                        dir.getOpposite()
                                ) || tile instanceof IEmitter && ((IEmitter) tile).emitsTo(
                                        this,
                                        dir.getOpposite()
                                )) && this.canInteractWith()) {
                                    newConnectivity = (byte) (newConnectivity + 1);

                                }

                            } else {
                                map = this.energyTypeConductorMap.computeIfAbsent(
                                        EnergyType.RADIATION,
                                        k -> new HashMap<>()
                                );
                                tile = map.get(dir);

                                if (tile != null) {
                                    if ((tile instanceof IAcceptor && ((IAcceptor) tile).acceptsFrom(
                                            this,
                                            dir.getOpposite()
                                    ) || tile instanceof IEmitter && ((IEmitter) tile).emitsTo(
                                            this,
                                            dir.getOpposite()
                                    )) && this.canInteractWith()) {
                                        newConnectivity = (byte) (newConnectivity + 1);

                                    }

                                } else {
                                    map = this.energyTypeConductorMap.computeIfAbsent(
                                            EnergyType.EXPERIENCE,
                                            k -> new HashMap<>()
                                    );
                                    tile = map.get(dir);
                                    if (tile != null) {
                                        if ((tile instanceof IAcceptor && ((IAcceptor) tile).acceptsFrom(
                                                this,
                                                dir.getOpposite()
                                        ) || tile instanceof IEmitter && ((IEmitter) tile).emitsTo(
                                                this,
                                                dir.getOpposite()
                                        )) && this.canInteractWith()) {
                                            newConnectivity = (byte) (newConnectivity + 1);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        setConnectivity(newConnectivity);
        this.cableItem = cableType;
    }


    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean canInteractWith() {

        return true;
    }

    public double getConductionLoss() {
        return this.cableType.loss;
    }

    @Override
    public double getConductorBreakdownEnergy() {
        return this.cableType.capacity + 1;
    }

    @Override
    public double getConductorBreakdownEnergy(
            EnergyType energyType
    ) {
        return Integer.MAX_VALUE;
    }

    Map<EnergyType, Map<EnumFacing, ITile>> energyTypeConductorMap = new HashMap<>();


    Map<EnergyType, List<InfoTile<ITile>>> validTypeReceivers = new HashMap<>();

    public long getIdNetwork() {
        return this.id;
    }

    int hashCodeSource;

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }


    public void setId(final long id) {
        this.id = id;
    }

    Map<EnumFacing, ICoolTile> energyCoolConductorMap = new HashMap<>();

    @Override
    public void AddCoolTile(final ICoolTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
            if (!this.energyCoolConductorMap.containsKey(dir)) {
                this.energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            updateConnect = true;
        }
    }

    @Override
    public void RemoveCoolTile(final ICoolTile tile, final EnumFacing dir) {
        if (!this.getWorld().isRemote) {
            this.energyCoolConductorMap.remove(dir);
            final Iterator<InfoTile<ICoolTile>> iter = validColdReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ICoolTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public Map<EnumFacing, ICoolTile> getCoolTiles() {
        return energyCoolConductorMap;
    }

    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();

    @Override
    public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
        return validColdReceivers;
    }


    @Override
    public List<InfoTile<ITile>> getValidReceivers(EnergyType type) {
        return validTypeReceivers.computeIfAbsent(type, k -> new LinkedList<>());
    }

    public Map<EnumFacing, ITile> getTiles(EnergyType type) {
        return energyTypeConductorMap.computeIfAbsent(type, k -> new HashMap<>());
    }


    @Override
    public com.denfop.api.sytem.InfoCable getCable(EnergyType type) {
        return typeCable;
    }

    private com.denfop.api.sytem.InfoCable typeCable;

    @Override
    public void setCable(EnergyType type, final com.denfop.api.sytem.InfoCable cable) {
        this.typeCable = cable;
    }

    @Override
    public void RemoveTile(EnergyType type, ITile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            this.energyTypeConductorMap.computeIfAbsent(type, k -> new HashMap<>()).remove(facing1);
            final Iterator<InfoTile<ITile>> iter = validTypeReceivers.computeIfAbsent(type, k -> new LinkedList<>()).iterator();
            while (iter.hasNext()) {
                InfoTile<ITile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public void AddTile(EnergyType type, ITile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            final Map<EnumFacing, ITile> map = this.energyTypeConductorMap.computeIfAbsent(type, k -> new HashMap<>());
            if (!map.containsKey(facing1)) {
                map.put(facing1, tile);
                validTypeReceivers.computeIfAbsent(type, k -> new LinkedList<>()).add(new InfoTile<>(
                        tile,
                        facing1.getOpposite()
                ));
            }
            updateConnect = true;
        }
    }

    @Override
    public double getConductorBreakdownHeat() {
        return 16001;
    }


    public double getConductorBreakdownCold() {
        return 65;
    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cableType);
            EncoderHandler.encode(packet, connectivity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cableType = UniversalType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update_render() {
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }
    }

    private com.denfop.api.cool.InfoCable typeColdCable;

    @Override
    public com.denfop.api.cool.InfoCable getCoolCable() {
        return typeColdCable;
    }

    @Override
    public void setCoolCable(final com.denfop.api.cool.InfoCable cable) {
        typeColdCable = cable;
    }

    @Override
    public EnergyType getEnergyType() {
        return EnergyType.QUANTUM;
    }

    @Override
    public boolean hasEnergies() {
        return true;
    }

    List<EnergyType> energies = Arrays.asList(EnergyType.QUANTUM, EnergyType.SOLARIUM, EnergyType.EXPERIENCE,
            EnergyType.RADIATION);

    @Override
    public List<EnergyType> getEnergies() {
        return energies;
    }

    @Override
    public boolean acceptsCoolFrom(final ICoolEmitter var1, final EnumFacing var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean emitsCoolTo(final ICoolAcceptor var1, final EnumFacing var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final EnumFacing var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean emitsHeatTo(final IHeatAcceptor var1, final EnumFacing var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean acceptsFrom(final IEmitter var1, final EnumFacing var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean emitsTo(final IAcceptor var1, final EnumFacing var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public TileEntity getTile() {
        return this;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

}
