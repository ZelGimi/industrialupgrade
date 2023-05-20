package com.denfop.network;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class TeUpdateDataServer {

    private final Set<String> globalFields = new HashSet<>();
    private final Map<EntityPlayerMP, Set<String>> playerFieldMap = new IdentityHashMap<>();

    public TeUpdateDataServer() {
    }

    public Map<EntityPlayerMP, Set<String>> getPlayerFieldMap() {
        return playerFieldMap;
    }

    void addGlobalField(String name) {
        if (this.globalFields.add(name)) {
            if (!this.playerFieldMap.isEmpty()) {

                for (final Set<String> strings : this.playerFieldMap.values()) {
                    strings.remove(name);
                }
            }

        }
    }

    void addPlayerField(String name, EntityPlayerMP player) {
        if (!this.globalFields.contains(name)) {
            Set<String> playerFields = this.playerFieldMap.computeIfAbsent(player, k -> new HashSet<>());

            (playerFields).add(name);
        }
    }

    Collection<String> getGlobalFields() {
        return this.globalFields;
    }

    Collection<String> getPlayerFields(EntityPlayerMP player) {
        Set<String> ret = this.playerFieldMap.get(player);
        return (ret == null ? Collections.emptyList() : ret);
    }

}
