package de.hskl.rateme.model;


public class PoiTag {
    private long osmId;
    private String tag = null;
    private String value = null;

    public PoiTag(long osmId, String tag, String value) {
        this.osmId = osmId;
        this.tag = tag;
        this.value = value;
    }

    public long getOsmId() {
        return osmId;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }
}
