package pl.uldav.pgprototype.service.query;

import pl.uldav.pgprototype.controller.dto.RuleDTO;
import pl.uldav.pgprototype.controller.dto.RuleGroupDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;

/**
 * Basic interface for AST visitors
 */
public interface QueryAstVisitor {
    void enterGroup(RuleGroupDTO groupDTO) throws QueryValidationException;
    void exitGroup(RuleGroupDTO groupDTO);
    void visitRule(RuleDTO ruleDTO) throws QueryValidationException;
}
