package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.brewage.EnumBeerVariety;
import com.denfop.api.brewage.EnumTimeVariety;
import com.denfop.api.brewage.EnumWaterVariety;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBarrel;
import com.denfop.componets.Fluids;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityBarrel extends TileEntityInventory {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank tank;
    EnumBeerVariety beerVariety;
    EnumTimeVariety timeVariety = EnumTimeVariety.BREW;
    EnumWaterVariety waterVariety;
    byte hops;
    byte wheat;
    int time;
    private int prev;

    public TileEntityBarrel() {
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 64000, Fluids.fluidPredicate(FluidRegistry.WATER));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.barrel;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBarrel.barrel;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.barrel.info"));
        tooltip.add(Localization.translate("iu.barrel.info1"));
        tooltip.add(Localization.translate("iu.barrel.info2"));
        if (world != null) {
            tooltip.add(Localization.translate("iu.beer.recipe"));
            tooltip.add(Localization.translate("iu.beer.recipe1") + " " + hops);
            tooltip.add(Localization.translate("iu.beer.recipe2") + " " + wheat);
            tooltip.add(Localization.translate("iu.beer.recipe3") + " " + tank.getFluidAmount() / 1000);
            tooltip.add(Localization.translate("iu.beer.recipe4") + " " + new Timer(time/20).getDisplay());
            if (this.waterVariety != null && this.timeVariety != null && this.beerVariety != null) {
                tooltip.add(Localization.translate("iu.beer.recipe5") + " " + waterVariety.name() + " " + beerVariety.name() + " " + timeVariety.name());
            }
        }
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
        ItemStack stack = player.getHeldItem(hand);
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                        null
                ) && this.tank.getFluidAmount() + 1000 <= this.tank.getCapacity()) {
            ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
            this.prev = this.tank.getFluidAmount();
            waterVariety = EnumWaterVariety.getVarietyFromLevelWater(this.tank.getFluidAmount() / 1000);
            time = 0;
            return true;
        }
        if (!this.getWorld().isRemote && stack.getItem() == Items.WHEAT && (this.hops + this.wheat < 10)) {
            this.wheat++;
            stack.shrink(1);
        }
        if (!this.getWorld().isRemote && stack.getItem() == IUItem.hops && (this.hops + this.wheat < 10)) {
            this.hops++;
            stack.shrink(1);
        }
        if (!this.getWorld().isRemote && hops + this.wheat == 10 && beerVariety == null) {
            beerVariety = EnumBeerVariety.getBeerVarietyFromRatio(this.wheat, hops);
        }
        if (!this.getWorld().isRemote && stack.getItem() == IUItem.booze_mug && !ModUtils
                .nbt(stack)
                .hasKey("beer") && waterVariety != null && beerVariety != null) {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            nbt.setBoolean("beer", true);
            nbt.setByte("amount", (byte) 5);
            nbt.setByte("waterVariety", (byte) waterVariety.ordinal());
            nbt.setByte("timeVariety", (byte) timeVariety.ordinal());
            nbt.setByte("beerVariety", (byte) beerVariety.ordinal());
            hops = 0;
            wheat = 0;
            time = 0;
            prev = 0;
            this.tank.drain(this.tank.getFluidAmount(), true);
            beerVariety = null;
            timeVariety = EnumTimeVariety.BREW;
            waterVariety = null;
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (prev != this.tank.getFluidAmount()) {
            prev = this.tank.getFluidAmount();
            this.waterVariety = EnumWaterVariety.getVarietyFromLevelWater(prev / 1000);
            time = 0;
        }
        if (this.hops + this.wheat == 10 && this.waterVariety != null) {
            this.time++;
            if (this.time % 1200 == 0) {
                this.timeVariety = EnumTimeVariety.getVarietyFromTime(this.time / (3600 * 20D));
            }
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.prev = nbtTagCompound.getInteger("prev");
        this.timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
        if (nbtTagCompound.getBoolean("hasBeerVariety")) {
            this.beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("hasBeerVariety")];
        }
        if (nbtTagCompound.getBoolean("hasWaterVariety")) {
            this.waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
        }
        this.time = nbtTagCompound.getInteger("time");
        this.wheat = nbtTagCompound.getByte("wheat");
        this.hops = nbtTagCompound.getByte("hops");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        final NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("prev", this.prev);
        nbtTagCompound.setByte("timeVariety", (byte) this.timeVariety.ordinal());
        if (this.beerVariety != null) {
            nbtTagCompound.setBoolean("hasBeerVariety", true);
            nbtTagCompound.setByte("beerVariety", (byte) this.beerVariety.ordinal());
        } else {
            nbtTagCompound.setBoolean("hasBeerVariety", false);
        }
        if (this.waterVariety != null) {
            nbtTagCompound.setBoolean("hasWaterVariety", true);
            nbtTagCompound.setByte("waterVariety", (byte) this.waterVariety.ordinal());
        } else {
            nbtTagCompound.setBoolean("hasWaterVariety", false);
        }
        nbtTagCompound.setInteger("time", this.time);
        nbtTagCompound.setByte("wheat", this.wheat);
        nbtTagCompound.setByte("hops", this.hops);
        return nbtTagCompound;
    }

}
