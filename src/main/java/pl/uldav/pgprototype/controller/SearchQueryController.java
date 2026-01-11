package pl.uldav.pgprototype.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uldav.pgprototype.controller.dto.SearchRequestDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;
import pl.uldav.pgprototype.service.AnalyticQueryService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/search")
public class SearchQueryController {

    private final AnalyticQueryService analyticQueryService;

    /**
     *
     * @param searchRequestDTO - ATS query
     * @return - List with IDs of patients found using filter query
     * @throws QueryValidationException - error in case provided query is invalid
     */
    @GetMapping
    public ResponseEntity<List<Long>> search(@RequestBody SearchRequestDTO searchRequestDTO) throws QueryValidationException {
        List<Long> patientIds = analyticQueryService.search(searchRequestDTO);
        return ResponseEntity.ok().body(patientIds);
    }
}
