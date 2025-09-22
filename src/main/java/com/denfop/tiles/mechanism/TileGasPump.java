package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGasPump;
import com.denfop.gui.GuiGasPump;
import com.denfop.invslot.*;
import com.denfop.invslot.InventoryFluid;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileGasPump extends TileElectricLiquidTankInventory implements IUpgradableBlock, IManufacturerBlock {


    public final int defaultTier;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryFluid containerslot;
    public int level;
    public boolean find;
    public int count;
    public int maxcount;
    public Vein vein;
    public int type;

    public TileGasPump() {
        super(50000, 14, 20, Fluids.fluidPredicate(FluidName.fluidgas.getInstance()
        ));
        this.containerslot = new InventoryFluidByList(this,
                Inventory.TypeItemSlot.INPUT, 1, InventoryFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluidgas.getInstance()
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.defaultTier = 14;
        this.level = 0;

        fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.gas_pump;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 2 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);
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

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            count = (int) DecoderHandler.decode(customPacketBuffer);
            find = (boolean) DecoderHandler.decode(customPacketBuffer);
            maxcount = (int) DecoderHandler.decode(customPacketBuffer);
            level = (int) DecoderHandler.decode(customPacketBuffer);
            type = (int) DecoderHandler.decode(customPacketBuffer);
            vein = (Vein) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, count);
            EncoderHandler.encode(packet, find);
            EncoderHandler.encode(packet, maxcount);
            EncoderHandler.encode(packet, level);
            EncoderHandler.encode(packet, type);
            EncoderHandler.encode(packet, vein);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public Vein getVein() {
        return vein;
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

        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    private void updateTileEntityField() {
        new PacketUpdateFieldTile(this, "vein", vein);
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.isItemEqual(this.getPickBlock(
                null,
                null
        )) && (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self)) {
            NBTTagCompound nbt = ModUtils.nbt(drop);
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
        if (this.vein != VeinSystem.system.getEMPTY()) {
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
            new PacketUpdateFieldTile(this, "fluidTank", fluidTank);
        }
        updateTileEntityField();
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("fluidTank")) {
            try {
                FluidTank fluidTank = (FluidTank) DecoderHandler.decode(is);
                this.fluidTank.readFromNBT(fluidTank.writeToNBT(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("vein")) {
            try {
                vein = (Vein) DecoderHandler.decode(is);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        super.updateField(name, is);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.world.isRemote) {
            return;
        }
        this.vein = VeinSystem.system.getVein(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        if (this.vein != VeinSystem.system.getEMPTY()) {
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
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null || this.vein.getType() != Type.GAS) {
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
            getGas();
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

    private void getGas() {
        if (vein.getCol() >= 1) {
            int size = Math.min(this.level + 1, vein.getCol());
            size = Math.min(size, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
            if (this.fluidTank.getFluidAmount() + size <= this.fluidTank.getCapacity()) {
                this.fluidTank.fill(new FluidStack(FluidName.fluidgas.getInstance(), size), true);
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


    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
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
    public ContainerGasPump getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGasPump(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiGasPump getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGasPump(new ContainerGasPump(entityPlayer, this));
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.FluidExtract
        );
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.oilgetter.getSoundEvent();
    }

}
