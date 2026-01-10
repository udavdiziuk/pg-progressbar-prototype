package pl.uldav.pgprototype.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.uldav.pgprototype.controller.dto.SearchRequestDTO;
import pl.uldav.pgprototype.controller.dto.SearchResponseDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;
import pl.uldav.pgprototype.service.query.QueryAstWalker;
import pl.uldav.pgprototype.service.query.ast.InternalAstBuilderVisitor;
import pl.uldav.pgprototype.service.query.ast.InternalNode;
import pl.uldav.pgprototype.service.query.printer.impl.IndentedPrettyPrinter;
import pl.uldav.pgprototype.service.query.validation.SemanticVisitorValidator;

@AllArgsConstructor
@Slf4j
@Service
public class AnalyticQueryService {

    private final IndentedPrettyPrinter indentedPrettyPrinter;

    public SearchResponseDTO search(SearchRequestDTO searchRequestDTO) throws QueryValidationException {

        //Semantic validation
        SemanticVisitorValidator validationVisitor =
                new SemanticVisitorValidator();

        QueryAstWalker validationWalker =
                new QueryAstWalker(validationVisitor);

        validationWalker.walk(searchRequestDTO.getQuery());

        //Mapping to internal AST
        InternalAstBuilderVisitor mapperVisitor =
                new InternalAstBuilderVisitor();

        QueryAstWalker mappingWalker =
                new QueryAstWalker(mapperVisitor);

        mappingWalker.walk(searchRequestDTO.getQuery());

        InternalNode internalAst = mapperVisitor.getResult();
        log.info("Search result:\n {}", indentedPrettyPrinter.print(internalAst));
        return null;
    }
}
