package com.denfop.api.energy;

import java.util.ArrayList;
import java.util.List;

public class PathConductors<T> {
    private List<T> conductorList;

    public PathConductors(List<T> conductorList){
        this.conductorList=conductorList;
    }
    public List<T> getConductorList() {
        return conductorList;
    }
    public void add(T element) {
        conductorList.add(element);
    }
    public void rework(){
        conductorList = new ArrayList<>(conductorList);
    }
    public void setConductorList(final List<T> conductorList) {
        this.conductorList = conductorList;
    }

}
