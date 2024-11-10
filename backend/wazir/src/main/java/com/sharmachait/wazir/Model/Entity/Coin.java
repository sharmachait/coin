package com.sharmachait.wazir.Model.Entity;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Coin {
    @Id
    private String id;

    private String symbol;

    private String name;

    private String image;

    @JsonProperty("current_price")
    private BigDecimal currentPrice;

    @JsonProperty("market_cap")
    private BigDecimal marketCap;

    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;

    @JsonProperty("fully_diluted_valuation")
    private BigDecimal fullyDilutedValuation;

    @JsonProperty("total_volume")
    private BigDecimal totalVolume;

    @JsonProperty("high_24h")
    private BigDecimal high24h;

    @JsonProperty("low_24h")
    private BigDecimal low24h;

    @JsonProperty("price_change_24h")
    private BigDecimal priceChange24h;

    @JsonProperty("price_change_percentage_24h")
    private BigDecimal priceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    private BigDecimal marketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    private BigDecimal marketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    private BigDecimal circulatingSupply;

    @JsonProperty("total_supply")
    private BigDecimal totalSupply;

    @JsonProperty("max_supply")
    private BigDecimal maxSupply;

    private BigDecimal ath;

    @JsonProperty("ath_change_percentage")
    private BigDecimal athChangePercentage;

    @JsonProperty("ath_date")
    private String athDate;

    private BigDecimal atl;

    @JsonProperty("atl_change_percentage")
    private BigDecimal atlChangePercentage;

    @JsonProperty("atl_date")
    private String atlDate;

    @JsonProperty("last_updated")
    private String lastUpdated;

    @Embedded
    private Roi roi;
}