package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Fluids;
import com.denfop.network.DecoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.io.IOException;
import java.util.List;

public class TileEntityAutoLatexCollector extends TileEntityInventory {
    private final Fluids fluids;
    public final Fluids.InternalFluidTank tank;

    public TileEntityAutoLatexCollector(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.auto_latex_collector, pos, state);
        fluids = this.addComponent(new Fluids(this));
        tank = fluids.addTankExtract("tank", 64 * 100);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("auto_latex_collector.info"));
    }

    @Override
    public boolean canPlace(TileEntityBlock te, BlockPos pos, Level world, Direction direction, LivingEntity entity) {
        BlockState state = world.getBlockState(pos.offset(direction.getOpposite().getNormal()));
        if (state.getBlock() == IUItem.swampRubWood.getBlock().get()){
            BlockSwampRubWood.RubberWoodState rwState = state.getValue(BlockSwampRubWood.stateProperty);
            return  (!rwState.isPlain() && rwState.facing == direction);
        }
        if (state.getBlock() == IUItem.tropicalRubWood.getBlock().get()){
            BlockTropicalRubWood.RubberWoodState rwState = state.getValue(BlockTropicalRubWood.stateProperty);
            return  (!rwState.isPlain() && rwState.facing == direction);
        }
        if (state.getBlock() == IUItem.rubWood.getBlock().get()){
            BlockRubWood.RubberWoodState rwState = state.getValue(BlockRubWood.stateProperty);
            return  (!rwState.isPlain() && rwState.facing == direction);
        }
        return false;

    }

    boolean work = true;

    @Override
    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (neighborPos.equals(pos.offset(getFacing().getOpposite().getNormal()))) {
            BlockState state = neighbor;
            work = state.getBlock() == IUItem.swampRubWood.getBlock().get() || state.getBlock() == IUItem.tropicalRubWood.getBlock().get() || state.getBlock() == IUItem.rubWood.getBlock().get();

        }
    }
    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }
    @Override
    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("tank")){
            try {
                this.tank.setFluid((FluidStack) DecoderHandler.decode(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    BlockState state;

    @Override
    public void onLoaded() {
        super.onLoaded();
        BlockState state = level.getBlockState(pos.offset(getFacing().getOpposite().getNormal()));
        work = state.getBlock() == IUItem.swampRubWood.getBlock().get() || state.getBlock() == IUItem.tropicalRubWood.getBlock().get() || state.getBlock() == IUItem.rubWood.getBlock().get();
        if (!this.level.isClientSide)
            new PacketUpdateFieldTile(this,"tank",tank.getFluid());
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0 && work) {
            BlockState state = level.getBlockState(pos.offset(getFacing().getOpposite().getNormal()));
            BlockPos pos1 = pos.offset(getFacing().getOpposite().getNormal());
            if (state.getBlock() == IUItem.swampRubWood.getBlock().get()) {
                BlockSwampRubWood.RubberWoodState rwState = state.getValue(BlockSwampRubWood.stateProperty);
                if (rwState.wet) {
                    level.setBlock(pos1, state.setValue(BlockSwampRubWood.stateProperty, rwState.getDry()), 3);
                    tank.fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100*(level.random.nextInt(3) + 1)), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if (state.getBlock() == IUItem.tropicalRubWood.getBlock().get()) {
                BlockTropicalRubWood.RubberWoodState rwState = state.getValue(BlockTropicalRubWood.stateProperty);
                if (rwState.wet) {
                    level.setBlock(pos1, state.setValue(BlockTropicalRubWood.stateProperty, rwState.getDry()), 3);
                    tank.fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100*(level.random.nextInt(3) + 1)), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if (state.getBlock() == IUItem.rubWood.getBlock().get()) {
                BlockRubWood.RubberWoodState rwState = state.getValue(BlockRubWood.stateProperty);
                if (rwState.wet) {
                    level.setBlock(pos1, state.setValue(BlockRubWood.stateProperty, rwState.getDry()), 3);
                    tank.fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100*(level.random.nextInt(3) + 1)), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            new PacketUpdateFieldTile(this,"tank",tank.getFluid());
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.auto_latex_collector;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
