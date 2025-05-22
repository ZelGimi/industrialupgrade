package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.container.ContainerMultiMatter;
import com.denfop.gui.GuiMultiMatter;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
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
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class TileMultiMatter extends TileElectricMachine implements IUpgradableBlock,
        IUpdatableTileEvent, IType, IUpdateTick, IMatter {

    public static Map<Integer, Map<ChunkPos, List<IMatter>>> worldMatterMap = new HashMap<>();
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotRecipes amplifierSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotFluid containerslot;
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

    public TileMultiMatter(float storageEnergy, int sizeTank, float maxtempEnergy) {
        super(Math.round(maxtempEnergy * 1), 3, 1);
        this.amplifierSlot = new InvSlotRecipes(this, "matterAmplifier", this);

        this.energycost = storageEnergy * 1;
        this.outputSlot = new InvSlotOutput(this, 1);
        this.containerslot = new InvSlotFluidByList(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluiduu_matter.getInstance()
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", sizeTank * 1000,
                Fluids.fluidPredicate(FluidName.fluiduu_matter.getInstance()), InvSlot.TypeItemSlot.OUTPUT
        );
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
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
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.matter_work_info") + (int) this.energycost);
        }

        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
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
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
            return;
        }
        this.work = !this.work;
        this.energy.setReceivingEnabled(this.work);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.scrap = nbt.getInteger("scrap");
        this.lastEnergy = nbt.getDouble("lastEnergy");
        this.work = nbt.getBoolean("work");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("scrap", this.scrap);
        nbt.setDouble("lastEnergy", this.lastEnergy);
        nbt.setBoolean("work", this.work);
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
            this.amplifierSlot.load();

            getOutput();
            energy.setReceivingEnabled(work);
            Map<ChunkPos, List<IMatter>> chunkPosListMap = worldMatterMap.get(this.world.provider.getDimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                matters.add(this);
                chunkPosListMap.put(chunkPos, matters);
                worldMatterMap.put(this.world.provider.getDimension(), chunkPosListMap);
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

    public void onUnloaded() {


        super.onUnloaded();
        if (!this.getWorld().isRemote) {
            Map<ChunkPos, List<IMatter>> chunkPosListMap = worldMatterMap.get(this.world.provider.getDimension());
            if (chunkPosListMap == null) {
                chunkPosListMap = new HashMap<>();
                ChunkPos chunkPos = new ChunkPos(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                List<IMatter> matters = new LinkedList<>();
                chunkPosListMap.put(chunkPos, matters);
                worldMatterMap.put(this.world.provider.getDimension(), chunkPosListMap);
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
        this.fluidTank.fillInternal(new FluidStack(FluidName.fluiduu_matter.getInstance(), Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerMultiMatter getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerMultiMatter(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiMultiMatter(new ContainerMultiMatter(entityPlayer, this));
    }

    @Override
    public FluidTank getMatterTank() {
        return this.fluidTank;
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(

                UpgradableProperty.Transformer,
                UpgradableProperty.ItemInput,
                UpgradableProperty.FluidExtract
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

}
