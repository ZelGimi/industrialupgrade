package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.*;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockRefractoryFurnaceEntity;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BlockEntityRefractoryFurnace extends BlockEntityInventory implements IUpdateTick, IHasRecipe {


    public final InventoryRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InventoryFluidByList fluidSlot1;
    public final FluidHandlerRecipe fluid_handler;
    public short progress;
    private MachineRecipe output;
    private boolean work;

    public BlockEntityRefractoryFurnace(BlockPos pos, BlockState state) {
        super(BlockRefractoryFurnaceEntity.refractory_furnace, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "refractory_furnace", this);
        this.progress = 0;
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 144 * 12, Inventory.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("refractory_furnace", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot1.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.inputSlotA.setStackSizeLimit(1);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addRecipe(ItemStack container, FluidStack fluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "refractory_furnace",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, container)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("refractory_furnace", new BaseFluidMachineRecipe(new InputFluid(
                container), Collections.singletonList(
                fluidStack)));

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 5; i++) {
            tooltip.add(Localization.translate("refractory_furnace.info" + i));
        }
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }


    @Override
    public void init() {
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(0)), // mikhail
                new FluidStack(FluidName.fluidmoltenmikhail.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(1)), // aluminium
                new FluidStack(FluidName.fluidmoltenaluminium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(2)), // vanadium
                new FluidStack(FluidName.fluidmoltenvanadium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(3)), // wolfram
                new FluidStack(FluidName.fluidmoltentungsten.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(6)), // cobalt
                new FluidStack(FluidName.fluidmoltencobalt.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(7)), // magnesium
                new FluidStack(FluidName.fluidmoltenmagnesium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(8)), // nickel
                new FluidStack(FluidName.fluidmoltennickel.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(9)), // platinum
                new FluidStack(FluidName.fluidmoltenplatinum.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(10)), // titanium
                new FluidStack(FluidName.fluidmoltentitanium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(11)), // chromium
                new FluidStack(FluidName.fluidmoltenchromium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(12)), // spinel
                new FluidStack(FluidName.fluidmoltenspinel.getInstance().get(), 144)
        );

        addRecipe(
                new ItemStack(IUItem.crushed.getStack(14)), // silver
                new FluidStack(FluidName.fluidmoltensilver.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(15)), // zinc
                new FluidStack(FluidName.fluidmoltenzinc.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(16)), // manganese
                new FluidStack(FluidName.fluidmoltenmanganese.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(17)), // iridium
                new FluidStack(FluidName.fluidmolteniridium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(18)), // germanium
                new FluidStack(FluidName.fluidmoltengermanium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(19)), // copper
                new FluidStack(FluidName.fluidmoltencopper.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(20)), // gold
                new FluidStack(FluidName.fluidmoltengold.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(21)), // iron
                new FluidStack(FluidName.fluidmolteniron.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(22)), // lead
                new FluidStack(FluidName.fluidmoltenlead.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(23)), // tin
                new FluidStack(FluidName.fluidmoltentin.getInstance().get(), 144)
        );

        addRecipe(
                new ItemStack(IUItem.crushed.getStack(25)), // osmium
                new FluidStack(FluidName.fluidmoltenosmium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(26)), // tantalum
                new FluidStack(FluidName.fluidmoltentantalum.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(27)), // cadmium
                new FluidStack(FluidName.fluidmoltencadmium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(28)), // arsenic
                new FluidStack(FluidName.fluidmoltenarsenic.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(29)), // barium
                new FluidStack(FluidName.fluidmoltenbarium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(30)), // bismuth
                new FluidStack(FluidName.fluidmoltenbismuth.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(31)), // gadolinium
                new FluidStack(FluidName.fluidmoltengadolinium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(32)), // gallium
                new FluidStack(FluidName.fluidmoltengallium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(33)), // hafnium
                new FluidStack(FluidName.fluidmoltenhafnium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(34)), // yttrium
                new FluidStack(FluidName.fluidmoltenyttrium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(35)), // molybdenum
                new FluidStack(FluidName.fluidmoltenmolybdenum.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(36)), // neodymium
                new FluidStack(FluidName.fluidmoltenneodymium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(37)), // niobium
                new FluidStack(FluidName.fluidmoltenniobium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(38)), // palladium
                new FluidStack(FluidName.fluidmoltenpalladium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(39)), // polonium
                new FluidStack(FluidName.fluidmoltenpolonium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(40)), // strontium
                new FluidStack(FluidName.fluidmoltenstrontium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(41)), // thallium
                new FluidStack(FluidName.fluidmoltenthallium.getInstance().get(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed.getStack(42)), // zirconium
                new FluidStack(FluidName.fluidmoltenzirconium.getInstance().get(), 144)
        );


    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.refractoryFurnace.getBlock();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockRefractoryFurnaceEntity.refractory_furnace;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get(0));
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
            BlockState blockState = level.getBlockState(this.pos.below());
            if (!blockState.isAir()) {
                this.work = blockState.getFluidState().is(net.minecraft.world.level.material.Fluids.LAVA);
            } else {
                work = false;
            }

            this.getOutput();
        }


    }

    @Override
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                BlockState blockState = level.getBlockState(this.pos.below());
                if (!blockState.isAir()) {
                    this.work = blockState.getFluidState().is(net.minecraft.world.level.material.Fluids.LAVA);
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                BlockState blockState = level.getBlockState(this.pos.below());
                if (!blockState.isAir()) {
                    this.work = blockState.getFluidState().is(net.minecraft.world.level.material.Fluids.LAVA);
                } else {
                    work = false;
                }
            }
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
    public void onUpdate() {

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

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            Inventory inventory = ((Inventory) (DecoderHandler.decode(customPacketBuffer)));
            inputSlotA.read(inventory);
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(customPacketBuffer.registryAccess(), fluidTank1.writeToNBT(customPacketBuffer.registryAccess(), new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("slot")) {
            try {
                Inventory inventory = ((Inventory) (DecoderHandler.decode(is)));
                inputSlotA.read(inventory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(is.registryAccess(), fluidTank1.writeToNBT(is.registryAccess(), new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, inputSlotA);
            EncoderHandler.encode(customPacketBuffer, fluidTank1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            return true;
        }


        if (!stack.isEmpty() && this.inputSlotA.canPlaceItem(0, stack)) {
            if (this.inputSlotA.get(0).isEmpty()) {
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
            if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player
                    .getItemInHand(hand)) != null && this.fluidTank1.getFluidAmount() >= 1000) {
                ModUtils.interactWithFluidHandler(player, hand,
                        this.getComp(Fluids.class).getCapability(Capabilities.FluidHandler.BLOCK, side)
                );
                if (!level.isClientSide) {
                    new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
                }
                return true;
            } else {
                if (!inputSlotA.isEmpty()) {
                    if (!level.isClientSide) {
                        ModUtils.dropAsEntity(level, pos, inputSlotA.get(0));
                    }
                    inputSlotA.set(0, ItemStack.EMPTY);
                    this.output = null;
                    this.setActive(false);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot3", false);
                    }
                    return true;
                }
            }

        }
        return super.onActivated(player, hand, side, vec3);
    }


    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && !this.inputSlotA.isEmpty())) {
            this.fluid_handler.getOutput(this.inputSlotA.get(0));
        } else {
            if (this.fluid_handler.output() != null && this.inputSlotA.isEmpty()) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (work) {
            if (!inputSlotA.isEmpty() && this.output != null && this.fluid_handler.output() != null && this.fluid_handler.canFillFluid()) {
                progress += 2;
                this.setActive(true);
                if (progress >= 150) {
                    this.progress = 0;
                    this.setActive("");

                    this.fluid_handler.fillFluid();
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
                    }
                }
            } else {
                this.setActive(false);
            }
        } else {
            this.setActive(false);
        }
    }

}
