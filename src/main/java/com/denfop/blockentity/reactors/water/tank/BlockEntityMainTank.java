package com.denfop.blockentity.reactors.water.tank;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.ITank;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWaterTank;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMainTank;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.io.IOException;

public class BlockEntityMainTank extends BlockEntityMultiBlockElement implements ITank {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public int level = 0;

    public BlockEntityMainTank(int col, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        tank = this.fluids.addTank("fluidTank", col);
        tank.setCanAccept(false);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        tank.setCanAccept(this.getMain() != null && this.getMain().isFull());
        int level = (int) (3 * (this.tank.getFluidAmount() / (this.tank.getCapacity() * 1D)));
        if (level != this.level) {
            this.level = level;
            new PacketUpdateFieldTile(this, "level", this.level);
            if (level != 0) {
                this.setActive(String.valueOf(this.level));
            } else {
                this.setActive("");
            }
        }
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }

    }


    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("level")) {
            try {
                this.level = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenMainTank((ContainerMenuWaterTank) menu);
    }

    @Override
    public ContainerMenuWaterTank getGuiContainer(final Player var1) {
        return new ContainerMenuWaterTank(this, var1);
    }

    public Fluids getFluids() {
        return fluids;
    }

    public Fluids.InternalFluidTank getTank() {
        return tank;
    }

    @Override
    public void setFluid(final Fluid fluid) {
        tank.setAcceptedFluids(Fluids.fluidPredicate(fluid));
    }

}
