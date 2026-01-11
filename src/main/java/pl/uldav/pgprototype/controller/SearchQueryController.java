package pl.uldav.pgprototype.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uldav.pgprototype.controller.dto.SearchRequestDTO;
import pl.uldav.pgprototype.controller.dto.SearchResponseDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;
import pl.uldav.pgprototype.service.AnalyticQueryService;

@AllArgsConstructor
@RestController
@RequestMapping("v1/search")
public class SearchQueryController {

    private final AnalyticQueryService analyticQueryService;

    /**
     *
     * @param searchRequestDTO - ATS query
     * @return - Info about SearchJob
     * @throws QueryValidationException - error in case provided query is invalid
     */
    @GetMapping
    public ResponseEntity<SearchResponseDTO> search(@RequestBody SearchRequestDTO searchRequestDTO) throws QueryValidationException {
        SearchResponseDTO response =  analyticQueryService.search(searchRequestDTO);
        return ResponseEntity.ok().body(response);
    }
}
