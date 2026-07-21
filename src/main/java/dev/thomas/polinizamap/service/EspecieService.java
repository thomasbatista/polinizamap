package dev.thomas.polinizamap.service;

import dev.thomas.polinizamap.dto.request.EspecieRequest;
import dev.thomas.polinizamap.dto.response.EspecieResponse;
import dev.thomas.polinizamap.entity.Especie;
import dev.thomas.polinizamap.exception.BusinessException;
import dev.thomas.polinizamap.exception.NotFoundException;
import dev.thomas.polinizamap.repository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecieService {

    private final EspecieRepository especieRepository;

    public EspecieResponse criar(EspecieRequest request) {
        if (especieRepository.existsByNomeCientifico(request.nomeCientifico())) {
            throw new BusinessException("Espécie já cadastrada com esse nome científico");
        }

        Especie especie = Especie.builder()
                .nomePopular(request.nomePopular())
                .nomeCientifico(request.nomeCientifico())
                .statusConservacao(request.statusConservacao())
                .build();

        return EspecieResponse.from(especieRepository.save(especie));
    }

    public List<EspecieResponse> listar() {
        return especieRepository.findAll()
                .stream()
                .map(EspecieResponse::from)
                .toList();
    }

    public EspecieResponse buscarPorId(Long id) {
        return especieRepository.findById(id)
                .map(EspecieResponse::from)
                .orElseThrow(() -> new NotFoundException("Espécie não encontrada"));
    }

    public EspecieResponse atualizar(Long id, EspecieRequest request) {
        Especie especie = especieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Espécie não encontrada"));

        if (!especie.getNomeCientifico().equals(request.nomeCientifico())
                && especieRepository.existsByNomeCientifico(request.nomeCientifico())) {
            throw new BusinessException("Já existe uma espécie com esse nome científico");
        }

        especie.setNomePopular(request.nomePopular());
        especie.setNomeCientifico(request.nomeCientifico());
        especie.setStatusConservacao(request.statusConservacao());

        return EspecieResponse.from(especieRepository.save(especie));
    }

    public void deletar(Long id) {
        if (!especieRepository.existsById(id)) {
            throw new NotFoundException("Espécie não encontrada");
        }
        especieRepository.deleteById(id);
    }
}