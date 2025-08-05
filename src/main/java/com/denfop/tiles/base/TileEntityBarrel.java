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
import com.denfop.datacomponent.BeerInfo;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import com.denfop.utils.ParticleUtils;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

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

    public TileEntityBarrel(BlockPos pos, BlockState state) {
        super(BlockBarrel.barrel, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 64000, Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.barrel.getBlock();
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
        if (level != null) {
            tooltip.add(Localization.translate("iu.beer.recipe"));
            tooltip.add(Localization.translate("iu.beer.recipe1") + " " + hops);
            tooltip.add(Localization.translate("iu.beer.recipe2") + " " + wheat);
            tooltip.add(Localization.translate("iu.beer.recipe3") + " " + tank.getFluidAmount() / 1000);
            tooltip.add(Localization.translate("iu.beer.recipe4") + " " + new Timer(time / 20).getDisplay());
            if (this.waterVariety != null && this.timeVariety != null && this.beerVariety != null) {
                tooltip.add(Localization.translate("iu.beer.recipe5") + " " + waterVariety.name() + " " + beerVariety.name() + " " + timeVariety.name());
            }
        }
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player
                .getItemInHand(hand)) != null && this.tank.getFluidAmount() + 1000 <= this.tank.getCapacity()) {
            ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
            this.prev = this.tank.getFluidAmount();
            waterVariety = EnumWaterVariety.getVarietyFromLevelWater(this.tank.getFluidAmount() / 1000);
            time = 0;
            return true;
        }
        if (!this.getWorld().isClientSide && stack.getItem() == Items.WHEAT && (this.hops + this.wheat < 10)) {
            this.wheat++;
            stack.shrink(1);
        }
        if (!this.getWorld().isClientSide && stack.getItem() == IUItem.hops.getItem() && (this.hops + this.wheat < 10)) {
            this.hops++;
            stack.shrink(1);
        }
        if (!this.getWorld().isClientSide && hops + this.wheat == 10 && beerVariety == null) {
            beerVariety = EnumBeerVariety.getBeerVarietyFromRatio(this.wheat, hops);
        }
        if (!this.getWorld().isClientSide && stack.getItem() == IUItem.booze_mug.getItem() && !stack.has(DataComponentsInit.BEER) && waterVariety != null && beerVariety != null) {
            stack.set(DataComponentsInit.BEER, new BeerInfo(waterVariety, timeVariety, beerVariety, 5));
            hops = 0;
            wheat = 0;
            time = 0;
            prev = 0;
            this.tank.drain(this.tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            beerVariety = null;
            timeVariety = EnumTimeVariety.BREW;
            waterVariety = null;
        }
        return super.onActivated(player, hand, side, vec3);
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
            if (this.level.getGameTime() % 5 == 0) {
                ParticleUtils.spawnFermenterParticles(level, pos, level.random);
            }
            if (this.time % 1200 == 0) {
                this.timeVariety = EnumTimeVariety.getVarietyFromTime(this.time / (3600 * 20D));
            }
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.prev = nbtTagCompound.getInt("prev");
        this.timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
        if (nbtTagCompound.getBoolean("hasBeerVariety")) {
            this.beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("hasBeerVariety")];
        }
        if (nbtTagCompound.getBoolean("hasWaterVariety")) {
            this.waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
        }
        this.time = nbtTagCompound.getInt("time");
        this.wheat = nbtTagCompound.getByte("wheat");
        this.hops = nbtTagCompound.getByte("hops");
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        final CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("prev", this.prev);
        nbtTagCompound.putByte("timeVariety", (byte) this.timeVariety.ordinal());
        if (this.beerVariety != null) {
            nbtTagCompound.putBoolean("hasBeerVariety", true);
            nbtTagCompound.putByte("beerVariety", (byte) this.beerVariety.ordinal());
        } else {
            nbtTagCompound.putBoolean("hasBeerVariety", false);
        }
        if (this.waterVariety != null) {
            nbtTagCompound.putBoolean("hasWaterVariety", true);
            nbtTagCompound.putByte("waterVariety", (byte) this.waterVariety.ordinal());
        } else {
            nbtTagCompound.putBoolean("hasWaterVariety", false);
        }
        nbtTagCompound.putInt("time", this.time);
        nbtTagCompound.putByte("wheat", this.wheat);
        nbtTagCompound.putByte("hops", this.hops);
        return nbtTagCompound;
    }

}
