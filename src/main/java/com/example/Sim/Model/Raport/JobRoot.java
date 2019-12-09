package com.example.Sim.Model.Raport;

import com.example.Sim.Model.Jobs.Customer;
import com.example.Sim.Model.Jobs.JobCustomers;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class JobRoot extends FinanceEndTurnRapport {
    String name;
    Integer moneyEarned;
    String description;
    String path;

    String popularity;

    List<CustomerRoot> allCustomers = new ArrayList<>();
    public JobRoot(){};
    public JobRoot(String jobName, JobCustomers jobCustomers) {
        this.setName(jobName);

        this.allCustomers = createCustomersNodes(jobCustomers.getCustomesList());


        }
    private List<CustomerRoot> createCustomersNodes(List<Customer> customerList) {
        List<CustomerRoot> customerRoots = new ArrayList<>();
        customerList.forEach(customer -> customerRoots.add(new CustomerRoot(customer)));
        this.moneyEarned = calculateMoneyEarned(customerList);
        return customerRoots;
    }

    private Integer calculateMoneyEarned(List<Customer> customers) {

        AtomicReference<Integer> result = new AtomicReference<>(0);
        customers.forEach(customer -> result.updateAndGet(v -> v + customer.getMoneySpent()));
        return result.get();
    }
}
