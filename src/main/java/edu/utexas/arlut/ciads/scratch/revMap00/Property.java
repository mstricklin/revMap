// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;

import java.util.Objects;

import lombok.Data;

@Data
public class Property {
    final String key;
    final Object value;

    @Override
    public int hashCode() {
        return key.hashCode();
    }
    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Property)) return false;
        Property op = (Property) o;
        return Objects.equals(key, op.key) && Objects.equals(value, op.value);
    }
}
