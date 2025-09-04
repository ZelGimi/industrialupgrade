package com.denfop.villager;

import net.minecraftforge.eventbus.api.Event;

public class EventTradeVillager extends Event {
    private final Profession profession;

    public EventTradeVillager(Profession profession) {
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
    }
}
