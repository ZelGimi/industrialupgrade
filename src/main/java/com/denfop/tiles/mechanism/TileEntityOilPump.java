package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerOilPump;
import com.denfop.gui.GuiOilPump;
import com.denfop.tiles.base.TileEntityElectricLiquidTankInventory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityOilPump extends TileEntityElectricLiquidTankInventory implements IUpgradableBlock {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1.0, 1.0, 2.0D,
            2.0
    ));
    public final int defaultTier;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotConsumableLiquid containerslot;
    public int level;
    public boolean find;
    public int count;
    private Vein vein;

    public TileEntityOilPump() {
        super(50000, 14, 20, Fluids.fluidPredicate(FluidName.fluidneft.getInstance()));
        this.containerslot = new InvSlotConsumableLiquidByList(this,
                "containerslot", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill,
                FluidName.fluidneft.getInstance()
        );
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.defaultTier = 14;
        this.level = 0;
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
    }

    public Vein getVein() {
        return vein;
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return false;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    private void updateTileEntityField() {
        IC2.network.get(true).updateTileEntityField(this, "level");
        IC2.network.get(true).updateTileEntityField(this, "count");
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != null) {
            this.find = this.vein.get();
            this.count = this.vein.getCol();
        }
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != null) {
            this.find = this.vein.get();
            this.count = this.vein.getCol();
        }
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.oilgetter);
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

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null || this.vein.getType() == Type.EMPTY) {
            return;
        }
        updateTileEntityField();
        boolean needsInvUpdate = false;
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                if (((IUpgradeItem) stack.getItem()).onTick(stack, this)) {
                    needsInvUpdate = true;
                }
            }
        }
        MutableObject<ItemStack> output = new MutableObject<>();
        if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerslot.transferFromTank(this.fluidTank, output, false);

            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }
        if (this.energy.getEnergy() >= 10 && this.vein.getType() == Type.OIL && this.vein.get()) {
            get_oil();
            initiate(0);
        } else {
            initiate(2);
        }
        if (getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
        if (needsInvUpdate) {
            markDirty();
        }

    }

    private void get_oil() {
        if (vein.getCol() >= 1) {
            int size = Math.min(this.level + 1, vein.getCol());
            size = Math.min(size, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
            if (this.fluidTank.getFluidAmount() + size <= this.fluidTank.getCapacity()) {
                fill(new FluidStack(FluidName.fluidneft.getInstance(), size), true);
                vein.removeCol(size);
                this.count = vein.getCol();
                this.energy.useEnergy(10);
            }
        }
    }

    @Override
    public boolean canFill(Fluid fluid) {
        return (fluid == FluidName.fluidneft.getInstance());
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setUpgradestat();
        }
    }

    public void setUpgradestat() {
        this.upgradeSlot.onChanged();
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.getEnergy() >= amount) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public String getStartSoundFile() {
        return "Machines/oilgetter.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public boolean canDrain(Fluid fluid) {
        return true;
    }


    @Override
    public ContainerBase<TileEntityOilPump> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerOilPump(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiOilPump(new ContainerOilPump(entityPlayer, this));
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
        );
    }

}
