package pl.uldav.pgprototype.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.uldav.pgprototype.repository.PatientRepository;
import pl.uldav.pgprototype.service.query.ast.InternalAstBuilderVisitor;
import pl.uldav.pgprototype.service.query.QueryAstVisitor;
import pl.uldav.pgprototype.service.query.pipeline.PatientSearchPipeline;
import pl.uldav.pgprototype.service.query.pipeline.impl.PatientSearchPipelineImpl;
import pl.uldav.pgprototype.service.query.sql.InternalAstSqlBuilder;
import pl.uldav.pgprototype.service.query.sql.SqlBuilder;

@Configuration
public class PgProtoConfig {
    @Bean
    public QueryAstVisitor queryAstVisitor() {
        return new InternalAstBuilderVisitor();
    }

    @Bean
    public InternalAstSqlBuilder internalAstSqlBuilder() {
        return new SqlBuilder();
    }

    @Bean
    public PatientSearchPipeline patientSearchPipeline(JdbcTemplate jdbcTemplate, PatientRepository patientRepository) {
        return new PatientSearchPipelineImpl(jdbcTemplate, patientRepository);
    }
}
