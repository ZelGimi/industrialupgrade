package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerHandlerHeavyOre;
import com.denfop.gui.GuiHandlerHeavyOre;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class TileEntityBaseHandlerHeavyOre extends TileEntityElectricMachine
        implements IUpgradableBlock, IUpdateTick, IType {

    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final HeatComponent heat;
    private final EnumTypeStyle enumTypeSlot;
    private final double coef;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    protected short progress;
    protected double guiProgress;
    private boolean auto;
    private int[] col;

    public TileEntityBaseHandlerHeavyOre(EnumTypeStyle enumTypeSlot) {
        this(1, 300, 3, 1, enumTypeSlot);
    }

    public TileEntityBaseHandlerHeavyOre(
            int energyPerTick,
            int length,
            int outputSlots,
            int aDefaultTier,
            EnumTypeStyle enumTypeSlot
    ) {
        super(energyPerTick * length, 1, 1);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.enumTypeSlot = enumTypeSlot;
        this.defaultOperationLength = this.operationLength = (int) (length / getSpeed());
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.outputSlot = new InvSlotOutput(this, "output", outputSlots + 2 * enumTypeSlot.ordinal());
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.inputSlotA = new InvSlotRecipes(this, "handlerho", this);
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        this.col = new int[0];

        this.coef = getCoef();
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public EnumTypeStyle getEnumTypeSlot() {
        return enumTypeSlot;
    }

    protected double getCoef() {
        switch (this.enumTypeSlot) {
            case ADVANCED:
                return 1.1;
            case PERFECT:
                return 1.3;
            case IMPROVED:
                return 1.2;
            default:
                return 1;
        }
    }

    @Override
    public EnumTypeStyle getStyle() {
        return this.enumTypeSlot;
    }

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.auto) {
            ret.add(new ItemStack(IUItem.autoheater));
            this.auto = false;
        }
        return ret;
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        final ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem().equals(IUItem.autoheater) && !this.auto) {
            this.auto = true;
            stack.shrink(1);
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("auto", this.auto);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.auto = nbttagcompound.getBoolean("auto");
        this.progress = nbttagcompound.getShort("progress");
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.inputSlotA.isEmpty() && output != null && this.outputSlot.canAdd(this.output.getRecipe().output.items) && this.energy.getEnergy() >= this.energyConsume && output.getRecipe().output.metadata != null) {

            if (output.getRecipe().output.metadata.getShort("temperature") == 0 || output.getRecipe().output.metadata.getInteger(
                    "temperature") > this.heat.getEnergy()) {
                if (!(this).heat.need) {
                    (this).heat.need = true;
                }
                return;

            } else if ((this).heat.need) {
                (this).heat.need = false;
            }
            (this).heat.storage--;

            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                this.initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                this.progress = 0;
                this.initiate(2);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                this.initiate(1);
            }
            if (output == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
            if (this.heat.getEnergy() > 0) {
                this.heat.useEnergy(1);
            }

        }


        if (this.auto) {
            if (this.heat.getEnergy() + 1 <= this.heat.getCapacity()) {
                this.heat.addEnergy(2);
            }
        }
        if ((!this.inputSlotA.isEmpty() || !this.outputSlot.isEmpty()) && this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.heatmachine.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);

        }
        super.addInformation(stack, tooltip, advanced);
    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if (!this.inputSlotA.continue_process(this.output) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }
            if (this.output == null) {
                break;
            }
        }
    }

    public String getStartSoundFile() {
        return "Machines/handlerho.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }
        inputSlotA.load();
        this.getOutput();
        if (this.output == null) {
            (this).heat.need = false;
        }
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }


    @Override
    public ContainerBase<? extends TileEntityBaseHandlerHeavyOre> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerHandlerHeavyOre(entityPlayer, this);

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiHandlerHeavyOre(new ContainerHandlerHeavyOre(entityPlayer, this));
    }

    public void operateOnce(List<ItemStack> processResult) {
        for (int i = 0; i < col.length; i++) {
            final Random rand = world.rand;
            if ((100 - col[i]) <= rand.nextInt(100)) {
                this.outputSlot.add(processResult.get(i));
            }
        }
        this.inputSlotA.consume();
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
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


    public double getProgress() {
        return this.guiProgress;
    }

    @Override
    public void onUpdate() {

    }

    private double getSpeed() {
        switch (this.enumTypeSlot) {
            case ADVANCED:
                return 1.3;
            case IMPROVED:
                return 1.5;
            case PERFECT:
                return 2;
            default:
                return 1;
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        if (output == null) {
            this.col = new int[0];
        } else {
            this.col = new int[output.getRecipe().output.items.size()];
            for (int i = 0; i < col.length; i++) {
                col[i] = (int) (output.getRecipe().output.metadata.getInteger(("input" + i)) * this.coef);
            }
        }
    }

}
