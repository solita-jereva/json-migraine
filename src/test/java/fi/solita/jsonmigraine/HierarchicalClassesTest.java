// Copyright © 2012 Solita Oy <www.solita.fi>
// This software is released under the MIT License.
// The license text is at http://opensource.org/licenses/MIT

package fi.solita.jsonmigraine;

import fi.solita.jsonmigraine.api.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HierarchicalClassesTest {

    private final TypeRenames renames = new TypeRenames();
    private final JsonMigraine jsonMigraine = new JsonMigraine(new ObjectMapper(), renames);

    {
        renames.rename(ChildV1a.class.getName(), ChildV1b.class.getName());
        renames.rename(ParentV1.class.getName(), ParentV2.class.getName());
    }

    @Test
    public void upgrades_class_hierarchies() throws Exception {
        ChildV1a v1 = new ChildV1a();
        v1.unmodified = "foo";
        v1.oldField = "bar";

        ChildV1b v2 = upgrade(v1);

        assertThat("should not change unrelated fields", v2.unmodified, is(v1.unmodified));
        assertThat("should migrate values of renamed fields", v2.newField, is(v1.oldField));
    }

    private ChildV1b upgrade(ChildV1a v1) throws Exception {
        String serialized = jsonMigraine.serialize(v1);
        return (ChildV1b) jsonMigraine.deserialize(serialized);
    }


    @Upgradeable(ParentUpgraderV1.class)
    static class ParentV1 {
        public String oldField;
    }

    @Upgradeable(ParentUpgraderV2.class)
    static class ParentV2 {
        public String newField;
    }

    @Upgradeable(ChildUpgraderV1.class)
    static class ChildV1a extends ParentV1 {
        public String unmodified;
    }

    @Upgradeable(ChildUpgraderV1.class)
    static class ChildV1b extends ParentV2 {
        public String unmodified;
    }

    static class ParentUpgraderV1 implements Upgrader {

        @Override
        public int version() {
            return 1;
        }

        @Override
        public JsonNode upgrade(JsonNode data, int version) {
            return data;
        }
    }

    static class ParentUpgraderV2 extends ObjectUpgrader {

        @Override
        public int version() {
            return 2;
        }

        @Override
        public ObjectNode upgrade(ObjectNode data, int version) {
            if (version == 1) {
                Refactor.renameField(data, "oldField", "newField");
            }
            return data;
        }
    }

    static class ChildUpgraderV1 implements Upgrader {

        @Override
        public int version() {
            return 1;
        }

        @Override
        public JsonNode upgrade(JsonNode data, int version) {
            return data;
        }
    }
}
