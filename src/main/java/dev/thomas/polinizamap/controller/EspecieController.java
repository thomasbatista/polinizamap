package dev.thomas.polinizamap.controller;

import dev.thomas.polinizamap.dto.request.EspecieRequest;
import dev.thomas.polinizamap.dto.response.EspecieResponse;
import dev.thomas.polinizamap.service.EspecieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especies")
@RequiredArgsConstructor
@Tag(name = "Espécies", description = "Espécies de Animais")
public class EspecieController {

    private final EspecieService especieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecieResponse> criar(@RequestBody @Valid EspecieRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especieService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<EspecieResponse>> listar() {
        return ResponseEntity.ok(especieService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecieResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especieService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecieResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid EspecieRequest request) {
        return ResponseEntity.ok(especieService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especieService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}