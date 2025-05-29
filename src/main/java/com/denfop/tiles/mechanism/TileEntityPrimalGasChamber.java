package com.denfop.tiles.mechanism;

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
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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
    @OnlyIn(Dist.CLIENT)
    public DataFluid dataFluid;
    @OnlyIn(Dist.CLIENT)
    public DataFluid dataFluid1;
    @OnlyIn(Dist.CLIENT)
    public DataFluid dataFluid2;
    protected short progress;
    protected double guiProgress;
    private int prevAmount;
    private int prevAmount1;
    private int prevAmount2;

    public TileEntityPrimalGasChamber(BlockPos pos, BlockState state) {
        super(0, 0, 0, BlockGasChamber.primal_gas_chamber, pos, state);
        this.progress = 0;
        this.defaultOperationLength = this.operationLength = 600;

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 12 * 1000);


        this.fluidTank2 = fluids.addTankInsert("fluidTank2", 12 * 1000);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
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

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(-0.05D, 0.0D, -0.05D, 1.05D, 2D, 1.05D));

    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance().get(),
                        300
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuroxide.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidacetylene.getInstance().get(), 400), new FluidStack(
                FluidName.fluidhyd.getInstance().get(),
                1000
        )), Collections.singletonList(new FluidStack(FluidName.fluidethylene.getInstance().get(), 400))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfuroxide.getInstance().get(), 200),
                new FluidStack(
                        FluidName.fluidoxy.getInstance().get(),
                        100
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 200))));

        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 500),
                new FluidStack(
                        FluidName.fluidsteam.getInstance().get(),
                        500
                )
        ), Collections.singletonList(new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 500))));
        Recipes.recipes.getRecipeFluid().addRecipe("gas_chamber", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidpropane.getInstance().get(), 400), new FluidStack(
                FluidName.fluidbromine.getInstance().get(),
                800
        )), Collections.singletonList(new FluidStack(FluidName.fluiddibromopropane.getInstance().get(), 400))));

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
        if (!this.level.isClientSide()) {
            this.fluid_handler.load();
        }
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
            FluidTank fluidTank3 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank3 != null) {
                this.fluidTank3.readFromNBT(fluidTank3.writeToNBT(new CompoundTag()));
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
        if (name.equals("fluidtank2")) {
            try {
                FluidTank fluidTank3 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank3 != null) {
                    this.fluidTank3.readFromNBT(fluidTank3.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && player
                .getItemInHand(hand)
                .getCapability(
                        ForgeCapabilities.FLUID_HANDLER_ITEM,
                        null
                ).orElse((IFluidHandlerItem) player
                        .getItemInHand(hand).getItem().initCapabilities(player
                                .getItemInHand(hand), player
                                .getItemInHand(hand).getTag())) != null) {


            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {

            return super.onActivated(player, hand, side, vec3);
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
            if (this.level.getGameTime() % 20 == 0) {
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
        return IUItem.gasChamber.getBlock();
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput
        );
    }


}
