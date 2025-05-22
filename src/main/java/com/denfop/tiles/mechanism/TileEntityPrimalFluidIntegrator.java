package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
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
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockPrimalFluidIntegrator;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerFluidIntegrator;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityPrimalFluidIntegrator extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {


    public final InvSlotRecipes inputSlotA;
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
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid1;
    protected short progress;
    private int prevAmount;
    private int prevAmount1;

    public TileEntityPrimalFluidIntegrator() {
        super(0, 0, 1);
        Recipes.recipes.addInitRecipes(this);

        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 200;
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 1000);
        this.fluidTank2 = fluids.addTank("fluidTank2", 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.inputSlotA = new InvSlotRecipes(this, "primal_fluid_integrator", this, this.fluidTank1) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4) {
                    return super.accepts(itemStack, 0);
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


    public void addInformation(ItemStack stack, List<String> tooltip) {
        for (int i = 1; i < 6; i++) {
            tooltip.add(Localization.translate("fluid_integrator.info" + i));
        }

    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(-0.2D, 0.0D, -0.2D, 1.2D, 1D, 1.2D));

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

    public ContainerFluidIntegrator getGuiContainer(final EntityPlayer var1) {
        return null;

    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
            }
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.fluidTank2.readFromNBT(fluidTank2.writeToNBT(new NBTTagCompound()));
            }
            try {
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
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
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
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
                    this.fluidTank2.readFromNBT(fluidTank2.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank_empty")) {
            this.fluidTank1.drain(Integer.MAX_VALUE, true);
        }
        if (name.equals("fluidtank1_empty")) {
            this.fluidTank2.drain(Integer.MAX_VALUE, true);
        }
        if (name.equals("slot")) {
            try {
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.put(0, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.put(0, ItemStack.EMPTY);
        }
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
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
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
                if (!outputSlot.isEmpty()) {
                    if (!world.isRemote) {
                        ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                    }
                    outputSlot.put(0, ItemStack.EMPTY);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot2", false);
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
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {

        return null;
    }

    @Override
    public void init() {

        addRecipe(new ItemStack(IUItem.iuingot, 1, 15), new ItemStack(IUItem.iudust, 1, 42),
                new FluidStack(FluidName.fluiddibromopropane.getInstance(), 200),
                new FluidStack(FluidName.fluidpropylene.getInstance()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 482), new ItemStack(IUItem.iudust, 1, 43),
                new FluidStack(FluidRegistry.WATER, 200),
                new FluidStack(FluidName.fluidacetylene.getInstance()
                        , 200)
        );

        addRecipe(new ItemStack(IUItem.iuingot, 1, 21), new ItemStack(IUItem.smalldust, 3, 20),
                new FluidStack(FluidName.fluidsulfuricacid.getInstance(), 200),
                new FluidStack(FluidName.fluidcoppersulfate.getInstance()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 489), new ItemStack(IUItem.crafting_elements, 1, 487),
                new FluidStack(FluidName.fluidcoppersulfate.getInstance(), 500),
                new FluidStack(FluidName.fluidwastesulfuricacid.getInstance()
                        , 500)
        );
        addRecipe(new ItemStack(IUItem.heavyore, 1, 4), new ItemStack(IUItem.smalldust, 1, 22),
                new FluidStack(FluidRegistry.WATER, 1000),
                new FluidStack(FluidName.fluidsulfuroxide.getInstance()
                        , 200)
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 492), new ItemStack(IUItem.crafting_elements, 1, 493),
                new FluidStack(FluidRegistry.WATER, 500),
                new FluidStack(FluidName.fluidhyd.getInstance()
                        , 50)
        );

        addRecipe(new ItemStack(IUItem.iudust, 1, 21), new ItemStack(IUItem.crafting_elements, 1, 498),
                new FluidStack(FluidName.fluidhyd.getInstance(), 200),
                new FluidStack(FluidRegistry.WATER
                        , 100)
        );

        addRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.MYCELIUM),
                new FluidStack(FluidName.fluidhoney.getInstance(), 1000),
                new FluidStack(FluidName.fluidoxy.getInstance()
                        , 75)
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.fluidIntegrator;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPrimalFluidIntegrator.primal_fluid_integrator;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.fluid_handler.load(this.inputSlotA.get());
            this.getOutput();
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
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
            this.fluid_handler.getOutput(this.inputSlotA.get());
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
            if (this.world.getWorldTime() % 20 == 0) {
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
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemInput,
                UpgradableProperty.FluidExtract
        );
    }


    public double getProgress() {
        return this.guiProgress;
    }

}
