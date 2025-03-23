package com.djeno.ipnexignbootcamp2025.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель для хранения абонентов
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Subscriber {

    /**
     * Уникальный номер абонента
     */
    @Id
    private String msisdn;
}
