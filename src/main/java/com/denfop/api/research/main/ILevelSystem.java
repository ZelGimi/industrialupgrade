package com.denfop.api.research.main;

public interface ILevelSystem {

    int getLevel(EnumLeveling enumLeveling);

    void setLevel(EnumLeveling enumLeveling, int level);

    void addLevel(EnumLeveling enumLeveling, int level);

    int getLevelPoint(EnumLeveling enumLeveling);

    double getBar(EnumLeveling enumLeveling, int length);

    void setOwnBaseLevel(EnumLeveling enumLeveling, int level);

}
