package com.denfop.tiles.base;

import com.denfop.Constants;
import com.denfop.container.ContainerTank;
import com.denfop.gui.GUITank;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityLiquedTank extends TileEntityElectricLiquidTankInventory implements IHasGui, IUpgradableBlock {
    public final InvSlotUpgrade upgradeSlot;



    public final InvSlotConsumableLiquidByList containerslot;
    public final InvSlotConsumableLiquidByList containerslot1;
    public final ResourceLocation texture;
    private final String name;
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

    @Override
    public void onNetworkUpdate(String field) {

    }
    public double gaugeLiquidScaled(double i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
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

    public TileEntityLiquedTank(String name, int tanksize, String texturename) {
        super(name,1000,1,tanksize);


        this.containerslot = new InvSlotConsumableLiquidByList(this,
                "containerslot", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill
        );
        this.containerslot1 = new InvSlotConsumableLiquidByList(this,
                "containerslot1", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Drain
        );
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.texture = new ResourceLocation(Constants.TEXTURES,
                "textures/models/" + texturename + ".png");
        this.name = name;
    }

    private static int applyModifier(int extra) {
        double ret = Math.round((14 + extra) * 1.0);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("fluidTank");
        return ret;
    }

    public boolean needsFluid() {
        return this.getFluidTank().getFluidAmount() <= this.getFluidTank().getCapacity();
    }



    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if(this.world.provider.getWorldTime() % 20 == 0)
        IC2.network.get(true).updateTileEntityField(this, "fluidTank");

        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null && stack.getItem() instanceof IUpgradeItem)
                if (((IUpgradeItem) stack.getItem()).onTick(stack, this))
                    needsInvUpdate = true;
        }
        MutableObject<ItemStack> output = new MutableObject();
        if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerslot.transferFromTank(this.fluidTank, output, false);
            if (output.getValue() != null)
                this.outputSlot.add(output.getValue());
        }
        if (this.needsFluid()) {
            output = new MutableObject();
            if (this.containerslot1.transferToTank(this.fluidTank, output, true) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                needsInvUpdate = this.containerslot1.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }
        }
        if (needsInvUpdate)
            markDirty();

    }

    @Override
    public boolean canFill(Fluid paramFluid) {
        return true;
    }

    @Override
    public boolean canDrain(Fluid paramFluid) {
        return true;
    }

    @Override
    public String getInventoryName() {
        return Localization.translate(this.name);
    }

    public ContainerBase<TileEntityLiquedTank> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerTank(entityPlayer, this);
    }

    public int fill(FluidStack resource, boolean doFill) {
        return this.canFill(resource.getFluid()) ? this.getFluidTank().fill(resource, doFill) : 0;
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {

        return !this.canDrain(null) ? null : this.getFluidTank().drain(maxDrain, doDrain);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUITank(new ContainerTank(entityPlayer, this));
    }


    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating())
            setUpgradestat();
    }

    public void setUpgradestat() {
        this.upgradeSlot.onChanged();
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating())
            setUpgradestat();
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing);
    }
}
