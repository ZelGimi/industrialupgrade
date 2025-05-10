package com.denfop.api.tesseract;

import java.util.LinkedList;
import java.util.List;

public class ChannelHandler {

    List<Channel> listEnergy = new LinkedList<>();
    List<Channel> listFluid = new LinkedList<>();
    List<Channel> listItem = new LinkedList<>();

    public ChannelHandler() {
    }

    public List<Channel> getListEnergy() {
        return listEnergy;
    }

    public List<Channel> getListFluid() {
        return listFluid;
    }

    public void addChannel(Channel channel) {
        if (channel.getTypeChannel() == TypeChannel.ENERGY) {
            listEnergy.add(channel);
        } else if (channel.getTypeChannel() == TypeChannel.FLUID) {
            listFluid.add(channel);
        } else if (channel.getTypeChannel() == TypeChannel.ITEM) {
            listItem.add(channel);
        }
    }

    public void removeChannel(Channel channel) {
        if (channel.getTypeChannel() == TypeChannel.ENERGY) {
            listEnergy.remove(channel);
        } else if (channel.getTypeChannel() == TypeChannel.FLUID) {
            listFluid.remove(channel);
        } else if (channel.getTypeChannel() == TypeChannel.ITEM) {
            listItem.remove(channel);
        }
    }

    public List<Channel> getListItem() {
        return listItem;
    }

}
