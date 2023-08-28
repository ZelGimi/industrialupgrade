package com.denfop.api.space.upgrades;

import com.denfop.api.space.rovers.EnumTypeUpgrade;

public class SpaceUpgradeItemInform {

    public final EnumTypeUpgrade upgrade;
    public final int number;

    public SpaceUpgradeItemInform(EnumTypeUpgrade modules, int number) {
        this.upgrade = modules;
        this.number = number;

    }

    public boolean matched(EnumTypeUpgrade modules) {
        return this.upgrade.getUpgrade().equals(modules.getUpgrade());
    }

    public int getInformation(EnumTypeUpgrade modules) {
        if (this.upgrade.getUpgrade().equals(modules.getUpgrade())) {
            return this.number;
        }
        return 0;
    }

    public String getName() {
        switch (this.upgrade) {


        }
        return "";
    }


}
