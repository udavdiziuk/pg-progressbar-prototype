package pl.uldav.pgprototype.service.query.printer.impl;

import org.springframework.stereotype.Component;
import pl.uldav.pgprototype.service.query.ast.InternalExistsNode;
import pl.uldav.pgprototype.service.query.ast.InternalGroupNode;
import pl.uldav.pgprototype.service.query.ast.InternalNode;
import pl.uldav.pgprototype.service.query.printer.InternalAstPrettyPrinter;

@Component
public class IndentedPrettyPrinter implements InternalAstPrettyPrinter {
    private static final String INDENT = "  ";

    @Override
    public String print(InternalNode node) {
        StringBuilder sb = new StringBuilder();
        printNode(node, sb, 0);
        return sb.toString();
    }

    private void printNode(InternalNode node, StringBuilder sb, int depth) {
        if (node instanceof InternalGroupNode group) {
            printGroup(group, sb, depth);
        } else if (node instanceof InternalExistsNode exists) {
            printExists(exists, sb, depth);
        } else {
            throw new IllegalStateException(
                    "Unknown InternalNode type: " + node.getClass()
            );
        }
    }

    private void printGroup(InternalGroupNode group, StringBuilder sb, int depth) {
        indent(sb, depth);

        if (group.isNegated()) {
            sb.append("NOT\n");
            indent(sb, depth);
            sb.append("(\n");
        }

        indent(sb, depth);
        sb.append(group.getOperator()).append("\n");

        for (InternalNode child : group.getChildren()) {
            printNode(child, sb, depth + 1);
        }

        if (group.isNegated()) {
            indent(sb, depth);
            sb.append(")\n");
        }
    }

    private void printExists(InternalExistsNode exists, StringBuilder sb, int depth) {
        indent(sb, depth);
        sb.append("EXISTS ")
                .append(exists.getEventType())
                .append(" ")
                .append(printOperator(exists))
                .append(" ")
                .append(exists.getValue())
                .append("\n");
    }

    private String printOperator(InternalExistsNode exists) {
        return switch (exists.getOperator()) {
            case EQ  -> "=";
            case NEQ -> "!=";
        };
    }

    private void indent(StringBuilder sb, int depth) {
        sb.append(INDENT.repeat(depth));
    }
}
