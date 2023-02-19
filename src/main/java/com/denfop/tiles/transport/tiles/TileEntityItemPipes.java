package com.denfop.tiles.transport.tiles;

import com.denfop.IUItem;
import com.denfop.api.transport.EnumTypeList;
import com.denfop.api.transport.FluidHandler;
import com.denfop.api.transport.ITransportAcceptor;
import com.denfop.api.transport.ITransportConductor;
import com.denfop.api.transport.ITransportEmitter;
import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportFluidItemSinkSource;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.TypeSlots;
import com.denfop.api.transport.event.TransportTileLoadEvent;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.tiles.transport.types.ItemType;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.IWorldTickCallback;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedProperty;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class TileEntityItemPipes extends TileEntityInventory implements ITransportConductor, INetworkTileEntityEventListener {

    public static final IUnlistedProperty<CableRenderState> renderStateProperty = (IUnlistedProperty<CableRenderState>) new UnlistedProperty(
            "renderstate",
            CableRenderState.class
    );
    public boolean addedToEnergyNet = false;
    protected ItemType cableType = ItemType.itemcable;
    private byte connectivity = 0;
    private byte color = 0;
    private volatile CableRenderState renderState;
    private EnumMap<EnumFacing, EnumTypeList> enumTypeListEnumMap = new EnumMap<>(EnumFacing.class);
    private EnumMap<EnumFacing, List<ItemStack>> enumFacingListEnumMap = new EnumMap<>(EnumFacing.class);
    public TileEntityItemPipes(ItemType cableType) {
        this();
        this.cableType = cableType;
    }

    public TileEntityItemPipes() {
    }

    public static TileEntityItemPipes delegate(ItemType cableType) {
        return new TileEntityItemPipes(cableType);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = ItemType.values[nbt.getByte("cableType") & 0xFF];
        this.color = nbt.getByte("color");
        EnumFacing[] facings = EnumFacing.values();
        NBTTagCompound  nbt_facing = (NBTTagCompound) nbt.getTag("facing");
        for(EnumFacing facing : facings){
            final String name = nbt_facing.getString(facing.toString().toLowerCase());
            if(name.isEmpty())
                continue;
                enumTypeListEnumMap.put(facing, EnumTypeList.valueOf(name));
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        nbt.setByte("color", this.color);
        EnumFacing[] facings = EnumFacing.values();
        NBTTagCompound  nbt_facing = new NBTTagCompound();
        for(EnumFacing facing : facings){
            nbt_facing.setString(facing.toString().toLowerCase(),enumTypeListEnumMap.get(facing).toString());
        }
        nbt.setTag("facing",nbt_facing);
        return nbt;
    }

    protected void onLoaded() {
        super.onLoaded();
        if ((getWorld()).isRemote) {
            updateRenderState();
        } else {
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
            this.addedToEnergyNet = true;
            updateConnectivity();
        }
    }

    protected void onUnloaded() {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
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

    protected SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        updateRenderState();
        super.onPlaced(stack, placer, facing);
    }

    protected ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.item_pipes, 1, this.cableType.ordinal());
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        float th = 0.25F;
        float sp = (1.0F - th) / 2.0F;
        List<AxisAlignedBB> ret = new ArrayList<>(7);
        ret.add(new AxisAlignedBB(sp, sp, sp, (sp + th), (sp + th), (sp + th)));
        EnumFacing[] var5 = EnumFacing.VALUES;
        for (EnumFacing facing : var5) {
            boolean hasConnection = ((this.connectivity & 1 << facing.ordinal()) != 0);
            if (hasConnection) {
                float zS = sp;
                float yS = sp;
                float xS = sp;
                float zE = sp + th, yE = zE, xE = yE;
                switch (facing) {
                    case DOWN:
                        yS = 0.0F;
                        yE = sp;
                        break;
                    case UP:
                        yS = sp + th;
                        yE = 1.0F;
                        break;
                    case NORTH:
                        zS = 0.0F;
                        zE = sp;
                        break;
                    case SOUTH:
                        zS = sp + th;
                        zE = 1.0F;
                        break;
                    case WEST:
                        xS = 0.0F;
                        xE = sp;
                        break;
                    case EAST:
                        xS = sp + th;
                        xE = 1.0F;
                        break;
                    default:
                        throw new RuntimeException();
                }
                ret.add(new AxisAlignedBB(xS, yS, zS, xE, yE, zE));
            }
        }
        return ret;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        state = super.getExtendedState(state);
        CableRenderState cableRenderState = this.renderState;
        if (cableRenderState != null) {
            state = state.withProperties(renderStateProperty, cableRenderState);
        }
        return state;
    }

    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!(getWorld()).isRemote) {
            updateConnectivity();
        }
    }

    private void updateConnectivity() {
        World world = getWorld();
        byte newConnectivity = 0;
        int mask = 1;
        EnumFacing[] var4 = EnumFacing.VALUES;
        for (EnumFacing dir : var4) {
            ITransportTile tile = TransportNetGlobal.instance.getSubTile(world, this.pos.offset(dir));
            if ((tile instanceof ITransportAcceptor && ((ITransportAcceptor) tile).acceptsFrom(this, dir

                    .getOpposite())) || (tile instanceof ITransportEmitter && ((ITransportEmitter) tile)
                    .emitsTo(this, dir

                            .getOpposite()))) {
                newConnectivity = (byte) (newConnectivity | mask);
            }
            mask *= 2;
        }
        if (this.connectivity != newConnectivity) {
            this.connectivity = newConnectivity;
            IC2.network.get(true).updateTileEntityField(this, "connectivity");
        }
    }

    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    protected void onClicked(EntityPlayer player) {
        super.onClicked(player);
    }

    protected float getHardness() {
        return super.getHardness();
    }

    protected int getLightOpacity() {
        return 0;
    }

    protected boolean recolor(EnumFacing side, EnumDyeColor mcColor) {
        return false;
    }

    protected boolean onRemovedByPlayer(EntityPlayer player, boolean willHarvest) {
        return super.onRemovedByPlayer(player, willHarvest);
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
    public List<TypeSlots> getTypeSlotsFromFacing(final EnumFacing facing, final boolean input) {
        return null;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = new ArrayList<>();
        ret.add("cableType");
        ret.add("connectivity");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public void onNetworkUpdate(String field) {
        updateRenderState();
        rerender();
        super.onNetworkUpdate(field);
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
        } else {
            IC2.platform.displayError(
                    "An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\nT: " + this + " (" + this.pos + ")"
            );
        }
    }

    private void updateRenderState() {
        this

                .renderState = new CableRenderState(this.cableType, this.connectivity, getActive());
    }

    public boolean acceptsFrom(ITransportEmitter var1, EnumFacing var2) {
        if (this.cableType.isItem()) {
            return var1.getHandler() instanceof IItemHandler;
        }
        return var1.getHandler() instanceof IFluidHandler;
    }

    public boolean emitsTo(ITransportAcceptor var1, EnumFacing var2) {
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

    public static class CableRenderState {

        public final ItemType type;

        public final int connectivity;

        public final boolean active;

        public CableRenderState(ItemType type, int connectivity, boolean active) {
            this.type = type;
            this.connectivity = connectivity;
            this.active = active;
        }

        public int hashCode() {
            int ret = this.type.hashCode();
            ret = ret * 31 + this.connectivity;
            ret = ret << 1 | (this.active ? 1 : 0);
            return ret;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof CableRenderState)) {
                return false;
            }
            CableRenderState o = (CableRenderState) obj;
            return (o.type == this.type && o.connectivity == this.connectivity && o.active == this.active);
        }

        public String toString() {
            return "CableState<" + this.type + ", " + this.connectivity + ", " + this.active + '>';
        }

    }

}
