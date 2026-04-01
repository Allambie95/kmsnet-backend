package com.example.backend.BackEnd.service;

import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;

import com.example.backend.BackEnd.config.WebpayConfig;
import com.example.backend.BackEnd.dto.pago.PagoInitResponseDTO;
import com.example.backend.BackEnd.model.Orden;
import com.example.backend.BackEnd.model.Pago;
import com.example.backend.BackEnd.repository.CarritoRepository; // 👈 NUEVO
import com.example.backend.BackEnd.repository.OrdenRepository;
import com.example.backend.BackEnd.repository.PagoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final OrdenRepository ordenRepository;
    private final PagoRepository pagoRepository;
    private final WebpayConfig webpayConfig;
    private final CarritoRepository carritoRepository; // 👈 NUEVO

    @Transactional
    public PagoInitResponseDTO iniciarPago(Long ordenId, Pago.Proveedor proveedor) {

        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no existe: " + ordenId));

        if (orden.getEstado() != Orden.EstadoOrden.PENDING_PAYMENT) {
            throw new RuntimeException(
                    "La orden no está en estado PENDING_PAYMENT. Estado actual: " + orden.getEstado()
            );
        }

        Pago pago = Pago.builder()
                .orden(orden)
                .proveedor(proveedor)
                .estado(Pago.EstadoPago.INITIATED)
                .monto(orden.getTotal() == null ? BigDecimal.ZERO : orden.getTotal())
                .providerPaymentId(null)
                .rawPayload(null)
                .build();

        pago = pagoRepository.save(pago);

        String token = null;
        String url = null;

        if (proveedor == Pago.Proveedor.WEBPAY) {
            try {
                String buyOrder = "orden_" + orden.getId() + "_pago_" + pago.getId();
                String sessionId = "session_" + orden.getUsuario().getId();

                WebpayOptions options = new WebpayOptions(
                        webpayConfig.getCommerceCode(),
                        webpayConfig.getApiKey(),
                        IntegrationType.TEST
                );

                WebpayPlus.Transaction tx = new WebpayPlus.Transaction(options);

                double amount = pago.getMonto()
                        .setScale(0, RoundingMode.HALF_UP)
                        .doubleValue();

                var resp = tx.create(
                        buyOrder,
                        sessionId,
                        amount,
                        webpayConfig.getReturnUrl()
                );

                token = resp.getToken();
                url = resp.getUrl();

                pago.setProviderPaymentId(token);
                pago.setRawPayload("{\"url\":\"" + url + "\",\"token\":\"" + token + "\"}");

                pagoRepository.save(pago);

            } catch (Exception e) {
                throw new RuntimeException("Error iniciando Webpay: " + e.getMessage(), e);
            }
        }

        return PagoInitResponseDTO.builder()
                .pagoId(pago.getId())
                .ordenId(orden.getId())
                .proveedor(proveedor.name())
                .estado(pago.getEstado().name())
                .monto(pago.getMonto())
                .redirectUrl(url)
                .token(token)
                .build();
    }

    @Transactional
    public String confirmarPagoWebpay(String token) {
        try {
            WebpayOptions options = new WebpayOptions(
                    webpayConfig.getCommerceCode(),
                    webpayConfig.getApiKey(),
                    IntegrationType.TEST
            );

            WebpayPlus.Transaction tx = new WebpayPlus.Transaction(options);
            var resp = tx.commit(token);

            Pago pago = pagoRepository.findByProviderPaymentId(token)
                    .orElseThrow(() -> new RuntimeException("No se encontró pago con token: " + token));

            if ("AUTHORIZED".equalsIgnoreCase(resp.getStatus())) {
                pago.setEstado(Pago.EstadoPago.APPROVED);

                Orden orden = pago.getOrden();
                orden.setEstado(Orden.EstadoOrden.PAID);
                ordenRepository.save(orden);

                // 🔥 NUEVO: vaciar carrito del usuario
                Long usuarioId = orden.getUsuario().getId();
                carritoRepository.deleteByUsuarioId(usuarioId);

            } else {
                pago.setEstado(Pago.EstadoPago.REJECTED);
            }

            pago.setRawPayload(resp.toString());
            pagoRepository.save(pago);

            return resp.getStatus();

        } catch (Exception e) {
            throw new RuntimeException("Error confirmando pago Webpay: " + e.getMessage(), e);
        }
    }
}