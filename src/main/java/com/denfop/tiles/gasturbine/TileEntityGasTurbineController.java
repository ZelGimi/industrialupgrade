package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGasTurbineController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGasTurbine;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

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
    private List<BlockPos> posesBearings;

    public TileEntityGasTurbineController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.GasTurbineMultiBlock, BlockGasTurbine.gas_turbine_controller, pos, state);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (full) {
            if (!tank.getTank().getFluid().isEmpty() && work) {
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
                    tank.getTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
                    energy.getEnergy().addEnergy(generate * coef);
                    if (this.getWorld().getGameTime() % 4 == 0) {
                        spawnExhaustParticles(level);
                    }
                    if (this.getWorld().getGameTime() % 20 == 0) {
                        for (IRecuperator recuperator : recuperators) {
                            ((IExchangerItem) recuperator.getExchanger().get(0).getItem()).damageItem(recuperator
                                    .getExchanger()
                                    .get(0), -1);
                        }
                    }
                    energy.getEnergy().setSourceTier(EnergyNetGlobal.instance.getTierFromPower(generate * coef) + 1);
                }
            }
        }
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
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
                this.tank.getTank().readFromNBT(customPacketBuffer.registryAccess(), fluidTank.writeToNBT(customPacketBuffer.registryAccess(), new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ContainerGasTurbineController getGuiContainer(final Player entityPlayer) {
        return new ContainerGasTurbineController(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiGasTurbine((ContainerGasTurbineController) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        this.work = !this.work;
    }

    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.energy = (ISocket) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRecuperator.class
                );
        for (BlockPos pos : pos1) {
            this.recuperators.add((IRecuperator) getWorld().getBlockEntity(pos));
        }

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.tank = (ITank) this.getWorld().getBlockEntity(pos1.get(0));
        this.posesBearings = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IAirBearings.class
                );
    }

    public void spawnExhaustParticles(Level level) {
        if (!(level instanceof ServerLevel server)) return;
        List<BlockPos> exhausts = posesBearings;

        for (BlockPos pos : exhausts) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;


            Vec3 motion = Vec3.atLowerCornerOf(getFacing().getOpposite().getNormal());

            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 0, motion.x, motion.y, motion.z, 0.2);
        }
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
        gasMapValue.put(FluidName.fluidgas.getInstance().get(), 250);
        gasMapValue.put(FluidName.fluidmethane.getInstance().get(), 140);
        gasMapValue.put(FluidName.fluidpropane.getInstance().get(), 65);
        gasMapValue.put(FluidName.fluidacetylene.getInstance().get(), 10);
        gasMapValue.put(FluidName.fluidethylene.getInstance().get(), 40);
        gasMapValue.put(FluidName.fluidethane.getInstance().get(), 52);
        gasMapValue.put(FluidName.fluidbenzene.getInstance().get(), 35);
        gasMapValue.put(FluidName.fluidcyclohexane.getInstance().get(), 70);
        gasMapValue.put(FluidName.fluidbiogas.getInstance().get(), 15);
        gasMapValue.put(FluidName.fluidpropylene.getInstance().get(), 70);
        gasMapValue.put(FluidName.fluidbutadiene.getInstance().get(), 110);
        gasMapValue.put(FluidName.fluidbutene.getInstance().get(), 120);
        gasMapValue.put(FluidName.fluidtertbutylmethylether.getInstance().get(), 250);
    }

}
