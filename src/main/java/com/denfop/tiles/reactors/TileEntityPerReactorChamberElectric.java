package com.denfop.tiles.reactors;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.reactor.IReactorChamber;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Fluids;
import ic2.core.block.comp.Redstone;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPerReactorChamberElectric extends TileEntityBlock implements IInventory, IReactorChamber, IEnergyEmitter {

    public final Redstone redstone = this.addComponent(new Redstone(this));
    protected final Fluids fluids = this.addComponent(new Fluids(this));
    private TileEntityPerNuclearReactor reactor;
    private long lastReactorUpdate;

    public TileEntityPerReactorChamberElectric() {

    }

    protected void onLoaded() {
        super.onLoaded();
        this.updateRedstoneLink();
    }

    private void updateRedstoneLink() {
        if (!this.getWorld().isRemote) {
            TileEntityPerNuclearReactor reactor = this.getReactor();
            if (reactor != null) {
                this.redstone.linkTo(reactor.redstone);
            }

        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            TileEntityPerNuclearReactor.showHeatEffects(this.getWorld(), this.pos, reactor.getHeat());
        }

    }

    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            World world = this.getWorld();
            return reactor.getBlockType().onBlockActivated(
                    world,
                    reactor.getPos(),
                    world.getBlockState(reactor.getPos()),
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

    protected void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        this.lastReactorUpdate = 0L;
        if (this.getReactor() == null) {
            this.destoryChamber(true);
        }

    }

    public void destoryChamber(boolean wrench) {
        World world = this.getWorld();
        world.setBlockToAir(this.pos);

        for (final ItemStack drop : this.getSelfDrops(0, wrench)) {
            StackUtil.dropAsEntity(world, this.pos, drop);
        }

    }

    public String getName() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getName() : "<null>";
    }

    public boolean hasCustomName() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null && reactor.hasCustomName();
    }

    public ITextComponent getDisplayName() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getDisplayName() : new TextComponentString("<null>");
    }

    public int getSizeInventory() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getSizeInventory() : 0;
    }

    public boolean isEmpty() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor == null || reactor.isEmpty();
    }

    public ItemStack getStackInSlot(int index) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getStackInSlot(index) : null;
    }

    public ItemStack decrStackSize(int index, int count) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.decrStackSize(index, count) : null;
    }

    public ItemStack removeStackFromSlot(int index) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.removeStackFromSlot(index) : null;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            reactor.setInventorySlotContents(index, stack);
        }

    }

    public int getInventoryStackLimit() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getInventoryStackLimit() : 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null && reactor.isUsableByPlayer(player);
    }

    public void openInventory(EntityPlayer player) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            reactor.openInventory(player);
        }

    }

    public void closeInventory(EntityPlayer player) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            reactor.closeInventory(player);
        }

    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null && reactor.isItemValidForSlot(index, stack);
    }

    public int getField(int id) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getField(id) : 0;
    }

    public void setField(int id, int value) {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            reactor.setField(id, value);
        }

    }

    public int getFieldCount() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        return reactor != null ? reactor.getFieldCount() : 0;
    }

    public void clear() {
        TileEntityPerNuclearReactor reactor = this.getReactor();
        if (reactor != null) {
            reactor.clear();
        }

    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true;
    }

    public TileEntityPerNuclearReactor getReactorInstance() {
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

    private TileEntityPerNuclearReactor getReactor() {
        long time = this.getWorld().getTotalWorldTime();
        if (time != this.lastReactorUpdate) {
            this.updateReactor();
            this.lastReactorUpdate = time;
        } else if (this.reactor != null && this.reactor.isInvalid()) {
            this.reactor = null;
        }

        return this.reactor;
    }

    private void updateReactor() {
        World world = this.getWorld();
        this.reactor = null;
        EnumFacing[] var2 = EnumFacing.VALUES;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            EnumFacing facing = var2[var4];
            TileEntity te = world.getTileEntity(this.pos.offset(facing));
            if (te instanceof TileEntityPerNuclearReactor) {
                this.reactor = (TileEntityPerNuclearReactor) te;
                break;
            }
        }

    }

}
