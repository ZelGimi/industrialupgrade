package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntitySqueezer extends TileEntityInventory implements IUpdateTick, IHasRecipe {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -0.5, 1, 1.7D,
            1.5
    ));
    private static final List<AxisAlignedBB> aabbs1 = Collections.singletonList(new AxisAlignedBB(-0.5, 0.0D, 0, 1.5, 1.7D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InvSlotFluidByList fluidSlot1;
    public final FluidHandlerRecipe fluid_handler;
    public short progress;
    public Map<UUID, Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.SQUEEZER);
    private MachineRecipe output;

    public TileEntitySqueezer() {

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
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public void init() {
        addRecipe(
                new ItemStack(IUItem.rawLatex),
                new FluidStack(FluidName.fluidrawlatex.getInstance(), 100)
        );

        addRecipe(
                new ItemStack(Items.WHEAT_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance(), 10)
        );
        addRecipe(
                new ItemStack(Items.MELON_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance(), 35)
        );
        addRecipe(
                new ItemStack(Items.PUMPKIN_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance(), 35)
        );
        addRecipe(
                new ItemStack(IUItem.crops),
                new FluidStack(FluidName.fluidseedoil.getInstance(), 50)
        );
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (!(facing == 4 || facing == 5)) {
            return aabbs1;
        }
        return Collections.singletonList(new AxisAlignedBB(0, 0.0D, -0.5, 1, 1.7D,
                1.5
        ));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.squeezer;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSqueezer.squeezer;
    }

    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.SQUEEZER);
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get());
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
        }


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
            inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
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
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.put(0, ItemStack.EMPTY);
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
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (this.world.isRemote) {
            return true;
        }

        if (stack
                .getItem()
                .equals(IUItem.treetap) && !inputSlotA.isEmpty() && this.output != null && this.fluid_handler.output() != null && this.fluid_handler.canFillFluid()) {
            progress += (short) (10 + (short) (data.getOrDefault(player.getUniqueID(), 0.0) / 2.5d));
            this.getCooldownTracker().setTick(8);
            this.setActive(String.valueOf(progress / 10));
            if (progress >= 150) {
                this.progress = 0;
                this.setActive("");
                if (!this.getWorld().isRemote) {
                    PrimitiveHandler.addExperience(EnumPrimitive.SQUEEZER, 0.5, player.getUniqueID());
                }
                this.fluid_handler.fillFluid();
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (!world.isRemote) {
                    new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
                }
            }

            return this.getWorld().isRemote;
        } else {
            if (!stack.isEmpty() && this.inputSlotA.accepts(stack, 4)) {
                if (this.inputSlotA.get(0).isEmpty()) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);
                    this.inputSlotA.put(0, stack1);
                    stack.shrink(1);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                }
            } else {
                if (!this.getWorld().isRemote && player
                        .getHeldItem(hand)
                        .hasCapability(
                                CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                                null
                        ) && this.fluidTank1.getFluidAmount() >= 1000) {
                    ModUtils.interactWithFluidHandler(player, hand,
                            this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                    );
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
                    }
                    return true;
                } else {
                    if (!inputSlotA.isEmpty()) {
                        if (!world.isRemote) {
                            ModUtils.dropAsEntity(world, pos, inputSlotA.get(), player);
                        }
                        inputSlotA.put(0, ItemStack.EMPTY);
                        this.output = null;
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot3", false);
                        }
                        return true;
                    }
                }
            }
        }


        return false;
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

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && !this.inputSlotA.isEmpty())) {
            this.fluid_handler.getOutput(this.inputSlotA.get());
        } else {
            if (this.fluid_handler.output() != null && this.inputSlotA.isEmpty()) {
                this.fluid_handler.setOutput(null);
            }
        }
    }

}
