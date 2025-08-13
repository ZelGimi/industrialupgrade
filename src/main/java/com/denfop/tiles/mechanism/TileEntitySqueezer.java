package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSqueezer;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntitySqueezer extends TileEntityInventory implements IUpdateTick, IHasRecipe {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, -0.5, 1, 1.7D,
            1.5
    ));
    private static final List<AABB> aabbs1 = Collections.singletonList(new AABB(-0.5, 0.0D, 0, 1.5, 1.7D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InvSlotFluidByList fluidSlot1;
    public final FluidHandlerRecipe fluid_handler;
    public short progress;
    public Map<UUID, Double> data;
    private MachineRecipe output;

    public TileEntitySqueezer(BlockPos pos, BlockState state) {
        super(BlockSqueezer.squeezer, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "squeezer", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4) {
                    return super.accepts(itemStack, 0);
                }
                return false;
            }
        };
        this.progress = 0;
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("squeezer", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot1.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.inputSlotA.setStackSizeLimit(1);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addRecipe(ItemStack container, FluidStack fluidStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "squeezer",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(null, container)
                )
        );
        Recipes.recipes.getRecipeFluid().addRecipe("squeezer", new BaseFluidMachineRecipe(new InputFluid(
                container), Collections.singletonList(
                fluidStack)));

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 5; i++) {
            tooltip.add(Localization.translate("squeezer.info" + i));
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }


    @Override
    public void init() {
        addRecipe(
                new ItemStack(IUItem.rawLatex.getItem()),
                new FluidStack(FluidName.fluidrawlatex.getInstance().get(), 100)
        );

        addRecipe(
                new ItemStack(Items.WHEAT_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 10)
        );
        addRecipe(
                new ItemStack(Items.MELON_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 35)
        );
        addRecipe(
                new ItemStack(Items.PUMPKIN_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 35)
        );
        addRecipe(
                new ItemStack(IUItem.crops.getStack(0)),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 50)
        );
    }

    public List<AABB> getAabbs(boolean forCollision) {
        if (!(facing == 4 || facing == 5)) {
            return aabbs1;
        }
        return Collections.singletonList(new AABB(0, 0.0D, -0.5, 1, 1.7D,
                1.5
        ));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.squeezer.getBlock();
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSqueezer.squeezer;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!getLevel().isClientSide) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get(0));
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
            data = PrimitiveHandler.getPlayersData(EnumPrimitive.SQUEEZER);
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
            inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
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
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
    public boolean onActivated(
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            return true;
        }

        if (stack.getItem().equals(IUItem.treetap.getItem()) && !inputSlotA.isEmpty() && this.output != null && this.fluid_handler.output() != null && this.fluid_handler.canFillFluid()) {
            progress += (short) (20 + (short) (data.getOrDefault(player.getUUID(), 0.0) / 15d));

            this.getCooldownTracker().setTick(8);
            this.setActive(String.valueOf(progress / 10));
            if (progress >= 100) {
                this.progress = 0;
                this.setActive("");
                if (!this.getWorld().isClientSide)
                    PrimitiveHandler.addExperience(EnumPrimitive.SQUEEZER, 0.5, player.getUUID());
                this.fluid_handler.fillFluid();
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (!level.isClientSide) {
                    new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
                }
            }

            return this.getWorld().isClientSide;
        } else {
            if (!stack.isEmpty() && this.inputSlotA.accepts(stack, 4)) {
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
                if (!this.getWorld().isClientSide && player
                        .getItemInHand(hand)
                        .getCapability(
                                ForgeCapabilities.FLUID_HANDLER_ITEM,
                                null
                        ).orElse((IFluidHandlerItem) stack.getItem().initCapabilities(stack, stack.getTag())) != null && this.fluidTank1.getFluidAmount() >= 1000) {
                    ModUtils.interactWithFluidHandler(player, hand,
                            this.getComp(Fluids.class).getCapability(ForgeCapabilities.FLUID_HANDLER, side)
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
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot3", false);
                        }
                        return true;
                    }
                }
            }
        }


        return false;
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
    }

}
