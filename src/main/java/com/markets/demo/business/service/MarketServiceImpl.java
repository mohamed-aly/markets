package com.markets.demo.business.service;

import com.markets.demo.business.dto.PagingDTO;
import com.markets.demo.business.entity.Market;
import com.markets.demo.business.repo.MarketRepository;
import com.markets.demo.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;

    public MarketServiceImpl(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    @Override
    public PagingDTO<Market> listMarkets(Pageable pageable, String query) {
        Page<Market> marketPage;

        if(query != null){
            query = "%" + query + "%";
            marketPage = marketRepository.searchMarkets(pageable, query);
        }else {
            marketPage = marketRepository.findAll(pageable);
        }

        return PagingDTO.<Market>builder()
                .content(marketPage.getContent())
                .pageNumber(marketPage.getNumber())
                .totalElements(marketPage.getTotalElements())
                .totalPages(marketPage.getTotalPages()).build();
    }

    @Override
    public Market save(Market market) {
        return marketRepository.save(market);
    }

    @Override
    public Market update(Market market) {
        Market savedMarket = getMarketIfExist(market.getId());
        myCopyProperties(market, savedMarket);
        return marketRepository.save(savedMarket);
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // then use Spring BeanUtils to copy and ignore null using our function
    private static void myCopyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    @Override
    public void delete(long id) {
        Market market = getMarketIfExist(id);
        marketRepository.delete(market);
    }

    @Override
    public Market setActivated(long id, boolean state) {
        Market market = getMarketIfExist(id);
        market.setActive(state);

        return marketRepository.save(market);
    }


    private Market getMarketIfExist(Long id) {
        Optional<Market> optionalMarket = marketRepository.findById(id);

        if (optionalMarket.isEmpty())
            throw new ResourceNotFoundException("Market With ID: " + id + " Not Found");

        return optionalMarket.get();
    }


}
