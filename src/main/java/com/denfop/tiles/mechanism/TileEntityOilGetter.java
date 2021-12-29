package com.denfop.tiles.mechanism;

import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerOilGetter;
import com.denfop.gui.GUIOilGetter;
import com.denfop.tiles.base.TileEntityElectricLiquidTankInventory;
import com.denfop.tiles.base.TileOilBlock;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class TileEntityOilGetter extends TileEntityElectricLiquidTankInventory implements IUpgradableBlock {

    public static int heading;
    public final int defaultTier;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotConsumableLiquid containerslot;
    public int number;
    public int max;
    public boolean notoil = true;

    public TileEntityOilGetter() {
        super("", 50000, 14, 20);
        this.containerslot = new InvSlotConsumableLiquidByList(this,
                "containerslot", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill,
                FluidName.fluidneft.getInstance()
        );
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.defaultTier = 3;
        heading = 2;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.number = nbttagcompound.getInteger("number");
        this.max = nbttagcompound.getInteger("max");
        this.notoil = nbttagcompound.getBoolean("notoil");
        heading = nbttagcompound.getInteger("heading");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("number", number);
        nbttagcompound.setInteger("max", max);
        nbttagcompound.setBoolean("notoil", notoil);
        nbttagcompound.setInteger("heading", heading);
        return nbttagcompound;
    }

    @Override
    public boolean shouldRenderInPass(final int pass) {
        return true;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        boolean needsInvUpdate = false;
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                if (((IUpgradeItem) stack.getItem()).onTick(stack, this)) {
                    needsInvUpdate = true;
                }
            }
        }
        MutableObject<ItemStack> output = new MutableObject();
        if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerslot.transferFromTank(this.fluidTank, output, false);

            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }
        get_oil_max();
        if (this.energy.getEnergy() >= 10 && !notoil) {
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

    private void get_oil_max() {
        Map map = this.getWorld().getChunkFromBlockCoords(this.pos).tileEntities;
        for (Object o : map.values()) {
            TileEntity tile = (TileEntity) o;
            if (tile instanceof TileOilBlock) {
                TileOilBlock tile1 = (TileOilBlock) tile;
                this.max = tile1.max;
                this.number = tile1.number;
                notoil = false;
                return;
            } else {
                notoil = true;
            }
        }
    }

    private void get_oil() {
        Map map = this.getWorld().getChunkFromBlockCoords(this.pos).tileEntities;
        for (Object o : map.values()) {
            TileEntity tile = (TileEntity) o;
            if (tile instanceof TileOilBlock) {
                TileOilBlock tile1 = (TileOilBlock) tile;
                if (tile1.number >= 1) {
                    if (this.fluidTank.getFluidAmount() + 1 <= this.fluidTank.getCapacity()) {
                        fill(new FluidStack(FluidName.fluidneft.getInstance(), 1), true);
                        tile1.number -= 1;
                        this.energy.useEnergy(10);
                    }
                }
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

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 1 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
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
    public ContainerBase<TileEntityOilGetter> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerOilGetter(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIOilGetter(new ContainerOilGetter(entityPlayer, this));
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
        );
    }

}
