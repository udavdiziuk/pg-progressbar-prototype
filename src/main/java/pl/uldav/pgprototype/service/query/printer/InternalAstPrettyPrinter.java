package pl.uldav.pgprototype.service.query.printer;

import pl.uldav.pgprototype.service.query.ast.InternalNode;

public interface InternalAstPrettyPrinter {
    String print(InternalNode internalNode);
}
