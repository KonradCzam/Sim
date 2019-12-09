package com.example.Sim.Model.Raport;


import com.example.Sim.Model.Jobs.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRoot extends FinanceEndTurnRapport {
    String description;
    Integer moneyEarned;
    String name = "Customer";
    String tier;
    Double happiness;
    public CustomerRoot(Customer customer){
        this.moneyEarned = customer.getMoneySpent();
        this.tier = customer.getTier();
        this.happiness = customer.getHappiness();
    }
}
