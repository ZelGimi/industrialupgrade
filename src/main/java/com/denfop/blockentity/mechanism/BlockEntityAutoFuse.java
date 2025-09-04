package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.containermenu.ContainerMenuAutoFuse;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenAutoFuse;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BlockEntityAutoFuse extends BlockEntityInventory {

    public final Inventory slotBomb;
    public final ComponentBaseEnergy rad_energy;
    private final Redstone redstone;
    public boolean fuse = false;
    public int timer = 60;
    private boolean boom = false;

    public BlockEntityAutoFuse(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.autofuse, pos, state);
        this.slotBomb = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.nuclear_bomb.getItem();
            }
        };
        this.rad_energy = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.RADIATION, this, 100000));
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
                                    @Override
                                    public void action(final int input) {
                                        fuse = !fuse;
                                        new PacketUpdateFieldTile(getParent(), "fuse", fuse);
                                    }

                                }
        );
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.autofuse.info"));
    }

    private BlockEntityInventory getParent() {
        return this;
    }
    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.autofuse;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.fuse = customPacketBuffer.readBoolean();
        this.timer = customPacketBuffer.readInt();
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.fuse);
        customPacketBuffer.writeInt(this.timer);
        return customPacketBuffer;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenAutoFuse((ContainerMenuAutoFuse) menu);
    }

    @Override
    public ContainerMenuAutoFuse getGuiContainer(final Player var1) {
        return new ContainerMenuAutoFuse(this, var1);
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("timer")) {
            is.readUnsignedByte();
            this.timer = is.readInt();
            if (this.timer == 0) {
                this.boom = true;
            }
        }
        if (name.equals("fuse")) {
            is.readUnsignedByte();
            this.fuse = is.readBoolean();

        }
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putBoolean("fuse", fuse);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        fuse = nbtTagCompound.getBoolean("fuse");
    }

    @Override
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.fuse && !this.slotBomb.isEmpty()) {
            if (timer > 0) {
                timer--;
                if (timer == 0) {
                    if (boom) {
                        boom = false;
                        this.level.addParticle(ParticleTypes.EXPLOSION,
                                this.worldPosition.getX() + 0.5D,
                                this.worldPosition.getY() + 1.0D,
                                this.worldPosition.getZ() + 0.5D,
                                0.0D, 0.0D, 0.0D);
                    }
                    this.timer = 60;
                } else {
                    this.level.addParticle(ParticleTypes.SMOKE,
                            this.worldPosition.getX() + 0.5D,
                            this.worldPosition.getY() + 0.5D,
                            this.worldPosition.getZ() + 0.5D,
                            0.0D, 0.0D, 0.0D);
                    this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                            this.worldPosition.getX() + 0.5D,
                            this.worldPosition.getY() + 0.5D,
                            this.worldPosition.getZ() + 0.5D,
                            0.0D, 0.0D, 0.0D);
                }
            }

        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.setActive(this.fuse);
        if (this.fuse && !this.slotBomb.isEmpty()) {
            if (timer >= 0) {
                if (timer != 0) {
                    timer--;
                }
                if (timer % 10 == 0) {
                    new PacketUpdateFieldTile(this, "timer", timer);
                }
                if (timer == 0) {
                    this.rad_energy.addEnergy(300);
                    this.slotBomb.get(0).shrink(1);
                    timer = 60;
                }


            }
        }
    }

}
