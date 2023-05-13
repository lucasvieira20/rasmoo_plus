package com.client.ws.rasmooplus.service.implementations;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.enums.UserTypeEnum;
import com.client.ws.rasmooplus.exception.BusinessException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.integration.WsRaspayIntegration;
import com.client.ws.rasmooplus.mapper.UserPaymentoInfoMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CreditCardMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.OrderMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.rasmooplus.model.User;
import com.client.ws.rasmooplus.model.UserCredentials;
import com.client.ws.rasmooplus.model.UserPaymentInfo;
import com.client.ws.rasmooplus.model.UserType;
import com.client.ws.rasmooplus.repository.*;
import com.client.ws.rasmooplus.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Value("${webservices.rasplus.default.password}")
    private String defaultPass;
    private final UserRepository userRepository;
    private final UserPaymentInfoRepository userPaymentInfoRepository;
    private final WsRaspayIntegration wsRaspayIntegration;
    private final MailIntegration mailIntegration;
    private final UserDetailsRepository userDetailsRepository;
    private final UserTypeRepository userTypeRepository;

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    PaymentInfoServiceImpl(UserRepository userRepository,
                           UserPaymentInfoRepository userPaymentInfoRepository,
                           WsRaspayIntegration wsRaspayIntegration,
                           MailIntegration mailIntegration,
                           UserDetailsRepository userDetailsRepository,
                           UserTypeRepository userTypeRepository,
                           SubscriptionTypeRepository subscriptionTypeRepository
    ) {
        this.userRepository = userRepository;
        this.userPaymentInfoRepository = userPaymentInfoRepository;
        this.wsRaspayIntegration = wsRaspayIntegration;
        this.mailIntegration = mailIntegration;
        this.userDetailsRepository = userDetailsRepository;
        this.userTypeRepository = userTypeRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    @Override
    public Boolean process(PaymentProcessDto dto) {
        var userOpt = userRepository.findById(dto.getUserPaymentInfoDto().getUserId());

        if(userOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        User user = userOpt.get();

        if(Objects.nonNull(user.getSubscriptionType())) {
            throw new BusinessException("Pagamento não pode ser processado pois usuário já possui assinatura");
        }

        CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(user));
        OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.getId(), dto));
        PaymentDto paymentDto = PaymentMapper.build(customerDto.getId(), orderDto.getId(), CreditCardMapper.build(dto.getUserPaymentInfoDto(), user.getCpf()));
        Boolean successPayment = wsRaspayIntegration.processPayment(paymentDto);

        if(Boolean.TRUE.equals(successPayment)) {
            UserPaymentInfo userPaymentInfo = UserPaymentoInfoMapper.fromDtoToEntity(dto.getUserPaymentInfoDto(), user);
            userPaymentInfoRepository.save(userPaymentInfo);

            var userTypeOpt = userTypeRepository.findById(UserTypeEnum.ALUNO.getId());

            if(userTypeOpt.isEmpty()) {
                throw new NotFoundException("UserType não encontrado");
            }

            UserCredentials userCredentials = new UserCredentials(null, user.getEmail(), new BCryptPasswordEncoder().encode(defaultPass), userTypeOpt.get());

            userDetailsRepository.save(userCredentials);

            var subscritionTypeOpt = subscriptionTypeRepository.findByProductKey(dto.getProductKey());

            if (subscritionTypeOpt.isEmpty()) {
                throw new NotFoundException("SubscriptionType não encontrado");
            }

            user.setSubscriptionType(subscritionTypeOpt.get());

            userRepository.save(user);

            mailIntegration.send(user.getEmail(), "Acesso liberado", "Usuário: %S" +user.getEmail()+ " - Senha: " +defaultPass);

            return true;
        }

        return false;
    }
}
