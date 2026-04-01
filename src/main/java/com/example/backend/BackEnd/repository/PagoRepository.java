package com.example.backend.BackEnd.repository;

import com.example.backend.BackEnd.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Todos los pagos asociados a una orden
    List<Pago> findByOrdenId(Long ordenId);

    // Último pago creado para una orden (útil si el cliente reintenta pagar)
    Optional<Pago> findTopByOrdenIdOrderByFechaCreacionDesc(Long ordenId);

    // Para callbacks/webhooks: buscar el pago por id externo del proveedor
    Optional<Pago> findByProviderPaymentId(String providerPaymentId);
}
