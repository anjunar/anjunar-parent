package de.bitvale.common.rest.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class RestEntities {

    public static Collection<UUID> toIds(Collection<? extends RestEntity> collection) {
        List<UUID> result = new ArrayList<>();
        for (RestEntity restEntity : collection) {
            result.add(restEntity.getId());
        }
        return result;
    }

}
