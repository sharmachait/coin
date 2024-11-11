package com.sharmachait.wazir.Service.CoinService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sharmachait.wazir.Model.Entity.Coin;

import java.util.List;
import java.util.NoSuchElementException;

public interface ICoinService {
    List<Coin> getCoinList(int page) throws JsonProcessingException;
    JsonNode getMarketChart(String coinId, int days) throws JsonProcessingException;
    Coin getCoinDetails(String coinId) throws JsonProcessingException;
    Coin findById(String coinId) throws NoSuchElementException;
    JsonNode searchCoin(String keyword) throws JsonProcessingException;
    List<Coin> getTop50CoinsByMarketCapRank() throws JsonProcessingException;
    JsonNode getTrendingCoins() throws JsonProcessingException;
}
