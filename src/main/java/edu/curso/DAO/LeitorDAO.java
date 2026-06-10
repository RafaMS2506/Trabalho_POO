package edu.curso.DAO;

import edu.curso.entity.Leitor;

public interface LeitorDAO {

    void cadastrar(Leitor leitor);
    void apagar(Leitor leitor);
    void atualizar (Leitor leitor);
    Leitor pesquisarPorCpf(String cpf);
}
