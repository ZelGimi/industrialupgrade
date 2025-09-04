package com.denfop.api.container;

import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface CustomWorldContainer extends Container, MenuProvider {

    Level getWorld();
    void addInventorySlot(Inventory var1);

    int getBaseIndex(Inventory var1);

    MenuType<?> getMenuType();

    int getContainerId();

    ContainerMenuBase<?> getGuiContainer(Player var1);

    @OnlyIn(Dist.CLIENT)
    ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> var2);

}
