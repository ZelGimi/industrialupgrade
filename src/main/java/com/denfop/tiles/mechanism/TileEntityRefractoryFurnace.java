package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
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
import com.denfop.blocks.mechanism.BlockRefractoryFurnace;
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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TileEntityRefractoryFurnace extends TileEntityInventory implements IUpdateTick, IHasRecipe {


    public final InvSlotRecipes inputSlotA;
    public final Fluids.InternalFluidTank fluidTank1;
    public final InvSlotFluidByList fluidSlot1;
    public final FluidHandlerRecipe fluid_handler;
    public short progress;
    private MachineRecipe output;
    private boolean work;

    public TileEntityRefractoryFurnace() {

        this.inputSlotA = new InvSlotRecipes(this, "refractory_furnace", this);
        this.progress = 0;
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 144 * 12, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("refractory_furnace", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot1.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.inputSlotA.setStackSizeLimit(1);
        Recipes.recipes.addInitRecipes(this);
    }
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 5; i++) {
            tooltip.add(Localization.translate("refractory_furnace.info" + i));
        }
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
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public void init() {
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 0), // mikhail
                new FluidStack(FluidName.fluidmoltenmikhail.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 1), // aluminium
                new FluidStack(FluidName.fluidmoltenaluminium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 2), // vanady
                new FluidStack(FluidName.fluidmoltenvanadium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 3), // wolfram
                new FluidStack(FluidName.fluidmoltentungsten.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 6), // cobalt
                new FluidStack(FluidName.fluidmoltencobalt.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 7), // magnesium
                new FluidStack(FluidName.fluidmoltenmagnesium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 8), // nickel
                new FluidStack(FluidName.fluidmoltennickel.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 9), // platium
                new FluidStack(FluidName.fluidmoltenplatinum.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 10), // titanium
                new FluidStack(FluidName.fluidmoltentitanium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 11), // chromium
                new FluidStack(FluidName.fluidmoltenchromium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 12), // spinel
                new FluidStack(FluidName.fluidmoltenspinel.getInstance(), 144)
        );

        addRecipe(
                new ItemStack(IUItem.crushed, 1, 14), // silver
                new FluidStack(FluidName.fluidmoltensilver.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 15), // zinc
                new FluidStack(FluidName.fluidmoltenzinc.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 16), // manganese
                new FluidStack(FluidName.fluidmoltenmanganese.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 17), // iridium
                new FluidStack(FluidName.fluidmolteniridium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 18), // germanium
                new FluidStack(FluidName.fluidmoltengermanium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 19), // copper
                new FluidStack(FluidName.fluidmoltencopper.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 20), // gold
                new FluidStack(FluidName.fluidmoltengold.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 21), // iron
                new FluidStack(FluidName.fluidmolteniron.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 22), // lead
                new FluidStack(FluidName.fluidmoltenlead.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 23), // tin
                new FluidStack(FluidName.fluidmoltentin.getInstance(), 144)
        );

        addRecipe(
                new ItemStack(IUItem.crushed, 1, 25), // osmium
                new FluidStack(FluidName.fluidmoltenosmium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 26), // tantalum
                new FluidStack(FluidName.fluidmoltentantalum.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 27), // cadmium
                new FluidStack(FluidName.fluidmoltencadmium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 28), // arsenic
                new FluidStack(FluidName.fluidmoltenarsenic.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 29), // barium
                new FluidStack(FluidName.fluidmoltenbarium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 30), // bismuth
                new FluidStack(FluidName.fluidmoltenbismuth.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 31), // gadolinium
                new FluidStack(FluidName.fluidmoltengadolinium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 32), // gallium
                new FluidStack(FluidName.fluidmoltengallium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 33), // hafnium
                new FluidStack(FluidName.fluidmoltenhafnium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 34), // yttrium
                new FluidStack(FluidName.fluidmoltenyttrium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 35), // molybdenum
                new FluidStack(FluidName.fluidmoltenmolybdenum.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 36), // neodymium
                new FluidStack(FluidName.fluidmoltenneodymium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 37), // niobium
                new FluidStack(FluidName.fluidmoltenniobium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 38), // palladium
                new FluidStack(FluidName.fluidmoltenpalladium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 39), // polonium
                new FluidStack(FluidName.fluidmoltenpolonium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 40), // strontium
                new FluidStack(FluidName.fluidmoltenstrontium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 41), // thallium
                new FluidStack(FluidName.fluidmoltenthallium.getInstance(), 144)
        );
        addRecipe(
                new ItemStack(IUItem.crushed, 1, 42), // zirconium
                new FluidStack(FluidName.fluidmoltenzirconium.getInstance(), 144)
        );


    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.refractoryFurnace;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockRefractoryFurnace.refractory_furnace;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get());
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
            IBlockState blockState = world.getBlockState(this.pos.down());
            if (blockState.getMaterial() != Material.AIR) {
                this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                        .getInstance()
                        .getBlock();
            } else {
                work = false;
            }
        }


    }

    @Override
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.down().distanceSq(neighborPos) == 0) {
                IBlockState blockState = world.getBlockState(this.pos.down());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                            .getInstance()
                            .getBlock();
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.down().distanceSq(neighborPos) == 0) {
                IBlockState blockState = world.getBlockState(this.pos.down());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                            .getInstance()
                            .getBlock();
                } else {
                    work = false;
                }
            }
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


        if (!stack.isEmpty() && this.inputSlotA.accepts(stack, 0)) {
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
                    this.setActive(false);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot3", false);
                    }
                    return true;
                }
            }

        }


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
        if (work) {
            if ( !inputSlotA.isEmpty() && this.output != null && this.fluid_handler.output() != null && this.fluid_handler.canFillFluid()) {
                progress += 1;
                this.setActive(true);
                if (progress >= 150) {
                    this.progress = 0;
                    this.setActive("");

                    this.fluid_handler.fillFluid();
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
                    }
                }
            }else{
                this.setActive(false);
            }
        }else{
            this.setActive(false);
        }
    }

}
