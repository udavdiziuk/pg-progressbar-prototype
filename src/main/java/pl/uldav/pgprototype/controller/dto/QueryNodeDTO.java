package pl.uldav.pgprototype.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.DEDUCTION
)
@JsonSubTypes({
        @JsonSubTypes.Type(RuleGroupDTO.class),
        @JsonSubTypes.Type(RuleDTO.class)
})
public sealed interface QueryNodeDTO
        permits RuleGroupDTO, RuleDTO {
}
