package com.denfop.items;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerVeinSensor;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiVeinSensor;
import com.denfop.utils.Vector2;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

public class ItemStackVeinSensor extends ItemStackInventory {


    public final ItemStack itemStack1;
    private final Map<Integer, Map<Vector2, DataOres>> map;
    public Vector2 vector;


    public ItemStackVeinSensor(
            Player player, ItemStack stack, final Map<Integer, Map<Vector2, DataOres>> map,
            final Vector2 vector2
    ) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        this.map = map;
        this.vector = vector2;
    }

    public Vector2 getVector() {
        return vector;
    }

    public Map<Integer, Map<Vector2, DataOres>> getMap() {
        return map;
    }


    public ContainerBase<ItemStackVeinSensor> getGuiContainer(Player player) {
        return new ContainerVeinSensor(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        return new GuiVeinSensor((ContainerVeinSensor) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


}
