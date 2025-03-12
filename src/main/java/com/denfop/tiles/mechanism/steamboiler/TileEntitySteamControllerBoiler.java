package com.denfop.tiles.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.List;

public class TileEntitySteamControllerBoiler extends TileMultiBlockBase implements IController{

    ITank waterTank;
    ITank steamTank;

    IHeater heater;

    IExchanger exchanger;
    public TileEntitySteamControllerBoiler() {
        super(InitMultiBlockSystem.SteamBoilerMultiBlock);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.isFull()){
            if (this.exchanger.isWork() && this.heater.isWork() && this.waterTank.getTank().getFluidAmount() > 0 && this.steamTank.getTank().getFluidAmount() + 2 < this.steamTank.getTank().getCapacity()){
                this.steamTank.getSteam().addEnergy(2);
                this.waterTank.getTank().drain(1,true);
                this.setActive(true);
            }else{
                this.setActive(false);
            }
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamBoiler.steam_boiler_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {

    }
    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            if (steamTank != null) {
                this.steamTank.setMainMultiElement(null);
                this.steamTank.getTank().setAcceptedFluids(Fluids.fluidPredicate());
                this.steamTank.getTank().setTypeItemSlot(InvSlot.TypeItemSlot.NONE);
                this.steamTank = null;
            }
            if (waterTank != null) {
                this.waterTank.getTank().setAcceptedFluids(Fluids.fluidPredicate());
                this.waterTank.getTank().setTypeItemSlot(InvSlot.TypeItemSlot.NONE);
                this.waterTank = null;
            }
            this.exchanger = null;
            this.heater = null;
        }


    }
    @Override
    public void updateAfterAssembly() {
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        this.exchanger = (IExchanger) this.getWorld().getTileEntity(pos1.get(0));


       pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IHeater.class
                );
        this.heater = (IHeater) this.getWorld().getTileEntity(pos1.get(0));

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.waterTank = (ITank) this.getWorld().getTileEntity(pos1.get(0));
        this.waterTank.getTank().setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
        this.waterTank.getTank().setAcceptedFluids(Fluids.fluidPredicate(FluidRegistry.WATER));
        if (this.waterTank.getTank().getFluidAmount() > 0 && this.waterTank.getTank().getFluid().getFluid() !=FluidRegistry.WATER )
            this.waterTank.getTank().drain(this.waterTank.getTank().getFluidAmount(), true);
        this.steamTank = (ITank) this.getWorld().getTileEntity(pos1.get(1));
        if (this.steamTank.getTank().getFluidAmount() > 0 && this.steamTank.getTank().getFluid().getFluid() != FluidName.fluidsteam.getInstance())
            this.steamTank.getTank().drain(this.steamTank.getTank().getFluidAmount(), true);
        this.steamTank.getTank().setTypeItemSlot(InvSlot.TypeItemSlot.OUTPUT);
        this.steamTank.getTank().setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidsteam.getInstance()));
        this.waterTank.setUnloaded();
        this.steamTank.setSteam();
    }
    @Override
    public void usingBeforeGUI() {

    }

}
