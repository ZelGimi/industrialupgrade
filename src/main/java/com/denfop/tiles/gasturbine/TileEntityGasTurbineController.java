package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.container.ContainerGasTurbineController;
import com.denfop.gui.GuiGasTurbine;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityGasTurbineController extends TileMultiBlockBase implements IUpdatableTileEvent, IController, IHasRecipe {

    public static Map<Fluid, Integer> gasMapValue = new HashMap<>();
    public ISocket energy;
    public ITank tank;
    public boolean work;
    Fluid momentGas;
    List<IRecuperator> recuperators = new ArrayList<>();
    int generate = 0;

    public TileEntityGasTurbineController() {
        super(InitMultiBlockSystem.GasTurbineMultiBlock);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (full) {
            if (tank.getTank().getFluid() != null && work) {
                FluidStack stack = tank.getTank().getFluid();
                if (stack.getFluid() != momentGas) {
                    momentGas = stack.getFluid();
                    generate = gasMapValue.get(stack.getFluid());
                }
                double coef = 0;
                boolean canWork = true;
                for (IRecuperator recuperator : recuperators) {
                    if (recuperator.getExchanger().isEmpty()) {
                        canWork = false;
                        break;
                    } else {
                        coef += recuperator.getPower();
                    }
                }
                coef = coef / 4;
                if (canWork && energy.getEnergy().getFreeEnergy() >= generate * coef) {
                    tank.getTank().drain(1, true);
                    energy.getEnergy().addEnergy(generate * coef);
                    if (this.getWorld().getWorldTime() % 20 == 0) {
                        for (IRecuperator recuperator : recuperators) {
                            ((IExchangerItem) recuperator.getExchanger().get().getItem()).damageItem(recuperator
                                    .getExchanger()
                                    .get(), -1);
                        }
                    }
                    energy.getEnergy().setSourceTier(EnergyNetGlobal.instance.getTierFromPower(generate * coef) + 1);
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("work", work);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        customPacketBuffer.writeBytes(this.energy.getEnergy().updateComponent());
        try {
            EncoderHandler.encode(customPacketBuffer, this.tank.getTank());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        try {
            this.energy.getEnergy().onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            final FluidTank fluidTank = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank != null) {
                this.tank.getTank().readFromNBT(fluidTank.writeToNBT(new NBTTagCompound()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ContainerGasTurbineController getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerGasTurbineController(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGasTurbine(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        this.work = !this.work;
    }

    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.energy = (ISocket) this.getWorld().getTileEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRecuperator.class
                );
        for (BlockPos pos : pos1) {
            this.recuperators.add((IRecuperator) world.getTileEntity(pos));
        }

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.tank = (ITank) this.getWorld().getTileEntity(pos1.get(0));
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.energy = null;
            this.recuperators.clear();
            this.tank = null;
        }
    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public void init() {
        gasMapValue.put(FluidName.fluidgas.getInstance(), 250);
        gasMapValue.put(FluidName.fluidmethane.getInstance(), 140);
        gasMapValue.put(FluidName.fluidpropane.getInstance(), 65);
        gasMapValue.put(FluidName.fluidacetylene.getInstance(), 10);
        gasMapValue.put(FluidName.fluidethylene.getInstance(), 40);
        gasMapValue.put(FluidName.fluidethane.getInstance(), 52);
        gasMapValue.put(FluidName.fluidbenzene.getInstance(), 35);
        gasMapValue.put(FluidName.fluidcyclohexane.getInstance(), 70);
        gasMapValue.put(FluidName.fluidbiogas.getInstance(), 15);
        gasMapValue.put(FluidName.fluidpropylene.getInstance(), 70);
        gasMapValue.put(FluidName.fluidbutadiene.getInstance(), 110);
        gasMapValue.put(FluidName.fluidbutene.getInstance(), 120);
        gasMapValue.put(FluidName.fluidtertbutylmethylether.getInstance(), 250);
    }

}
