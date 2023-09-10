package com.denfop.api;

import com.denfop.Localization;

public interface IItemSoon {

    default String getDescription() {
        return Localization.translate("iu.soon");
    }

}
