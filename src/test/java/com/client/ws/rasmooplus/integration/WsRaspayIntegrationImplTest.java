package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class WsRaspayIntegrationImplTest {

    @Autowired
    private WsRaspayIntegration wsRaspayIntegration;

    @Test
    void createCustomerWhenDtoOk() {
        CustomerDto dto = new CustomerDto(null, "44635762025", "teste@teste.com", "Lucas", "Vieira");
        wsRaspayIntegration.createCustomer(dto);
    }

    @Test
    void createOrderWhenDtoOk() {
        OrderDto dto = new OrderDto(null, "6436c69416869c357e9017b6", BigDecimal.ZERO, "MONTH22" );
        wsRaspayIntegration.createOrder(dto);
    }

    @Test
    void processPaymentWhenDtoOk() {
        CreditCardDto creditCard = new CreditCardDto(123L,"44635762025",0L, 6L,"1234546812340010", 2027L);
        PaymentDto paymentDto = new PaymentDto(creditCard, "6436c69416869c357e9017b6","64378df621133f0897c7d617" );
        wsRaspayIntegration.processPayment(paymentDto);
    }
}
