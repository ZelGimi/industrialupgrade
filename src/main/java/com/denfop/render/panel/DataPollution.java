package com.denfop.render.panel;

public class DataPollution {

    private  PollutionModel model;
    private  int index;

    public DataPollution(int index, PollutionModel model){
        this.index = index;
        this.model = model;
    }

    public int getIndex() {
        return index;
    }

    public PollutionModel getModel() {
        return model;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public void setModel(final PollutionModel model) {
        this.model = model;
    }

}
