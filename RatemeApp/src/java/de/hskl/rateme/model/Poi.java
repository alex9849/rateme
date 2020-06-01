package de.hskl.rateme.model;


import java.util.ArrayList;
import java.util.List;

public class Poi {
    private long osmId;
    private double positionX;
    private double positionY;
    private String type = null;
    private List<PoiTag> poiTags;

    public Poi(long osmId, double posX, double posY, String type) {
        this.osmId = osmId;
        this.positionX = posX;
        this.positionY = posY;
        this.type = type;
        this.poiTags = new ArrayList<>();
    }

    public long getOsmId() {
        return osmId;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public String getType() {
        return type;
    }

    public List<PoiTag> getPoiTags() {
        return poiTags;
    }

    public void addTag(PoiTag poiTag) {
        this.poiTags.add(poiTag);
    }
}
