package com.denfop.tiles.mechanism.steam;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamElectrolyzer;
import com.denfop.gui.GuiSteamElectrolyzer;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileSteamElectrolyzer extends TileElectricMachine {

    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank3;
    public final FluidHandlerRecipe fluid_handler;
    public final ComponentSteamEnergy steam;
    public final ComponentBaseEnergy ampere;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank4;
    private int level;

    public TileSteamElectrolyzer() {
        super(0, 1, 0);
        this.fluids = this.addComponent(new Fluids(this));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.ampere = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.AMPERE, this, 4000));


        this.fluidTank1 = fluids.addTank("fluidTank1", 4 * 1000, Inventory.TypeItemSlot.INPUT);


        this.fluidTank2 = fluids.addTank("fluidTank2", 4 * 1000, Inventory.TypeItemSlot.OUTPUT);


        this.fluidTank3 = fluids.addTank("fluidTank3", 4 * 1000, Inventory.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("electrolyzer", fluids);
        this.fluidTank4 = fluids.addTank("fluidTank4", 4 * 1000, Inventory.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()));
        this.steam.setFluidTank(fluidTank4);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));


    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_electrolyzer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public ContainerSteamElectrolyzer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSteamElectrolyzer(entityPlayer, this);

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSteamElectrolyzer(new ContainerSteamElectrolyzer(entityPlayer, this));

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {

        super.addInformation(stack, tooltip);

    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }


        boolean drain = false;
        boolean drain1 = false;
        if (this.fluid_handler.output() != null && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.steam.canUseEnergy(
                4) && this.ampere.canUseEnergy(1)) {
            final BaseFluidMachineRecipe output = this.fluid_handler.output();
            final FluidStack inputFluidStack = output.input.getInputs().get(0);
            int size = this.getFluidTank(0).getFluidAmount() / inputFluidStack.amount;
            size = Math.min(this.level + 1, size);
            int cap = this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount();
            FluidStack outputFluidStack = output.output_fluid.get(0);
            cap /= outputFluidStack.amount;
            cap = Math.min(cap, size);
            int cap1 = this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount();
            FluidStack outputFluidStack1 = output.output_fluid.get(1);
            cap1 /= outputFluidStack1.amount;
            size = Math.min(Math.min(size, cap1), cap);
            if (this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount() >= outputFluidStack.amount) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack.getFluid(),
                        outputFluidStack.amount * size
                );
                this.fluidTank2.fill(fluidStack, true);
                drain = true;

            }
            if (this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount() >= outputFluidStack1.amount) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack1.getFluid(),
                        outputFluidStack1.amount * size
                );
                this.fluidTank3.fill(fluidStack, true);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = size * inputFluidStack.amount;
                this.getFluidTank(0).drain(drains, true);
                if (!this.getActive()) {
                    this.setActive(true);
                    initiate(0);
                }
                this.steam.useEnergy(4);
                this.ampere.useEnergy(1);
                setActive(true);

            } else {
                setActive(false);
            }


        } else {
            setActive(false);
        }


    }

    public void setOverclockRates() {
    }

    public FluidTank getFluidTank(int i) {
        switch (i) {
            case 1:
                return this.fluidTank2;
            case 2:
                return this.fluidTank3;
            default:
                return this.fluidTank1;
        }
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.electrolyzer.getSoundEvent();
    }

}
