package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockDryer;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.*;

public class TileEntityDryer extends TileEntityInventory implements IUpgradableBlock, IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank1;
    public InvSlotOutput outputSlot;
    public short progress;

    public Map<UUID, Double> data;

    public TileEntityDryer(BlockPos pos, BlockState state) {
        super(BlockDryer.dryer, pos, state);
        this.progress = 0;

        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 5 * 1000);
        outputSlot = new InvSlotOutput(this, 1);

        this.fluid_handler = new FluidHandlerRecipe("dryer", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));

        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1.6D, 1.0D));

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 4; i++) {
            tooltip.add(Localization.translate("dryer.info" + i));
        }
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidrawlatex.getInstance().get(), 100)), new RecipeOutput(
                null,
                IUItem.latex
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhoney.getInstance().get(), 500)), new RecipeOutput(
                null,
                new ItemStack(IUItem.honeycomb.getItem())
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbeeswax.getInstance().get(), 500)), new RecipeOutput(
                null,
                new ItemStack(IUItem.beeswax.getItem())
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidplantmixture.getInstance().get(), 500)), new RecipeOutput(
                null,
                new ItemStack(IUItem.plant_mixture.getItem())
        )));

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


    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            data = PrimitiveHandler.getPlayersData(EnumPrimitive.DRYER);
            this.fluid_handler.load();
            new PacketUpdateFieldTile(this, "slot", outputSlot);
            new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, outputSlot);
            EncoderHandler.encode(customPacketBuffer, fluidTank1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (this.level.isClientSide) {
            return true;
        }
        if (!this.getWorld().isClientSide && player
                .getItemInHand(hand)
                .getCapability(
                        ForgeCapabilities.FLUID_HANDLER_ITEM,
                        null
                ).orElse((IFluidHandlerItem) player
                        .getItemInHand(hand).getItem().initCapabilities(player
                                .getItemInHand(hand), player
                                .getItemInHand(hand).getTag())) != null && this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity()) {


            ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
            if (!level.isClientSide) {
                new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
            }
            return true;
        } else {
            if (!level.isClientSide) {
                ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
            }
            outputSlot.set(0, ItemStack.EMPTY);
            if (!level.isClientSide) {
                new PacketUpdateFieldTile(this, "slot3", false);
            }
            return true;
        }
    }


    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.fluid_handler.output() != null && this.outputSlot.canAdd(this.fluid_handler
                .output()
                .getOutput().items) && this.fluid_handler.canOperate()) {

            if (!this.getActive()) {
                setActive(true);
            }

            this.progress = (short) (this.progress + 1 + (data.getOrDefault(getComponentPrivate().getPlayersUUID().get(0), 0.0) / 20));
            double k = this.progress;

            if (this.progress >= 100) {
                operate();
                if (!this.getWorld().isClientSide)
                    PrimitiveHandler.addExperience(EnumPrimitive.DRYER, 0.25,
                            getComponentPrivate().getPlayersUUID().get(0));
                this.progress = 0;
            }
        } else {


            if (this.fluid_handler.output() == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }

    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("slot")) {
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
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
            outputSlot.set(0, ItemStack.EMPTY);
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
        this.outputSlot.add(this.fluid_handler.output().getOutput().items);
        new PacketUpdateFieldTile(this, "slot", outputSlot);
        new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockDryer.dryer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.dryer.getBlock();
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput, UpgradableProperty.ItemExtract
        );
    }


}
