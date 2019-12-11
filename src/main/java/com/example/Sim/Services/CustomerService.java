package com.example.Sim.Services;

import com.example.Sim.Model.CustomerTemplate;
import com.example.Sim.Model.Jobs.Customer;
import com.example.Sim.Model.Jobs.JobCustomers;
import lombok.Getter;

import javax.annotation.Resource;
import java.util.*;

@Getter
public class CustomerService {
    @Resource
    JobService jobService;


    Map<String, JobCustomers> customersMap = new HashMap<>();

    public void generateAllCustomers() {
        jobService.getJobList().forEach(job -> {
            if (job.getName().equals("Brothel") || job.getName().equals("Gambling Den")) {
                JobCustomers jobCustomers = new JobCustomers();
                List<Customer> customers = generateCustomersForJobAndTier(job.getName(), new CustomerTemplate("low"));
                customers.addAll(generateCustomersForJobAndTier(job.getName(), new CustomerTemplate("mid")));
                customers.addAll(generateCustomersForJobAndTier(job.getName(), new CustomerTemplate("high")));

                jobCustomers.setCustomesList(customers);

                customersMap.put(job.getName(), jobCustomers);
            }
        });
    }

    private List<Customer> generateCustomersForJobAndTier(String jobName, CustomerTemplate customerTemplate) {

        List<Customer> customerList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            customerList.add(new Customer(jobName, customerTemplate.getTier(),
                    rand.nextInt(customerTemplate.getMaxRandGold()) + customerTemplate.getMinGold(),
                    customerTemplate.getStartHappiness(),
                    0));
        }
        return customerList;
    }

    public Map<String, JobCustomers> getDividedCustomersByPopularity() {
        //TODO customers going to diffrent brothel
        return customersMap;
    }




}
