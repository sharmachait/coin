package com.sharmachait.wazir.Service.CoinService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sharmachait.wazir.Model.Entity.Coin;

import java.util.List;

public interface ICoinService {
    List<Coin> getCoinList(int page) throws JsonProcessingException;
    String getMarketChart(String coinId, int days);
    Coin getCoinDetails(String coinId) throws JsonProcessingException;
    Coin findById(String coinId);
    String searchCoin(String keyword);
    String getTop50CoinsByMarketCapRank();
    String getTradingCoins();
}
