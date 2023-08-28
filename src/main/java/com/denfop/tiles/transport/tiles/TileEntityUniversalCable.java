package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.cool.CoolNet;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.heat.HeatNet;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.sytem.EnergyBase;
import com.denfop.api.sytem.EnergyEvent;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.sytem.EnumTypeEvent;
import com.denfop.api.sytem.IAcceptor;
import com.denfop.api.sytem.IConductor;
import com.denfop.api.sytem.IEmitter;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUniversalCable;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.tiles.transport.types.UniversalType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import scala.actors.threadpool.Arrays;

import java.io.IOException;
import java.util.List;


public class TileEntityUniversalCable extends TileEntityMultiCable implements IEnergyConductor, IHeatConductor, ICoolConductor,
        IConductor {


    public boolean addedToEnergyNet;
    protected UniversalType cableType;

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
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = UniversalType.values[nbt.getByte("cableType") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {


            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.QUANTUM, this));
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.EXPERIENCE, this));
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.SOLARIUM, this));

            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.QUANTUM, this));
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.EXPERIENCE, this));
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.SOLARIUM, this));

            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }


    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.universal_cable, 1, this.cableType.ordinal());
    }


    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }

    }


    private void updateConnectivity() {
        World world = this.getWorld();
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);

            Object tile = EnergyNetGlobal.instance.getTile(world, this.pos.offset(dir));
            if (tile != EnergyNetGlobal.EMPTY) {
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
                tile = EnergyBase.SE.getSubTile(world, this.pos.offset(dir));

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
                    tile = HeatNet.instance.getSubTile(world, this.pos.offset(dir));
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
                        tile = CoolNet.instance.getSubTile(world, this.pos.offset(dir));
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
                            tile = EnergyBase.QE.getSubTile(world, this.pos.offset(dir));
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
                                tile = EnergyBase.EXP.getSubTile(world, this.pos.offset(dir));
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

        setConnectivity(newConnectivity);

    }


    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return this.canInteractWith();
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return this.canInteractWith();
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
        return Arrays.asList(new EnergyType[]{EnergyType.QUANTUM, EnergyType.SOLARIUM, EnergyType.EXPERIENCE});
    }

    @Override
    public boolean acceptsCoolFrom(final ICoolEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean emitsCoolTo(final ICoolAcceptor var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean emitsHeatTo(final IHeatAcceptor var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean acceptsFrom(final IEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean emitsTo(final IAcceptor var1, final EnumFacing var2) {
        return true;
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
