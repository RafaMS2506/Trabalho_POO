package edu.curso.DAO;

import edu.curso.entity.Autor;
import java.util.List;

public interface AutorDAO {
    void cadastrar(Autor autor);
    void apagar(Autor autor);
    void atualizar(int id, Autor autor);
    Autor pesquisarPorCodigo(int id);
    List<Autor> listarTodos();
}
