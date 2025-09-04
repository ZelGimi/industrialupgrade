package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuStamp;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenStamp;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityStampMechanism extends BlockEntityElectricMachine implements
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

    public BlockEntityStampMechanism(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3Entity.stamp_mechanism, pos, state);
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
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemCraftingElements<?>)) {
                    return false;
                }
                int damage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                return damage == 369 || damage == 370 || damage == 412 || damage == 413 || damage == 438;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                ((BlockEntityStampMechanism) this.base).inputSlotA.setRecipe("empty");
                if (content.isEmpty()) {
                    ((BlockEntityStampMechanism) this.base).getOutput();
                    return content;
                }
                int damage = ((ItemCraftingElements<?>) content.getItem()).getElement().getId();
                if (damage == 369) {
                    ((BlockEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_coolant");
                }
                if (damage == 370) {
                    ((BlockEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_plate");
                }
                if (damage == 412) {
                    ((BlockEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_exchanger");
                }
                if (damage == 413) {
                    ((BlockEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_vent");
                }
                if (damage == 438) {
                    ((BlockEntityStampMechanism) this.base).inputSlotA.setRecipe("stamp_capacitor");
                }
                ((BlockEntityStampMechanism) this.base).getOutput();
                return content;
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
    public ContainerMenuStamp getGuiContainer(final Player var1) {
        return new ContainerMenuStamp(this, var1);
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
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenStamp((ContainerMenuStamp) menu);
    }

    @Override
    public void init() {
        addRecipe("stamp_plate", new ItemStack(IUItem.reactor_plate.getItem()), "forge:plates/Iron", "forge:ingots/Lead", "forge:ingots/Tin", "forge:gems/Quartz");
        addRecipe("stamp_plate", new ItemStack(IUItem.adv_reactor_plate.getItem()), "forge:plates/Bronze", new ItemStack(IUItem.reactor_plate.getItem()),
                "forge:storage_blocks/Redstone",
                "forge:ingots/Spinel"
        );
        addRecipe("stamp_plate", new ItemStack(IUItem.imp_reactor_plate.getItem()), ModUtils.setSize(IUItem.advancedAlloy, 4),
                "forge:ingots/Aluminium",
                "forge:ingots/Vanadoalumite", new ItemStack(IUItem.adv_reactor_plate.getItem())
        );
        addRecipe("stamp_plate", new ItemStack(IUItem.per_reactor_plate.getItem()), "forge:plateDense/Steel",
                new ItemStack(IUItem.compresscarbon.getItem(), 2),
                new ItemStack(IUItem.plast.getItem()), new ItemStack(IUItem.imp_reactor_plate.getItem())
        );

        addRecipe("stamp_vent", new ItemStack(IUItem.vent.getItem()), "forge:gears/Chromium", "forge:storage_blocks/Copper", new ItemStack(Items.REDSTONE, 4),
                "forge:ingots/Iron"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.adv_Vent.getItem()), new ItemStack(IUItem.vent.getItem()), "forge:plates/Gold", "forge:ingots/Electrum",
                "forge:ingots/Bronze"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.imp_Vent.getItem()), new ItemStack(IUItem.adv_Vent.getItem()), "forge:gems/Diamond", "forge:plates/Platinum",
                "forge:rods/Iridium"
        );
        addRecipe(
                "stamp_vent",
                new ItemStack(IUItem.per_Vent.getItem()),
                new ItemStack(IUItem.imp_Vent.getItem()),
                new ItemStack(IUItem.iudust.getStack(24), 8),
                "forge:storage_blocks/Redbrass",
                "forge:plates/Germanium"
        );


        addRecipe("stamp_vent", new ItemStack(IUItem.componentVent.getItem()), new ItemStack(IUItem.vent.getItem()),
                new ItemStack(IUItem.plastic_plate.getItem(), 4),
                "forge:plates/Iron",
                "forge:ingots/Tungsten"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.adv_componentVent.getItem()), new ItemStack(IUItem.componentVent.getItem()), "forge:plates/Aluminium",
                "forge:ingots/Iron",
                new ItemStack(IUItem.iudust.getStack(29), 8)
        );
        addRecipe(
                "stamp_vent",
                new ItemStack(IUItem.imp_componentVent.getItem()),
                new ItemStack(IUItem.adv_componentVent.getItem()),
                "forge:plates/Aluminumbronze",
                "forge:ingots/Duralumin",
                "forge:storage_blocks/Redstone"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.per_componentVent.getItem()), new ItemStack(IUItem.imp_componentVent.getItem()),
                "forge:storage_blocks/Diamond",
                new ItemStack(IUItem.crafting_elements.getStack(319), 4),
                "forge:plates/Iridium"
        );


        addRecipe("stamp_exchanger", new ItemStack(IUItem.heat_exchange.getItem()), new ItemStack(IUItem.crafting_elements.getStack(122), 2),
                new ItemStack(IUItem.crafting_elements.getStack(280), 4),
                new ItemStack(IUItem.crafting_elements.getStack(277), 1),
                "forge:ingots/Zinc"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.adv_heat_exchange.getItem()), new ItemStack(IUItem.heat_exchange.getItem()),
                new ItemStack(IUItem.plastic_plate.getItem(), 4),
                "forge:storage_blocks/Redstone",
                "forge:ingots/Redbrass"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.imp_heat_exchange.getItem()), new ItemStack(IUItem.adv_heat_exchange.getItem()),
                new ItemStack(IUItem.crafting_elements.getStack(320), 1),
                "forge:ingots/Titanium",
                "forge:plates/Germanium"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.per_heat_exchange.getItem()), new ItemStack(IUItem.imp_heat_exchange.getItem()),
                IUItem.compresscarbon.getItem(),
                IUItem.compressAlloy.getItem(),
                new ItemStack(IUItem.iudust.getStack(24), 16)
        );


        addRecipe("stamp_capacitor", new ItemStack(IUItem.capacitor.getItem()), new ItemStack(IUItem.reactor_plate.getItem()),
                "forge:storage_blocks/Bronze",
                "forge:storage_blocks/Redstone",
                IUItem.carbonFiber
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.adv_capacitor.getItem()), new ItemStack(IUItem.capacitor.getItem()),
                new ItemStack(IUItem.plastic_plate.getItem(), 4),
                "forge:storage_blocks/Iron",
                "forge:storage_blocks/Coal"
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.imp_capacitor.getItem()), new ItemStack(IUItem.adv_capacitor.getItem()),
                new ItemStack(IUItem.crafting_elements.getStack(320), 4),
                "forge:plates/Invar",
                new ItemStack(IUItem.crafting_elements.getStack(282), 4)
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.per_capacitor.getItem()), new ItemStack(IUItem.imp_capacitor.getItem()),
                "forge:storage_blocks/Diamond",
                new ItemStack(IUItem.crafting_elements.getStack(285), 1),
                "forge:plates/Vitalium"
        );

        addRecipe("stamp_coolant", new ItemStack(IUItem.coolant.getItem()), ModUtils.getCellFromFluid(FluidName.fluidhydrogen.getInstance().get()),
                "forge:storage_blocks/Iron",
                "forge:ingots/Lithium",
                "forge:ingots/Tin"
        );
        addRecipe("stamp_coolant", new ItemStack(IUItem.adv_coolant.getItem()), new ItemStack(IUItem.coolant.getItem()),
                ModUtils.getCellFromFluid(FluidName.fluidnitrogen.getInstance().get()), "forge:storage_blocks/Steel",
                "forge:plates/Obsidian"
        );
        addRecipe("stamp_coolant", new ItemStack(IUItem.imp_coolant.getItem()), new ItemStack(IUItem.adv_coolant.getItem()),
                ModUtils.getCellFromFluid(FluidName.fluidhelium.getInstance().get()), "forge:plates/Tungsten",
                "forge:doubleplate/Duralumin"
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.stamp_mechanism;
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
        if (!level.isClientSide) {
            this.inputSlotA.setRecipe("empty");
            if (!this.inputSlotB.get(0).isEmpty()) {
                int damage = ((ItemCraftingElements<?>) this.inputSlotB.get(0).getItem()).getElement().getId();
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
