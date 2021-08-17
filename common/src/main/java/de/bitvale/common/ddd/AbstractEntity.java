package de.bitvale.common.ddd;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Patrick Bittner on 31/01/15.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Entity {

    @Id
    @Type(type = "uuid-binary")
    @Column(name = "id", length = 16, unique = true, nullable = false)
    private UUID id = UUID.randomUUID();

    @Version
    private int version;

    private Instant created;

    private Instant modified;

    @PreUpdate
    private void postUpdate() {
        modified = Instant.now();
    }

    @PrePersist
    private void postCreate() {
        modified = Instant.now();
        created = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getModified() {
        return modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
