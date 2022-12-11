package com.anagracetech.customer;

import com.anagracetech.clients.fraud.FraudCheckResponse;
import com.anagracetech.clients.fraud.FraudClient;
import com.anagracetech.clients.notification.NotificationClient;
import com.anagracetech.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegisterationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);
        // todo: Check if fraudster
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraud()){
            throw new IllegalStateException("fraudster");
        }

        // todo: send notification
        // todo: make it async. i.e add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to AnagraceTech....",
                                customer.getFirstName())
                )
        );
    }
}
