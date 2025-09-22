package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGasChamber;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerFluidMixer;
import com.denfop.gui.GuiFluidMixer;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

public class TileEntityPrimalGasChamber extends TileElectricMachine implements IUpgradableBlock, IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank3;
    public final int defaultOperationLength;
    public final Fluids fluids;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid1;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid2;
    protected short progress;
    protected double guiProgress;
    private int prevAmount;
    private int prevAmount1;
    private int prevAmount2;

    public TileEntityPrimalGasChamber() {
        super(0, 0, 0);
        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 600;

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);


        this.fluidTank2 = fluids.addTankInsert("fluidTank2", 12 * 1000);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("gas_chamber", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(1)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));

        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(-0.05D, 0.0D, -0.05D, 1.05D, 2D, 1.05D));

    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhydrogensulfide.getInstance(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance(),
                        300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuroxide.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidacetylene.getInstance(), 400), new FluidStack(
                FluidName.fluidhyd.getInstance(),
                1000
        )), Collections.singletonList(new FluidStack(FluidName.fluidethylene.getInstance(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfuroxide.getInstance(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance(),
                        100
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfurtrioxide.getInstance(), 500),
                new FluidStack(
                        FluidName.fluidsteam.getInstance(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuricacid.getInstance(), 500))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpropane.getInstance(), 400), new FluidStack(
                FluidName.fluidbromine.getInstance(),
                800
        )), Collections.singletonList(new FluidStack(FluidName.fluiddibromopropane.getInstance(), 400))));

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

    public void addInformation(ItemStack stack, List<String> tooltip) {
        for (int i = 1; i < 4; i++) {
            tooltip.add(Localization.translate("gas_chamber.info" + i));
        }

    }


    public double getProgress() {
        return this.guiProgress;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.fluid_handler.load();
        }
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
            FluidTank fluidTank3 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank3 != null) {
                this.fluidTank3.readFromNBT(fluidTank3.writeToNBT(new NBTTagCompound()));
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
        if (name.equals("fluidtank2")) {
            try {
                FluidTank fluidTank3 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank3 != null) {
                    this.fluidTank3.readFromNBT(fluidTank3.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, fluidTank1);
            EncoderHandler.encode(customPacketBuffer, fluidTank2);
            EncoderHandler.encode(customPacketBuffer, fluidTank3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    public void onUnloaded() {
        super.onUnloaded();

    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && this.fluidTank2.getFluidAmount() >= 1 && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (this.prevAmount != this.fluidTank1.getFluidAmount()) {
            this.prevAmount = this.fluidTank1.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank1);
        }
        if (this.prevAmount1 != this.fluidTank2.getFluidAmount()) {
            this.prevAmount1 = this.fluidTank2.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank1", this.fluidTank2);
        }
        if (this.prevAmount2 != this.fluidTank3.getFluidAmount()) {
            this.prevAmount2 = this.fluidTank3.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank2", this.fluidTank3);
        }
        if (this.fluid_handler.output() != null && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid()) {
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
            if (this.fluid_handler.output() == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }


    }


    public void operate() {
        for (int i = 0; i < 1; i++) {
            operateOnce();

            this.fluid_handler.checkOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.fluid_handler.consume();
        this.fluid_handler.fillFluid();
    }


    public IMultiTileBlock getTeBlock() {
        return BlockGasChamber.primal_gas_chamber;
    }

    public BlockTileEntity getBlock() {
        return IUItem.gasChamber;
    }

    public ContainerFluidMixer getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public GuiFluidMixer getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return null;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput
        );
    }


}
