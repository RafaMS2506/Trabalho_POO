package edu.curso.DAO;

import edu.curso.entity.Autor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AutorDAOImplementation implements AutorDAO {

    @Override
    public void cadastrar(Autor autor) {
        String sql = "INSERT INTO autor (nome, nacionalidade, data_nascimento, email, telefone) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, autor.getNome());
            stm.setString(2, autor.getNacionalidade());
            stm.setDate(3, autor.getDataNascimento() != null ? java.sql.Date.valueOf(autor.getDataNascimento()) : null);
            stm.setString(4, autor.getEmail());
            stm.setString(5, autor.getTelefone());

            stm.executeUpdate();
            System.out.println("Autor cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar autor!");
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Autor autor) {
        String sql = "UPDATE autor SET nome = ?, nacionalidade = ?, data_nascimento = ?, email = ?, telefone = ? WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, autor.getNome());
            stm.setString(2, autor.getNacionalidade());
            stm.setDate(3, java.sql.Date.valueOf(autor.getDataNascimento()));
            stm.setString(4, autor.getEmail());
            stm.setString(5, autor.getTelefone());
            stm.setInt(6, autor.getCodigo());

            stm.executeUpdate();
            System.out.println("Autor atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar autor!");
            e.printStackTrace();
        }
    }

    @Override
    public void apagar(Autor autor) {
        String sql = "DELETE FROM autor WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, autor.getCodigo());
            stm.executeUpdate();
            System.out.println("Autor removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover autor!");
            e.printStackTrace();
        }
    }

    @Override
    public Autor pesquisarPorCodigo(int id) {
        String sql = "SELECT * FROM autor WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return montarAutor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Autor não encontrado!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Autor> pesquisarPorNome(String nome) {
        String sql = "SELECT * FROM autor WHERE nome LIKE ?";
        List<Autor> lista = new ArrayList<>();
        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, "%" + nome + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(montarAutor(rs));
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Autor não encontrado!");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Autor> listarTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autor ORDER BY nome";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(montarAutor(rs));
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Erro ao listar autores!");
            e.printStackTrace();
        }
        return lista;
    }

    private Autor montarAutor(ResultSet rs) throws SQLException {
        LocalDate dataNasc = rs.getDate("data_nascimento") != null ? rs.getDate("data_nascimento").toLocalDate() : null;
        Autor autor = new Autor(rs.getString("nome"), rs.getString("nacionalidade"), dataNasc,
                rs.getString("email"), rs.getString("telefone"));
        autor.setCodigo(rs.getInt("codigo"));
        return autor;
    }
}
