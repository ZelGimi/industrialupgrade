package com.denfop.api.upgrade;

import com.denfop.items.EnumInfoUpgradeModules;

import java.util.List;

public interface IUpgradeItem {

    List<EnumInfoUpgradeModules> getUpgradeModules();
}
