package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.cool.*;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.heat.*;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.tiles.transport.CableFoam;
import com.denfop.tiles.transport.types.HeatColdType;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.IWorldTickCallback;
import ic2.core.block.BlockFoam;
import ic2.core.block.BlockFoam.FoamType;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Obscuration;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.block.state.UnlistedProperty;
import ic2.core.ref.TeBlock;
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


public class TileEntityHeatColdPipes extends TileEntityBlock implements ICoolConductor, IHeatConductor,
        INetworkTileEntityEventListener {

    public static final IUnlistedProperty<TileEntityHeatColdPipes.CableRenderState> renderStateProperty = new UnlistedProperty<>(
            "renderstate",
            TileEntityHeatColdPipes.CableRenderState.class
    );
    private final Obscuration obscuration;
    public boolean addedToEnergyNet;
    protected HeatColdType cableType;
    protected int insulation;
    private CableFoam foam;
    private byte connectivity;
    private volatile TileEntityHeatColdPipes.CableRenderState renderState;
    private IWorldTickCallback continuousUpdate;

    public TileEntityHeatColdPipes(HeatColdType cableType, int insulation) {
        this();
        this.cableType = cableType;
        this.insulation = insulation;
    }

    public TileEntityHeatColdPipes() {
        this.cableType = HeatColdType.heatcool;
        this.foam = CableFoam.None;
        this.connectivity = 0;
        this.addedToEnergyNet = false;
        this.continuousUpdate = null;
        this.obscuration = this.addComponent(new Obscuration(
                this,
                () -> IUCore.network.get(true).updateTileEntityField(TileEntityHeatColdPipes.this, "obscuration")
        ));
    }

    public static TileEntityHeatColdPipes delegate(HeatColdType cableType, int insulation) {
        return new TileEntityHeatColdPipes(cableType, insulation);
    }

    public static TileEntityHeatColdPipes delegate() {
        return new TileEntityHeatColdPipes();
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = HeatColdType.values[nbt.getByte("cableType") & 255];
        this.insulation = nbt.getByte("insulation") & 255;
        this.foam = CableFoam.values[nbt.getByte("foam") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        nbt.setByte("insulation", (byte) this.insulation);
        nbt.setByte("foam", (byte) this.foam.ordinal());
        return nbt;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isRemote) {
            this.updateRenderState();
        } else {


            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this, this.getWorld()));

            this.addedToEnergyNet = true;
            this.updateConnectivity();
            if (this.foam == CableFoam.Soft) {
                this.changeFoam(this.foam, true);
            }
        }

    }

    protected void onUnloaded() {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this, this.getWorld()));
            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this, this.getWorld()));

            this.addedToEnergyNet = false;
        }

        if (this.continuousUpdate != null) {
            IC2.tickHandler.removeContinuousWorldTick(this.getWorld(), this.continuousUpdate);
            this.continuousUpdate = null;
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
        return new ItemStack(IUItem.heatcold_pipes, 1, cableType.ordinal());
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (this.foam == CableFoam.Hardened || this.foam == CableFoam.Soft && !forCollision) {
            return super.getAabbs(forCollision);
        } else {
            float th = this.cableType.thickness + (float) (this.insulation * 2) * 0.0625F;
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
        TileEntityHeatColdPipes.CableRenderState cableRenderState = this.renderState;
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
            ICoolTile tile = CoolNet.instance.getSubTile(world, this.pos.offset(dir));
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
                IHeatTile heatTile = HeatNet.instance.getSubTile(world, this.pos.offset(dir));

                if ((heatTile instanceof IHeatAcceptor && ((IHeatAcceptor) heatTile).acceptsHeatFrom(
                        this,
                        dir.getOpposite()
                ) || heatTile instanceof IHeatEmitter && ((IHeatEmitter) heatTile).emitsHeatTo(
                        this,
                        dir.getOpposite()
                )) && this.canInteractWith()) {
                    newConnectivity = (byte) (newConnectivity | mask);
                }

            }
            mask *= 2;
        }

        if (this.connectivity != newConnectivity) {
            this.connectivity = newConnectivity;
            IUCore.network.get(true).updateTileEntityField(this, "connectivity");
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
        return this.foam == CableFoam.Hardened ? 255 : 0;
    }


    protected boolean recolor(EnumFacing side, EnumDyeColor mcColor) {
        return false;
    }

    protected boolean onRemovedByPlayer(EntityPlayer player, boolean willHarvest) {
        return !this.changeFoam(CableFoam.None, false) && super.onRemovedByPlayer(player, willHarvest);
    }


    public void tryRemoveInsulation(boolean simulate) {
        if (!simulate && this.insulation > 0) {
            if (this.insulation == this.cableType.minColoredInsulation) {
                CableFoam foam = this.foam;
                this.foam = CableFoam.None;
                this.recolor(this.getFacing(), EnumDyeColor.BLACK);
                this.foam = foam;
            }

            --this.insulation;
            if (!this.getWorld().isRemote) {
                IUCore.network.get(true).updateTileEntityField(this, "insulation");
            }

        }
    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
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
    public double getConductorBreakdownHeat() {
        return this.cableType.capacity1 + 1;
    }

    public double getConductorBreakdownCold() {
        return this.cableType.capacity + 1;
    }

    public void removeInsulation() {
        this.tryRemoveInsulation(false);
    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        IUCore.network.get(true).initiateTileEntityEvent(this, 0, true);
    }

    @Override
    public void update_render() {
        this.updateConnectivity();
    }


    public List<String> getNetworkedFields() {
        List<String> ret = new ArrayList<>();
        ret.add("cableType");
        ret.add("insulation");
        ret.add("foam");
        ret.add("connectivity");
        ret.add("obscuration");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public void onNetworkUpdate(String field) {
        this.updateRenderState();
        if (field.equals("foam") && (this.foam == CableFoam.None || this.foam == CableFoam.Hardened)) {
            this.relight();
        }

        this.rerender();
        super.onNetworkUpdate(field);
    }

    private void relight() {
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

    private boolean changeFoam(CableFoam foam, boolean duringLoad) {
        if (this.foam == foam && !duringLoad) {
            return false;
        } else {
            World world = this.getWorld();
            if (!world.isRemote) {
                this.foam = foam;
                if (this.continuousUpdate != null) {
                    IC2.tickHandler.removeContinuousWorldTick(world, this.continuousUpdate);
                    this.continuousUpdate = null;
                }

                if (foam != CableFoam.Hardened) {
                    this.obscuration.clear();

                }

                if (foam == CableFoam.Soft) {
                    this.continuousUpdate = world1 -> {
                        if (world1.rand.nextFloat() < BlockFoam.getHardenChance(
                                world1,
                                TileEntityHeatColdPipes.this.pos,
                                TileEntityHeatColdPipes.this.getBlockType().getState(TeBlock.cable),
                                FoamType.normal
                        )) {
                            TileEntityHeatColdPipes.this.changeFoam(CableFoam.Hardened, false);
                        }

                    };
                    IC2.tickHandler.requestContinuousWorldTick(world, this.continuousUpdate);
                }

                if (!duringLoad) {
                    IUCore.network.get(true).updateTileEntityField(this, "foam");
                    world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
                    this.markDirty();
                }

            }
            return true;
        }
    }


    private void updateRenderState() {
        this.renderState = new TileEntityHeatColdPipes.CableRenderState(
                this.cableType,
                this.insulation,
                this.foam,
                this.connectivity,
                this.getActive()
        );
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
    public TileEntity getTile() {
        return this;
    }


    public static class CableRenderState {

        public final HeatColdType type;
        public final int insulation;
        public final CableFoam foam;
        public final int connectivity;
        public final boolean active;

        public CableRenderState(HeatColdType type, int insulation, CableFoam foam, int connectivity, boolean active) {
            this.type = type;
            this.insulation = insulation;
            this.foam = foam;
            this.connectivity = connectivity;
            this.active = active;
        }

        public int hashCode() {
            int ret = this.type.hashCode();
            ret = ret * 31 + this.insulation;
            ret = ret * 31 + this.foam.hashCode();
            ret = ret * 31 + this.connectivity;
            ret = ret << 1 | (this.active ? 1 : 0);
            return ret;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof TileEntityHeatColdPipes.CableRenderState)) {
                return false;
            } else {
                TileEntityHeatColdPipes.CableRenderState o = (TileEntityHeatColdPipes.CableRenderState) obj;
                return o.type == this.type && o.insulation == this.insulation && o.foam == this.foam && o.connectivity == this.connectivity && o.active == this.active;
            }
        }

        public String toString() {
            return "CableState<" + this.type + ", " + this.insulation + ", " + this.foam + ", " + this.connectivity + ", " + this.active + '>';
        }

    }

}
