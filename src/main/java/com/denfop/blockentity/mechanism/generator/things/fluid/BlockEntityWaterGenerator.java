package com.denfop.blockentity.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWaterGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWaterGenerator;
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

public class BlockEntityWaterGenerator extends BlockEntityElectricMachine implements IUpgradableBlock {

    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final InventoryFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    private double lastEnergy;

    public BlockEntityWaterGenerator(BlockPos pos, BlockState state) {
        super(10000, 1, 1, BlockBaseMachine3Entity.watergenerator, pos, state);

        this.energycost = 40;
        this.outputSlot = new InventoryOutput(this, 1);
        this.containerslot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.OUTPUT,
                net.minecraft.world.level.material.Fluids.WATER
        );

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 20 * 1000, Inventory.TypeItemSlot.OUTPUT,
                Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER)
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.25));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));

    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.watergenerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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


    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnWaterGeneratorParticles(level, pos, level.random);
        }
        if (!(this.energy.getEnergy() <= 0.0D) && this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {
            if (!this.getActive()) {
                this.setActive(true);
            }

            if (this.energy.getEnergy() >= this.energycost) {
                this.attemptGeneration();
                if (this.level.getGameTime() % 40 == 0) {
                    initiate(0);
                }
            } else {
                if (this.level.getGameTime() % 40 == 0) {
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

    public void attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fill(new FluidStack(net.minecraft.world.level.material.Fluids.WATER, Math.min(m, k)), IFluidHandler.FluidAction.EXECUTE);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerMenuWaterGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuWaterGenerator(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenWaterGenerator((ContainerMenuWaterGenerator) menu);
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


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
