package com.denfop.tiles.base;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.recipe.*;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerCombinerMatter;
import com.denfop.gui.GuiCombinerMatter;
import com.denfop.invslot.*;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileCombinerMatter extends TileElectricLiquidTankInventory implements IUpgradableBlock,
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

    public TileCombinerMatter() {
        super(0, 14, 12);
        this.energycost = 0;
        this.amplifierSlot = new InventoryRecipes(this, "matterAmplifier", this);
        this.outputSlot = new InventoryOutput(this, 1);

        fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.containerslot = new InventoryFluidByList(this, Inventory.TypeItemSlot.INPUT, 1,
                InventoryFluid.TypeFluidSlot.OUTPUT, FluidName.fluiduu_matter.getInstance()
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.combiner_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    public int getInventoryStackLimit() {
        return 4;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.scrap = nbttagcompound.getInteger("scrap");
        this.lastEnergy = nbttagcompound.getDouble("lastEnergy");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("scrap", this.scrap);
        nbttagcompound.setDouble("lastEnergy", this.lastEnergy);
        return nbttagcompound;

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidTank.getCapacity() <= 0) {
            this.fluidTank.drain(Integer.MAX_VALUE, true);
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
        if (!this.getWorld().isRemote) {
            Map<ChunkPos, List<IMatter>> chunkPosListMap = TileMultiMatter.worldMatterMap.get(this.world.provider.getDimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                chunkPosListMap.put(chunkPos, matters);
                TileMultiMatter.worldMatterMap.put(this.world.provider.getDimension(), chunkPosListMap);
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
        this.fluidTank.fillInternal(new FluidStack(FluidName.fluiduu_matter.getInstance(), Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }


    public ContainerCombinerMatter getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerCombinerMatter(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiCombinerMatter(new ContainerCombinerMatter(entityPlayer, this));
    }


    public void onGuiClosed(EntityPlayer entityPlayer) {
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
        if (IUCore.proxy.isSimulating()) {
            setUpgradestat();
            this.inputSlot.update();
            this.amplifierSlot.load();
            getOutput();
            Map<ChunkPos, List<IMatter>> chunkPosListMap = TileMultiMatter.worldMatterMap.get(this.world.provider.getDimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                matters.add(this);
                chunkPosListMap.put(chunkPos, matters);
                TileMultiMatter.worldMatterMap.put(this.world.provider.getDimension(), chunkPosListMap);
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

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer,
                UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput, UpgradableProperty.FluidExtract
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
            this.amountScrap = recipe.getRecipe().getOutput().metadata.getInteger("amount");
        }
    }

    @Override
    public FluidTank getMatterTank() {
        return this.fluidTank;
    }

}
