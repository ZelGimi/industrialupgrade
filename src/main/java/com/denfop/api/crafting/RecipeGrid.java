package com.denfop.api.crafting;

import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeGrid {


    private final List<List<String>> grids;
    private final List<String> args;
    private List<Character> charactersList;
    private int x2;
    private int y2;
    private int index;
    private boolean hasTwoY;
    private boolean hasTwoX;

    public RecipeGrid(List<String> args) {

        while (3 - args.size() > 0) {
            args.add("   ");
        }
        Set<Character> characters = new HashSet<>();

        for (String s : args) {
            for (char c : s.toCharArray()) {
                if (c != ' ') {
                    characters.add(c);
                }
            }
        }

        this.charactersList = new ArrayList<>(characters);
        
        for (int i = 0; i < 3; i++) {
            final StringBuilder stringBuilder = new StringBuilder(args.get(i));
            while (stringBuilder.length() < 3) {
                stringBuilder.append(" ");
            }
            args.set(i, stringBuilder.toString());
        }
        this.args = args;
        int empty = 0;
        for (int i = args.size() - 1; i >= 0; i--) {
            if (args.get(i).equals("   ")) {
                empty++;
            } else {
                break;
            }
        }
        List<String> listCopy = new ArrayList<>(args);
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
        this.hasTwoY = false;
        this.hasTwoX = false;
        List<List<String>> list1 = new ArrayList<>();

        for (List<String> list2 : list) {
            List<String> k = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    String k1 = list2.get(y);
                    String k2 = String.valueOf(k1.charAt(x));
                    k.add(k2);
                }
            }
            list1.add(k);
        }
        int m = 0;
        this.x2 = -1;
        this.y2 = -1;
        this.index = -1;
        for (List<String> lists : list1) {
            boolean xEmpty = false;
            boolean yEmpty = false;
            int x1 = 0;
            for (int x = 0; x < 3; x += 2) {
                for (int y = 0; y < 3; y++) {
                    x1 = x;
                    yEmpty = lists.get(x + y * 3).trim().isEmpty();
                    if (!yEmpty) {
                        break;
                    }
                }
                if (yEmpty) {
                    break;
                }
            }
            int y1 = 0;
            for (int y = 0; y < 3; y += 2) {
                for (int x = 0; x < 3; x++) {
                    y1 = y;
                    xEmpty = lists.get(x + y * 3).trim().isEmpty();
                    if (!xEmpty) {
                        break;
                    }
                }
                if (xEmpty) {
                    break;
                }
            }
            if (xEmpty && yEmpty) {
                List<String> recipe = new ArrayList<>();
                if (x1 == 0 && y1 == 0) {
                    if (this.x2 == -1 && this.y2 == -1) {
                        this.x2 = x1;
                        this.y2 = y1;
                        this.index = m;
                    }
                    List<String> list2 = list.get(m);
                    final String one = list2.get(0);
                    StringBuilder two = new StringBuilder(list2.get(1));
                    StringBuilder three = new StringBuilder(list2.get(2));
                    two.deleteCharAt(0).append(" ");
                    three.deleteCharAt(0).append(" ");
                    recipe.add(one);
                    recipe.add(two.toString());
                    recipe.add(three.toString());
                    list.add(recipe);
                } else if (x1 == 2 && y1 == 0) {
                    if (this.x2 == -1 && this.y2 == -1) {
                        this.x2 = x1;
                        this.y2 = y1;
                        this.index = m;
                    }
                    List<String> list2 = list.get(m);
                    final String one = list2.get(0);
                    StringBuilder two = new StringBuilder(list2.get(1));
                    StringBuilder three = new StringBuilder(list2.get(2));
                    two.deleteCharAt(2);
                    three.deleteCharAt(2);
                    recipe.add(one);
                    recipe.add(" " + two);
                    recipe.add(" " + three);
                    list.add(recipe);
                } else if (x1 == 2 && y1 == 2) {
                    if (this.x2 == -1 && this.y2 == -1) {
                        this.x2 = x1;
                        this.y2 = y1;
                        this.index = m;
                    }
                    List<String> list2 = list.get(m);
                    StringBuilder one = new StringBuilder(list2.get(0));
                    StringBuilder two = new StringBuilder(list2.get(1));
                    final String three = list2.get(2);
                    two.deleteCharAt(2);
                    one.deleteCharAt(2);
                    recipe.add(" " + one);
                    recipe.add(" " + two);
                    recipe.add(three);
                    list.add(recipe);
                } else if (x1 == 0 && y1 == 2) {
                    if (this.x2 == -1 && this.y2 == -1) {
                        this.x2 = x1;
                        this.y2 = y1;
                        this.index = m;
                    }
                    List<String> list2 = list.get(m);
                    StringBuilder one = new StringBuilder(list2.get(0));
                    StringBuilder two = new StringBuilder(list2.get(1));
                    final String three = list2.get(2);
                    two.deleteCharAt(0).append(" ");
                    one.deleteCharAt(0).append(" ");
                    recipe.add(one.toString());
                    recipe.add(two.toString());
                    recipe.add(three);
                    list.add(recipe);
                }

            }
            m++;
            hasTwoX = hasTwoX || xEmpty;
            hasTwoY = hasTwoY || yEmpty;
        }
        grids = list;
    }

    public List<Character> getCharactersList() {
        return charactersList;
    }

    public boolean isHasTwoX() {
        return hasTwoX;
    }

    public int getIndex() {
        return index;
    }

    public int getY2() {
        return y2;
    }

    public int getX2() {
        return x2;
    }

    public boolean isHasTwoY() {
        return hasTwoY;
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

    public void encode(CustomPacketBuffer customPacketBuffer) {
        try {
            EncoderHandler.encode(customPacketBuffer,args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
