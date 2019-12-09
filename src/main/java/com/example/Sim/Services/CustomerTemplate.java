package com.example.Sim.Services;

import lombok.Getter;

@Getter
public class CustomerTemplate {
    private final Double low_start_happines = 50.0;
    private final Double mid_start_happines = 25.0;
    private final Double high_start_happines = 0.0;

    private final Integer low_max_rand_gold = 100;
    private final Integer low_min_gold = 0;
    private final Integer mid_max_rand_gold = 150;
    private final Integer mid_min_gold = 100;
    private final Integer high_max_rand_gold = 250;
    private final Integer high_min_gold = 250;

    private final Integer minGold;
    private final Integer maxRandGold;
    private final Double startHappiness;
    private final String tier;

    public CustomerTemplate(String tier) {
        this.tier = tier;
        switch (tier) {
            case "low": {
                minGold = low_min_gold;
                maxRandGold = low_max_rand_gold;
                startHappiness = low_start_happines;
                break;
            }
            case "mid": {
                minGold = mid_min_gold;
                maxRandGold = mid_max_rand_gold;
                startHappiness = mid_start_happines;
                break;
            }
            case "high": {
                minGold = high_min_gold;
                maxRandGold = high_max_rand_gold;
                startHappiness = high_start_happines;
                break;
            }
            default: {
                minGold = low_min_gold;
                maxRandGold = low_max_rand_gold;
                startHappiness = low_start_happines;
                break;
            }
        }
    }
}
