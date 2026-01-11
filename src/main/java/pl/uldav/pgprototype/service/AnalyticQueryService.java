package pl.uldav.pgprototype.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.uldav.pgprototype.controller.dto.SearchRequestDTO;
import pl.uldav.pgprototype.controller.dto.SearchResponseDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;
import pl.uldav.pgprototype.repository.PatientRepository;
import pl.uldav.pgprototype.service.job.JobRegistry;
import pl.uldav.pgprototype.service.job.SearchJob;
import pl.uldav.pgprototype.service.job.SearchJobRunner;
import pl.uldav.pgprototype.service.query.QueryAstWalker;
import pl.uldav.pgprototype.service.query.ast.InternalAstBuilderVisitor;
import pl.uldav.pgprototype.service.query.ast.InternalNode;
import pl.uldav.pgprototype.service.query.printer.impl.IndentedPrettyPrinter;
import pl.uldav.pgprototype.service.query.sql.InternalAstSqlBuilder;
import pl.uldav.pgprototype.service.query.sql.SqlCondition;
import pl.uldav.pgprototype.service.query.validation.SemanticVisitorValidator;

@AllArgsConstructor
@Slf4j
@Service
public class AnalyticQueryService {

    private final IndentedPrettyPrinter indentedPrettyPrinter;
    private final InternalAstSqlBuilder internalAstSqlBuilder;
    private final PatientRepository patientRepository;
    private final JobRegistry jobRegistry;
    private final SearchJobRunner jobRunner;

    /**
     * Executes the following steps:
     *  - Validates the provided ATS query
     *  - Maps to internal AST types
     *  - Converts AST to SQL
     *  - Executes SQL
     * @param searchRequestDTO - input AST
     * @return - Info about created SearchJob
     * @throws QueryValidationException - an error in case provided AST is invalid
     */
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
        log.info("AST query:\n {}", indentedPrettyPrinter.print(internalAst));
        SqlCondition sqlCondition = internalAstSqlBuilder.build(internalAst);
        log.info("Sql Query: {}", sqlCondition);

        //Run Job
        long totalPatients = patientRepository.countAll();
        SearchJob job = jobRegistry.create(totalPatients);
        jobRunner.run(job, sqlCondition);
        return SearchResponseDTO.builder()
                .jobId(job.getJobId())
                .jobStatus(job.getStatus())
                .build();
    }
}
