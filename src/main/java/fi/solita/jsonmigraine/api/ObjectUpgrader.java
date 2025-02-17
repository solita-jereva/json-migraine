// Copyright © 2012-2013 Solita Oy <www.solita.fi>
// This software is released under the MIT License.
// The license text is at http://opensource.org/licenses/MIT

package fi.solita.jsonmigraine.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class ObjectUpgrader implements Upgrader {

    @Override
    public final JsonNode upgrade(JsonNode data, int version) {
        return upgrade((ObjectNode) data, version);
    }

    /**
     * @see Upgrader#upgrade
     */
    public abstract ObjectNode upgrade(ObjectNode data, int version);
}
