package org.unir.msbookpayment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unir.msbookpayment.client.BookCatalogueClient;
import org.unir.msbookpayment.client.dto.BookCatalogueResponse;
import org.unir.msbookpayment.dto.PurchaseRequest;
import org.unir.msbookpayment.dto.PurchaseResponse;
import org.unir.msbookpayment.model.Purchase;
import org.unir.msbookpayment.model.PurchaseStatus;
import org.unir.msbookpayment.repository.PurchaseRepository;
import org.unir.msbookpayment.service.PurchaseService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final BookCatalogueClient bookCatalogueClient;

    @Override
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        // 1 Consultar Catalago de Libros
        BookCatalogueResponse book = bookCatalogueClient.getBookById(request.getBookId());

        // 2 Crear entidad base de la compra
        Purchase purchase = new Purchase();
        purchase.setBookId(request.getBookId());
        purchase.setQuantity(request.getQuantity());
        purchase.setCreatedAt(LocalDateTime.now());

        // 3. Validaciones de negocio
        if (book == null) {
            purchase.setStatus(PurchaseStatus.REJECTED);
            purchase.setRejectionReason("Book not found");
            purchase.setTotalAmount(BigDecimal.ZERO);

        } else if (!Boolean.TRUE.equals(book.getVisible())) {
            purchase.setStatus(PurchaseStatus.REJECTED);
            purchase.setRejectionReason("Book is hidden");
            purchase.setTotalAmount(BigDecimal.ZERO);

        } else {
            purchase.setStatus(PurchaseStatus.ACCEPTED);
            purchase.setTotalAmount(
                    BigDecimal.valueOf(request.getQuantity()).multiply(BigDecimal.TEN));
        }

        // 4. Persistir
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // 5. Mapear respuesta
        return mapToResponse(savedPurchase);
    }

    @Override
    public Optional<PurchaseResponse> getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .map(this::mapToResponse);
    }

    private PurchaseResponse mapToResponse(Purchase purchase) {

        PurchaseResponse response = new PurchaseResponse();
        response.setPurchaseId(purchase.getId());
        response.setBookId(purchase.getBookId());
        response.setQuantity(purchase.getQuantity());
        response.setStatus(purchase.getStatus().name());
        response.setTotalAmount(purchase.getTotalAmount());
        response.setCreatedAt(purchase.getCreatedAt());

        if (purchase.getStatus() == PurchaseStatus.REJECTED) {
            response.setMessage(purchase.getRejectionReason());
        } else {
            response.setMessage("Purchase completed successfully");
        }

        return response;
    }

    @Override
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}
