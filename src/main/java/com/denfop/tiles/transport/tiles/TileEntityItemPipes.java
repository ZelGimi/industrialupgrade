package com.denfop.tiles.transport.tiles;

import com.denfop.IUItem;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.transport.*;
import com.denfop.api.transport.event.TransportTileLoadEvent;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCable;
import com.denfop.container.SlotInfo;
import com.denfop.gui.GuiCable1;
import com.denfop.gui.GuiCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.tiles.transport.types.ItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TileEntityItemPipes extends TileEntityMultiCable implements ITransportConductor {


    private final Redstone redstone;
    public SlotInfo list;
    public boolean addedToEnergyNet = false;
    public ItemType cableType;
    public boolean redstoneSignal = false;
    Map<Direction, ITransportTile> energyConductorMap = new HashMap<>();
    boolean hasHashCode = false;
    int hashCodeSource;
    List<InfoTile<ITransportTile>> validReceivers = new LinkedList<>();
    InfoCable cable;
    List<FluidStack> blackList = new ArrayList<>();
    List<FluidStack> whiteList = new ArrayList<>();
    private SlotInfo listDown;
    private SlotInfo listUp;
    private SlotInfo listWest;
    private SlotInfo listEast;
    private SlotInfo listNorth;
    private SlotInfo listSouth;
    private boolean needUpdate;
    private long id;
    private boolean update;
    private boolean work = false;
    private Direction facingSide;
    private byte tick;
    private int max;
    private int hashCode;


    public TileEntityItemPipes(ItemType cableType, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(cableType, block, pos, state);
        this.cableType = cableType;
        this.listDown = new SlotInfo(this, 18, !this.cableType.isItem());
        this.listUp = new SlotInfo(this, 18, !this.cableType.isItem());
        this.listWest = new SlotInfo(this, 18, !this.cableType.isItem());
        this.listEast = new SlotInfo(this, 18, !this.cableType.isItem());
        this.listNorth = new SlotInfo(this, 18, !this.cableType.isItem());
        this.listSouth = new SlotInfo(this, 18, !this.cableType.isItem());
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
                                    @Override
                                    public void action(final int input) {
                                        work = input != 0;
                                    }
                                }
        );
    }

    @Override
    public void addInformation(ItemStack stack, List<String> info) {
        final ItemType type =cableType;
        info.add("Maximum: " + type.getMax() + (type.isItem() ? " item/t" : " mb/t"));

    }
    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.redstoneSignal = customPacketBuffer.readBoolean();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(redstoneSignal);
        return customPacketBuffer;
    }

    public boolean isWork() {
        return work;
    }


    public ICableItem getCableItem() {
        return cableType;
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.cableType = ItemType.values[nbt.getByte("cableType") & 0xFF];
        redstoneSignal = nbt.getBoolean("redstoneSignal");
        if (!this.cableType.isOutput && !this.cableType.isInput()) {
            this.listDown = null;
            this.listUp = null;
            this.listWest = null;
            this.listEast = null;
            this.listNorth = null;
            this.listSouth = null;
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putByte("cableType", (byte) this.cableType.ordinal());
        nbt.putBoolean("redstoneSignal", redstoneSignal);
        return nbt;
    }

    public long getIdNetwork() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void RemoveTile(ITransportTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<ITransportTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ITransportTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }

            this.update = true;
        }
    }

    @Override
    public int hashCode() {
        if (!hasHashCode) {
            hasHashCode = true;
            this.hashCode = super.hashCode();
            return hashCode;
        } else {
            return hashCode;
        }
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public Map<Direction, ITransportTile> getTiles() {
        return energyConductorMap;
    }

    public List<InfoTile<ITransportTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public BlockEntity getTileEntity() {
        return this;
    }

    public void AddTile(ITransportTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1) && ((isOutput() || isInput()) || (!isOutput() && !isInput() && tile instanceof ITransportConductor))) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
                this.update = true;
            }
        }
    }

    @Override
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);

        BlockEntity tile = getWorld().getBlockEntity(neighborPos);
        if (tile != null && !(tile instanceof ITransportConductor)) {
            if (!tile.isRemoved()) {
                for (Direction enumFacing : Direction.values()) {
                    if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER, enumFacing).orElse(null) != null && tile.getCapability(
                            ForgeCapabilities.FLUID_HANDLER,
                            enumFacing
                    ).orElse(null) != null) {
                        final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                tile,
                                neighborPos
                        );

                        MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                getWorld(), transport

                        ));
                        break;
                    } else if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER, enumFacing).orElse(null) != null) {
                        final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile, neighborPos
                        );

                        MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                getWorld(), transport

                        ));
                        break;
                    } else if (tile.getCapability(ForgeCapabilities.FLUID_HANDLER, enumFacing).orElse(null) != null) {
                        final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile, neighborPos
                        );

                        MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                getWorld(), transport

                        ));
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(final Player var1, final ContainerBase<?> var2) {
        if (var1.getItemInHand(InteractionHand.MAIN_HAND).getItem() == IUItem.connect_item.getItem()) {
            return super.getGui(var1, var2);
        } else {
            return new GuiCable1(getGuiContainer(var1));
        }
    }

    @Override
    public ContainerCable getGuiContainer(final Player var1) {
        return new ContainerCable(var1, this, facingSide.getOpposite());
    }

    public void onLoaded() {
        super.onLoaded();
        if (!(getWorld()).isClientSide && !addedToEnergyNet) {

            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            }
            this.addedToEnergyNet = true;
            updateConnectivity();

            for (Direction facing1 : Direction.values()) {
                BlockPos neighborPos = this.pos.offset(facing1.getNormal());
                BlockEntity tile = getWorld().getBlockEntity(neighborPos);
                if (tile != null && !(tile instanceof ITransportConductor)) {
                    if (!tile.isRemoved()) {
                        for (Direction enumFacing : Direction.values()) {
                            if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER, enumFacing).orElse(null) != null && tile.getCapability(
                                    ForgeCapabilities.FLUID_HANDLER,
                                    enumFacing
                            ).orElse(null) != null) {
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                        tile,
                                        neighborPos
                                );

                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                                break;
                            } else if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER, enumFacing).orElse(null) != null) {
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile, neighborPos
                                );

                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                                break;
                            } else if (tile.getCapability(ForgeCapabilities.FLUID_HANDLER, enumFacing).orElse(null) != null) {
                                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile, neighborPos
                                );

                                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                        getWorld(), transport

                                ));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 < Direction.values().length) {
            super.updateTileServer(var1, var2);
            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            }
            this.needUpdate = true;
        } else {
            if (var2 == 10) {
                this.redstoneSignal = !this.redstoneSignal;
            }
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {

            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(getWorld(), this));
            }

            this.updateConnectivity();
            this.needUpdate = false;
        }
        if (update) {
            update = false;
            this.updateConnectivity();
        }
    }

    public void onUnloaded() {
        if (!this.getLevel().isClientSide && this.addedToEnergyNet) {
            if (this.cableType.isItem()) {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            } else {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(getWorld(), this));
            }
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        this.facingSide = side;
        return super.onActivated(player, hand, side, vec3);
    }


    public SlotInfo getInfoSlotFromFacing(Direction facing) {
        if (facing == null) {
            return listUp;
        }
        switch (facing) {
            case UP:
                return listUp;
            case DOWN:
                return listDown;
            case NORTH:
                return listNorth;
            case SOUTH:
                return listSouth;
            case WEST:
                return listWest;
            case EAST:
                return listEast;
        }
        return listUp;
    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();
        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            ITransportTile tile = getTiles().get(dir);
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
        this.cableItem = cableType;
    }

    public boolean wrenchCanRemove(Player player) {
        return false;
    }

    public boolean isOutput() {
        return this.cableType.isOutput;
    }

    @Override
    public boolean isInput() {
        return cableType.isInput();
    }

    @Override
    public InfoCable getCable() {
        return cable;
    }

    @Override
    public void setCable(final InfoCable cable) {
        this.cable = cable;
    }

    public boolean isItem() {
        return this.cableType.isItem();
    }

    @Override
    public List<ItemStack> getBlackListItems(Direction facing) {
        list = getInfoSlotFromFacing(facing);
        return this.list.getListBlack();
    }

    @Override
    public List<ItemStack> getWhiteListItems(Direction facing) {
        list = getInfoSlotFromFacing(facing);
        return this.list.getListWhite();

    }

    @Override
    public List<FluidStack> getBlackListFluids(Direction facing) {
        list = getInfoSlotFromFacing(facing);
        if (this.getWorld().getGameTime() % 20 == 0) {
            blackList =
                    this.list.getFluidStackList().subList(0, 9).stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        return blackList;

    }

    @Override
    public List<FluidStack> getWhiteListFluids(Direction facing) {
        list = getInfoSlotFromFacing(facing);
        if (this.getWorld().getGameTime() % 20 == 0) {
            whiteList =
                    this.list
                            .getFluidStackList()
                            .subList(9, this.list.size())
                            .stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
        }
        return whiteList;

    }

    @Override
    public boolean canWork() {
        return !redstoneSignal || work;
    }

    @Override
    public int getMax() {
        return cableType.getMax();
    }

    @Override
    public void setMax(final int value) {
        this.max -= value;
    }

    @Override
    public int getMax(final byte tick) {
        if (this.tick != tick) {
            this.tick = tick;
            this.max = getMax();
            return getMax();
        }
        return this.max;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cableType);
            EncoderHandler.encode(packet, connectivity);
            if (listUp != null) {
                EncoderHandler.encode(packet, this.listUp.isFluid());
                EncoderHandler.encode(packet, this.listDown.isFluid());
                EncoderHandler.encode(packet, this.listWest.isFluid());
                EncoderHandler.encode(packet, this.listEast.isFluid());
                EncoderHandler.encode(packet, this.listNorth.isFluid());
                EncoderHandler.encode(packet, this.listSouth.isFluid());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cableType = ItemType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            if (!this.cableType.isOutput && !this.cableType.isInput()) {
                this.listDown = null;
                this.listUp = null;
                this.listWest = null;
                this.listEast = null;
                this.listNorth = null;
                this.listSouth = null;

            }
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            if (listUp != null) {
                this.listUp.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
                this.listDown.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
                this.listWest.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
                this.listEast.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
                this.listNorth.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
                this.listSouth.setFluid((Boolean) DecoderHandler.decode(customPacketBuffer));
            }
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean acceptsFrom(ITransportEmitter var1, Direction var2) {
        if (getBlackList().contains(var2)) {
            return false;
        }
        if (!this.cableType.isOutput && !this.cableType.isInput()) {
            if (var1 instanceof ITransportConductor) {
                ITransportConductor conductor = (ITransportConductor) var1;
                return (conductor.isItem() && this.isItem()) || (!conductor.isItem() && !this.isItem());
            }
        }
        if (this.cableType.isItem()) {
            return var1.getHandler(var2.getOpposite()) instanceof IItemHandler;
        }

        return var1.getHandler(var2.getOpposite()) instanceof IFluidHandler;
    }

    public boolean emitsTo(ITransportAcceptor var1, Direction var2) {
        if (getBlackList().contains(var2)) {
            return false;
        }
        if (!this.cableType.isOutput && !this.cableType.isInput()) {
            if (var1 instanceof ITransportConductor) {
                ITransportConductor conductor = (ITransportConductor) var1;
                return (conductor.isItem() && this.isItem()) || (!conductor.isItem() && !this.isItem());
            }
            return false;
        }
        if (this.cableType.isItem()) {
            return var1.getHandler(var2.getOpposite()) instanceof IItemHandler;
        }

        return var1.getHandler(var2.getOpposite()) instanceof IFluidHandler;
    }

    public Object getHandler(Direction facing) {
        if (this.cableType.isItem()) {
            return new ItemStackHandler();
        }
        return new FluidHandler();
    }


}
