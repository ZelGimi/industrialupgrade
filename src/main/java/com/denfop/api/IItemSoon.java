package com.denfop.api;

import ic2.core.init.Localization;

public interface IItemSoon {
      default String getDescription(){
          return Localization.translate("iu.soon");
      }
}
