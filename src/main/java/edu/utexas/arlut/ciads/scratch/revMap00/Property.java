// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;


import com.google.common.base.Objects;
import lombok.Data;

import static com.google.common.collect.Maps.newHashMap;

@Data
public class Property {
    final Long id;
    final String key;
    final Object value;

    public static Property copy(final Property p) {
        Property np = new Property(p.id, p.key, p.value);
        return np;
    }
    // =================================
    public static Property make(final String a, final Object b) {
        return new Property(sID++, a, b);
    }
    // =================================
    @Override
    public int hashCode() {
        return key.hashCode();
    }
    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Property)) return false;
        Property op = (Property) o;
        return Objects.equal(key, op.key) && Objects.equal(value, op.value);
    }
    // =================================
    private static long sID = 1;
}
