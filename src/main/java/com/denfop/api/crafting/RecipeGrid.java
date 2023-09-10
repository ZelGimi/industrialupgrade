package com.denfop.api.crafting;

import java.util.ArrayList;
import java.util.List;

public class RecipeGrid {


    private final List<List<String>> grids;

    public RecipeGrid(List<String> args) {

        while (3 - args.size() > 0) {
            args.add("   ");
        }


        for (int i = 0; i < 3; i++) {
            final StringBuilder stringBuilder = new StringBuilder(args.get(i));
            while (stringBuilder.length() < 3) {
                stringBuilder.append(" ");
            }
            args.set(i, stringBuilder.toString());
        }
        int empty = 0;
        for (int i = args.size() - 1; i >= 0; i--) {
            if (args.get(i).equals("   ")) {
                empty++;
            } else {
                break;
            }
        }
        ArrayList<String> listCopy = new ArrayList<>(args);
        List<List<String>> list = new ArrayList<>();
        list.add(args);
        int j = 0;
        while (j < empty) {
            String one = listCopy.get(0);
            String two = listCopy.get(1);
            String three = listCopy.get(2);
            listCopy.set(0, three);
            listCopy.set(1, one);
            listCopy.set(2, two);
            if (listCopy.get(0).trim().equals(args.get(0))) {
                break;
            }
            list.add(listCopy);
            listCopy = new ArrayList<>(listCopy);
            j++;
        }
        grids = list;
    }

    public List<List<String>> getGrids() {
        return grids;
    }


    List<Integer> getIndexesInGrid(int indexGrid, PartRecipe recipe) {
        String index = recipe.getIndex();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            StringBuilder grid = new StringBuilder(this.grids.get(indexGrid).get(y));
            if (grid.charAt(x) == index.charAt(0)) {
                list.add(i);
            }
        }
        return list;
    }

}
