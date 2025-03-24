package com.djeno.ipnexignbootcamp2025.persistence.models;

import com.djeno.ipnexignbootcamp2025.persistence.converters.CallTypeConverter;
import com.djeno.ipnexignbootcamp2025.persistence.enums.CallType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Модель для хранения CDR (Call Data Record) записей.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CDR {
    /**
     * Уникальный идентификатор записи CDR.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип вызова.
     * <p>
     *  * Допустимые значения:
     *  * <ul>
     *  *   <li>{@code 1} — исходящий звонок</li>
     *  *   <li>{@code 2} — входящий звонок</li>
     *  * </ul>
     *  * </p>
     */
    @Convert(converter = CallTypeConverter.class)
    private CallType callType;

    /**
     * Абонент, инициирующий звонок.
     */
    @ManyToOne
    @JoinColumn(name = "caller_msisdn", referencedColumnName = "msisdn")
    private Subscriber caller;

    /**
     * Абонент, принимающий звонок.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_msisdn", referencedColumnName = "msisdn")
    private Subscriber receiver;

    /**
     * Дата и время начала разговора.
     */
    private LocalDateTime startTime;

    /**
     * Дата и время конца разговора
     */
    private LocalDateTime endTime;
}
