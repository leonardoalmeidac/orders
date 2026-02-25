package org.unir.msbookpayment.service;

import org.unir.msbookpayment.dto.PurchaseRequest;
import org.unir.msbookpayment.dto.PurchaseResponse;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    PurchaseResponse createPurchase(PurchaseRequest purchaseRequest);

    Optional<PurchaseResponse> getPurchaseById(Long id);

    List<PurchaseResponse> getAllPurchases();
}
