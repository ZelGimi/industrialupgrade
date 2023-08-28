package com.denfop.items.reactors;

import com.denfop.api.reactors.IAdvReactor;
import net.minecraft.item.ItemStack;

public interface IBaseReactorComponent {

    boolean canBePlacedIn(ItemStack var1, IAdvReactor var2);

}
