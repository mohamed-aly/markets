package com.markets.demo.business.service;

import com.markets.demo.business.dto.PagingDTO;
import com.markets.demo.business.entity.Market;
import org.springframework.data.domain.Pageable;

public interface MarketService {

    PagingDTO<Market> listMarkets(Pageable pageable, String query);

    Market save(Market market);

    Market update(Market market);

    void delete(long id);

    Market setActivated(long id, boolean state);
}
