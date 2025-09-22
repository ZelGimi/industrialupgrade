package com.denfop.tiles.bee;

import com.denfop.api.item.IDamageItem;

public interface IFrameItem extends IDamageItem {

    FrameAttributeLevel getAttribute(int meta);

}
