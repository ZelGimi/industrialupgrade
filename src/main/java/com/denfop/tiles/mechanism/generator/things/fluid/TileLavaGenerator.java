package com.denfop.tiles.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerLavaGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiLavaGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.EnumSet;
import java.util.Set;

public class TileLavaGenerator extends TileElectricMachine implements IUpgradableBlock {

    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private double lastEnergy;

    public TileLavaGenerator(BlockPos pos, BlockState state) {
        super(20000, 1, 1, BlockBaseMachine2.lava_gen, pos, state);

        this.energycost = 80;
        this.outputSlot = new InvSlotOutput(this, 1);
        this.containerslot = new InvSlotFluidByList(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT,
                net.minecraft.world.level.material.Fluids.LAVA
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 20 * 1000, InvSlot.TypeItemSlot.OUTPUT,
                Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.LAVA)
        );

        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.3));
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.lava_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.lastEnergy = nbt.getDouble("lastEnergy");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putDouble("lastEnergy", this.lastEnergy);
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.setUpgradestat();
        }

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.gen_lava.getSoundEvent();
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive()  && this.getLevel().getGameTime() % 5 == 0){
            ParticleUtils.spawnLavaGeneratorParticles(this.getLevel(),pos,this.getLevel().random);
        }

        boolean needsInvUpdate = false;
        if (!(this.energy.getEnergy() <= 0.0D) && this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {


            if (this.energy.getEnergy() >= this.energycost) {
                this.setActive(true);
                needsInvUpdate = this.attemptGeneration();
                if (this.getLevel().getGameTime() % 40 == 0) {
                    initiate(0);
                }
            } else {
                if (this.getLevel().getGameTime() % 40 == 0) {
                    initiate(2);
                }
            }

            this.lastEnergy = this.energy.getEnergy();

        } else {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        if (!this.containerslot.isEmpty() && this.fluidTank.getFluidAmount() > 0) {
            this.containerslot.processFromTank(this.fluidTank, this.outputSlot);
        }
        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }
    }

    public boolean attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return false;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fill(new FluidStack(net.minecraft.world.level.material.Fluids.LAVA, Math.min(m, k)), IFluidHandler.FluidAction.EXECUTE);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
        return true;
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerLavaGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerLavaGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiLavaGenerator((ContainerLavaGenerator) isAdmin);
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(

                UpgradableProperty.Transformer,
                UpgradableProperty.FluidExtract
        );
    }


}
