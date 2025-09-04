package com.denfop.blockentity.base;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.widget.IType;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuMultiMatter;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMultiMatter;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.*;


public abstract class BlockEntityMultiMatter extends BlockEntityElectricMachine implements BlockEntityUpgrade,
        IUpdatableTileEvent, IType, IUpdateTick, IMatter {

    public static Map<ResourceKey<Level>, Map<ChunkPos, List<IMatter>>> worldMatterMap = new HashMap<>();
    public final InventoryUpgrade upgradeSlot;
    public final InventoryRecipes amplifierSlot;
    public final InventoryOutput outputSlot;
    public final InventoryFluid containerslot;
    public final FluidTank fluidTank;
    public final float energycost;
    protected final Fluids fluids;
    private final Redstone redstone;
    private final int state = 0;
    private final int prevState = 0;
    public boolean work;
    public int scrap = 0;
    public boolean redstonePowered = false;
    private int amountScrap;
    private double lastEnergy;
    private MachineRecipe recipe;

    public BlockEntityMultiMatter(float storageEnergy, int sizeTank, float maxtempEnergy, MultiBlockEntity multiTileBlock, BlockPos pos, BlockState blockState) {
        super(Math.round(maxtempEnergy * 1), 3, 1, multiTileBlock, pos, blockState);
        this.amplifierSlot = new InventoryRecipes(this, "matterAmplifier", this);

        this.energycost = storageEnergy * 1;
        this.outputSlot = new InventoryOutput(this, 1);
        this.containerslot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluiduu_matter.getInstance().get()
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", sizeTank * 1000,
                Fluids.fluidPredicate(FluidName.fluiduu_matter.getInstance().get()), Inventory.TypeItemSlot.OUTPUT
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
                                    @Override
                                    public void action(final int input) {
                                        energy.setEnabled(input == 0);
                                        work = input != 0;
                                        energy.setReceivingEnabled(work);
                                    }
                                }
        );
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 3 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(InputConstants.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(InputConstants.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.matter_work_info") + (int) this.energycost);
        }


        super.addInformation(stack, tooltip);

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            scrap = (int) DecoderHandler.decode(customPacketBuffer);
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, scrap);
            EncoderHandler.encode(packet, work);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
            return;
        }
        this.work = !this.work;
        this.energy.setReceivingEnabled(this.work);
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.scrap = nbt.getInt("scrap");
        this.lastEnergy = nbt.getDouble("lastEnergy");
        this.work = nbt.getBoolean("work");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("scrap", this.scrap);
        nbt.putDouble("lastEnergy", this.lastEnergy);
        nbt.putBoolean("work", this.work);
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getLevel().isClientSide) {
            this.setUpgradestat();
            this.amplifierSlot.load();

            getOutput();
            energy.setReceivingEnabled(work);
            Map<ChunkPos, List<IMatter>> chunkPosListMap = worldMatterMap.get(this.getWorld().dimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.getBlockPos().getX() >> 4, this.getBlockPos().getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                matters.add(this);
                chunkPosListMap.put(chunkPos, matters);
                worldMatterMap.put(this.getWorld().dimension(), chunkPosListMap);
            } else {
                ChunkPos chunkPos = new ChunkPos(this.getBlockPos().getX() >> 4, this.getBlockPos().getZ() >> 4);
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

    public void onUnloaded() {


        super.onUnloaded();
        if (!this.getWorld().isClientSide) {
            Map<ChunkPos, List<IMatter>> chunkPosListMap = worldMatterMap.get(this.getWorld().dimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.getBlockPos().getX() >> 4, this.getBlockPos().getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                chunkPosListMap.put(chunkPos, matters);
                worldMatterMap.put(this.getWorld().dimension(), chunkPosListMap);
            } else {
                ChunkPos chunkPos = new ChunkPos(this.getBlockPos().getX() >> 4, this.getBlockPos().getZ() >> 4);
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

    private void getOutput() {
        this.recipe = this.amplifierSlot.process();
        this.setRecipeOutput(recipe);
        this.onUpdate();
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.redstonePowered = false;
        if (this.work && !(this.energy.getEnergy() <= 0.0D)) {
            if (this.scrap > 0) {
                double bonus = Math.min(this.scrap, this.energy.getEnergy() - this.lastEnergy);
                if (bonus > 0.0D) {
                    this.energy.forceAddEnergy(5.0D * bonus);
                    this.scrap = (int) ((double) this.scrap - bonus);
                }

            } else {

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
            if (this.energy.getEnergy() >= this.energycost) {
                this.attemptGeneration();
            }
            if (!this.containerslot.isEmpty()) {
                this.containerslot.processFromTank(this.fluidTank, this.outputSlot);
            }

            this.lastEnergy = this.energy.getEnergy();

        } else {
            if (this.getActive()) {
                this.setActive(false);
            }
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
        this.fluidTank.fill(new FluidStack(FluidName.fluiduu_matter.getInstance().get(), Math.min(m, k)), IFluidHandler.FluidAction.EXECUTE);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerMenuMultiMatter getGuiContainer(Player entityPlayer) {
        return new ContainerMenuMultiMatter(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenMultiMatter((ContainerMenuMultiMatter) menu);
    }

    @Override
    public FluidTank getMatterTank() {
        return this.fluidTank;
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(

                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.ItemInput,
                EnumBlockEntityUpgrade.FluidExtract
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

}
