package com.denfop.blockentity.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCyclotronEntity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCyclotronCryogen;
import com.denfop.screen.ScreenCyclotronCryogen;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class BlockEntityCyclotronCryogen extends BlockEntityMultiBlockElement implements ICryogen {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public BlockEntityCyclotronCryogen(BlockPos pos, BlockState state) {
        super(BlockCyclotronEntity.cyclotron_cryogen, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidcryogen.getInstance().get()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuCyclotronCryogen getGuiContainer(final Player var1) {
        return new ContainerMenuCyclotronCryogen(this, var1);
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
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCyclotronCryogen((ContainerMenuCyclotronCryogen) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCyclotronEntity.cyclotron_cryogen;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }

    @Override
    public FluidTank getCryogenTank() {
        return fluidTank;
    }

}
