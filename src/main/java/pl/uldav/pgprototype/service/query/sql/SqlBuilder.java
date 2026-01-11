package pl.uldav.pgprototype.service.query.sql;

import pl.uldav.pgprototype.service.query.ast.InternalExistsNode;
import pl.uldav.pgprototype.service.query.ast.InternalGroupNode;
import pl.uldav.pgprototype.service.query.ast.InternalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SqlBuilder implements InternalAstSqlBuilder {

    @Override
    public SqlCondition build(InternalNode node) {
        return buildNode(node);
    }


    /**
     * Builds SQL condition according to provided AST node.
     * @param node - AST node
     * @return - SQL condition
     */
    private SqlCondition buildNode(InternalNode node) {
        if (node instanceof InternalGroupNode group) {
            return buildGroup(group);
        }
        if (node instanceof InternalExistsNode exists) {
            return buildExists(exists);
        }
        throw new IllegalStateException("Unknown InternalNode: " + node);
    }

    /**
     * Builds group of nodes
     * @param group - AST group node
     * @return - SQL condition
     */
    private SqlCondition buildGroup(InternalGroupNode group) {
        List<SqlCondition> children = new ArrayList<>();
        for (InternalNode child : group.getChildren()) {
            children.add(buildNode(child));
        }

        StringJoiner joiner = new StringJoiner(
                " " + group.getOperator().name() + " ",
                "(",
                ")"
        );

        List<Object> params = new ArrayList<>();

        for (SqlCondition child : children) {
            joiner.add(child.getSql());
            params.addAll(child.getParams());
        }

        String sql = joiner.toString();

        if (group.isNegated()) {
            sql = "NOT " + sql;
        }

        return new SqlCondition(sql, params);
    }

    /**
     * Build EXISTS SQL condition
     * @param exists - node with AST condition
     * @return - SQL condition
     */
    private SqlCondition buildExists(InternalExistsNode exists) {
        String sql = """
        EXISTS (
          SELECT 1
          FROM medical_events e
          WHERE e.patient_id = p.id
            AND e.event_type = ?
            AND %s
        )
        """.formatted(buildValueCondition(exists));

        List<Object> params = new ArrayList<>();
        params.add(exists.getEventType().name());
        params.add(exists.getValue());

        return new SqlCondition(sql, params);
    }

    /**
     * Builds value condition
     * @param exists - AST node with EXISTS condition
     * @return - condition String
     */
    private String buildValueCondition(InternalExistsNode exists) {
        return switch (exists.getOperator()) {
            case EQ  -> "e.value = ?";
            case NEQ -> "e.value <> ?";
        };
    }
}
