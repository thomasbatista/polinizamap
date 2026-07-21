package dev.thomas.polinizamap.controller;

import dev.thomas.polinizamap.dto.request.RegiaoRequest;
import dev.thomas.polinizamap.dto.response.RegiaoResponse;
import dev.thomas.polinizamap.service.RegiaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regioes")
@RequiredArgsConstructor
@Tag(name = "Regiões", description = "Regiões e Locais")
public class RegiaoController {

    private final RegiaoService regiaoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegiaoResponse> criar(@RequestBody @Valid RegiaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regiaoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<RegiaoResponse>> listar() {
        return ResponseEntity.ok(regiaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegiaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(regiaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegiaoResponse> atualizar(@PathVariable Long id,
                                                    @RequestBody @Valid RegiaoRequest request) {
        return ResponseEntity.ok(regiaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        regiaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}