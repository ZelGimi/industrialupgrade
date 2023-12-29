package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.transport.FluidHandler;
import com.denfop.api.transport.ITransportAcceptor;
import com.denfop.api.transport.ITransportConductor;
import com.denfop.api.transport.ITransportEmitter;
import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportFluidItemSinkSource;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.event.TransportTileLoadEvent;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockItemPipes;
import com.denfop.container.SlotInfo;
import com.denfop.gui.GuiCable1;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.tiles.transport.types.ItemType;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TileEntityItemPipes extends TileEntityMultiCable implements ITransportConductor {


    public final SlotInfo list;
    public boolean addedToEnergyNet = false;
    public ItemType cableType;
    private boolean needUpdate;

    public TileEntityItemPipes() {
        super(ItemType.itemcable);
        this.cableType = ItemType.itemcable;
        this.list = new SlotInfo(this, 18, !this.cableType.isItem());

    }

    public TileEntityItemPipes(ItemType cableType) {
        super(cableType);
        this.cableType = cableType;
        this.list = new SlotInfo(this, 18, !this.cableType.isItem());

    }


    public static TileEntityItemPipes delegate(ItemType cableType) {
        return new TileEntityItemPipes(cableType);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockItemPipes.item_pipes;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockItemPipes;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = ItemType.values[nbt.getByte("cableType") & 0xFF];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!(getWorld()).isRemote) {
            EnumFacing[] var4 = EnumFacing.VALUES;


            for (EnumFacing dir : var4) {
                TileEntity tile = getWorld().getTileEntity(this.pos.offset(dir));
                if (!getBlackList().contains(dir)) {
                    if (tile != null) {

                         if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir) && tile.hasCapability(
                                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                                dir.getOpposite()
                        )) {
                            ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(
                                    this.world,

                                    getPos().offset(dir)
                            );
                            IItemHandler item_storage = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir

                                    .getOpposite());
                            IFluidHandler fluid_storage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir

                                    .getOpposite());
                            boolean isSink = false;
                            boolean isSource = false;
                            boolean isSinkFluid = false;
                            boolean isSourceFluid = false;
                            if (transportTile == null) {
                                List<EnumFacing> facingListSink = new ArrayList<>();

                                for (EnumFacing dir1 : var4) {
                                    TileEntity tile2 = getWorld().getTileEntity(getPos().offset(dir).offset(dir1));
                                    if (tile2 instanceof ITransportConductor) {
                                        ITransportConductor transportConductor = (ITransportConductor) tile2;
                                        if (transportConductor.isItem()) {
                                            if (!transportConductor.isOutput()) {
                                                isSink = true;
                                                facingListSink.add(dir);
                                            } else {
                                                isSource = true;
                                            }
                                        } else if (!transportConductor.isOutput()) {
                                            isSinkFluid = true;
                                            facingListSink.add(dir);
                                        } else {
                                            isSourceFluid = true;
                                        }
                                    }
                                }
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                        this.pos
                                                .offset(dir),
                                        item_storage,
                                        fluid_storage,
                                        isSink,
                                        isSource,
                                        isSinkFluid,
                                        isSourceFluid
                                );
                                transport.setFacingListSink(facingListSink);
                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                            } else {
                                TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) transportTile;
                                if (isItem()) {
                                    if (isOutput()) {
                                        transportFluidItemSinkSource.setSource(true);
                                    } else {
                                        transportFluidItemSinkSource.setSink(true);
                                        transportFluidItemSinkSource.canAdd(dir);
                                    }
                                } else if (isOutput()) {
                                    transportFluidItemSinkSource.setSourceFluid(true);
                                } else {
                                    transportFluidItemSinkSource.setSinkFluid(true);
                                    transportFluidItemSinkSource.canAdd(dir);
                                }
                                if (transportFluidItemSinkSource.isNeed_update()) {
                                    transportFluidItemSinkSource.setNeed_update(false);
                                    isSink = transportFluidItemSinkSource.isSink();
                                    isSource = transportFluidItemSinkSource.isSource();
                                    isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                                    isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                                    BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                                    IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                                    IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                                    List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();
                                    MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                            getWorld(), transportTile));
                                    final TransportFluidItemSinkSource trasport = new TransportFluidItemSinkSource(
                                            pos,
                                            handler,
                                            fluidHandler,
                                            isSink,
                                            isSource,
                                            isSinkFluid,
                                            isSourceFluid
                                    );
                                    trasport.setFacingListSink(enumFacings);
                                    MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                            getWorld(), trasport

                                    ));
                                }
                            }
                        } else if (isItem() && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir)) {
                            ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(
                                    this.world,

                                    getPos().offset(dir)
                            );
                            IItemHandler item_storage = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir

                                    .getOpposite());

                            boolean isSink = false;
                            boolean isSource = false;
                            if (transportTile == null) {
                                List<EnumFacing> facingListSink = new ArrayList<>();

                                for (EnumFacing dir1 : var4) {

                                    TileEntity tile2 = getWorld().getTileEntity(getPos().offset(dir).offset(dir1));
                                    if (tile2 instanceof ITransportConductor) {
                                        ITransportConductor transportConductor = (ITransportConductor) tile2;
                                        if (transportConductor.isItem()) {
                                            if (!transportConductor.isOutput()) {
                                                isSink = true;
                                                facingListSink.add(dir);

                                            } else {
                                                isSource = true;
                                            }
                                        }
                                    }
                                }
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(this.pos
                                        .offset(dir), item_storage, null, isSink, isSource, false, false);
                                transport.setFacingListSink(facingListSink);
                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport));
                            } else {
                                TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) transportTile;
                                if (isItem()) {
                                    if (isOutput()) {
                                        transportFluidItemSinkSource.setSource(true);
                                    } else {
                                        transportFluidItemSinkSource.setSink(true);
                                        transportFluidItemSinkSource.canAdd(dir);
                                    }
                                }
                                if (transportFluidItemSinkSource.isNeed_update()) {
                                    transportFluidItemSinkSource.setNeed_update(false);
                                    isSink = transportFluidItemSinkSource.isSink();
                                    isSource = transportFluidItemSinkSource.isSource();
                                    boolean isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                                    boolean isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                                    BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                                    IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                                    IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                                    List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();

                                    MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                            getWorld(), transportTile));
                                    final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                            pos,
                                            handler,
                                            fluidHandler,
                                            isSink,
                                            isSource,
                                            isSinkFluid,
                                            isSourceFluid
                                    );
                                    transport.setFacingListSink(enumFacings);
                                    MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                            getWorld(), transport

                                    ));
                                }
                            }
                        } else if (!isItem() && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir
                                .getOpposite())) {
                            ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(
                                    this.world,

                                    getPos().offset(dir)
                            );
                            IFluidHandler fluid_storage = tile.getCapability(
                                    CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                                    dir

                                            .getOpposite()
                            );
                            boolean isSink = false;
                            boolean isSource = false;
                            boolean isSinkFluid = false;
                            boolean isSourceFluid = false;
                            if (transportTile == null) {
                                List<EnumFacing> facingListSink = new ArrayList<>();

                                for (EnumFacing dir1 : var4) {
                                    TileEntity tile2 = getWorld().getTileEntity(getPos().offset(dir).offset(dir1));
                                    if (tile2 instanceof ITransportConductor) {
                                        ITransportConductor transportConductor = (ITransportConductor) tile2;
                                        if (!transportConductor.isItem()) {
                                            if (!transportConductor.isOutput()) {
                                                isSinkFluid = true;
                                                facingListSink.add(dir1);
                                            } else {
                                                isSourceFluid = true;
                                            }
                                        }
                                    }
                                }
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(this.pos
                                        .offset(dir), null, fluid_storage, isSink, isSource, isSinkFluid, isSourceFluid);
                                transport.setFacingListSink(facingListSink);
                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                            } else {
                                TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) transportTile;
                                if (!isItem()) {
                                    if (isOutput()) {
                                        transportFluidItemSinkSource.setSourceFluid(true);
                                    } else {
                                        transportFluidItemSinkSource.setSinkFluid(true);
                                        transportFluidItemSinkSource.canAdd(dir);
                                    }
                                }
                                if (transportFluidItemSinkSource.isNeed_update()) {
                                    transportFluidItemSinkSource.setNeed_update(false);
                                    isSink = transportFluidItemSinkSource.isSink();
                                    isSource = transportFluidItemSinkSource.isSource();
                                    isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                                    isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                                    BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                                    IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                                    IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                                    List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();

                                    MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                            getWorld(), transportTile));
                                    final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                            pos,
                                            handler,
                                            fluidHandler,
                                            isSink,
                                            isSource,
                                            isSinkFluid,
                                            isSourceFluid
                                    );
                                    transport.setFacingListSink(enumFacings);
                                    MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                            getWorld(), transport

                                    ));
                                }
                            }
                        }
                    }
                }
            }
            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            }
            this.addedToEnergyNet = true;
            updateConnectivity();
        }
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 < EnumFacing.VALUES.length) {
            super.updateTileServer(var1, var2);
            EnumFacing[] var4 = EnumFacing.VALUES;

            for (EnumFacing dir : var4) {
                ITransportTile tile = TransportNetGlobal.instance.getSubTile(this.world, this.pos.offset(dir));
                if (!(tile instanceof ITransportConductor)) {
                    if (tile != null) {
                        TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) tile;
                        if (isItem()) {
                            if (isOutput()) {
                                transportFluidItemSinkSource.setSource(false);
                            } else {
                                transportFluidItemSinkSource.setSink(false);
                                transportFluidItemSinkSource.removeFacing(dir);
                            }
                        } else if (isOutput()) {
                            transportFluidItemSinkSource.setSourceFluid(false);
                        } else {
                            transportFluidItemSinkSource.setSinkFluid(false);
                            transportFluidItemSinkSource.removeFacing(dir);
                        }
                        if (transportFluidItemSinkSource.need_delete()) {
                            MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                    getWorld(), tile));
                        } else if (transportFluidItemSinkSource.isNeed_update()) {
                            transportFluidItemSinkSource.setNeed_update(false);
                            boolean isSink = transportFluidItemSinkSource.isSink();
                            boolean isSource = transportFluidItemSinkSource.isSource();
                            boolean isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                            boolean isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                            BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                            IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                            IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                            List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();

                            MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                    getWorld(), tile));
                            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                    pos,
                                    handler,
                                    fluidHandler,
                                    isSink,
                                    isSource,
                                    isSinkFluid,
                                    isSourceFluid
                            );
                            transport.setFacingListSink(enumFacings);
                            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                    getWorld(), transport

                            ));
                        }
                    }
                }
            }
            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            }
            this.needUpdate = true;
        } else {

        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            EnumFacing[] var4 = EnumFacing.VALUES;


            for (EnumFacing dir : var4) {
                TileEntity tile = getWorld().getTileEntity(this.pos.offset(dir));

                if (tile != null) {

                    if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir) && tile.hasCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                            dir.getOpposite()
                    )) {
                        ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(
                                this.world,

                                getPos().offset(dir)
                        );
                        IItemHandler item_storage = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir

                                .getOpposite());
                        IFluidHandler fluid_storage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir

                                .getOpposite());
                        boolean isSink = false;
                        boolean isSource = false;
                        boolean isSinkFluid = false;
                        boolean isSourceFluid = false;
                        if (transportTile == null) {
                            List<EnumFacing> facingListSink = new ArrayList<>();

                            for (EnumFacing dir1 : var4) {
                                TileEntity tile2 = getWorld().getTileEntity(getPos().offset(dir).offset(dir1));
                                if (tile2 instanceof ITransportConductor) {
                                    ITransportConductor transportConductor = (ITransportConductor) tile2;
                                    if (transportConductor.isItem()) {
                                        if (!transportConductor.isOutput()) {
                                            isSink = true;
                                            facingListSink.add(dir);
                                        } else {
                                            isSource = true;
                                        }
                                    } else if (!transportConductor.isOutput()) {
                                        isSinkFluid = true;
                                        facingListSink.add(dir);
                                    } else {
                                        isSourceFluid = true;
                                    }
                                }
                            }
                            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                    this.pos
                                            .offset(dir),
                                    item_storage,
                                    fluid_storage,
                                    isSink,
                                    isSource,
                                    isSinkFluid,
                                    isSourceFluid
                            );
                            transport.setFacingListSink(facingListSink);
                            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                    getWorld(), transport

                            ));
                        } else {
                            TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) transportTile;
                            if (isItem()) {
                                if (isOutput()) {
                                    transportFluidItemSinkSource.setSource(true);
                                } else {
                                    transportFluidItemSinkSource.setSink(true);
                                    transportFluidItemSinkSource.canAdd(dir);
                                }
                            } else if (isOutput()) {
                                transportFluidItemSinkSource.setSourceFluid(true);
                            } else {
                                transportFluidItemSinkSource.setSinkFluid(true);
                                transportFluidItemSinkSource.canAdd(dir);
                            }
                            if (transportFluidItemSinkSource.isNeed_update()) {
                                transportFluidItemSinkSource.setNeed_update(false);
                                isSink = transportFluidItemSinkSource.isSink();
                                isSource = transportFluidItemSinkSource.isSource();
                                isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                                isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                                BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                                IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                                IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                                List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();
                                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                        getWorld(), transportTile));
                                final TransportFluidItemSinkSource trasport = new TransportFluidItemSinkSource(
                                        pos,
                                        handler,
                                        fluidHandler,
                                        isSink,
                                        isSource,
                                        isSinkFluid,
                                        isSourceFluid
                                );
                                trasport.setFacingListSink(enumFacings);
                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), trasport

                                ));
                            }
                        }
                    } else if (isItem() && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir)) {
                        ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(
                                this.world,

                                getPos().offset(dir)
                        );
                        IItemHandler item_storage = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir

                                .getOpposite());

                        boolean isSink = false;
                        boolean isSource = false;
                        if (transportTile == null) {
                            List<EnumFacing> facingListSink = new ArrayList<>();

                            for (EnumFacing dir1 : var4) {

                                TileEntity tile2 = getWorld().getTileEntity(getPos().offset(dir).offset(dir1));
                                if (tile2 instanceof ITransportConductor) {
                                    ITransportConductor transportConductor = (ITransportConductor) tile2;
                                    if (transportConductor.isItem()) {
                                        if (!transportConductor.isOutput()) {
                                            isSink = true;
                                            facingListSink.add(dir);

                                        } else {
                                            isSource = true;
                                        }
                                    }
                                }
                            }
                            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(this.pos
                                    .offset(dir), item_storage, null, isSink, isSource, false, false);
                            transport.setFacingListSink(facingListSink);
                            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                    getWorld(), transport));
                        } else {
                            TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) transportTile;
                            if (isItem()) {
                                if (isOutput()) {
                                    transportFluidItemSinkSource.setSource(true);
                                } else {
                                    transportFluidItemSinkSource.setSink(true);
                                    transportFluidItemSinkSource.canAdd(dir);
                                }
                            }
                            if (transportFluidItemSinkSource.isNeed_update()) {
                                transportFluidItemSinkSource.setNeed_update(false);
                                isSink = transportFluidItemSinkSource.isSink();
                                isSource = transportFluidItemSinkSource.isSource();
                                boolean isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                                boolean isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                                BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                                IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                                IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                                List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();

                                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                        getWorld(), transportTile));
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                        pos,
                                        handler,
                                        fluidHandler,
                                        isSink,
                                        isSource,
                                        isSinkFluid,
                                        isSourceFluid
                                );
                                transport.setFacingListSink(enumFacings);
                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                            }
                        }
                    } else if (!isItem() && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir
                            .getOpposite())) {
                        ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(
                                this.world,

                                getPos().offset(dir)
                        );
                        IFluidHandler fluid_storage = tile.getCapability(
                                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                                dir

                                        .getOpposite()
                        );
                        boolean isSink = false;
                        boolean isSource = false;
                        boolean isSinkFluid = false;
                        boolean isSourceFluid = false;
                        if (transportTile == null) {
                            List<EnumFacing> facingListSink = new ArrayList<>();

                            for (EnumFacing dir1 : var4) {
                                TileEntity tile2 = getWorld().getTileEntity(getPos().offset(dir).offset(dir1));
                                if (tile2 instanceof ITransportConductor) {
                                    ITransportConductor transportConductor = (ITransportConductor) tile2;
                                    if (!transportConductor.isItem()) {
                                        if (!transportConductor.isOutput()) {
                                            isSinkFluid = true;
                                            facingListSink.add(dir1);
                                        } else {
                                            isSourceFluid = true;
                                        }
                                    }
                                }
                            }
                            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(this.pos
                                    .offset(dir), null, fluid_storage, isSink, isSource, isSinkFluid, isSourceFluid);
                            transport.setFacingListSink(facingListSink);
                            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                    getWorld(), transport

                            ));
                        } else {
                            TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) transportTile;
                            if (!isItem()) {
                                if (isOutput()) {
                                    transportFluidItemSinkSource.setSourceFluid(true);
                                } else {
                                    transportFluidItemSinkSource.setSinkFluid(true);
                                    transportFluidItemSinkSource.canAdd(dir);
                                }
                            }
                            if (transportFluidItemSinkSource.isNeed_update()) {
                                transportFluidItemSinkSource.setNeed_update(false);
                                isSink = transportFluidItemSinkSource.isSink();
                                isSource = transportFluidItemSinkSource.isSource();
                                isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                                isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                                BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                                IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                                IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                                List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();

                                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                        getWorld(), transportTile));
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                        pos,
                                        handler,
                                        fluidHandler,
                                        isSink,
                                        isSource,
                                        isSinkFluid,
                                        isSourceFluid
                                );
                                transport.setFacingListSink(enumFacings);
                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                            }
                        }
                    }
                }
            }

            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            }

            this.updateConnectivity();
            this.needUpdate = false;
        }
    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            EnumFacing[] var4 = EnumFacing.VALUES;
            for (EnumFacing dir : var4) {
                ITransportTile tile = TransportNetGlobal.instance.getSubTile(this.world, this.pos.offset(dir));
                if (!(tile instanceof ITransportConductor)) {
                    if (tile != null) {
                        TransportFluidItemSinkSource transportFluidItemSinkSource = (TransportFluidItemSinkSource) tile;
                        if (isItem()) {
                            if (isOutput()) {
                                transportFluidItemSinkSource.setSource(false);
                            } else {
                                transportFluidItemSinkSource.setSink(false);
                                transportFluidItemSinkSource.removeFacing(dir);
                            }
                        } else if (isOutput()) {
                            transportFluidItemSinkSource.setSourceFluid(false);
                        } else {
                            transportFluidItemSinkSource.setSinkFluid(false);
                            transportFluidItemSinkSource.removeFacing(dir);
                        }
                        if (transportFluidItemSinkSource.need_delete()) {
                            MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                    getWorld(), tile));
                        } else if (transportFluidItemSinkSource.isNeed_update()) {
                            transportFluidItemSinkSource.setNeed_update(false);
                            boolean isSink = transportFluidItemSinkSource.isSink();
                            boolean isSource = transportFluidItemSinkSource.isSource();
                            boolean isSinkFluid = transportFluidItemSinkSource.isSinkFluid();
                            boolean isSourceFluid = transportFluidItemSinkSource.isSourceFluid();
                            BlockPos pos = transportFluidItemSinkSource.getBlockPos();
                            IItemHandler handler = transportFluidItemSinkSource.getItemHandler();
                            IFluidHandler fluidHandler = transportFluidItemSinkSource.getFluidHandler();
                            List<EnumFacing> enumFacings = transportFluidItemSinkSource.getFacingList();

                            MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                    getWorld(), tile));
                            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                    pos,
                                    handler,
                                    fluidHandler,
                                    isSink,
                                    isSource,
                                    isSinkFluid,
                                    isSourceFluid
                            );
                            transport.setFacingListSink(enumFacings);
                            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                    getWorld(), transport

                            ));
                        }
                    }
                }
            }
            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            }
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }


    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.item_pipes, 1, this.cableType.ordinal());
    }


    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!(getWorld()).isRemote) {
            updateConnectivity();
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
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        if (var1.getHeldItem(EnumHand.MAIN_HAND).getItem() == IUItem.connect_item) {
            return super.getGui(var1, var2);
        } else {
            return new GuiCable1(getGuiContainer(var1));

        }
    }

    public void updateConnectivity() {
        World world = getWorld();
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;
        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            ITransportTile tile = TransportNetGlobal.instance.getSubTile(world, this.pos.offset(dir));
            if (!getBlackList().contains(dir)) {
                if ((tile instanceof ITransportAcceptor && ((ITransportAcceptor) tile).acceptsFrom(this, dir

                        .getOpposite())) || (tile instanceof ITransportEmitter && ((ITransportEmitter) tile)
                        .emitsTo(this, dir

                                .getOpposite()))) {
                    newConnectivity = (byte) (newConnectivity + 1);
                }
            }

        }
        setConnectivity(newConnectivity);
    }


    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean isOutput() {
        return this.cableType.isOutput;
    }

    public void update_render() {
        updateConnectivity();
    }

    public boolean isItem() {
        return this.cableType.isItem();
    }

    @Override
    public List<ItemStack> getBlackListItems() {

        return this.list.getContents().subList(0, 9).stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getWhiteListItems() {
        return this.list.getContents().subList(9, this.list.size()).stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());

    }

    @Override
    public List<FluidStack> getBlackListFluids() {
        return this.list.getFluidStackList().subList(0, 9).stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

    @Override
    public List<FluidStack> getWhiteListFluids() {
        return this.list.getFluidStackList().subList(9, this.list.size()).stream().filter(Objects::nonNull).collect(Collectors.toList());

    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cableType);
            EncoderHandler.encode(packet, connectivity);
            EncoderHandler.encode(packet, this.list.isFluid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cableType = ItemType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.list.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onNetworkEvent(int event) {
        World world = getWorld();
        if (event == 0) {
            world.playSound(null, this.pos, SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand

                    .nextFloat() - world.rand.nextFloat()) * 0.8F);
            for (int l = 0; l < 8; l++) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.pos

                        .getX() + Math.random(), this.pos
                        .getY() + 1.2D, this.pos
                        .getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
            }
        }
    }


    public boolean acceptsFrom(ITransportEmitter var1, EnumFacing var2) {
        if (getBlackList().contains(var2)) {
            return false;
        }
        if (this.cableType.isItem()) {
            return var1.getHandler() instanceof IItemHandler;
        }
        return var1.getHandler() instanceof IFluidHandler;
    }

    public boolean emitsTo(ITransportAcceptor var1, EnumFacing var2) {
        if (getBlackList().contains(var2)) {
            return false;
        }
        if (this.cableType.isItem()) {
            return var1.getHandler() instanceof IItemHandler;
        }
        return var1.getHandler() instanceof IFluidHandler;
    }

    public Object getHandler() {
        if (this.cableType.isItem()) {
            return new ItemStackHandler();
        }
        return new FluidHandler();
    }

    public BlockPos getBlockPos() {
        return this.pos;
    }


}
