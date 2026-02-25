package org.unir.msbookpayment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unir.msbookpayment.model.Purchase;

;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
