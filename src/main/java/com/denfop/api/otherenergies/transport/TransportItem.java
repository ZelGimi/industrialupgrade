package com.denfop.api.otherenergies.transport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransportItem<T> {

    private List<T> list = new ArrayList<>();
    private List<Integer> list1 = new ArrayList<>();

    public TransportItem() {
    }

    public List<Integer> getList1() {
        return list1;
    }

    public void setList1(final List<Integer> list1) {
        this.list1 = list1;
    }

    public final void setList1(final Integer... list1) {
        this.list1 = Arrays.asList(list1);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(final List<T> list) {
        this.list = list;
    }

    @SafeVarargs
    public final void setList(final T... list) {
        this.list = Arrays.asList(list);
    }

}
