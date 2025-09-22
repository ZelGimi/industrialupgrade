package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
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
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerStamp;
import com.denfop.gui.GuiStamp;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityStampMechanism extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public final Inventory inputSlotB;
    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;
    public MachineRecipe output;

    public TileEntityStampMechanism() {
        super(200, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InventoryRecipes(this, "stamp_vent", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.inputSlotB = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                if (stack.getItem() != IUItem.crafting_elements) {
                    return false;
                }
                int damage = stack.getItemDamage();
                return damage == 369 || damage == 370 || damage == 412 || damage == 413 || damage == 438;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("empty");
                int damage = content.getItemDamage();
                if (damage == 369) {
                    ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_coolant");
                }
                if (damage == 370) {
                    ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_plate");
                }
                if (damage == 412) {
                    ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_exchanger");
                }
                if (damage == 413) {
                    ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_vent");
                }
                if (damage == 438) {
                    ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_capacitor");
                }
                ((TileEntityStampMechanism) this.base).getOutput();
            }
        };

        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }

    public static void addRecipe(String name, ItemStack output, Object... objects) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                name,
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(objects[0]), input.getInput(objects[1]), input.getInput(objects[2]),
                                input.getInput(objects[3])
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    @Override
    public ContainerStamp getGuiContainer(final EntityPlayer var1) {
        return new ContainerStamp(this, var1);
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiStamp(getGuiContainer(var1));
    }

    @Override
    public void init() {
        addRecipe("stamp_plate", new ItemStack(IUItem.reactor_plate), "plateIron", "ingotLead", "ingotTin", "gemQuartz");
        addRecipe("stamp_plate", new ItemStack(IUItem.adv_reactor_plate), "plateBronze", new ItemStack(IUItem.reactor_plate),
                "blockRedstone",
                "ingotSpinel"
        );
        addRecipe("stamp_plate", new ItemStack(IUItem.imp_reactor_plate), ModUtils.setSize(IUItem.advancedAlloy, 4),
                "ingotAluminium",
                "ingotVanadoalumite", new ItemStack(IUItem.adv_reactor_plate)
        );
        addRecipe("stamp_plate", new ItemStack(IUItem.per_reactor_plate), "plateDenseSteel",
                new ItemStack(IUItem.compresscarbon, 2),
                new ItemStack(IUItem.plast), new ItemStack(IUItem.imp_reactor_plate)
        );

        addRecipe("stamp_vent", new ItemStack(IUItem.vent), "gearChromium", "blockCopper", new ItemStack(Items.REDSTONE, 4),
                "ingotIron"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.adv_Vent), new ItemStack(IUItem.vent), "plateGold", "ingotElectrum",
                "ingotBronze"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.imp_Vent), new ItemStack(IUItem.adv_Vent), "gemDiamond", "platePlatinum",
                "stickIridium"
        );
        addRecipe(
                "stamp_vent",
                new ItemStack(IUItem.per_Vent),
                new ItemStack(IUItem.imp_Vent),
                new ItemStack(IUItem.iudust, 8, 24),
                "blockRedbrass",
                "plateGermanium"
        );


        addRecipe("stamp_vent", new ItemStack(IUItem.componentVent), new ItemStack(IUItem.vent),
                new ItemStack(IUItem.plastic_plate, 4),
                "plateIron",
                "ingotTungsten"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.adv_componentVent), new ItemStack(IUItem.componentVent), "plateAluminium",
                "ingotIron",
                new ItemStack(IUItem.iudust, 8, 29)
        );
        addRecipe(
                "stamp_vent",
                new ItemStack(IUItem.imp_componentVent),
                new ItemStack(IUItem.adv_componentVent),
                "plateAluminumbronze",
                "ingotDuralumin",
                "blockRedstone"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.per_componentVent), new ItemStack(IUItem.imp_componentVent),
                "blockDiamond",
                new ItemStack(IUItem.crafting_elements, 4, 319),
                "plateIridium"
        );


        addRecipe("stamp_exchanger", new ItemStack(IUItem.heat_exchange), new ItemStack(IUItem.crafting_elements, 2, 122),
                new ItemStack(IUItem.crafting_elements, 4, 280),
                new ItemStack(IUItem.crafting_elements, 1, 277),
                "ingotZinc"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.adv_heat_exchange), new ItemStack(IUItem.heat_exchange),
                new ItemStack(IUItem.plastic_plate, 4),
                "blockRedstone",
                "ingotRedbrass"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.imp_heat_exchange), new ItemStack(IUItem.adv_heat_exchange),
                new ItemStack(IUItem.crafting_elements, 4, 320),
                "ingotTitanium",
                "plateGermanium"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.per_heat_exchange), new ItemStack(IUItem.imp_heat_exchange),
                IUItem.compresscarbon,
                IUItem.compressAlloy,
                new ItemStack(IUItem.iudust, 16, 24)
        );


        addRecipe("stamp_capacitor", new ItemStack(IUItem.capacitor), new ItemStack(IUItem.reactor_plate),
                "blockBronze",
                "blockRedstone",
                "itemCarbonFibre"
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.adv_capacitor), new ItemStack(IUItem.capacitor),
                new ItemStack(IUItem.plastic_plate, 4),
                "blockIron",
                "blockCoal"
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.imp_capacitor), new ItemStack(IUItem.adv_capacitor),
                new ItemStack(IUItem.crafting_elements, 4, 320),
                "plateInvar",
                new ItemStack(IUItem.crafting_elements, 4, 282)
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.per_capacitor), new ItemStack(IUItem.imp_capacitor),
                "blockDiamond",
                new ItemStack(IUItem.crafting_elements, 1, 285),
                "plateVitalium"
        );

        addRecipe("stamp_coolant", new ItemStack(IUItem.coolant), ModUtils.getCellFromFluid(FluidName.fluidhyd.getInstance()),
                "blockIron",
                "ingotLithium",
                "ingotTin"
        );
        addRecipe("stamp_coolant", new ItemStack(IUItem.adv_coolant), new ItemStack(IUItem.coolant),
                ModUtils.getCellFromFluid(FluidName.fluidazot.getInstance()), "blockSteel",
                "plateObsidian"
        );
        addRecipe("stamp_coolant", new ItemStack(IUItem.imp_coolant), new ItemStack(IUItem.adv_coolant),
                ModUtils.getCellFromFluid(FluidName.fluidHelium.getInstance()), "plateTungsten",
                "doubleplateDuralumin"
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.stamp_mechanism;
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.inputSlotA.setRecipe("empty");
            int damage = this.inputSlotB.get().getItemDamage();
            if (damage == 369) {
                this.inputSlotA.setRecipe("stamp_coolant");
            }
            if (damage == 370) {
                this.inputSlotA.setRecipe("stamp_plate");
            }
            if (damage == 412) {
                this.inputSlotA.setRecipe("stamp_exchanger");
            }
            if (damage == 413) {
                this.inputSlotA.setRecipe("stamp_vent");
            }
            if (damage == 438) {
                this.inputSlotA.setRecipe("stamp_capacitor");
            }
            inputSlotA.load();
            this.getOutput();
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
