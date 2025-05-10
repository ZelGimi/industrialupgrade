package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCyclotronController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiCyclotronController;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.List;

public class TileEntityCyclotronController extends TileMultiBlockBase implements IController, IHasRecipe {

    public ICoolant coolant;
    public ICryogen cryogen;
    public IElectrostaticDeflector electrostaticDeflector;
    public IPositrons positrons;
    public IQuantum quantum;
    public IBombardmentChamber bombardmentChamber;
    public boolean work = false;
    public int progress;

    public TileEntityCyclotronController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.CyclotronMultiBlock,BlockCyclotron.cyclotron_controller,pos,state);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        this.work = !work;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        customPacketBuffer.writeInt(this.progress);
        try {
            EncoderHandler.encode(customPacketBuffer, coolant.getCoolantTank());
            EncoderHandler.encode(customPacketBuffer, cryogen.getCryogenTank());
            EncoderHandler.encode(customPacketBuffer, positrons.getPositrons(), false);
            EncoderHandler.encode(customPacketBuffer, quantum.getQuantum(), false);
            EncoderHandler.encode(customPacketBuffer, bombardmentChamber.getInputSlot());
            customPacketBuffer.writeBoolean(this.bombardmentChamber.getInputSlot().isEmpty());
            EncoderHandler.encode(customPacketBuffer, electrostaticDeflector.getOutputSlot());
            customPacketBuffer.writeBoolean(this.electrostaticDeflector.getOutputSlot().isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        progress = customPacketBuffer.readInt();
        try {
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.coolant.getCoolantTank().readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
            fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.cryogen.getCryogenTank().readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
            positrons.getPositrons().onNetworkUpdate(customPacketBuffer);
            quantum.getQuantum().onNetworkUpdate(customPacketBuffer);
            bombardmentChamber
                    .getInputSlot()
                    .readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            boolean empty = customPacketBuffer.readBoolean();
            if (empty && !bombardmentChamber.getInputSlot().isEmpty()) {
                bombardmentChamber.getInputSlot().set(0, ItemStack.EMPTY);
            }
            electrostaticDeflector.getOutputSlot().readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(
                    new CompoundTag()));
            empty = customPacketBuffer.readBoolean();
            if (empty && !electrostaticDeflector.getOutputSlot().isEmpty()) {
                electrostaticDeflector.getOutputSlot().set(0, ItemStack.EMPTY);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContainerCyclotronController getGuiContainer(final Player var1) {
        return new ContainerCyclotronController(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCyclotronController((ContainerCyclotronController) menu);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && this.isFull()) {
            if (this.cryogen.getCryogenTank().getFluidAmount() > 2 * this.bombardmentChamber.getCryogen() && this.quantum
                    .getQuantum()
                    .getEnergy() > 10 && this.positrons
                    .getPositrons()
                    .getEnergy() > this.bombardmentChamber.getPositrons() && this.bombardmentChamber.getRecipeOutput() != null && this.bombardmentChamber
                    .getInputSlot()
                    .continue_process(this.bombardmentChamber.getRecipeOutput()) && this.electrostaticDeflector
                    .getOutputSlot()
                    .canAdd(this.bombardmentChamber.getRecipeOutput().getRecipe().getOutput().items)) {
                if (!this.getActive()) {
                    setActive(true);
                }
                this.progress += 1;
                this.quantum.getQuantum().useEnergy(10);
                this.positrons.getPositrons().useEnergy(this.bombardmentChamber.getPositrons());
                this.cryogen.getCryogenTank().drain(2 * this.bombardmentChamber.getCryogen(), IFluidHandler.FluidAction.EXECUTE);
                if (this.coolant.getCoolantTank().getFluidAmount() + 1 < this.coolant.getCoolantTank().getCapacity()) {
                    this.coolant.getCoolantTank().fill(new FluidStack(FluidName.fluidcoolant.getInstance().get(), 1), IFluidHandler.FluidAction.EXECUTE);
                }
                if (this.progress >= 1000) {
                    this.progress = 0;
                    if (this.bombardmentChamber.getChance() == 100) {
                        this.electrostaticDeflector.getOutputSlot().add(this.bombardmentChamber
                                .getRecipeOutput()
                                .getRecipe()
                                .getOutput().items);
                        this.bombardmentChamber.getInputSlot().consume();
                        this.bombardmentChamber.getOutput();
                    } else {
                        if (WorldBaseGen.random.nextInt(this.bombardmentChamber.getChance()) < this.bombardmentChamber.getChance()) {
                            this.electrostaticDeflector.getOutputSlot().add(this.bombardmentChamber
                                    .getRecipeOutput()
                                    .getRecipe()
                                    .getOutput().items);
                        }
                        this.bombardmentChamber.getInputSlot().consume();
                        this.bombardmentChamber.getOutput();
                    }
                }
            } else {
                this.setActive(false);
            }
        } else {
            this.setActive(false);
        }
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            coolant = null;
            cryogen = null;
            electrostaticDeflector = null;
            positrons = null;
            quantum = null;
            bombardmentChamber = null;
        }


    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICoolant.class
                );
        this.coolant = (ICoolant) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICryogen.class
                );
        this.cryogen = (ICryogen) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IBombardmentChamber.class
                );
        this.bombardmentChamber = (IBombardmentChamber) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IPositrons.class
                );
        this.positrons = (IPositrons) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IQuantum.class
                );
        this.quantum = (IQuantum) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IElectrostaticDeflector.class
                );
        this.electrostaticDeflector = (IElectrostaticDeflector) this.getWorld().getBlockEntity(pos1.get(0));

    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }

    @Override
    public void init() {
        final IInputHandler input_recipe = com.denfop.api.Recipes.inputFactory;
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("chance", 100);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(new Input(input_recipe.getInput("forge:storage_blocks/Palladium")), new RecipeOutput(
                        nbt,
                        new ItemStack(IUItem.toriy.getItem())
                ))
        );
        nbt = ModUtils.nbt();
        nbt.putInt("chance", 100);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(new Input(input_recipe.getInput("forge:storage_blocks/uranium")), new RecipeOutput(
                        nbt,
                        new ItemStack(IUItem.radiationresources.getStack(1), 1)
                ))
        );
        nbt = ModUtils.nbt();
        nbt.putInt("chance", 100);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(
                        new Input(input_recipe.getInput(new ItemStack(IUItem.nuclear_res.getStack(3), 1))),
                        new RecipeOutput(
                                nbt,
                                new ItemStack(IUItem.radiationresources.getStack(0), 1)
                        )
                )
        );
        nbt = ModUtils.nbt();
        nbt.putInt("chance", 100);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(
                        new Input(input_recipe.getInput(new ItemStack(IUItem.radiationresources.getStack(0), 1))),
                        new RecipeOutput(
                                nbt,
                                new ItemStack(IUItem.radiationresources.getStack(2), 1)
                        )
                )
        );
        nbt = ModUtils.nbt();
        nbt.putInt("chance", 75);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(
                        new Input(input_recipe.getInput(new ItemStack(IUItem.radiationresources.getStack(11), 1))),
                        new RecipeOutput(
                                nbt,
                                new ItemStack(IUItem.radiationresources.getStack(5), 1)
                        )
                )
        );
        nbt = ModUtils.nbt();
        nbt.putInt("chance", 60);
        nbt.putInt("cryogen", 2);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(
                        new Input(input_recipe.getInput(new ItemStack(IUItem.radiationresources.getStack(5), 1))),
                        new RecipeOutput(
                                nbt,
                                new ItemStack(IUItem.radiationresources.getStack(10), 1)
                        )
                )
        );
        nbt = ModUtils.nbt();
        nbt.putInt("chance", 50);
        nbt.putInt("positrons", 2);
        nbt.putInt("cryogen", 2);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(
                        new Input(input_recipe.getInput(new ItemStack(IUItem.radiationresources.getStack(10), 1))),
                        new RecipeOutput(
                                nbt,
                                new ItemStack(IUItem.radiationresources.getStack(9), 1)
                        )
                )
        );

        nbt = ModUtils.nbt();
        nbt.putInt("chance", 50);
        nbt.putInt("positrons", 2);
        nbt.putInt("cryogen", 1);
        Recipes.recipes.addRecipe(
                "cyclotron",
                new BaseMachineRecipe(
                        new Input(input_recipe.getInput(new ItemStack(IUItem.crafting_elements.getStack(641), 1))),
                        new RecipeOutput(
                                nbt,
                                new ItemStack(IUItem.crafting_elements.getStack(647), 1)
                        )
                )
        );
    }

}
