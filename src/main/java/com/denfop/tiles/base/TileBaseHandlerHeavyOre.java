package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerHandlerHeavyOre;
import com.denfop.gui.GuiHandlerHeavyOre;
import com.denfop.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class TileBaseHandlerHeavyOre extends TileElectricMachine
        implements IUpgradableBlock, IUpdateTick, IType {


    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final HeatComponent heat;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProcess componentProcess;
    public final ComponentProgress componentProgress;
    private final EnumTypeStyle enumTypeSlot;
    private final double coef;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    private boolean auto;
    private int[] col;

    public TileBaseHandlerHeavyOre(EnumTypeStyle enumTypeSlot) {
        this(1, 300, 3, 1, enumTypeSlot);
    }

    public TileBaseHandlerHeavyOre(
            int energyPerTick,
            int length,
            int outputSlots,
            int aDefaultTier,
            EnumTypeStyle enumTypeSlot
    ) {
        super(energyPerTick * length, 1, 1);
        this.enumTypeSlot = enumTypeSlot;
        this.outputSlot = new InvSlotOutput(this, "output", outputSlots + 2 * enumTypeSlot.ordinal());
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.inputSlotA = new InvSlotRecipes(this, "handlerho", this);
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        this.col = new int[0];
        this.coef = getCoef();
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileBaseHandlerHeavyOre) this.getParent()).componentProcess;

            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, (int) (length / this.getSpeed()), energyPerTick) {
            @Override
            public void operateOnce(final List<ItemStack> processResult) {
                for (int i = 0; i < col.length; i++) {
                    final Random rand = world.rand;
                    if ((100 - col[i]) <= rand.nextInt(100)) {
                        this.outputSlot.add(processResult.get(i));
                    }
                }
                this.invSlotRecipes.consume();
            }
        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
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


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.heatmachine.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getDefaultEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getDefaultOperationLength());

        }
        super.addInformation(stack, tooltip, advanced);
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

    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        this.getOutput();
    }

    public void onUnloaded() {
        super.onUnloaded();
    }


    @Override
    public ContainerHandlerHeavyOre getGuiContainer(EntityPlayer entityPlayer) {
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

    @Override
    public SoundEvent getSound() {
        return EnumSound.handlerho.getSoundEvent();
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
