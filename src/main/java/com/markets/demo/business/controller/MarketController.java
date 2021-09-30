package com.markets.demo.business.controller;


import com.markets.demo.business.dto.PagingDTO;
import com.markets.demo.business.entity.Market;
import com.markets.demo.business.service.MarketService;
import com.markets.demo.shared.validation.ValidQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/markets")
@Validated
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public ResponseEntity<PagingDTO<Market>> listMarkets(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
                                                         @RequestParam(required = false) @ValidQuery String query){
        return new ResponseEntity<>(marketService.listMarkets(pageable, query), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Market> createMarket(@RequestBody Market market){
        return new ResponseEntity<>(marketService.save(market), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Market> editMarket(@RequestBody Market market){
        return new ResponseEntity<>(marketService.update(market), HttpStatus.OK);
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Market> activateMarket(@PathVariable long id,
                                                 @RequestParam boolean active){
        return new ResponseEntity<>(marketService.setActivated(id, active), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteMarket(@PathVariable long id){
        marketService.delete(id);
    }


}
