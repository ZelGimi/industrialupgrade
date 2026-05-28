package com.denfop.blockentity.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockChemicalPlantEntity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDefaultMultiElement;
import com.denfop.screen.ScreenChemicalWaste;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;

public class BlockEntityChemicalPlantWaste extends BlockEntityMultiBlockElement implements IWaste {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public BlockEntityChemicalPlantWaste(BlockPos pos, BlockState state) {
        super(BlockChemicalPlantEntity.chemical_plant_waste, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidcryogen.getInstance().get()));
    }
    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }
    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockChemicalPlantEntity.chemical_plant_waste;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant.getBlock(getTeBlock().getId());
    }

    @Override
    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuDefaultMultiElement getGuiContainer(final Player var1) {
        return new ContainerMenuDefaultMultiElement(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenChemicalWaste((ContainerMenuDefaultMultiElement) var2);
    }

}
