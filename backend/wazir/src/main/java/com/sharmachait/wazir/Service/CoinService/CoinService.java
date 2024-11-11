package com.sharmachait.wazir.Service.CoinService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharmachait.wazir.Model.Entity.Coin;
import com.sharmachait.wazir.Repository.ICoinRepository;
import org.springframework.core.env.Environment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CoinService implements ICoinService {
    @Autowired
    private ICoinRepository coinRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Environment env;
    @Autowired
    private ModelMapper modelMapper;

    private String baseUrl = "https://api.coingecko.com/api/v3";

    @Override
    public List<Coin> getCoinList(int page) throws JsonProcessingException {
        String coinListUrl = baseUrl + "/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        String response = getResponseBody(coinListUrl);
        List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>(){});
        return coins;
    }

    @Override
    public JsonNode getMarketChart(String coinId, int days) throws JsonProcessingException {
        String marketsUrl = baseUrl + "/coins/"+coinId+"/market_chart?vs_currency=usd&days=" + days;
        String response =  getResponseBody(marketsUrl);
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode;
    }

    @Override
    public Coin getCoinDetails(String coinId) throws JsonProcessingException {
        String coinUrl = baseUrl + "/coins/"+coinId;
        String response =  getResponseBody(coinUrl);
        Coin coin = jsonNodeToCoin(response);
        return coinRepository.save(coin);
    }

    @Override
    public Coin findById(String coinId) throws NoSuchElementException {
        Optional<Coin> coin = coinRepository.findById(coinId);
        return coin.get();
    }

    @Override
    public JsonNode searchCoin(String keyword) throws JsonProcessingException {
        String coinSearchUrl = baseUrl + "/search?query=" + keyword;
        String response = getResponseBody(coinSearchUrl);
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode;
//        List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>(){});
//        return coins;
//        return jsonNodeToCoinList(response);
    }

    @Override
    public List<Coin> getTop50CoinsByMarketCapRank() throws JsonProcessingException {
        String coinsUrl = baseUrl + "/coins/markets?vs_currency=usd&per_page=50&page=1";
        String response = getResponseBody(coinsUrl);
        List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>(){});
        return coins;
    }

    @Override
    public JsonNode getTrendingCoins() throws JsonProcessingException {
        String coinSearchUrl = baseUrl + "/search/trending";
        String response = getResponseBody(coinSearchUrl);
//        List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>(){});
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode;
    }

    private String getResponseBody(String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("x-cg-api-key", env.getProperty("geckocoinkey"));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,  entity, String.class);
            return response.getBody();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    private List<Coin> jsonNodeToCoinList(String json) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(json);
        List<Coin> coins = new ArrayList<>();
        JsonNode coinsNode = node.get("coins");
        if (coinsNode != null && coinsNode.isArray()) {
            for (JsonNode coinNode : coinsNode) {
                Coin coin = jsonNodeToCoin(coinNode.toString());
                coins.add(coin);
            }
        }
        return coins;
    }
    private Coin jsonNodeToCoin(String json) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(json);
        Coin coin = new Coin();
        coin.setId(node.get("id").asText());
        coin.setName(node.get("name").asText());
        coin.setSymbol(node.get("symbol").asText());
        coin.setImage(node.get("image").get("large").asText());
        JsonNode marketData = node.get("market_data");
        coin.setCurrentPrice(new BigDecimal(marketData.get("current_price").get("usd").asText()));
        coin.setMarketCap(new BigDecimal(marketData.get("market_cap").get("usd").asText()));
        coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
        coin.setTotalVolume(new BigDecimal(marketData.get("total_volume").get("usd").asText()));
        coin.setHigh24h(new BigDecimal(marketData.get("high_24h").get("usd").asText()));
        coin.setLow24h(new BigDecimal(marketData.get("low_24h").get("usd").asText()));
        coin.setPriceChange24h(new BigDecimal(marketData.get("price_change_24h").asText()));
        coin.setPriceChangePercentage24h(new BigDecimal(marketData.get("price_change_percentage_24h").asText()));
        coin.setMarketCapChange24h(new BigDecimal(marketData.get("market_cap_change_24h").asText()));
        coin.setMarketCapChangePercentage24h(new BigDecimal(marketData.get("market_cap_change_percentage_24h").asText()));
        coin.setTotalSupply(new BigDecimal(marketData.get("total_supply").asText()));
        return coin;
    }
}
