package com.sharmachait.wazir.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharmachait.wazir.Service.CoinService.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sharmachait.wazir.Model.Entity.Coin;

import java.util.List;

@RestController
@RequestMapping("/api/coins")
public class CoinController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CoinService coinService;

    @GetMapping
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) {
        try {
            List<Coin> coins = coinService.getCoinList(page);
            return new ResponseEntity<>(coins, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@PathVariable("coinId") String coinId, @RequestParam("days") int days) {
        try {
            JsonNode marketChart = coinService.getMarketChart(coinId, days);
            return new ResponseEntity<>(marketChart, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("query") String query) {
        try {
            JsonNode coins = coinService.searchCoin(query);
            return new ResponseEntity<>(coins, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/top50")
    public ResponseEntity<List<Coin>> top50() {
        try {
            List<Coin> coins = coinService.getTop50CoinsByMarketCapRank();
            return new ResponseEntity<>(coins, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<JsonNode> trending() {
        try {
            JsonNode coins = coinService.getTrendingCoins();
            return new ResponseEntity<>(coins, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/coinDetails")
    public ResponseEntity<Coin> coinDetails(@RequestParam("coinId") String coinId) {
        try {
            Coin coin = coinService.getCoinDetails(coinId);
            return new ResponseEntity<>(coin, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/coin/{id}")
    public ResponseEntity<Coin> coinById(@PathVariable("id") String id) {
        try {
            Coin coins = coinService.findById(id);
            return new ResponseEntity<>(coins, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
