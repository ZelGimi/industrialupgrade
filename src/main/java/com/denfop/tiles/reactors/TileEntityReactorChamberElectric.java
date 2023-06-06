package com.denfop.tiles.reactors;


import com.denfop.api.energy.IAdvEnergySource;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import ic2.api.reactor.IReactor;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TileEntityReactorChamberElectric extends TileEntityBlock implements IInventory, IAdvEnergySource, IChamber {

    private TileEntityNuclearReactorElectric reactor;
    private long lastReactorUpdate;
    private boolean load = false;


    public TileEntityReactorChamberElectric() {

    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if(!load){
            load = true;
            this.onNeighborChange(this.getBlockType().getBlockState().getBlock(), this.getPos());
            if (this.reactor != null) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this.reactor, this));

            }
        }
    }
    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.onNeighborChange(this.getBlockType().getBlockState().getBlock(), this.getPos());
    }
    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            TileEntityAdvNuclearReactorElectric.showHeatEffects(this.getWorld(), this.pos, reactor.getHeat());
        }

    }

    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            World world = this.getWorld();
            return reactor.getBlockType().onBlockActivated(
                    world,
                    reactor.getBlockPos(),
                    world.getBlockState(reactor.getBlockPos()),
                    player,
                    hand,
                    side,
                    hitX,
                    hitY,
                    hitZ
            );
        } else {
            return false;
        }
    }




    @Nonnull
    public String getName() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.getName() : "<null>";
    }

    public boolean hasCustomName() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null && reactor.hasCustomName();
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? Objects.requireNonNull(reactor.getDisplayName()) : new TextComponentString("<null>");
    }

    public int getSizeInventory() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.getSizeInventory() : 0;
    }

    public boolean isEmpty() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor == null || reactor.isEmpty();
    }

    @Nonnull
    public ItemStack getStackInSlot(int index) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.getStackInSlot(index) : ItemStack.EMPTY;
    }

    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.decrStackSize(index, count) : ItemStack.EMPTY;
    }

    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.removeStackFromSlot(index) : ItemStack.EMPTY;
    }

    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            reactor.setInventorySlotContents(index, stack);
        }

    }

    public int getInventoryStackLimit() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.getInventoryStackLimit() : 0;
    }

    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null && reactor.isUsableByPlayer(player);
    }

    public void openInventory(@Nonnull EntityPlayer player) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            reactor.openInventory(player);
        }

    }

    public void closeInventory(@Nonnull EntityPlayer player) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            reactor.closeInventory(player);
        }

    }

    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null && reactor.isItemValidForSlot(index, stack);
    }

    public int getField(int id) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.getField(id) : 0;
    }

    public void setField(int id, int value) {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            reactor.setField(id, value);
        }

    }

    public int getFieldCount() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        return reactor != null ? reactor.getFieldCount() : 0;
    }

    public void clear() {
        TileEntityNuclearReactorElectric reactor = this.getReactor();
        if (reactor != null) {
            reactor.clear();
        }

    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true;
    }

    public TileEntityNuclearReactorElectric getReactorInstance() {
        return this.reactor;
    }

    public boolean isWall() {
        return false;
    }

    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (super.hasCapability(capability, facing)) {
            return super.getCapability(capability, facing);
        } else {
            return this.reactor != null ? this.reactor.getCapability(capability, facing) : null;
        }
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing) || this.reactor != null && this.reactor.hasCapability(capability, facing);
    }

    public TileEntityNuclearReactorElectric getReactor() {
        long time = this.getWorld().getTotalWorldTime();
        if (time != this.lastReactorUpdate) {
            this.updateReactor();
            this.lastReactorUpdate = time;
        } else if (this.reactor != null && this.reactor.isInvalid()) {
            this.reactor = null;
        }

        return this.reactor;
    }

    @Override
    public void setReactor(final IReactor reactor) {
        this.reactor = (TileEntityNuclearReactorElectric) reactor;
    }

    protected void onBlockBreak() {
        super.onBlockBreak();

        if (this.reactor != null) {
            this.reactor.change = true;
            this.reactor.getReactorSize();
            BlockPos pos1 = pos.add(this.getFacing().getDirectionVec());
            TileEntity tile = this.getWorld().getTileEntity(pos1);
            if (tile instanceof TileEntityHeatSensor) {
                this.reactor.isLimit = false;
            }
        }
    }

    protected void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        this.updateReactor();
        if (this.reactor == null) {
            this.destoryChamber(true);

        } else {
            this.reactor.change = true;
        }
    }

    public void destoryChamber(boolean wrench) {
        World world = this.getWorld();
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));

        world.setBlockToAir(this.pos);

        for (final ItemStack drop : this.getSelfDrops(0, wrench)) {
            StackUtil.dropAsEntity(world, this.pos, drop);
        }

    }

    @Override
    protected void onUnloaded() {
        if (this.reactor != null) {
            if (!this.reactor.isInvalid()) {
                this.reactor.change = true;
                this.reactor.getReactorSize();
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            }
        }
        super.onUnloaded();
    }

    private void updateReactor() {
        World world = this.getWorld();
        this.reactor = null;
        EnumFacing[] var2 = EnumFacing.VALUES;


        for (EnumFacing facing : var2) {
            TileEntity te = world.getTileEntity(this.pos.offset(facing));

            if (te instanceof TileEntityNuclearReactorElectric) {

                this.reactor = (TileEntityNuclearReactorElectric) te;
                this.reactor.getReactorSize();
                break;
            }
        }

    }

    @Override
    public double getPerEnergy() {
        if (this.reactor != null) {
            return this.reactor.getPerEnergy();
        }
        return 0;
    }

    @Override
    public double getPastEnergy() {
        if (this.reactor != null) {
            return this.reactor.getPastEnergy();
        }
        return 0;
    }

    @Override
    public void setPastEnergy(final double pastEnergy) {
        if (this.reactor != null) {
            this.reactor.setPastEnergy(pastEnergy);
        }
    }

    @Override
    public void addPerEnergy(final double setEnergy) {
        if (this.reactor != null) {
            this.reactor.addPerEnergy(setEnergy);
        }
    }

    @Override
    public boolean isSource() {
        if (this.reactor != null) {
            return this.reactor.isSource();
        }
        return false;
    }

    @Override
    public double getOfferedEnergy() {
        if (this.reactor != null) {
            return this.reactor.getOfferedEnergy();
        }
        return 0;
    }

    @Override
    public void drawEnergy(final double var1) {
        if (this.reactor != null) {
            this.reactor.drawEnergy(var1);
        }
    }

    @Override
    public int getSourceTier() {
        if (this.reactor != null) {
            return this.reactor.getSourceTier();
        }
        return 0;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

}
