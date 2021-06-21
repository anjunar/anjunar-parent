package de.bitvale.common.security;

import de.bitvale.common.ddd.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "co_permission")
public class Permission extends AbstractEntity {

    private String url;

    @Size(min = 3)
    private String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
