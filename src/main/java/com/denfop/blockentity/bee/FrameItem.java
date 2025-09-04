package com.denfop.blockentity.bee;

import com.denfop.api.item.DamageItem;

public interface FrameItem extends DamageItem {

    FrameAttributeLevel getAttribute(int meta);
}
