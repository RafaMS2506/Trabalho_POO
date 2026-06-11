package edu.curso.controller;

import edu.curso.DAO.AutorDAO;
import edu.curso.DAO.AutorDAOImplementation;
import edu.curso.entity.Autor;

import java.util.List;

public class AutorController {

    private final AutorDAO autorDAO = new AutorDAOImplementation();

    public void cadastrar(Autor autor) {
        validar(autor);
        autorDAO.cadastrar(autor);
    }

    public void atualizar(int id, Autor autor) {
        validar(autor);
        autorDAO.atualizar(id, autor);
    }

    public void apagar(Autor autor) {
        autorDAO.apagar(autor);
    }

    public List<Autor> listar() {
        return autorDAO.listarTodos();
    }

    private void validar(Autor autor) {
        if (autor.getNome() == null || autor.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do autor não pode ficar vazio.");
        }
        if (autor.getTelefone() == null || autor.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O telefone não pode ficar vazio.");
        }
        if (autor.getEmail() == null || !autor.getEmail().contains("@") || !autor.getEmail().contains(".")) {
            throw new IllegalArgumentException("O e-mail deve ter um formato válido.");
        }
        if (autor.getDataNascimento() == null) {
            throw new IllegalArgumentException("A data de nascimento deve ser válida.");
        }
    }
}
