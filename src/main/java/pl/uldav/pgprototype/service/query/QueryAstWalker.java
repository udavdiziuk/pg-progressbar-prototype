package pl.uldav.pgprototype.service.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import pl.uldav.pgprototype.controller.dto.QueryNodeDTO;
import pl.uldav.pgprototype.controller.dto.RuleDTO;
import pl.uldav.pgprototype.controller.dto.RuleGroupDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;

/**
 * Logic to walk through AST query with provided visitor
 */
@Component
@AllArgsConstructor
public class QueryAstWalker {

    @Getter
    private final QueryAstVisitor visitor;

    public void walk(QueryNodeDTO node) throws QueryValidationException {
        if (node instanceof RuleGroupDTO group) {
            walkGroup(group);
        } else if (node instanceof RuleDTO rule) {
            visitor.visitRule(rule);
        } else {
            throw new IllegalStateException(
                    "Unknown QueryNodeDTO type: " + node.getClass()
            );
        }
    }

    private void walkGroup(RuleGroupDTO group) throws QueryValidationException {
        visitor.enterGroup(group);

        if (group.getRules() != null) {
            for (QueryNodeDTO child : group.getRules()) {
                walk(child);
            }
        }

        visitor.exitGroup(group);
    }
}
