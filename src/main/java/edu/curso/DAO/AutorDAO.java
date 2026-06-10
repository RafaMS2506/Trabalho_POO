package edu.curso.DAO;

import edu.curso.entity.Autor;
import java.util.List;

public interface AutorDAO {
    void cadastrar(Autor autor);
    void apagar(Autor autor);
    void atualizar(Autor autor);
    Autor pesquisarPorCodigo(int id);
    List<Autor> pesquisarPorNome(String nome);
    List<Autor> listarTodos();
}
