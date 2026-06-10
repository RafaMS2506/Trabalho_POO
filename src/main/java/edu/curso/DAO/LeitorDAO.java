package edu.curso.DAO;

import edu.curso.entity.Leitor;

import java.util.List;

public interface LeitorDAO {

    void cadastrar(Leitor leitor);
    void apagar(Leitor leitor);
    void atualizar (Leitor leitor);
    List<Leitor> pesquisarPorCpf(String cpf);
}
