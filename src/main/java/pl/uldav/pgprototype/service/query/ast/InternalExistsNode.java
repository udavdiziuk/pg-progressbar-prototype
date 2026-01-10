package pl.uldav.pgprototype.service.query.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class InternalExistsNode implements InternalNode {
    private EventType eventType;
    private ComparisonOperator operator;
    private Object value;
}
