package pl.uldav.pgprototype.service.query.validation;

import pl.uldav.pgprototype.controller.dto.RuleDTO;
import pl.uldav.pgprototype.controller.dto.RuleGroupDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;
import pl.uldav.pgprototype.service.query.QueryAstVisitor;

import java.util.Set;

public class SemanticVisitorValidator implements QueryAstVisitor {

    private static final int MAX_DEPTH = 10;
    private static final int MAX_RULES = 100;

    private int depth = 0;
    private int ruleCount = 0;

    @Override
    public void enterGroup(RuleGroupDTO group) throws QueryValidationException {
        depth++;
        if (depth > MAX_DEPTH) {
            throw new QueryValidationException(
                    "Query is too deeply nested (max depth = " + MAX_DEPTH + ")"
            );
        }

        if (group.getCombinator() == null ||
                !(group.getCombinator().equalsIgnoreCase("and")
                        || group.getCombinator().equalsIgnoreCase("or"))) {

            throw new QueryValidationException(
                    "Invalid combinator: " + group.getCombinator()
            );
        }
    }

    @Override
    public void exitGroup(RuleGroupDTO group) {
        depth--;
    }

    @Override
    public void visitRule(RuleDTO rule) throws QueryValidationException {
        ruleCount++;
        if (ruleCount > MAX_RULES) {
            throw new QueryValidationException(
                    "Too many rules in query (max = " + MAX_RULES + ")"
            );
        }

        validateRule(rule);
    }

    private void validateRule(RuleDTO rule) throws QueryValidationException {
        if (rule.getField() == null || rule.getField().isBlank()) {
            throw new QueryValidationException("Rule field is missing");
        }

        if (rule.getOperator() == null || rule.getOperator().isBlank()) {
            throw new QueryValidationException(
                    "Operator is missing for field: " + rule.getField()
            );
        }

        Set<String> allowedOperators =
                FieldRules.ALLOWED_OPERATIONS.get(rule.getField());

        if (allowedOperators == null) {
            throw new QueryValidationException(
                    "Unknown field: " + rule.getField()
            );
        }

        if (!allowedOperators.contains(rule.getOperator())) {
            throw new QueryValidationException(
                    "Operator '" + rule.getOperator() +
                            "' is not allowed for field '" + rule.getField() + "'"
            );
        }

        if (rule.getValue() == null) {
            throw new QueryValidationException(
                    "Value is required for field: " + rule.getField()
            );
        }

        // simple garbage defence
        if (rule.getValue() instanceof String s && s.length() > 255) {
            throw new QueryValidationException(
                    "Value too long for field: " + rule.getField()
            );
        }
    }
}
