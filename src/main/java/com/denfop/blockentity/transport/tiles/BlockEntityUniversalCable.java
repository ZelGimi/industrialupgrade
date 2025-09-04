package com.denfop.blockentity.transport.tiles;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.event.load.EnergyTileLoadEvent;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.interfaces.EnergyAcceptor;
import com.denfop.api.energy.interfaces.EnergyConductor;
import com.denfop.api.energy.interfaces.EnergyEmitter;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.otherenergies.common.*;
import com.denfop.api.otherenergies.cool.ICoolAcceptor;
import com.denfop.api.otherenergies.cool.ICoolConductor;
import com.denfop.api.otherenergies.cool.ICoolEmitter;
import com.denfop.api.otherenergies.cool.ICoolTile;
import com.denfop.api.otherenergies.cool.event.CoolTileLoadEvent;
import com.denfop.api.otherenergies.cool.event.CoolTileUnloadEvent;
import com.denfop.api.otherenergies.heat.IHeatAcceptor;
import com.denfop.api.otherenergies.heat.IHeatConductor;
import com.denfop.api.otherenergies.heat.IHeatEmitter;
import com.denfop.api.otherenergies.heat.IHeatTile;
import com.denfop.api.otherenergies.heat.event.HeatTileLoadEvent;
import com.denfop.api.otherenergies.heat.event.HeatTileUnloadEvent;
import com.denfop.blockentity.transport.types.ICableItem;
import com.denfop.blockentity.transport.types.UniversalType;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;


public class BlockEntityUniversalCable extends BlockEntityMultiCable implements EnergyConductor, IHeatConductor, ICoolConductor,
        IConductor {


    private final ConductorInfo conductor;
    private final ConductorInfo conductorHeat;
    private final ConductorInfo conductorCool;
    public boolean addedToEnergyNet;
    protected UniversalType cableType;
    EnumTypeOperation enumTypeOperation = null;
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    Map<Direction, IHeatTile> energyHeatConductorMap = new HashMap<>();
    boolean updateConnect = false;
    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();
    List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    Map<EnergyType, Map<Direction, ITile>> energyTypeConductorMap = new HashMap<>();
    Map<EnergyType, List<InfoTile<ITile>>> validTypeReceivers = new HashMap<>();
    int hashCodeSource;
    Map<Direction, ICoolTile> energyCoolConductorMap = new HashMap<>();
    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();
    List<EnergyType> energies = Arrays.asList(EnergyType.QUANTUM, EnergyType.SOLARIUM, EnergyType.EXPERIENCE,
            EnergyType.RADIATION
    );
    Map<EnergyType, ConductorInfo> conductorInfoMap = new HashMap<>();
    private boolean needUpdate;
    private ChunkPos chunkPos;
    private boolean heat;
    private boolean quantum;
    private boolean experience;
    private boolean solarium;
    private boolean radiation;
    private boolean cold;
    private InfoCable cable;
    private com.denfop.api.otherenergies.heat.InfoCable typeHeatCable;
    private long id;
    private com.denfop.api.otherenergies.common.InfoCable typeCable;
    private com.denfop.api.otherenergies.cool.InfoCable typeColdCable;

    public BlockEntityUniversalCable(UniversalType cableType, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(cableType, block, pos, state);
        this.cableType = cableType;
        this.conductor = new ConductorInfo(pos, (EnergyConductor) this);
        this.conductorCool = new ConductorInfo(pos, (ICoolConductor) this);
        this.conductorHeat = new ConductorInfo(pos, (IHeatConductor) this);
    }

    @Override
    public ConductorInfo getHeatConductor() {
        return conductorHeat;
    }

    @Override
    public ConductorInfo getCoolConductor() {
        return conductorCool;
    }

    @Override
    public ConductorInfo getInfo(EnergyType energyType) {
        if (conductorInfoMap.isEmpty()) {
            if (getEnergies() == null || getEnergies().isEmpty()) {
                conductorInfoMap.put(getEnergyType(), new ConductorInfo(pos, this, getEnergyType()));
            } else {
                for (EnergyType e : getEnergies())
                    conductorInfoMap.put(e, new ConductorInfo(pos, this, e));
            }
        }
        return conductorInfoMap.get(energyType);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> info) {
        UniversalType type = cableType;
        double capacity;
        double loss;

        capacity = type.capacity;
        loss = type.loss;

        info.add(Localization.translate("iu.universal_cable.info"));
        info.add(ModUtils.getString(capacity) + " " + Localization.translate("iu.generic.text.EUt"));
        info.add(Localization.translate("cable.tooltip.loss", lossFormat.format(loss)));
        info.add(Localization.translate("iu.transport.cold") + "-" + ModUtils.getString(64) + " °C");
        info.add(Localization.translate("iu.transport.heat") + ModUtils.getString(16000) + " °C");

    }

    @Override
    public ConductorInfo getInfo() {
        return conductor;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == IUItem.qcable.getItem(0) && !quantum) {
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.QUANTUM;
            return true;
        }
        if (stack.getItem() == IUItem.scable.getItem(0) && !solarium) {
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.SOLARIUM;
            return true;
        }
        if (stack.getItem() == IUItem.radcable_item.getItem(0) && !radiation) {
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.RADIATION;
            return true;
        }
        if (stack.getItem() == IUItem.expcable.getItem(0) && !experience) {
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.EXPERIENCE;
            return true;
        }
        if (stack.getItem() == IUItem.coolpipes.getItem(4) && !cold) {
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.COLD;
            return true;
        }
        if (stack.getItem() == IUItem.pipes.getItem(4) && !heat) {
            stack.shrink(1);
            this.enumTypeOperation = EnumTypeOperation.HEAT;
            return true;
        }
        return super.onActivated(player, hand, side, vec3);
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    @Override
    public InfoCable getCable() {
        return cable;
    }

    @Override
    public void setCable(final InfoCable cable) {
        this.cable = cable;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> stacks = new LinkedList<>(super.getSelfDrops(fortune, wrench));
        if (quantum) {
            stacks.add(new ItemStack(IUItem.qcable.getItem(0)));
        }
        if (solarium) {
            stacks.add(new ItemStack(IUItem.scable.getItem()));
        }
        if (radiation) {
            stacks.add(new ItemStack(IUItem.radcable_item.getItem()));
        }
        if (experience) {
            stacks.add(new ItemStack(IUItem.expcable.getItem()));
        }
        if (heat) {
            stacks.add(new ItemStack(IUItem.pipes.getItem(4), 1));
        }
        if (cold) {
            stacks.add(new ItemStack(IUItem.coolpipes.getItem(4), 1));
        }
        return stacks;
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.cableType = UniversalType.values[nbt.getByte("cableType") & 255];
        heat = nbt.getBoolean("Heat");
        quantum = nbt.getBoolean("Quantum");
        experience = nbt.getBoolean("Experience");
        solarium = nbt.getBoolean("Solarium");
        radiation = nbt.getBoolean("Radiation");
        cold = nbt.getBoolean("Cold");
    }

    @Override
    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<EnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
            updateConnect = true;
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putByte("cableType", (byte) this.cableType.ordinal());
        nbt.putBoolean("Heat", heat);
        nbt.putBoolean("Quantum", quantum);
        nbt.putBoolean("Experience", experience);
        nbt.putBoolean("Solarium", solarium);
        nbt.putBoolean("Radiation", radiation);
        nbt.putBoolean("Cold", cold);
        return nbt;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
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

    @Override
    public void AddHeatTile(final IHeatTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyHeatConductorMap.containsKey(dir)) {
                this.energyHeatConductorMap.put(dir, tile);
                validHeatReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            updateConnect = true;
        }
    }

    @Override
    public com.denfop.api.otherenergies.heat.InfoCable getHeatCable() {
        return typeHeatCable;
    }

    @Override
    public void setHeatCable(final com.denfop.api.otherenergies.heat.InfoCable cable) {
        typeHeatCable = cable;
    }

    @Override
    public void RemoveHeatTile(final IHeatTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
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
    public Map<Direction, IHeatTile> getHeatTiles() {
        return energyHeatConductorMap;
    }

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

    @Override
    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide && !addedToEnergyNet) {

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


    public void onUnloaded() {
        if (!this.getWorld().isClientSide && this.addedToEnergyNet) {
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


    public void updateConnectivity() {
        Level world = this.getWorld();
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();

        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            if (getBlackList().contains(dir)) {
                continue;
            }
            Object tile = energyConductorMap.get(dir);
            if (tile != null) {
                newConnectivity = (byte) (newConnectivity + 1);
            } else {
                Map<Direction, ITile> map = this.energyTypeConductorMap.computeIfAbsent(
                        EnergyType.SOLARIUM,
                        k -> new HashMap<>()
                );
                tile = map.get(dir);

                if (tile != null) {
                    newConnectivity = (byte) (newConnectivity + 1);

                } else {
                    tile = energyHeatConductorMap.get(dir);
                    if (tile != null) {
                        newConnectivity = (byte) (newConnectivity + 1);
                    } else {
                        tile = this.energyCoolConductorMap.get(dir);
                        if (tile != null) {
                            newConnectivity = (byte) (newConnectivity + 1);

                        } else {
                            map = this.energyTypeConductorMap.computeIfAbsent(
                                    EnergyType.QUANTUM,
                                    k -> new HashMap<>()
                            );
                            tile = map.get(dir);

                            if (tile != null) {
                                newConnectivity = (byte) (newConnectivity + 1);


                            } else {
                                map = this.energyTypeConductorMap.computeIfAbsent(
                                        EnergyType.RADIATION,
                                        k -> new HashMap<>()
                                );
                                tile = map.get(dir);

                                if (tile != null) {
                                    newConnectivity = (byte) (newConnectivity + 1);


                                } else {
                                    map = this.energyTypeConductorMap.computeIfAbsent(
                                            EnergyType.EXPERIENCE,
                                            k -> new HashMap<>()
                                    );
                                    tile = map.get(dir);
                                    if (tile != null) {
                                        newConnectivity = (byte) (newConnectivity + 1);

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

    public boolean wrenchCanRemove(Player player) {
        return false;
    }

    public boolean acceptsEnergyFrom(EnergyEmitter emitter, Direction direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean emitsEnergyTo(EnergyAcceptor receiver, Direction direction) {
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

    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public void AddCoolTile(final ICoolTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyCoolConductorMap.containsKey(dir)) {
                this.energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
            updateConnect = true;
        }
    }

    @Override
    public void RemoveCoolTile(final ICoolTile tile, final Direction dir) {
        if (!this.getWorld().isClientSide) {
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
    public Map<Direction, ICoolTile> getCoolTiles() {
        return energyCoolConductorMap;
    }

    @Override
    public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
        return validColdReceivers;
    }

    @Override
    public List<InfoTile<ITile>> getValidReceivers(EnergyType type) {
        return validTypeReceivers.computeIfAbsent(type, k -> new LinkedList<>());
    }

    public Map<Direction, ITile> getTiles(EnergyType type) {
        return energyTypeConductorMap.computeIfAbsent(type, k -> new HashMap<>());
    }

    @Override
    public com.denfop.api.otherenergies.common.InfoCable getCable(EnergyType type) {
        return typeCable;
    }

    @Override
    public void setCable(EnergyType type, final com.denfop.api.otherenergies.common.InfoCable cable) {
        this.typeCable = cable;
    }

    @Override
    public void RemoveTile(EnergyType type, ITile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
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
    public void AddTile(EnergyType type, ITile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            final Map<Direction, ITile> map = this.energyTypeConductorMap.computeIfAbsent(type, k -> new HashMap<>());
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
        if (!this.getWorld().isClientSide) {
            this.updateConnectivity();
        }
    }

    @Override
    public com.denfop.api.otherenergies.cool.InfoCable getCoolCable() {
        return typeColdCable;
    }

    @Override
    public void setCoolCable(final com.denfop.api.otherenergies.cool.InfoCable cable) {
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

    @Override
    public List<EnergyType> getEnergies() {
        return energies;
    }

    @Override
    public boolean acceptsCoolFrom(final ICoolEmitter var1, final Direction var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean emitsCoolTo(final ICoolAcceptor var1, final Direction var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final Direction var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean emitsHeatTo(final IHeatAcceptor var1, final Direction var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean acceptsFrom(final IEmitter var1, final Direction var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public boolean emitsTo(final IAcceptor var1, final Direction var2) {
        return (!getBlackList().contains(var2));
    }

    @Override
    public BlockEntity getTile() {
        return this;
    }

}
