package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerStamp;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiStamp;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityStampMechanism extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    public final InvSlot inputSlotB;
    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;
    public MachineRecipe output;

    public TileEntityStampMechanism(BlockPos pos, BlockState state) {
        super(200, 1, 1, BlockBaseMachine3.stamp_mechanism, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));
        this.inputSlotA = new InvSlotRecipes(this, "stamp_vent", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.inputSlotB = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemCraftingElements<?>)) {
                    return false;
                }
                int damage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                return damage == 369 || damage == 370 || damage == 412 || damage == 413 || damage == 438;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                ((TileEntityStampMechanism) this.base).inputSlotA.setRecipe("empty");
                if (content.isEmpty()) {
                    ((TileEntityStampMechanism) this.base).getOutput();
                    return content;
                }
                int damage = ((ItemCraftingElements<?>) content.getItem()).getElement().getId();
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
    public ContainerStamp getGuiContainer(final Player var1) {
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
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiStamp((ContainerStamp) menu);
    }

    @Override
    public void init() {
        addRecipe("stamp_plate", new ItemStack(IUItem.reactor_plate.getItem()), "c:plates/Iron", "c:ingots/Lead", "c:ingots/Tin", "c:gems/Quartz");
        addRecipe("stamp_plate", new ItemStack(IUItem.adv_reactor_plate.getItem()), "c:plates/Bronze", new ItemStack(IUItem.reactor_plate.getItem()),
                "c:storage_blocks/Redstone",
                "c:ingots/Spinel"
        );
        addRecipe("stamp_plate", new ItemStack(IUItem.imp_reactor_plate.getItem()), ModUtils.setSize(IUItem.advancedAlloy, 4),
                "c:ingots/Aluminium",
                "c:ingots/Vanadoalumite", new ItemStack(IUItem.adv_reactor_plate.getItem())
        );
        addRecipe("stamp_plate", new ItemStack(IUItem.per_reactor_plate.getItem()), "c:plateDense/Steel",
                new ItemStack(IUItem.compresscarbon.getItem(), 2),
                new ItemStack(IUItem.plast.getItem()), new ItemStack(IUItem.imp_reactor_plate.getItem())
        );

        addRecipe("stamp_vent", new ItemStack(IUItem.vent.getItem()), "c:gears/Chromium", "c:storage_blocks/Copper", new ItemStack(Items.REDSTONE, 4),
                "c:ingots/Iron"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.adv_Vent.getItem()), new ItemStack(IUItem.vent.getItem()), "c:plates/Gold", "c:ingots/Electrum",
                "c:ingots/Bronze"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.imp_Vent.getItem()), new ItemStack(IUItem.adv_Vent.getItem()), "c:gems/Diamond", "c:plates/Platinum",
                "c:rods/Iridium"
        );
        addRecipe(
                "stamp_vent",
                new ItemStack(IUItem.per_Vent.getItem()),
                new ItemStack(IUItem.imp_Vent.getItem()),
                new ItemStack(IUItem.iudust.getStack(24), 8),
                "c:storage_blocks/Redbrass",
                "c:plates/Germanium"
        );


        addRecipe("stamp_vent", new ItemStack(IUItem.componentVent.getItem()), new ItemStack(IUItem.vent.getItem()),
                new ItemStack(IUItem.plastic_plate.getItem(), 4),
                "c:plates/Iron",
                "c:ingots/Tungsten"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.adv_componentVent.getItem()), new ItemStack(IUItem.componentVent.getItem()), "c:plates/Aluminium",
                "c:ingots/Iron",
                new ItemStack(IUItem.iudust.getStack(29), 8)
        );
        addRecipe(
                "stamp_vent",
                new ItemStack(IUItem.imp_componentVent.getItem()),
                new ItemStack(IUItem.adv_componentVent.getItem()),
                "c:plates/Aluminumbronze",
                "c:ingots/Duralumin",
                "c:storage_blocks/Redstone"
        );
        addRecipe("stamp_vent", new ItemStack(IUItem.per_componentVent.getItem()), new ItemStack(IUItem.imp_componentVent.getItem()),
                "c:storage_blocks/Diamond",
                new ItemStack(IUItem.crafting_elements.getStack(319), 4),
                "c:plates/Iridium"
        );


        addRecipe("stamp_exchanger", new ItemStack(IUItem.heat_exchange.getItem()), new ItemStack(IUItem.crafting_elements.getStack(122), 2),
                new ItemStack(IUItem.crafting_elements.getStack(280), 4),
                new ItemStack(IUItem.crafting_elements.getStack(277), 1),
                "c:ingots/Zinc"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.adv_heat_exchange.getItem()), new ItemStack(IUItem.heat_exchange.getItem()),
                new ItemStack(IUItem.plastic_plate.getItem(), 4),
                "c:storage_blocks/Redstone",
                "c:ingots/Redbrass"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.imp_heat_exchange.getItem()), new ItemStack(IUItem.adv_heat_exchange.getItem()),
                new ItemStack(IUItem.crafting_elements.getStack(320), 4),
                "c:ingots/Titanium",
                "c:plates/Germanium"
        );
        addRecipe("stamp_exchanger", new ItemStack(IUItem.per_heat_exchange.getItem()), new ItemStack(IUItem.imp_heat_exchange.getItem()),
                IUItem.compresscarbon.getItem(),
                IUItem.compressAlloy.getItem(),
                new ItemStack(IUItem.iudust.getStack(24), 16)
        );


        addRecipe("stamp_capacitor", new ItemStack(IUItem.capacitor.getItem()), new ItemStack(IUItem.reactor_plate.getItem()),
                "c:storage_blocks/Bronze",
                "c:storage_blocks/Redstone",
                IUItem.carbonFiber
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.adv_capacitor.getItem()), new ItemStack(IUItem.capacitor.getItem()),
                new ItemStack(IUItem.plastic_plate.getItem(), 4),
                "c:storage_blocks/Iron",
                "c:storage_blocks/Coal"
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.imp_capacitor.getItem()), new ItemStack(IUItem.adv_capacitor.getItem()),
                new ItemStack(IUItem.crafting_elements.getStack(320), 4),
                "c:plates/Invar",
                new ItemStack(IUItem.crafting_elements.getStack(282), 4)
        );
        addRecipe("stamp_capacitor", new ItemStack(IUItem.per_capacitor.getItem()), new ItemStack(IUItem.imp_capacitor.getItem()),
                "c:storage_blocks/Diamond",
                new ItemStack(IUItem.crafting_elements.getStack(285), 1),
                "c:plates/Vitalium"
        );

        addRecipe("stamp_coolant", new ItemStack(IUItem.coolant.getItem()), ModUtils.getCellFromFluid(FluidName.fluidhyd.getInstance().get()),
                "c:storage_blocks/Iron",
                "c:ingots/Lithium",
                "c:ingots/Tin"
        );
        addRecipe("stamp_coolant", new ItemStack(IUItem.adv_coolant.getItem()), new ItemStack(IUItem.coolant.getItem()),
                ModUtils.getCellFromFluid(FluidName.fluidazot.getInstance().get()), "c:storage_blocks/Steel",
                "c:plates/Obsidian"
        );
        addRecipe("stamp_coolant", new ItemStack(IUItem.imp_coolant.getItem()), new ItemStack(IUItem.adv_coolant.getItem()),
                ModUtils.getCellFromFluid(FluidName.fluidHelium.getInstance().get()), "c:plates/Tungsten",
                "c:doubleplate/Duralumin"
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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
