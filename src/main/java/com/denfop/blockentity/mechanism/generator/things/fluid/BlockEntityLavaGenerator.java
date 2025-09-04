package com.denfop.blockentity.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuLavaGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenLavaGenerator;
import com.denfop.sound.EnumSound;
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

public class BlockEntityLavaGenerator extends BlockEntityElectricMachine implements BlockEntityUpgrade {

    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final InventoryFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private double lastEnergy;

    public BlockEntityLavaGenerator(BlockPos pos, BlockState state) {
        super(20000, 1, 1, BlockBaseMachine2Entity.lava_gen, pos, state);

        this.energycost = 80;
        this.outputSlot = new InventoryOutput(this, 1);
        this.containerslot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.OUTPUT,
                net.minecraft.world.level.material.Fluids.LAVA
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 20 * 1000, Inventory.TypeItemSlot.OUTPUT,
                Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.LAVA)
        );

        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.3));
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.lava_gen;
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
        if (this.getActive() && this.getLevel().getGameTime() % 5 == 0) {
            ParticleUtils.spawnLavaGeneratorParticles(this.getLevel(), pos, this.getLevel().random);
        }
        boolean needsInvUpdate = false;
        if (!(this.energy.getEnergy() <= 0.0D) && this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {


            if (this.energy.getEnergy() >= this.energycost) {
                needsInvUpdate = this.attemptGeneration();
                this.setActive(true);
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

    public ContainerMenuLavaGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuLavaGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenLavaGenerator((ContainerMenuLavaGenerator) isAdmin);
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(

                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.FluidExtract
        );
    }


}
