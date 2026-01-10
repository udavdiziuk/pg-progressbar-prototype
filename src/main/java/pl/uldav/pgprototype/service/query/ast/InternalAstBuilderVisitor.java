package pl.uldav.pgprototype.service.query.ast;

import lombok.Getter;
import pl.uldav.pgprototype.controller.dto.RuleDTO;
import pl.uldav.pgprototype.controller.dto.RuleGroupDTO;
import pl.uldav.pgprototype.exception.AstMappingException;
import pl.uldav.pgprototype.service.query.QueryAstVisitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InternalAstBuilderVisitor implements QueryAstVisitor {
    private final Deque<List<InternalNode>> stack = new ArrayDeque<>();
    @Getter
    private InternalNode result;

    @Override
    public void enterGroup(RuleGroupDTO groupDTO) {
        stack.push(new ArrayList<>());
    }

    @Override
    public void exitGroup(RuleGroupDTO group) {
        List<InternalNode> children = stack.pop();

        LogicalOperator operator = mapCombinator(group.getCombinator());
        boolean negated = Boolean.TRUE.equals(group.getNot());

        InternalGroupNode node =
                new InternalGroupNode(operator, negated, children);

        addNode(node);
    }

    @Override
    public void visitRule(RuleDTO rule) {
        InternalExistsNode node = new InternalExistsNode(
                mapField(rule.getField()),
                mapOperator(rule.getOperator()),
                rule.getValue()
        );
        addNode(node);
    }

    private void addNode(InternalNode node) {
        if (stack.isEmpty()) {
            result = node;
        } else {
            stack.peek().add(node);
        }
    }

    // ---- mapping helpers ----

    private LogicalOperator mapCombinator(String combinator) {
        return switch (combinator.toLowerCase()) {
            case "and" -> LogicalOperator.AND;
            case "or"  -> LogicalOperator.OR;
            default -> throw new AstMappingException(
                    "Unsupported combinator: " + combinator
            );
        };
    }

    private EventType mapField(String field) {
        return switch (field.toLowerCase()) {
            case "diagnosis" -> EventType.DIAGNOSIS;
            case "medication" -> EventType.MEDICATION;
            case "hospitalization" -> EventType.HOSPITALIZATION;
            default -> throw new AstMappingException(
                    "Unsupported field: " + field
            );
        };
    }

    private ComparisonOperator mapOperator(String operator) {
        return switch (operator) {
            case "="  -> ComparisonOperator.EQ;
            case "!=" -> ComparisonOperator.NEQ;
            default -> throw new AstMappingException(
                    "Unsupported operator: " + operator
            );
        };
    }
}
