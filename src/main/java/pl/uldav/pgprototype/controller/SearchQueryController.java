package pl.uldav.pgprototype.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uldav.pgprototype.controller.dto.SearchRequestDTO;
import pl.uldav.pgprototype.controller.dto.SearchResponseDTO;

@RestController
@RequestMapping("v1/search")
public class SearchQueryController {

    @GetMapping
    public SearchResponseDTO search(@RequestBody SearchRequestDTO searchRequestDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
