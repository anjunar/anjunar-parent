package de.bitvale.common.rest.api;

import de.bitvale.common.filedisk.Base64Resource;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.filedisk.HarddiskFile;

import java.time.LocalDateTime;

/**
 * @author Patrick Bittner on 01.08.17.
 */
public class Blob {

    private String name;

    private LocalDateTime lastModified;

    private String data = "";

    public static HarddiskFile updater(Blob blob, HarddiskFile harddiskFile) {
        if (harddiskFile == null) {
            return null;
        } else {
            Base64Resource process = FileDiskUtils.extractBase64(blob.getData());

            harddiskFile.setData(process.getData());
            harddiskFile.setType(process.getType());
            harddiskFile.setSubType(process.getSubType());

            harddiskFile.setName(blob.getName());
            harddiskFile.setLastModified(blob.getLastModified());
        }
        return harddiskFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static Blob factory(HarddiskFile harddiskFile) {
        if (harddiskFile == null) {
            return new Blob();
        }
        Blob blob = new Blob();
        blob.setName(harddiskFile.getName());
        blob.setLastModified(harddiskFile.getLastModified());
        blob.setData(FileDiskUtils.buildBase64(harddiskFile.getType(), harddiskFile.getSubType(), harddiskFile.getData()));
        return blob;
    }

}
