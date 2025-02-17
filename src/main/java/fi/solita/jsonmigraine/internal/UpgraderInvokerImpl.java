// Copyright © 2012 Solita Oy <www.solita.fi>
// This software is released under the MIT License.
// The license text is at http://opensource.org/licenses/MIT

package fi.solita.jsonmigraine.internal;

import fi.solita.jsonmigraine.api.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

public class UpgraderInvokerImpl implements UpgraderInvoker {

    @Override
    public JsonNode upgrade(JsonNode data, int dataVersion, Upgrader upgrader) {
        return upgrader.upgrade(data, dataVersion);
    }

    @Override
    public ObjectNode upgradeField(ObjectNode container, String fieldName, int dataVersion, Upgrader upgrader) {
        JsonNode original = container.get(fieldName);
        try {
            JsonNode upgraded = upgrader.upgrade(original, dataVersion);
            container.set(fieldName, upgraded);
        } catch (ValueRemovedException e) {
            container.remove(fieldName);
        }
        return container;
    }

    @Override
    public ObjectNode upgradeArrayField(ObjectNode container, String fieldName, int dataVersion, Upgrader upgrader) {
        JsonNode original = container.get(fieldName);
        if (!original.isNull()) {
            JsonNode upgraded = upgradeArray((ArrayNode) original, dataVersion, upgrader);
            container.set(fieldName, upgraded);
        }
        return container;
    }

    private ArrayNode upgradeArray(ArrayNode values, int dataVersion, Upgrader upgrader) {
        ArrayNode results = JsonNodeFactory.instance.arrayNode();
        for (JsonNode original : values) {
            try {
                JsonNode upgraded = upgrader.upgrade(original, dataVersion);
                results.add(upgraded);
            } catch (ValueRemovedException e) {
                // removed; don't add to results
            }
        }
        return results;
    }
}
