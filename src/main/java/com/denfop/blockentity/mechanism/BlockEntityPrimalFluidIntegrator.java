package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockPrimalFluidIntegratorEntity;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityPrimalFluidIntegrator extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent, IHasRecipe {


    public final InventoryRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank2;
    public final FluidHandlerRecipe fluid_handler;
    public final int defaultOperationLength;
    private final Fluids fluids;
    public MachineRecipe output;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public double guiProgress;

    protected short progress;
    private int prevAmount;
    private int prevAmount1;

    public BlockEntityPrimalFluidIntegrator(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockPrimalFluidIntegratorEntity.primal_fluid_integrator, pos, state);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 200;
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 1000);
        this.fluidTank2 = fluids.addTank("fluidTank2", 1000, Inventory.TypeItemSlot.OUTPUT);
        this.inputSlotA = new InventoryRecipes(this, "primal_fluid_integrator", this, this.fluidTank1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack itemStack) {
                if (index == 4) {
                    return super.canPlaceItem(0, itemStack);
                }
                return false;
            }
        };

        inputSlotA.setStackSizeLimit(1);
        this.fluid_handler = new FluidHandlerRecipe("primal_fluid_integrator", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));

    }


    public static void addRecipe(ItemStack container, ItemStack output, FluidStack fluidStack, FluidStack outputfluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "primal_fluid_integrator",
                new BaseMachineRecipe(
                        new Input(fluidStack, input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("primal_fluid_integrator", new BaseFluidMachineRecipe(new InputFluid(
                container, fluidStack), Collections.singletonList(
                outputfluidStack)));

    }

    public static void addRecipe(IInputItemStack container, ItemStack output, FluidStack fluidStack, FluidStack outputfluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "primal_fluid_integrator",
                new BaseMachineRecipe(
                        new Input(fluidStack, input.getInput(container)),
                        new RecipeOutput(null, output)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("primal_fluid_integrator", new BaseFluidMachineRecipe(new InputFluid(
                container, fluidStack), Collections.singletonList(
                outputfluidStack)));

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        for (int i = 1; i < 6; i++) {
            tooltip.add(Localization.translate("fluid_integrator.info" + i));
        }

    }

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(-0.2D, 0.0D, -0.2D, 1.2D, 1D, 1.2D));

    }


    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
            }
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.fluidTank2.readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
            try {
                inputSlotA.readFromNbt(((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                outputSlot.readFromNbt(((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("guiProgress")) {
            try {
                guiProgress = (double) DecoderHandler.decode(is);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank1")) {
            try {
                FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank2 != null) {
                    this.fluidTank2.readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank_empty")) {
            this.fluidTank1.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        }
        if (name.equals("fluidtank1_empty")) {
            this.fluidTank2.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        }
        if (name.equals("slot")) {
            try {
                inputSlotA.readFromNbt(((Inventory) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((Inventory) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.set(0, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player
                .getItemInHand(hand)) != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.canPlaceItem(4, stack)) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);
                    this.inputSlotA.set(0, stack1);
                    stack.shrink(1);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                }
            } else {
                if (!outputSlot.isEmpty()) {
                    if (!level.isClientSide) {
                        ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
                    }
                    outputSlot.set(0, ItemStack.EMPTY);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot2", false);
                    }
                    return true;
                } else {
                    if (!inputSlotA.isEmpty()) {
                        if (!level.isClientSide) {
                            ModUtils.dropAsEntity(level, pos, inputSlotA.get(0));
                        }
                        inputSlotA.set(0, ItemStack.EMPTY);
                        this.output = null;
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot3", false);
                        }
                        return true;
                    }
                }
            }
            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, fluidTank1);
            EncoderHandler.encode(customPacketBuffer, fluidTank2);
            EncoderHandler.encode(customPacketBuffer, inputSlotA);
            EncoderHandler.encode(customPacketBuffer, outputSlot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }


    @Override
    public void init() {

        addRecipe(new ItemStack(IUItem.iuingot.getStack(15), 1), new ItemStack(IUItem.iudust.getStack(42), 1),
                new FluidStack(FluidName.fluiddibromopropane.getInstance().get(), 200),
                new FluidStack(FluidName.fluidpropylene.getInstance().get()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(482), 1), new ItemStack(IUItem.iudust.getStack(43), 1),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 200),
                new FluidStack(FluidName.fluidacetylene.getInstance().get()
                        , 200)
        );

        addRecipe(new ItemStack(Items.COPPER_INGOT, 1), new ItemStack(IUItem.smalldust.getStack(20), 3),
                new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 200),
                new FluidStack(FluidName.fluidcoppersulfate.getInstance().get()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(489), 1), new ItemStack(IUItem.crafting_elements.getStack(487), 1),
                new FluidStack(FluidName.fluidcoppersulfate.getInstance().get(), 500),
                new FluidStack(FluidName.fluidwastesulfuricacid.getInstance().get()
                        , 500)
        );
        addRecipe(new ItemStack(IUItem.heavyore.getItemStack(4), 1), new ItemStack(IUItem.smalldust.getStack(22), 1),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 1000),
                new FluidStack(FluidName.fluidsulfuroxide.getInstance().get()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(492), 1), new ItemStack(IUItem.crafting_elements.getStack(493), 1),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 500),
                new FluidStack(FluidName.fluidhydrogen.getInstance().get()
                        , 50)
        );

        addRecipe(Recipes.inputFactory.getInput("forge:dusts/coal"), new ItemStack(IUItem.crafting_elements.getStack(498), 1),
                new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 200),
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER
                        , 100)
        );

        addRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.MYCELIUM),
                new FluidStack(FluidName.fluidhoney.getInstance().get(), 1000),
                new FluidStack(FluidName.fluidoxygen.getInstance().get()
                        , 75)
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.fluidIntegrator.getBlock();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockPrimalFluidIntegratorEntity.primal_fluid_integrator;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!getLevel().isClientSide) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get(0));
            this.getOutput();
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
        }


    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
        return nbttagcompound;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        this.fluid_handler.setOutput(null);
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.fluid_handler.setOutput(null);
        return this.output;
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && this.output != null && this.fluidTank1.getFluidAmount() > 0)) {
            this.fluid_handler.getOutput(this.inputSlotA.get(0));
        } else {
            if (this.fluid_handler.output() != null && this.output == null) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (this.prevAmount != this.fluidTank1.getFluidAmount()) {
            this.prevAmount = this.fluidTank1.getFluidAmount();
            if (prevAmount != 0) {
                new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
            } else {
                new PacketUpdateFieldTile(this, "fluidtank_empty", true);
            }
        }
        if (this.prevAmount1 != this.fluidTank2.getFluidAmount()) {
            this.prevAmount1 = this.fluidTank2.getFluidAmount();
            if (prevAmount1 != 0) {
                new PacketUpdateFieldTile(this, "fluidtank1", this.fluidTank2);
            } else {
                new PacketUpdateFieldTile(this, "fluidtank1_empty", true);
            }
        }

        if (this.output != null && !this.inputSlotA.isEmpty() && this.outputSlot.isEmpty() && this.outputSlot.canAdd(this.output
                .getRecipe()
                .getOutput().items) && this.inputSlotA.continue_process(this.output) && this.fluid_handler.output() != null && fluid_handler.canOperate() && this.fluid_handler.canFillFluid()) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate();
                this.progress = 0;
                initiate(2);
                new PacketUpdateFieldTile(this, "guiProgress", this.guiProgress);
            }
            if (this.level.getGameTime() % 20 == 0) {
                new PacketUpdateFieldTile(this, "guiProgress", this.guiProgress);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(0);
            }
            if (this.output == null) {
                this.progress = 0;
                guiProgress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
    }


    public void operate() {
        for (int i = 0; i < 1; i++) {
            operateOnce();

            this.getOutput();
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.inputSlotA.consume();
        this.outputSlot.add(this.output.getRecipe().getOutput().items);
        this.fluid_handler.fillFluid();
        new PacketUpdateFieldTile(this, "slot3", false);
        new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
    }

    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemInput,
                EnumBlockEntityUpgrade.FluidExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
