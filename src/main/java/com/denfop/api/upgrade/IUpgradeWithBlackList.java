package com.denfop.api.upgrade;

import java.util.List;

public interface IUpgradeWithBlackList extends IUpgradeItem {

    List<String> getBlackList();

    void setBlackList(boolean set);

    boolean haveBlackList();

}
