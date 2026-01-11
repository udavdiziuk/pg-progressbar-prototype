package pl.uldav.pgprototype.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.uldav.pgprototype.controller.dto.SearchRequestDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;
import pl.uldav.pgprototype.service.query.QueryAstWalker;
import pl.uldav.pgprototype.service.query.ast.InternalAstBuilderVisitor;
import pl.uldav.pgprototype.service.query.ast.InternalNode;
import pl.uldav.pgprototype.service.query.pipeline.PatientSearchPipeline;
import pl.uldav.pgprototype.service.query.printer.impl.IndentedPrettyPrinter;
import pl.uldav.pgprototype.service.query.sql.InternalAstSqlBuilder;
import pl.uldav.pgprototype.service.query.sql.SqlCondition;
import pl.uldav.pgprototype.service.query.validation.SemanticVisitorValidator;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AnalyticQueryService {

    private final IndentedPrettyPrinter indentedPrettyPrinter;
    private final InternalAstSqlBuilder internalAstSqlBuilder;
    private final PatientSearchPipeline patientSearchPipeline;

    /**
     * Executes the following steps:
     *  - Validates the provided ATS query
     *  - Maps to internal AST types
     *  - Converts AST to SQL
     *  - Executes SQL
     * @param searchRequestDTO
     * @return
     * @throws QueryValidationException
     */
    public List<Long> search(SearchRequestDTO searchRequestDTO) throws QueryValidationException {

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
        SqlCondition sqlCondition = internalAstSqlBuilder.build(internalAst);
        log.info("Sql Result: {}", sqlCondition);
        return patientSearchPipeline.execute(sqlCondition);
    }
}
