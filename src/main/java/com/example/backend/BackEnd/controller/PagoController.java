package com.example.backend.BackEnd.controller;

import com.example.backend.BackEnd.dto.pago.PagoInitResponseDTO;
import com.example.backend.BackEnd.model.Pago;
import com.example.backend.BackEnd.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/iniciar/{ordenId}")
    public ResponseEntity<PagoInitResponseDTO> iniciarPago(
            @PathVariable Long ordenId,
            @RequestParam Pago.Proveedor proveedor
    ) {
        PagoInitResponseDTO resp = pagoService.iniciarPago(ordenId, proveedor);
        return ResponseEntity.ok(resp);
    }

   @RequestMapping(value = "/retorno", method = {RequestMethod.GET, RequestMethod.POST})
public ResponseEntity<String> retornoWebpay(@RequestParam("token_ws") String token) {

    try {
        String resultado = pagoService.confirmarPagoWebpay(token);

        String destino = "AUTHORIZED".equalsIgnoreCase(resultado)
                ? "http://localhost:5173/pago-exitoso"
                : "http://localhost:5173/pago-error";

        String html = "<html><body>" +
                "<script>window.location.href='" + destino + "';</script>" +
                "</body></html>";

        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(html);

    } catch (Exception e) {
        e.printStackTrace();

        String html = "<html><body>" +
                "<script>window.location.href='http://localhost:5173/pago-error';</script>" +
                "</body></html>";

        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(html);
    }
}
}