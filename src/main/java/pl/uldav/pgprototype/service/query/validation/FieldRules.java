package pl.uldav.pgprototype.service.query.validation;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

/**
 * Describes field rules for validation
 */
@UtilityClass
public class FieldRules {
    public static final Map<String, Set<String>> ALLOWED_OPERATIONS = Map.of(
            "diagnosis", Set.of("=", "!="),
            "medication", Set.of("=", "!="),
            "hospitalization", Set.of("=", "!=")
    );
}
