package com.client.ws.rasmooplus.model.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String cpf;

    @Column(name = "dt_subscription")
    private LocalDate dtSubscription;

    @Column(name = "dt_expiration")
    private LocalDate dtExpiration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscriptions_type_id")
    private SubscriptionType subscriptionType;

}
