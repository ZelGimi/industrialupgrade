package com.denfop.container;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.items.EFReaderInventory;
import com.denfop.network.packet.PacketItemStackUpdate;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ContainerEFReader extends ContainerHandHeldInventory<EFReaderInventory> {

    private final IEnergyTile tile;
    private final int current;

    public ContainerEFReader(EFReaderInventory efReaderInventory, final ItemStack itemStack1, Player player) {
        super(efReaderInventory, null);
        this.player = player;
        this.inventory = player.getInventory();
        BlockPos pos = new BlockPos(ModUtils.nbt(itemStack1).getInt("x"), ModUtils.nbt(itemStack1).getInt("y"),
                ModUtils.nbt(itemStack1).getInt("z")
        );
        this.current = player.getInventory().selected;
        this.tile = EnergyNetGlobal.instance.getTile(efReaderInventory.player.level(), pos);
    }

    @Override
    public void clicked(int slot, int button, ClickType type, Player player) {
        if (slot >= 0 && slot < this.slots.size())
            if (this.base.isThisContainer(this.slots.get(slot).getItem()))
                return;
        boolean closeGUI;
        closeGUI = false;
        label82:
        switch (type) {
            case CLONE:
            case PICKUP_ALL:
            case QUICK_CRAFT:
                break;
            case PICKUP:
            case THROW:
                if (slot >= 0 && slot < this.slots.size()) {
                    closeGUI = this.base.isThisContainer(this.slots.get(slot).getItem());
                }
                break;
            case QUICK_MOVE:
                if (slot >= 0 && slot < this.slots.size() && this.base.isThisContainer(this.slots.get(
                                slot)
                        .getItem())) {
                    this.setCarried(ModUtils.emptyStack);
                }
                break;
            case SWAP:

                if (button == current) {
                    this.setCarried(ItemStack.EMPTY);
                    return;
                }
                assert this.getSlotFromInventory(player.getInventory(), button) != null;

                boolean swapOut = this.base.isThisContainer(this.getSlotFromInventory(player.getInventory(), button).getItem());
                boolean swapTo = this.base.isThisContainer(this.slots.get(slot).getItem());
                if (swapOut || swapTo) {
                    for (int i = 0; i < 9; ++i) {
                        if (swapOut && slot == Objects.requireNonNull(this.getSlotFromInventory(
                                player.getInventory(),
                                i
                        )).index || swapTo && button == i) {
                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.send(new ClientboundSetCarriedItemPacket(i));
                            }
                            break label82;
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException("Unexpected ClickType: " + type);
        }
        ItemStack stack = this.slotClick1(slot, button, type, player);
        if (closeGUI && !player.level().isClientSide()) {
            this.base.saveAsThrown(stack);
            player.closeContainer();
        } else if (type == ClickType.CLONE) {
            ItemStack held = player.getInventory().getSelected();
            if (this.base.isThisContainer(held)) {
                held.getTag().remove("uid");
            }
        }

        this.setCarried(stack);
    }


    public ItemStack slotClick1(int slotId, int dragType, ClickType clickType, Player player) {
        super.clicked(
                slotId,
                dragType,
                clickType,
                player
        );
        return getCarried();
    }


    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        NodeStats nodeStats = EnergyNetGlobal.instance.getNodeStats(this.tile,this.player.level());

        if (player instanceof ServerPlayer) {
            new PacketItemStackUpdate("energySink", nodeStats.getEnergyIn(), (ServerPlayer) player);
            new PacketItemStackUpdate("energySource", nodeStats.getEnergyOut(), (ServerPlayer) player);
        }

    }

}
