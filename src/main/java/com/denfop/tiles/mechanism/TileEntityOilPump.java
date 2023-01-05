package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerOilPump;
import com.denfop.gui.GuiOilPump;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityElectricLiquidTankInventory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.init.Localization;
import ic2.core.ref.TeBlock;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityOilPump extends TileEntityElectricLiquidTankInventory implements IUpgradableBlock, IManufacturerBlock {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1.0, 1.0, 2.0D,
            2.0
    ));
    public final int defaultTier;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotConsumableLiquid containerslot;
    public int level;
    public boolean find;
    public int count;
    public int maxcount;
    public Vein vein;
    public int type;

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
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 2 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getLocalizedName());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.amount / 1000 + " B");

        }
        super.addInformation(stack, tooltip, advanced);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        nbttagcompound.setBoolean("find", this.find);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
        this.find = nbttagcompound.getBoolean("find");
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

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    private void updateTileEntityField() {
        IC2.network.get(true).updateTileEntityField(this, "level");
        IC2.network.get(true).updateTileEntityField(this, "count");
        IC2.network.get(true).updateTileEntityField(this, "find");
        IC2.network.get(true).updateTileEntityField(this, "maxcount");
        IC2.network.get(true).updateTileEntityField(this, "type");
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
            if (this.fluidTank.getFluidAmount() > 0) {
                nbt.setTag("fluid", this.fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
            }
        }
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (this.world.isRemote) {
            return;
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != null) {
            this.find = this.vein.get();
            this.count = this.vein.getCol();
            this.maxcount = this.vein.getMaxCol();
            this.type = this.vein.getType().ordinal();
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));
            if (fluidStack != null) {
                this.fluidTank.fill(fluidStack, true);
            }
            IC2.network.get(true).updateTileEntityField(this, "fluidTank");
        }
        updateTileEntityField();
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (this.world.isRemote) {
            return;
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != null) {
            boolean find = this.vein.get();
            if (this.find != find) {
                this.vein.setFind(this.find);
            }
            this.count = this.vein.getCol();
            this.maxcount = this.vein.getMaxCol();
            this.type = this.vein.getType().ordinal();
        }
        updateTileEntityField();
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
        if (this.vein == null || this.vein.getType() != Type.OIL) {
            return;
        }

        MutableObject<ItemStack> output = new MutableObject<>();
        if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerslot.transferFromTank(this.fluidTank, output, false);

            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }
        if (this.energy.getEnergy() >= 2 && this.find) {
            get_oil();
            if (!this.getActive()) {
                this.setActive(true);
                initiate(0);
            }
        } else {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }
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
                this.energy.useEnergy(2);
                updateTileEntityField();
                if (this.upgradeSlot.tickNoMark()) {
                    setUpgradestat();
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
