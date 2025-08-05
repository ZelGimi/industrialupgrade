package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
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
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamElectrolyzer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamElectrolyzer;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

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
    private int levelBlock;

    public TileSteamElectrolyzer(BlockPos pos, BlockState state) {
        super(0, 1, 0, BlockBaseMachine3.steam_electrolyzer, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.ampere = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.AMPERE, this, 4000));


        this.fluidTank1 = fluids.addTank("fluidTank1", 4 * 1000, InvSlot.TypeItemSlot.INPUT);


        this.fluidTank2 = fluids.addTank("fluidTank2", 4 * 1000, InvSlot.TypeItemSlot.OUTPUT);


        this.fluidTank3 = fluids.addTank("fluidTank3", 4 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("electrolyzer", fluids);
        this.fluidTank4 = fluids.addTank("fluidTank4", 4 * 1000, InvSlot.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()));
        this.steam.setFluidTank(fluidTank4);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));


    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_electrolyzer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerSteamElectrolyzer getGuiContainer(Player entityPlayer) {
        return new ContainerSteamElectrolyzer(entityPlayer, this);

    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSteamElectrolyzer((ContainerSteamElectrolyzer) menu);

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {

        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate( "iu.steam_electrolyzer.info"));
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
            int size = this.getFluidTank(0).getFluidAmount() / inputFluidStack.getAmount();
            size = Math.min(this.levelBlock + 1, size);
            int cap = this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount();
            FluidStack outputFluidStack = output.output_fluid.get(0);
            cap /= outputFluidStack.getAmount();
            cap = Math.min(cap, size);
            int cap1 = this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount();
            FluidStack outputFluidStack1 = output.output_fluid.get(1);
            cap1 /= outputFluidStack1.getAmount();
            size = Math.min(Math.min(size, cap1), cap);
            if (this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount() >= outputFluidStack.getAmount()) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack.getFluid(),
                        outputFluidStack.getAmount() * size
                );
                this.fluidTank2.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                drain = true;

            }
            if (this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount() >= outputFluidStack1.getAmount()) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack1.getFluid(),
                        outputFluidStack1.getAmount() * size
                );
                this.fluidTank3.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = size * inputFluidStack.getAmount();
                this.getFluidTank(0).drain(drains, IFluidHandler.FluidAction.EXECUTE);
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
