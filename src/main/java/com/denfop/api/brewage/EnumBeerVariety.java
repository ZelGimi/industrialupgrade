package com.denfop.api.brewage;


import net.minecraft.util.Tuple;

import java.util.LinkedList;
import java.util.List;

public enum EnumBeerVariety {
    SOUP(10, 0, 9, 1),
    WHITE(7, 3),
    PREFIXLESS(6, 4, 5, 5, 4, 6),
    DARK(3, 7),
    BLACK(2, 8),
    BLACKSTUFF(1, 9, 0, 10);

    final List<Tuple<Integer, Integer>> ratioOfComponents = new LinkedList<>();

    EnumBeerVariety(int... components) {
        for (int i = 0; i < components.length / 2; i++) {
            ratioOfComponents.add(new Tuple<>(components[i * 2], components[i * 2 + 1]));
        }
    }

    public static EnumBeerVariety getBeerVarietyFromRatio(int wheat, int hop) {
        for (EnumBeerVariety enumBeerVariety : values()) {
            List<Tuple<Integer, Integer>> pairs = enumBeerVariety.getRatioOfComponents();
            for (Tuple<Integer, Integer> combination : pairs) {
                if (combination.getFirst() == wheat && combination.getSecond() == hop) {
                    return enumBeerVariety;
                }
            }
        }
        return EnumBeerVariety.SOUP;
    }

    public List<Tuple<Integer, Integer>> getRatioOfComponents() {
        return ratioOfComponents;
    }
}
