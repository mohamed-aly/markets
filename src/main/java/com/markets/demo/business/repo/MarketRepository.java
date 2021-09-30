package com.markets.demo.business.repo;

import com.markets.demo.business.entity.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {

    @Query("select m from Market m where upper(m.arabicName) like upper(:query) " +
            "or upper(m.englishName) like upper(:query) " +
            "or upper(m.address) like upper(:query)")
    Page<Market> searchMarkets(Pageable pageable, String query);

    @Modifying
    @Query("delete from Market m where m.id > 0")
    void deleteAll();
}
