package pl.uldav.pgprototype.controller.dto;


import lombok.Data;

import java.util.List;

@Data
public final class RuleGroupDTO implements QueryNodeDTO {

    /**
     * "and" | "or"
     */
    private String combinator;

    /**
     * List of RuleDTO or RuleGroupDTO
     */
    private List<QueryNodeDTO> rules;

    /**
     * Optional NOT flag
     */
    private Boolean not;
}
