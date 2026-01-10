package pl.uldav.pgprototype.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.uldav.pgprototype.service.query.ast.InternalAstBuilderVisitor;
import pl.uldav.pgprototype.service.query.QueryAstVisitor;

@Configuration
public class PgProtoConfig {
    @Bean
    public QueryAstVisitor queryAstVisitor() {
        return new InternalAstBuilderVisitor();
    }
}
