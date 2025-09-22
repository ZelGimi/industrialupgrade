package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerRodFactory;
import com.denfop.gui.GuiRodFactory;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityRodFactory extends TileElectricMachine implements IUpgradableBlock, IUpdateTick,
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

    public TileEntityRodFactory() {
        super(2000, 1, 1);
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
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.reactor_rod_factory;
    }

    @Override
    public void init() {
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 5), 1, IUItem.reactormendeleviumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 6), 1, IUItem.reactorberkeliumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 7), 1, IUItem.reactoreinsteiniumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 8), 1, IUItem.reactoruran233Simple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 5), 1, IUItem.reactormendeleviumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 0), 1, IUItem.reactoramericiumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 4), 1, IUItem.reactortoriySimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 9), 1, IUItem.reactorlawrenciumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 10), 1, IUItem.reactornobeliumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 11), 1, IUItem.reactorfermiumSimple);

        addSimpleRecipe(new ItemStack(IUItem.proton, 1), 0, IUItem.reactorprotonSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 3), 0, IUItem.reactorcaliforniaSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 2), 0, IUItem.reactorcuriumSimple);
        addSimpleRecipe(new ItemStack(IUItem.radiationresources, 1, 1), 0, IUItem.reactorneptuniumSimple);
        addSimpleRecipe(IUItem.UranFuel, 0, IUItem.uranium_fuel_rod);
        addSimpleRecipe(IUItem.mox, 0, IUItem.mox_fuel_rod);

        addDualRecipe(IUItem.reactormendeleviumSimple, 1, IUItem.reactormendeleviumDual);
        addDualRecipe(IUItem.reactorberkeliumSimple, 1, IUItem.reactorberkeliumDual);
        addDualRecipe(IUItem.reactoreinsteiniumSimple, 1, IUItem.reactoreinsteiniumDual);
        addDualRecipe(IUItem.reactoruran233Simple, 1, IUItem.reactoruran233Dual);
        addDualRecipe(IUItem.reactormendeleviumSimple, 1, IUItem.reactormendeleviumDual);
        addDualRecipe(IUItem.reactorlawrenciumSimple, 1, IUItem.reactorlawrenciumDual);
        addDualRecipe(IUItem.reactornobeliumSimple, 1, IUItem.reactornobeliumDual);
        addDualRecipe(IUItem.reactorfermiumSimple, 1, IUItem.reactorfermiumDual);


        addDualRecipe(IUItem.reactorprotonSimple, 0, IUItem.reactorprotonDual);
        addDualRecipe(IUItem.reactorcaliforniaSimple, 0, IUItem.reactorcaliforniaDual);
        addDualRecipe(IUItem.reactorcuriumSimple, 0, IUItem.reactorcuriumDual);
        addDualRecipe(IUItem.reactorneptuniumSimple, 0, IUItem.reactorneptuniumDual);
        addDualRecipe(IUItem.uranium_fuel_rod, 0, IUItem.dual_uranium_fuel_rod);
        addDualRecipe(IUItem.mox_fuel_rod, 0, IUItem.dual_mox_fuel_rod);
        addDualRecipe(IUItem.reactoramericiumSimple, 0, IUItem.reactoramericiumDual);
        addDualRecipe(IUItem.reactortoriySimple, 0, IUItem.reactortoriyDual);

        addQuadRecipe(IUItem.reactormendeleviumSimple, 1, IUItem.reactormendeleviumQuad);
        addQuadRecipe(IUItem.reactorberkeliumSimple, 1, IUItem.reactorberkeliumQuad);
        addQuadRecipe(IUItem.reactoreinsteiniumSimple, 1, IUItem.reactoreinsteiniumQuad);
        addQuadRecipe(IUItem.reactoruran233Simple, 1, IUItem.reactoruran233Quad);
        addQuadRecipe(IUItem.reactormendeleviumSimple, 1, IUItem.reactormendeleviumQuad);
        addQuadRecipe(IUItem.reactorlawrenciumSimple, 1, IUItem.reactorlawrenciumQuad);
        addQuadRecipe(IUItem.reactornobeliumDual, 1, IUItem.reactornobeliumQuad);
        addQuadRecipe(IUItem.reactorfermiumDual, 1, IUItem.reactorfermiumQuad);


        addQuadRecipe(IUItem.reactorprotonSimple, 0, IUItem.reactorprotonQuad);
        addQuadRecipe(IUItem.reactorcaliforniaSimple, 0, IUItem.reactorcaliforniaQuad);
        addQuadRecipe(IUItem.reactorcuriumSimple, 0, IUItem.reactorcuriumQuad);
        addQuadRecipe(IUItem.reactorneptuniumSimple, 0, IUItem.reactorneptuniumQuad);
        addQuadRecipe(IUItem.uranium_fuel_rod, 0, IUItem.quad_uranium_fuel_rod);
        addQuadRecipe(IUItem.mox_fuel_rod, 0, IUItem.quad_mox_fuel_rod);
        addQuadRecipe(IUItem.reactoramericiumSimple, 0, IUItem.reactoramericiumQuad);
        addQuadRecipe(IUItem.reactortoriySimple, 0, IUItem.reactortoriyQuad);
    }

    public void addQuadRecipe(ItemStack stack, int i, ItemStack output) {
        ItemStack topRod;
        ItemStack leftRod;
        ItemStack rightRod;
        final IInputHandler input = Recipes.inputFactory;
        if (i == 0) {
            topRod = new ItemStack(IUItem.crafting_elements, 1, 181);
            leftRod = new ItemStack(IUItem.crafting_elements, 1, 183);
            rightRod = new ItemStack(IUItem.crafting_elements, 1, 182);
        } else {
            topRod = new ItemStack(IUItem.crafting_elements, 1, 191);
            leftRod = new ItemStack(IUItem.crafting_elements, 1, 190);
            rightRod = new ItemStack(IUItem.crafting_elements, 1, 189);
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
            horizontalRod = new ItemStack(IUItem.crafting_elements, 1, 180);
        } else {
            horizontalRod = new ItemStack(IUItem.crafting_elements, 1, 188);
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
            topRod = new ItemStack(IUItem.crafting_elements, 1, 185);
            bottomRod = new ItemStack(IUItem.crafting_elements, 1, 184);
            leftRod = new ItemStack(IUItem.crafting_elements, 1, 186);
            rightRod = new ItemStack(IUItem.crafting_elements, 1, 187);
        } else {
            topRod = new ItemStack(IUItem.crafting_elements, 1, 194);
            bottomRod = new ItemStack(IUItem.crafting_elements, 1, 195);
            leftRod = new ItemStack(IUItem.crafting_elements, 1, 193);
            rightRod = new ItemStack(IUItem.crafting_elements, 1, 192);
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
        if (IUCore.proxy.isSimulating()) {

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
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        final NBTTagCompound nbt = super.writeToNBT(nbttagcompound);
        nbt.setInteger("type", this.type);
        return nbt;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.type = nbttagcompound.getInteger("type");
    }

    public void updateTileServer(EntityPlayer var1, double var2) {
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
        onActivated(var1, var1.getActiveHand(), EnumFacing.SOUTH, 0, 0, 0);
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiRodFactory(getGuiContainer(var1));
    }

    @Override
    public ContainerRodFactory getGuiContainer(final EntityPlayer var1) {
        return new ContainerRodFactory(this, var1);
    }

}
