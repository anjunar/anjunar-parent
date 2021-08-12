package de.bitvale.common.rest;

import java.util.Map;

/**
 * @author by Patrick Bittner on 09.06.15.
 */
public class SecurityAction {

    private final String path;

    private final String method;

    private final Map<String, Object> params;

    private boolean valid = false;

    public SecurityAction(String path, String method, Map<String, Object> params) {
        this.path = path;
        this.method = method;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
