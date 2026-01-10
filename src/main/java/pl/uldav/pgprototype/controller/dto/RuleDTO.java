package pl.uldav.pgprototype.controller.dto;

import lombok.Data;

@Data
public final class RuleDTO implements QueryNodeDTO {

    /**
     * Field name from react-querybuilder
     * e.g. "diagnosis", "medication"
     */
    private String field;

    /**
     * Operator from react-querybuilder
     * e.g. "=", "!=", "contains"
     */
    private String operator;

    /**
     * Value (string, number, array, etc.)
     */
    private Object value;
}

