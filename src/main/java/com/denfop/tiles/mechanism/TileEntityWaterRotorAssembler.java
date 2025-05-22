package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerWaterRotorAssembler;
import com.denfop.gui.GuiWaterRotorAssembler;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TileEntityWaterRotorAssembler extends TileEntityInventory implements IUpdateTick, IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final Energy energy;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotOutput outputSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe recipe;
    public short progress;
    public double guiProgress;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick = 1;

    public TileEntityWaterRotorAssembler() {
        this.inputSlotA = new InvSlotRecipes(this, "water_rotor_assembler", this);
        inputSlotA.setStackSizeLimit(1);
        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 100;
        this.defaultTier = 14;
        this.defaultEnergyStorage = 2 * 100;
        this.recipe = null;
        this.outputSlot = new InvSlotOutput(this, 1);
        this.energy = this.addComponent(Energy.asBasicSink(this, defaultEnergyStorage, defaultTier));
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
    }

    public static void addRecipe(int meta, int meta1, ItemStack stack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        ((ItemDamage) stack.getItem()).setCustomDamage(stack, ((ItemDamage) stack.getItem()).getMaxCustomDamage(stack));
        Recipes.recipes.addRecipe("water_rotor_assembler", new BaseMachineRecipe(
                new Input(
                        input.getInput(new ItemStack(IUItem.water_rod, 1, meta)),
                        input.getInput(new ItemStack(IUItem.water_rod, 1, meta)),
                        input.getInput(new ItemStack(IUItem.water_rod, 1, meta)),
                        input.getInput(new ItemStack(IUItem.water_rod, 1, meta)),
                        input.getInput(new ItemStack(IUItem.corewater, 1, meta1))

                ),
                new RecipeOutput(null, stack)
        ));
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.water_rotor_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void init() {
        addRecipe(0, 0, new ItemStack(IUItem.water_rotor_wood));
        addRecipe(1, 1, new ItemStack(IUItem.water_rotor_bronze));
        addRecipe(2, 2, new ItemStack(IUItem.water_rotor_iron));
        addRecipe(3, 3, new ItemStack(IUItem.water_rotor_steel));
        addRecipe(4, 4, new ItemStack(IUItem.water_rotor_carbon));

        addRecipe(5, 5, IUItem.water_iridium);
        addRecipe(6, 6, IUItem.water_compressiridium);
        addRecipe(7, 7, IUItem.water_spectral);
        addRecipe(8, 8, IUItem.water_myphical);
        addRecipe(10, 10, IUItem.water_photon);
        addRecipe(9, 9, IUItem.water_neutron);
        addRecipe(11, 11, IUItem.water_barionrotor);
        addRecipe(12, 12, IUItem.water_adronrotor);
        addRecipe(13, 13, IUItem.water_ultramarinerotor);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(getNBTFromSlot(customPacketBuffer));
            progress = (short) DecoderHandler.decode(customPacketBuffer);
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, inputSlotA);
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, guiProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }

    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        this.getOutput();
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if (!this.inputSlotA.continue_process(this.recipe) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }

            if (this.recipe == null) {
                break;
            }
        }


    }

    public MachineRecipe getOutput() {
        this.recipe = this.inputSlotA.process();


        return this.recipe;
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();

        this.outputSlot.add(processResult);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.recipe != null && this.energy.canUseEnergy(energyConsume) && !this.inputSlotA.isEmpty() && this.outputSlot.canAdd(
                this.recipe.getRecipe().getOutput().items)) {


            this.progress += 1;
            this.energy.useEnergy(energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(this.recipe);
                this.progress = 0;


            }

        } else {
            if (this.recipe == null) {
                this.progress = 0;

            }


        }
    }


    @Override
    public ContainerWaterRotorAssembler getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerWaterRotorAssembler(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiWaterRotorAssembler(getGuiContainer(entityPlayer));
    }


    @Override
    public void onUpdate() {

    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.recipe;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.recipe = output;
    }

}
