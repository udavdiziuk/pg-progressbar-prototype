package pl.uldav.pgprototype.service.query.sql;

import pl.uldav.pgprototype.service.query.ast.InternalNode;

public interface InternalAstSqlBuilder {
    SqlCondition build(InternalNode node);
}
