package de.bitvale.common.filedisk;

public class Base64Resource {

    private String type;

    private String subType;

    private byte[] data;

    public Base64Resource(String type, String subType, byte[] data) {
        this.type = type;
        this.subType = subType;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public byte[] getData() {
        return data;
    }
}
