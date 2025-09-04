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
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuRodFactory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRodFactory;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityRodFactory extends BlockEntityElectricMachine implements IUpgradableBlock, IUpdateTick,
        IHasRecipe, IUpdatableTileEvent {

    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final InventoryRecipes inputSlotA;
    public final ComponentProcess componentProcess;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public int type;
    public MachineRecipe output;

    public BlockEntityRodFactory(BlockPos pos, BlockState state) {
        super(2000, 1, 1, BlockBaseMachine3Entity.reactor_rod_factory, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 400
        ));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        this.inputSlotA = new InventoryRecipes(this, "battery_factory", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 400, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.type = 0;
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
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.reactor_rod_factory;
    }

    @Override
    public void init() {
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(5), 1), 1, IUItem.reactormendeleviumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(6), 1), 1, IUItem.reactorberkeliumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(7), 1), 1, IUItem.reactoreinsteiniumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(8), 1), 1, IUItem.reactoruran233Simple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(5), 1), 1, IUItem.reactormendeleviumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(0), 1), 1, IUItem.reactoramericiumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(4), 1), 1, IUItem.reactortoriySimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(9), 1), 1, IUItem.reactorlawrenciumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(10), 1), 1, IUItem.reactornobeliumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(11), 1), 1, IUItem.reactorfermiumSimple.getItemStack());

        addSimpleRecipe(new ItemStack(IUItem.proton.getItem(), 1), 0, IUItem.reactorprotonSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(3), 1), 0, IUItem.reactorcaliforniaSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(2), 1), 0, IUItem.reactorcuriumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.radiationresources.getStack(1), 1), 0, IUItem.reactorneptuniumSimple.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.UranFuel), 0, IUItem.uranium_fuel_rod.getItemStack());
        addSimpleRecipe(new ItemStack(IUItem.mox), 0, IUItem.mox_fuel_rod.getItemStack());

        addDualRecipe(IUItem.reactormendeleviumSimple.getItemStack(), 1, IUItem.reactormendeleviumDual.getItemStack());
        addDualRecipe(IUItem.reactorberkeliumSimple.getItemStack(), 1, IUItem.reactorberkeliumDual.getItemStack());
        addDualRecipe(IUItem.reactoreinsteiniumSimple.getItemStack(), 1, IUItem.reactoreinsteiniumDual.getItemStack());
        addDualRecipe(IUItem.reactoruran233Simple.getItemStack(), 1, IUItem.reactoruran233Dual.getItemStack());
        addDualRecipe(IUItem.reactormendeleviumSimple.getItemStack(), 1, IUItem.reactormendeleviumDual.getItemStack());
        addDualRecipe(IUItem.reactorlawrenciumSimple.getItemStack(), 1, IUItem.reactorlawrenciumDual.getItemStack());
        addDualRecipe(IUItem.reactornobeliumSimple.getItemStack(), 1, IUItem.reactornobeliumDual.getItemStack());
        addDualRecipe(IUItem.reactorfermiumSimple.getItemStack(), 1, IUItem.reactorfermiumDual.getItemStack());


        addDualRecipe(IUItem.reactorprotonSimple.getItemStack(), 0, IUItem.reactorprotonDual.getItemStack());
        addDualRecipe(IUItem.reactorcaliforniaSimple.getItemStack(), 0, IUItem.reactorcaliforniaDual.getItemStack());
        addDualRecipe(IUItem.reactorcuriumSimple.getItemStack(), 0, IUItem.reactorcuriumDual.getItemStack());
        addDualRecipe(IUItem.reactorneptuniumSimple.getItemStack(), 0, IUItem.reactorneptuniumDual.getItemStack());
        addDualRecipe(IUItem.uranium_fuel_rod.getItemStack(), 0, IUItem.dual_uranium_fuel_rod.getItemStack());
        addDualRecipe(IUItem.mox_fuel_rod.getItemStack(), 0, IUItem.dual_mox_fuel_rod.getItemStack());
        addDualRecipe(IUItem.reactoramericiumSimple.getItemStack(), 0, IUItem.reactoramericiumDual.getItemStack());
        addDualRecipe(IUItem.reactortoriySimple.getItemStack(), 0, IUItem.reactortoriyDual.getItemStack());

        addQuadRecipe(IUItem.reactormendeleviumSimple.getItemStack(), 1, IUItem.reactormendeleviumQuad.getItemStack());
        addQuadRecipe(IUItem.reactorberkeliumSimple.getItemStack(), 1, IUItem.reactorberkeliumQuad.getItemStack());
        addQuadRecipe(IUItem.reactoreinsteiniumSimple.getItemStack(), 1, IUItem.reactoreinsteiniumQuad.getItemStack());
        addQuadRecipe(IUItem.reactoruran233Simple.getItemStack(), 1, IUItem.reactoruran233Quad.getItemStack());
        addQuadRecipe(IUItem.reactormendeleviumSimple.getItemStack(), 1, IUItem.reactormendeleviumQuad.getItemStack());
        addQuadRecipe(IUItem.reactorlawrenciumSimple.getItemStack(), 1, IUItem.reactorlawrenciumQuad.getItemStack());
        addQuadRecipe(IUItem.reactornobeliumDual.getItemStack(), 1, IUItem.reactornobeliumQuad.getItemStack());
        addQuadRecipe(IUItem.reactorfermiumDual.getItemStack(), 1, IUItem.reactorfermiumQuad.getItemStack());


        addQuadRecipe(IUItem.reactorprotonSimple.getItemStack(), 0, IUItem.reactorprotonQuad.getItemStack());
        addQuadRecipe(IUItem.reactorcaliforniaSimple.getItemStack(), 0, IUItem.reactorcaliforniaQuad.getItemStack());
        addQuadRecipe(IUItem.reactorcuriumSimple.getItemStack(), 0, IUItem.reactorcuriumQuad.getItemStack());
        addQuadRecipe(IUItem.reactorneptuniumSimple.getItemStack(), 0, IUItem.reactorneptuniumQuad.getItemStack());
        addQuadRecipe(IUItem.uranium_fuel_rod.getItemStack(), 0, IUItem.quad_uranium_fuel_rod.getItemStack());
        addQuadRecipe(IUItem.mox_fuel_rod.getItemStack(), 0, IUItem.quad_mox_fuel_rod.getItemStack());
        addQuadRecipe(IUItem.reactoramericiumSimple.getItemStack(), 0, IUItem.reactoramericiumQuad.getItemStack());
        addQuadRecipe(IUItem.reactortoriySimple.getItemStack(), 0, IUItem.reactortoriyQuad.getItemStack());
    }

    public void addQuadRecipe(ItemStack stack, int i, ItemStack output) {
        ItemStack topRod;
        ItemStack leftRod;
        ItemStack rightRod;
        final IInputHandler input = Recipes.inputFactory;
        if (i == 0) {
            topRod = new ItemStack(IUItem.crafting_elements.getStack(181), 1);
            leftRod = new ItemStack(IUItem.crafting_elements.getStack(183), 1);
            rightRod = new ItemStack(IUItem.crafting_elements.getStack(182), 1);
        } else {
            topRod = new ItemStack(IUItem.crafting_elements.getStack(191), 1);
            leftRod = new ItemStack(IUItem.crafting_elements.getStack(190), 1);
            rightRod = new ItemStack(IUItem.crafting_elements.getStack(189), 1);
        }
        Recipes.recipes.addRecipe(
                "reactor_quad_rod",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(stack), input.getInput(leftRod), input.getInput(stack), input.getInput(rightRod),
                                input.getInput(stack), input.getInput(topRod), input.getInput(stack)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public void addDualRecipe(ItemStack stack, int i, ItemStack output) {
        ItemStack horizontalRod;
        final IInputHandler input = Recipes.inputFactory;
        if (i == 0) {
            horizontalRod = new ItemStack(IUItem.crafting_elements.getStack(180), 1);
        } else {
            horizontalRod = new ItemStack(IUItem.crafting_elements.getStack(188), 1);
        }
        Recipes.recipes.addRecipe(
                "reactor_dual_rod",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(stack), input.getInput(horizontalRod), input.getInput(stack)),
                        new RecipeOutput(null, output)
                )
        );
    }

    public void addSimpleRecipe(ItemStack stack, int i, ItemStack output) {
        ItemStack topRod;
        ItemStack bottomRod;
        ItemStack leftRod;
        ItemStack rightRod;
        final IInputHandler input = Recipes.inputFactory;
        if (i == 0) {
            topRod = new ItemStack(IUItem.crafting_elements.getStack(185), 1);
            bottomRod = new ItemStack(IUItem.crafting_elements.getStack(184), 1);
            leftRod = new ItemStack(IUItem.crafting_elements.getStack(186), 1);
            rightRod = new ItemStack(IUItem.crafting_elements.getStack(187), 1);
        } else {
            topRod = new ItemStack(IUItem.crafting_elements.getStack(194), 1);
            bottomRod = new ItemStack(IUItem.crafting_elements.getStack(195), 1);
            leftRod = new ItemStack(IUItem.crafting_elements.getStack(193), 1);
            rightRod = new ItemStack(IUItem.crafting_elements.getStack(192), 1);
        }
        Recipes.recipes.addRecipe(
                "reactor_simple_rod",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(topRod), input.getInput(leftRod), input.getInput(stack), input.getInput(rightRod),
                                input.getInput(bottomRod)
                        ),
                        new RecipeOutput(null, output)
                )
        );
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

            switch (type) {
                case 1:
                    this.inputSlotA.setRecipe("reactor_dual_rod");
                    break;
                case 2:
                    this.inputSlotA.setRecipe("reactor_quad_rod");
                    break;
                default:
                    this.inputSlotA.setRecipe("reactor_simple_rod");
                    break;
            }
            inputSlotA.load();
            this.getOutput();


        }


    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        final CompoundTag nbt = super.writeToNBT(nbttagcompound);
        nbt.putInt("type", this.type);
        return nbt;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.type = nbttagcompound.getInt("type");
    }

    public void updateTileServer(Player var1, double var2) {
        if (var2 == 0) {
            this.type--;
            if (type < 0) {
                type = 2;
            }
        } else {
            this.type++;
            if (type > 2) {
                type = 0;
            }
        }
        switch (type) {
            case 1:
                this.inputSlotA.setRecipe("reactor_dual_rod");
                break;
            case 2:
                this.inputSlotA.setRecipe("reactor_quad_rod");
                break;
            default:
                this.inputSlotA.setRecipe("reactor_simple_rod");
                break;
        }
        inputSlotA.load();
        this.getOutput();
        onActivated(var1, var1.getUsedItemHand(), Direction.SOUTH, new Vec3(0, 0, 0));
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        this.type = customPacketBuffer.readInt();
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        customPacketBuffer.writeInt(this.type);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.type = customPacketBuffer.readInt();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.type);
        return customPacketBuffer;
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenRodFactory((ContainerMenuRodFactory) menu);
    }

    @Override
    public ContainerMenuRodFactory getGuiContainer(final Player var1) {
        return new ContainerMenuRodFactory(this, var1);
    }

}
