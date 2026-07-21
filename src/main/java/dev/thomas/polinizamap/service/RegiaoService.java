package dev.thomas.polinizamap.service;

import dev.thomas.polinizamap.dto.request.RegiaoRequest;
import dev.thomas.polinizamap.dto.response.RegiaoResponse;
import dev.thomas.polinizamap.entity.Regiao;
import dev.thomas.polinizamap.exception.BusinessException;
import dev.thomas.polinizamap.exception.NotFoundException;
import dev.thomas.polinizamap.repository.RegiaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;

    public RegiaoResponse criar(RegiaoRequest request) {
        if (regiaoRepository.existsByNomeAndCidade(request.nome(), request.cidade())) {
            throw new BusinessException("Região já cadastrada nessa cidade");
        }

        Regiao regiao = Regiao.builder()
                .nome(request.nome())
                .cidade(request.cidade())
                .estado(request.estado())
                .build();

        return RegiaoResponse.from(regiaoRepository.save(regiao));
    }

    public List<RegiaoResponse> listar() {
        return regiaoRepository.findAll()
                .stream()
                .map(RegiaoResponse::from)
                .toList();
    }

    public RegiaoResponse buscarPorId(Long id) {
        return regiaoRepository.findById(id)
                .map(RegiaoResponse::from)
                .orElseThrow(() -> new NotFoundException("Região não encontrada"));
    }

    public RegiaoResponse atualizar(Long id, RegiaoRequest request) {
        Regiao regiao = regiaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Região não encontrada"));

        if (!regiao.getNome().equals(request.nome())
                && regiaoRepository.existsByNomeAndCidade(request.nome(), request.cidade())) {
            throw new BusinessException("Já existe uma região com esse nome nessa cidade");
        }

        regiao.setNome(request.nome());
        regiao.setCidade(request.cidade());
        regiao.setEstado(request.estado());

        return RegiaoResponse.from(regiaoRepository.save(regiao));
    }

    public void deletar(Long id) {
        if (!regiaoRepository.existsById(id)) {
            throw new NotFoundException("Região não encontrada");
        }
        regiaoRepository.deleteById(id);
    }
}