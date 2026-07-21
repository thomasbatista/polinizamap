package dev.thomas.polinizamap.controller;

import dev.thomas.polinizamap.dto.request.AvistamentoRequest;
import dev.thomas.polinizamap.dto.request.ValidacaoRequest;
import dev.thomas.polinizamap.dto.response.AvistamentoResponse;
import dev.thomas.polinizamap.enums.StatusAvistamento;
import dev.thomas.polinizamap.service.AvistamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/avistamentos")
@RequiredArgsConstructor
@Tag(name = "Avistamentos", description = "Avistamentos de Fauna")
public class AvistamentoController {

    private final AvistamentoService avistamentoService;

    @PostMapping
    public ResponseEntity<AvistamentoResponse> registrar(@RequestBody @Valid AvistamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avistamentoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<AvistamentoResponse>> listar(
            @RequestParam(required = false) Long especieId,
            @RequestParam(required = false) Long regiaoId,
            @RequestParam(required = false) StatusAvistamento status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(avistamentoService.listar(especieId, regiaoId, status, inicio, fim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvistamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avistamentoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/validar")
    @PreAuthorize("hasAnyRole('PESQUISADOR', 'ADMIN')")
    public ResponseEntity<AvistamentoResponse> validar(@PathVariable Long id,
                                                       @RequestBody @Valid ValidacaoRequest request) {
        return ResponseEntity.ok(avistamentoService.validar(id, request));
    }
}