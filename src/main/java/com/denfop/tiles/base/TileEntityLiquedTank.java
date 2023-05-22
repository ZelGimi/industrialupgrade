package com.denfop.tiles.base;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerTank;
import com.denfop.gui.GuiTank;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.ref.TeBlock;
import ic2.core.util.LiquidUtil;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityLiquedTank extends TileEntityInventory implements IHasGui, IUpgradableBlock {

    public final InvSlotUpgrade upgradeSlot;


    public final InvSlotConsumableLiquidByList containerslot;
    public final InvSlotConsumableLiquidByList containerslot1;
    public final ResourceLocation texture;
    public final Fluids fluids;
    public final FluidTank fluidTank;
    public final InvSlotOutput outputSlot;
    private int old_amount;

    public TileEntityLiquedTank(int tanksize, String texturename) {


        this.containerslot = new InvSlotConsumableLiquidByList(this,
                "containerslot", InvSlot.Access.I, 1, InvSlot.InvSide.ANY, InvSlotConsumableLiquid.OpType.Fill
        );
        this.containerslot.setUsually(true);
        this.containerslot1 = new InvSlotConsumableLiquidByList(this,
                "containerslot1", InvSlot.Access.I, 1, InvSlot.InvSide.ANY, InvSlotConsumableLiquid.OpType.Drain
        );
        this.containerslot1.setUsually(true);
        this.texture = new ResourceLocation(
                Constants.TEXTURES,
                "textures/models/" + texturename + ".png"
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", tanksize * 1000);
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);

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
        if (!this.getWorld().isRemote && LiquidUtil.isFluidContainer(player.getHeldItem(hand))) {

            return FluidUtil.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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

    @Override
    public void onNetworkUpdate(String field) {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getLocalizedName());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.amount / 1000 + " B");

        }
        super.addInformation(stack, tooltip, advanced);
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));
            if (fluidStack != null) {
                this.fluidTank.fill(fluidStack, true);
            }
            this.old_amount = this.fluidTank.getFluidAmount();
            IUCore.network.get(true).updateTileEntityField(this, "fluidTank");
        }
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            if (this.fluidTank.getFluidAmount() > 0) {
                NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
                nbt.setTag("fluid", this.fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
            }
        }
        return drop;
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

    public List<String> getNetworkFields() {
        List<String> ret = super.getNetworkFields();
        ret.add("fluidTank");
        return ret;
    }

    public boolean needsFluid() {
        return this.getFluidTank().getFluidAmount() < this.getFluidTank().getCapacity();
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if (this.world.provider.getWorldTime() % 20 == 0) {
            boolean need = false;
            if (this.fluidTank.getFluidAmount() != this.old_amount) {
                this.old_amount = this.fluidTank.getFluidAmount();
                need = true;
            }
            if (need) {
                IUCore.network.get(true).updateTileEntityField(this, "fluidTank");
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
        if (this.needsFluid()) {
            output = new MutableObject<>();
            if (this.containerslot1.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                needsInvUpdate = this.containerslot1.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }
        }
        if (this.upgradeSlot.tickNoMark() && needsInvUpdate) {
            this.setUpgradestat();
        }

    }


    public boolean canFill() {
        return true;
    }


    public boolean canDrain() {
        return true;
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    public ContainerTank getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerTank(entityPlayer, this);
    }

    public int fill(FluidStack resource, boolean doFill) {
        return this.canFill() ? this.getFluidTank().fill(resource, doFill) : 0;
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {

        return !this.canDrain() ? null : this.getFluidTank().drain(maxDrain, doDrain);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiTank(new ContainerTank(entityPlayer, this));
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
        if (IC2.platform.isSimulating()) {
            setUpgradestat();
        }
    }

    public void setUpgradestat() {
    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setUpgradestat();
        }
    }


    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public boolean useEnergy(final double v) {
        return false;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing,
                UpgradableProperty.FluidConsuming
        );
    }

}
