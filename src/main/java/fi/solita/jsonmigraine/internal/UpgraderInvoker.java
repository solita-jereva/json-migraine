// Copyright © 2012 Solita Oy <www.solita.fi>
// This software is released under the MIT License.
// The license text is at http://opensource.org/licenses/MIT

package fi.solita.jsonmigraine.internal;

import fi.solita.jsonmigraine.api.Upgrader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UpgraderInvoker {

    JsonNode upgrade(JsonNode data, int dataVersion, Upgrader upgrader);

    ObjectNode upgradeField(ObjectNode container, String fieldName, int dataVersion, Upgrader upgrader);

    ObjectNode upgradeArrayField(ObjectNode container, String fieldName, int dataVersion, Upgrader upgrader);
}
