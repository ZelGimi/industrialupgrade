package com.denfop.container;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.items.EFReaderInventory;
import com.denfop.network.packet.PacketItemStackUpdate;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ContainerEFReader extends ContainerHandHeldInventory<EFReaderInventory> {

    private final IEnergyTile tile;

    public ContainerEFReader(EFReaderInventory efReaderInventory, final ItemStack itemStack1) {
        super(efReaderInventory);

        BlockPos pos = new BlockPos(ModUtils.nbt(itemStack1).getInteger("x"), ModUtils.nbt(itemStack1).getInteger("y"),
                ModUtils.nbt(itemStack1).getInteger("z")
        );
        this.tile = EnergyNetGlobal.instance.getTile(efReaderInventory.player.getEntityWorld(), pos);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        NodeStats nodeStats = EnergyNetGlobal.instance.getNodeStats(this.tile);
        for (IContainerListener crafter : this.listeners) {
            if (crafter instanceof EntityPlayerMP) {
                new PacketItemStackUpdate("energySink", nodeStats.getEnergyIn(), (EntityPlayerMP) crafter);
                new PacketItemStackUpdate("energySource", nodeStats.getEnergyOut(), (EntityPlayerMP) crafter);
            }
        }
    }

}
