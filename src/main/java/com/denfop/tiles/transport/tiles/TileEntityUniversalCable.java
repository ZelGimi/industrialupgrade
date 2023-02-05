package com.denfop.tiles.transport.tiles;

import com.denfop.IUItem;
import com.denfop.api.cool.CoolNet;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.energy.IAdvConductor;
import com.denfop.api.exp.EXPNet;
import com.denfop.api.exp.IEXPAcceptor;
import com.denfop.api.exp.IEXPConductor;
import com.denfop.api.exp.IEXPEmitter;
import com.denfop.api.exp.event.EXPTileLoadEvent;
import com.denfop.api.exp.event.EXPTileUnloadEvent;
import com.denfop.api.heat.HeatNet;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.qe.IQEAcceptor;
import com.denfop.api.qe.IQEConductor;
import com.denfop.api.qe.IQEEmitter;
import com.denfop.api.qe.QENet;
import com.denfop.api.qe.event.QETileLoadEvent;
import com.denfop.api.qe.event.QETileUnloadEvent;
import com.denfop.api.se.ISEAcceptor;
import com.denfop.api.se.ISEConductor;
import com.denfop.api.se.ISEEmitter;
import com.denfop.api.se.SENet;
import com.denfop.api.se.event.SETileLoadEvent;
import com.denfop.api.se.event.SETileUnloadEvent;
import com.denfop.componets.QEComponent;
import com.denfop.tiles.transport.types.UniversalType;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
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
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;


public class TileEntityUniversalCable extends TileEntityBlock implements IAdvConductor, IHeatConductor, ICoolConductor,
        IQEConductor, ISEConductor, IEXPConductor,
        INetworkTileEntityEventListener {

    public static final IUnlistedProperty<TileEntityUniversalCable.CableRenderState> renderStateProperty = new UnlistedProperty<>(
            "renderstate",
            TileEntityUniversalCable.CableRenderState.class
    );
    public boolean addedToEnergyNet;
    public int type;
    protected UniversalType cableType;
    private byte connectivity;
    private volatile TileEntityUniversalCable.CableRenderState renderState;

    public TileEntityUniversalCable(UniversalType cableType, int insulation) {
        this();
        this.cableType = cableType;
        this.type = cableType.ordinal();
    }

    public TileEntityUniversalCable() {
        this.cableType = UniversalType.glass;
        this.connectivity = 0;
        this.addedToEnergyNet = false;

    }
    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }
    public static TileEntityUniversalCable delegate(UniversalType cableType, int insulation) {
        return new TileEntityUniversalCable(cableType, insulation);
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

    protected void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isRemote) {
            this.updateRenderState();
        } else {


            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new QETileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new EXPTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new SETileLoadEvent(this, this.getWorld()));

            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    protected void onUnloaded() {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new QETileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new EXPTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new SETileUnloadEvent(this, this.getWorld()));

            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }

    protected SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        this.updateRenderState();
        super.onPlaced(stack, placer, facing);
    }

    protected ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.universal_cable, 1, this.cableType.ordinal());
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        {
            float th = this.cableType.thickness + (float) (0) * 0.0625F;
            float sp = (1.0F - th) / 2.0F;
            List<AxisAlignedBB> ret = new ArrayList<>(7);
            ret.add(new AxisAlignedBB(
                    sp,
                    sp,
                    sp,
                    sp + th,
                    sp + th,
                    sp + th
            ));
            EnumFacing[] var5 = EnumFacing.VALUES;

            for (EnumFacing facing : var5) {
                boolean hasConnection = (this.connectivity & 1 << facing.ordinal()) != 0;
                if (hasConnection) {
                    float zS = sp;
                    float yS = sp;
                    float xS = sp;
                    float yE;
                    float zE;
                    float xE = yE = zE = sp + th;
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

    public Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        state = super.getExtendedState(state);
        TileEntityUniversalCable.CableRenderState cableRenderState = this.renderState;
        if (cableRenderState != null) {
            state = state.withProperties(renderStateProperty, cableRenderState);
        }


        return state;
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
        int mask = 1;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            Object tile = EnergyNet.instance.getSubTile(world, this.pos.offset(dir));
            if (tile != null) {
                if ((tile instanceof IEnergyAcceptor && ((IEnergyAcceptor) tile).acceptsEnergyFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof IEnergyEmitter && ((IEnergyEmitter) tile).emitsEnergyTo(
                        this,
                        dir.getOpposite()
                )) && this.canInteractWith()) {
                    newConnectivity = (byte) (newConnectivity | mask);
                }
            } else {
                tile = SENet.instance.getSubTile(world, this.pos.offset(dir));
                if (tile != null) {
                    if ((tile instanceof ISEAcceptor && ((ISEAcceptor) tile).acceptsSEFrom(
                            this,
                            dir.getOpposite()
                    ) || tile instanceof ISEEmitter && ((ISEEmitter) tile).emitsSETo(
                            this,
                            dir.getOpposite()
                    ) || tile instanceof TileEntityBlock && ((TileEntityBlock) tile).hasComponent(QEComponent.class)) && this.canInteractWith(
                    )) {
                        newConnectivity = (byte) (newConnectivity | mask);
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
                            newConnectivity = (byte) (newConnectivity | mask);
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
                                newConnectivity = (byte) (newConnectivity | mask);
                            }
                        } else {
                            tile = QENet.instance.getSubTile(world, this.pos.offset(dir));
                            if (tile != null) {
                                if ((tile instanceof IQEAcceptor && ((IQEAcceptor) tile).acceptsQEFrom(
                                        this,
                                        dir.getOpposite()
                                ) || tile instanceof IQEEmitter && ((IQEEmitter) tile).emitsQETo(
                                        this,
                                        dir.getOpposite()
                                ) || tile instanceof TileEntityBlock && ((TileEntityBlock) tile).hasComponent(QEComponent.class)) && this.canInteractWith(
                                )) {
                                    newConnectivity = (byte) (newConnectivity | mask);
                                }
                            } else {
                                tile = EXPNet.instance.getSubTile(world, this.pos.offset(dir));
                                if (tile != null) {
                                    if ((tile instanceof IEXPAcceptor && ((IEXPAcceptor) tile).acceptsEXPFrom(
                                            this,
                                            dir.getOpposite()
                                    ) || tile instanceof IEXPEmitter && ((IEXPEmitter) tile).emitsEXPTo(
                                            this,
                                            dir.getOpposite()
                                    )) && this.canInteractWith()) {
                                        newConnectivity = (byte) (newConnectivity | mask);
                                    }
                                }
                            }
                        }
                    }
                }
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

    protected float getExplosionResistance(Entity exploder, Explosion explosion) {

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

    public double getInsulationEnergyAbsorption() {

        return 2.147483647E9D;

    }

    public double getInsulationBreakdownEnergy() {
        return 9001.0D;
    }

    @Override
    public double getConductorBreakdownEnergy() {
        return this.cableType.capacity + 1;
    }

    @Override
    public double getConductorBreakdownSolariumEnergy() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double getConductorBreakdownQuantumEnergy() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double getConductorBreakdownHeat() {
        return 16001;
    }

    @Override
    public double getConductorBreakdownExperienceEnergy() {
        return Integer.MAX_VALUE;
    }

    public double getConductorBreakdownCold() {
        return 65;
    }

    public void removeInsulation() {

    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
    }


    public List<String> getNetworkedFields() {
        List<String> ret = new ArrayList<>();
        ret.add("cableType");
        ret.add("connectivity");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public void onNetworkUpdate(String field) {
        this.updateRenderState();


        this.rerender();
        super.onNetworkUpdate(field);
    }


    public void onNetworkEvent(int event) {
        World world = this.getWorld();
        if (event == 0) {
            world.playSound(
                    null,
                    this.pos,
                    SoundEvents.ENTITY_GENERIC_BURN,
                    SoundCategory.BLOCKS,
                    0.5F,
                    2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
            );

            for (int l = 0; l < 8; ++l) {
                world.spawnParticle(
                        EnumParticleTypes.SMOKE_LARGE,
                        (double) this.pos.getX() + Math.random(),
                        (double) this.pos.getY() + 1.2D,
                        (double) this.pos.getZ() + Math.random(),
                        0.0D,
                        0.0D,
                        0.0D
                );
            }

        } else {
            IC2.platform.displayError(
                    "An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\nT: " + this + " (" + this.pos + ")"
            );
        }
    }


    private void updateRenderState() {
        this.renderState = new TileEntityUniversalCable.CableRenderState(
                this.cableType,
                this.connectivity,
                this.getActive()
        );
    }

    @Override
    public void update_render() {
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }
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
    public boolean acceptsEXPFrom(final IEXPEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean emitsEXPTo(final IEXPAcceptor var1, final EnumFacing var2) {
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
    public boolean acceptsQEFrom(final IQEEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean emitsQETo(final IQEAcceptor var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean acceptsSEFrom(final ISEEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public boolean emitsSETo(final ISEAcceptor var1, final EnumFacing var2) {
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


    public static class CableRenderState {

        public final UniversalType type;
        public final int connectivity;
        public final boolean active;

        public CableRenderState(UniversalType type, int connectivity, boolean active) {
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
            } else if (!(obj instanceof TileEntityUniversalCable.CableRenderState)) {
                return false;
            } else {
                TileEntityUniversalCable.CableRenderState o = (TileEntityUniversalCable.CableRenderState) obj;
                return o.type == this.type && o.connectivity == this.connectivity && o.active == this.active;
            }
        }

        public String toString() {
            return "CableState<" + this.type + ", " + this.connectivity + ", " + this.active + '>';
        }

    }

}
