package org.unir.msbookpayment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseResponse {

    private Long purchaseId;

    private String bookId;

    private Integer quantity;

    private String status;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private String message;
}
