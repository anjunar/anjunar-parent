package de.bitvale.common.filedisk;

import de.bitvale.common.ddd.AbstractEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@MappedSuperclass
public class HarddiskFile extends AbstractEntity {

    private String name;

    private String type;

    private String subType;

    private LocalDateTime lastModified;

    @Transient
    private byte[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @PostLoad
    void postLoad() {
        try {
            File file = FileDiskUtils.workingFile(getId());
            data = IOUtils.toByteArray(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostPersist
    void postPersist() {
        try {
            File file = FileDiskUtils.workingFile(getId());
            FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostUpdate
    void postUpdate() {
        try {
            File file = FileDiskUtils.workingFile(getId());
            FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostRemove
    void postRemove() {
        try {
            File file = FileDiskUtils.workingFile(getId());
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
