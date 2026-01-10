package pl.uldav.pgprototype.service.query.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class InternalGroupNode implements InternalNode {
    LogicalOperator operator;
    boolean negated;
    List<InternalNode> children;

}
