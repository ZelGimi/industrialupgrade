package com.denfop.blockentity.base;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCombinerMatter;
import com.denfop.inventory.*;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenCombinerMatter;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.*;

public class BlockEntityCombinerMatter extends BlockEntityElectricLiquidTankInventory implements BlockEntityUpgrade,
        IUpdateTick, IMatter {

    public final InventoryMatter inputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryRecipes amplifierSlot;
    public final InventoryOutput outputSlot;
    public final InventoryFluid containerslot;
    protected final Redstone redstone;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public int scrap;
    public double energycost;
    private double lastEnergy;
    private MachineRecipe recipe;
    private int amountScrap;

    public BlockEntityCombinerMatter(BlockPos pos, BlockState state) {
        super(0, 14, 12, BlockBaseMachine2Entity.combiner_matter, pos, state);
        this.energycost = 0;
        this.amplifierSlot = new InventoryRecipes(this, "matterAmplifier", this);
        this.outputSlot = new InventoryOutput(this, 1);

        fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.containerslot = new InventoryFluidByList(this, Inventory.TypeItemSlot.INPUT, 1,
                InventoryFluid.TypeFluidSlot.OUTPUT, FluidName.fluiduu_matter.getInstance().get()
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.inputSlot = new InventoryMatter(this);
        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));

        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
                                    @Override
                                    public void action(final int input) {
                                        energy.setEnabled(input == 0);
                                    }
                                }
        );

        this.fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    private static int applyModifier(int base) {
        double ret = Math.round((base + 14) * 1.0);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            scrap = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, scrap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.combiner_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    @Override
    public int getInventoryStackLimit() {
        return 4;
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.scrap = nbttagcompound.getInt("scrap");
        this.lastEnergy = nbttagcompound.getDouble("lastEnergy");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("scrap", this.scrap);
        nbttagcompound.putDouble("lastEnergy", this.lastEnergy);
        return nbttagcompound;

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidTank.getCapacity() <= 0) {
            this.fluidTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        }
        if (this.redstone.hasRedstoneInput() || this.energy.getEnergy() <= 0.0D) {
            if (this.getActive()) {
                setActive(false);
            }
        } else {
            if (this.scrap > 0) {
                double bonus = Math.min(this.scrap, this.energy.getEnergy() - this.lastEnergy);
                if (bonus > 0.0D) {
                    this.energy.forceAddEnergy(5.0D * bonus);
                    this.scrap = (int) ((double) this.scrap - bonus);
                }


            }
            if (!this.getActive()) {
                this.setActive(true);
            }
            if (this.scrap < 10000 && this.amountScrap > 0) {
                recipe = this.getRecipeOutput();
                if (recipe != null) {
                    this.amplifierSlot.consume();
                    this.scrap += amountScrap;
                    if (this.amplifierSlot.isEmpty()) {
                        this.getOutput();
                    }
                }
            }


            if (this.energy.canUseEnergy(energycost)) {
                attemptGeneration();
            }
            this.lastEnergy = this.energy.getEnergy();


            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                    && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                this.containerslot.transferFromTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }

            if (this.upgradeSlot.tickNoMark()) {
                setUpgradestat();
            }


        }

    }

    private void getOutput() {
        this.recipe = this.amplifierSlot.process();
        this.setRecipeOutput(this.recipe);
    }


    public void onUnloaded() {

        super.onUnloaded();
        if (!this.getWorld().isClientSide) {
            Map<ChunkPos, List<IMatter>> chunkPosListMap = BlockEntityMultiMatter.worldMatterMap.get(this.getWorld().dimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                chunkPosListMap.put(chunkPos, matters);
                BlockEntityMultiMatter.worldMatterMap.put(this.getWorld().dimension(), chunkPosListMap);
            } else {
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = chunkPosListMap.get(chunkPos);
                if (matters == null) {
                    matters = new LinkedList<>();
                    chunkPosListMap.put(chunkPos, matters);
                } else {
                    matters.remove(this);
                }
            }
        }
    }

    public void attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fill(new FluidStack(FluidName.fluiduu_matter.getInstance().get(), Math.min(m, k)), IFluidHandler.FluidAction.EXECUTE);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }


    public ContainerMenuCombinerMatter getGuiContainer(Player entityPlayer) {
        return new ContainerMenuCombinerMatter(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenCombinerMatter((ContainerMenuCombinerMatter) isAdmin);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

    public float getWrenchDropRate() {
        return 0.7F;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!getWorld().isClientSide) {
            setUpgradestat();
            this.inputSlot.update();
            this.amplifierSlot.load();
            getOutput();
            Map<ChunkPos, List<IMatter>> chunkPosListMap = BlockEntityMultiMatter.worldMatterMap.get(this.getWorld().dimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                matters.add(this);
                chunkPosListMap.put(chunkPos, matters);
                BlockEntityMultiMatter.worldMatterMap.put(this.getWorld().dimension(), chunkPosListMap);
            } else {
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = chunkPosListMap.get(chunkPos);
                if (matters == null) {
                    matters = new LinkedList<>();
                    matters.add(this);
                    chunkPosListMap.put(chunkPos, matters);
                } else {
                    matters.add(this);
                }
            }
        }
    }


    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput, EnumBlockEntityUpgrade.FluidExtract
        );
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.recipe;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.recipe = output;
        if (this.recipe == null) {
            this.amountScrap = 0;
        } else {
            this.amountScrap = recipe.getRecipe().getOutput().metadata.getInt("amount");
        }
    }

    @Override
    public FluidTank getMatterTank() {
        return this.fluidTank;
    }

}
