package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamPeatGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamPeatGenerator;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.List;

public class BlockEntitySteamPeatGenerator extends BlockEntityElectricMachine implements IType {


    public final Inventory slot;
    public final Fluids.InternalFluidTank fluidTank1;
    public final ComponentSteamEnergy steam;
    public FluidTank fluidTank;
    public Fluids fluids;


    public int fuel = 0;

    public BlockEntitySteamPeatGenerator(BlockPos pos, BlockState state) {
        super(0, 1, 0, BlockBaseMachine3Entity.steam_peat_generator, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.cultivated_peat_balls.getItem();
            }
        };
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 4000, Inventory.TypeItemSlot.INPUT, Fluids.fluidPredicate(
                Fluids.WATER
        ));
        this.fluidTank1 = this.fluids.addTank("fluidTank1", 4000, Inventory.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSource(this, 4000));
        this.steam.setFluidTank(fluidTank1);
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            ModUtils.showFlames(this.getWorld(), this.pos, this.getFacing());
        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            fuel = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steam_peat_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, fuel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }


    }

    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.slot.isEmpty()) {
            if (fuel == 0) {
                fuel = 500;
                this.slot.get(0).shrink(1);
                if (!this.getActive()) {
                    this.setActive(true);
                }
            }
        }
        if (fuel == 0) {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        for (int i = 0; i < ComponentSteamEnergy.speedGeneration;i++)
        if (fuel > 0 &&
                !this.fluidTank.getFluid().isEmpty() && this.fluidTank.getFluid().getAmount() >= 4 && this.steam.getEnergy() + 4 <= this.steam.getCapacity()) {
            this.steam.addEnergy(4);
            this.fluidTank.drain(4, IFluidHandler.FluidAction.EXECUTE);
            this.setActive(true);
            fuel = Math.max(0, this.fuel - 1);
        } else {
            setActive(false);
        }


    }

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {


            return (int) Math.min(this.fuel * i / 500D, i);
        }
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fuel = nbttagcompound.getInt("fuel");
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("fuel", this.fuel);
        return nbt;
    }

    public ContainerMenuSteamPeatGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSteamPeatGenerator(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamPeatGenerator((ContainerMenuSteamPeatGenerator) menu);
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
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
