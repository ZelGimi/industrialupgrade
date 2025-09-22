package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerCentrifuge;
import com.denfop.gui.GuiCentrifuge;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCentrifuge extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public final Inventory input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;
    public short rpm = 0;

    public TileEntityCentrifuge() {
        super(200, 1, 1);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileEntityCentrifuge) this.parent).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InventoryRecipes(this, "genetic_centrifuge", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1) {
            @Override
            public boolean checkRecipe() {
                if (rpm < 120) {
                    rpm++;
                }
                return rpm == 120;
            }

            @Override
            public void onFailedProcess() {
                super.onFailedProcess();
                if (rpm > 0 && output == null) {
                    rpm--;
                }
            }
        });
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEntityCentrifuge) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEntityCentrifuge) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addRecipe(int container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "genetic_centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements, 4, container))),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addRecipe(ItemStack container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "genetic_centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addRecipe(String container, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "genetic_centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.rpm = customPacketBuffer.readShort();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeShort(rpm);
        return customPacketBuffer;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public ContainerCentrifuge getGuiContainer(final EntityPlayer var1) {
        return new ContainerCentrifuge(var1, this);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCentrifuge(getGuiContainer(var1));
    }

    @Override
    public void init() {

        addRecipe(new ItemStack(IUItem.honeycomb), new ItemStack(IUItem.honey_drop));
        addRecipe(new ItemStack(IUItem.beeswax), new ItemStack(IUItem.bee_pollen));

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.centrifuge;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.getOutput();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
        }


    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
