package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockDryer;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerOilPurifier;
import com.denfop.gui.GuiOilPurifier;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TileEntityDryer extends TileEntityInventory implements IUpgradableBlock, IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank1;
    public InvSlotOutput outputSlot;
    public short progress;

    public Map<UUID,Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.DRYER);
    public TileEntityDryer() {
        super();
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

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.6D, 1.0D));

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
                new FluidStack(FluidName.fluidrawlatex.getInstance(), 100)), new RecipeOutput(
                null,
                IUItem.latex
        )));

        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhoney.getInstance(), 500)), new RecipeOutput(
                null,
               new ItemStack(IUItem.honeycomb)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidbeeswax.getInstance(), 500)), new RecipeOutput(
                null,
                new ItemStack(IUItem.beeswax)
        )));
        Recipes.recipes.getRecipeFluid().addRecipe("dryer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidplantmixture.getInstance(), 500)), new RecipeOutput(
                null,
                new ItemStack(IUItem.plant_mixture)
        )));

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


    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
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
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (this.world.isRemote) {
            return true;
        }
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                        null
                ) && this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity()) {
            ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
            if (!world.isRemote) {
                new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
            }
            return true;
        } else {
            if (!outputSlot.isEmpty()) {
                if (!world.isRemote) {
                    ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                }
                outputSlot.put(0, ItemStack.EMPTY);
                if (!world.isRemote) {
                    new PacketUpdateFieldTile(this, "slot3", false);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
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

            this.progress = (short) (this.progress + 1 + (data.getOrDefault(getComponentPrivate().getPlayersUUID().get(0),0.0)/20));
            double k = this.progress;

            if (this.progress >= 100) {
                operate();
                if (!this.getWorld().isRemote)
                PrimitiveHandler.addExperience(EnumPrimitive.DRYER,0.25,
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
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
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
            outputSlot.put(0, ItemStack.EMPTY);
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
        return IUItem.dryer;
    }

    public ContainerOilPurifier getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public GuiOilPurifier getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return null;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput, UpgradableProperty.ItemExtract
        );
    }


}
