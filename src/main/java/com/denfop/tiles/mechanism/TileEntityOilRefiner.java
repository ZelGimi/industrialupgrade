package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerOilRefiner;
import com.denfop.gui.GuiOilRefiner;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityBaseLiquedMachine;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileEntityOilRefiner extends TileEntityBaseLiquedMachine implements IManufacturerBlock {

    public TileEntityOilRefiner() {
        super(24000, 14, 2, 3, new boolean[]{false, true, true}, new boolean[]{true, false, false},
                new Fluid[]{FluidName.fluidneft.getInstance(), FluidName.fluidbenz.getInstance(),
                        FluidName.fluiddizel.getInstance()}
        );

    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.oilrefiner);
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @Override
    public void onNetworkUpdate(String field) {

    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public ContainerBase<TileEntityOilRefiner> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerOilRefiner(entityPlayer, this);

    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public double gaugeLiquidScaled(double i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public double gaugeLiquidScaled1(double i) {
        return this.getFluidTank(1).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(1).getFluidAmount() * i / this.getFluidTank(1).getCapacity();
    }

    public double gaugeLiquidScaled2(double i) {
        return this.getFluidTank(2).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(2).getFluidAmount() * i / this.getFluidTank(2).getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiOilRefiner(new ContainerOilRefiner(entityPlayer, this));
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        boolean drain = false;
        boolean drain1 = false;

        if (this.getFluidTank(0).getFluidAmount() >= 5 && this.energy.getEnergy() >= 25) {
            int size = this.getFluidTank(0).getFluidAmount() / 5;
            size = Math.min(this.level + 1, size);
            int cap = this.fluidTank[1].getCapacity() - this.fluidTank[1].getFluidAmount();
            cap /= 3;
            cap = Math.min(cap, size);
            int cap1 = this.fluidTank[2].getCapacity() - this.fluidTank[2].getFluidAmount();
            cap1 /= 2;
            cap1 = Math.min(cap1, size);
            if (this.fluidTank[1].getCapacity() - this.fluidTank[1].getFluidAmount() >= 3) {
                fill(new FluidStack(FluidName.fluidbenz.getInstance(), cap * 3), true);
                drain = true;

            }
            if (this.fluidTank[2].getCapacity() - this.fluidTank[2].getFluidAmount() >= 2) {
                fill(new FluidStack(FluidName.fluiddizel.getInstance(), cap1 * 2), true);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = 0;
                drains = drain ? drains + 3 * cap : drains;
                drains = drain1 ? drains + 2 * cap1 : drains;

                this.getFluidTank(0).drain(drains, true);
                if (!this.getActive()) {
                    initiate(0);
                    setActive(true);
                }
                this.useEnergy(25);
            } else {
                if (!this.getActive()) {
                    initiate(2);
                    setActive(false);
                }
            }
        }

        if (this.world.provider.getWorldTime() % 20 == 0) {
            boolean need = false;
            for (int i = 0; i < this.fluidTank.length; i++) {
                if (this.fluidTank[i].getFluidAmount() != this.old_amount[i]) {
                    this.old_amount[i] = this.fluidTank[i].getFluidAmount();
                    need = true;
                }
            }
            if (need) {
                IC2.network.get(true).updateTileEntityField(this, "fluidTank");
            }
        }


    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 25 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);

    }

    public String getStartSoundFile() {
        return "Machines/oilrefiner.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

}
