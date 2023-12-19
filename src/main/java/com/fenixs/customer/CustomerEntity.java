package com.fenixs.customer;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table( name= "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                name = "customer_email_unique",
                columnNames = "email"
        )}
)
public class CustomerEntity {

    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    @Column(nullable = false)
    @NonNull
    private String email;

    @Column(nullable = false)
    @NonNull
    private Integer age;


}
