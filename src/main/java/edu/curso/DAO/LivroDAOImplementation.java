package edu.curso.DAO;

import edu.curso.entity.Autor;
import edu.curso.entity.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LivroDAOImplementation implements LivroDAO {

    @Override
    public void cadastrar(Livro livro) {
        String sql = "INSERT INTO livro (titulo, editora, ano, isbn, quantidade, codigo_autor) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, livro.getTitulo());
            stm.setString(2, livro.getEditora());
            stm.setInt(3, livro.getAno());
            stm.setString(4, livro.getIsbn());
            stm.setInt(5, livro.getQuantidade());
            stm.setInt(6, livro.getAutor().getCodigo());

            stm.executeUpdate();
            System.out.println("Livro cadastrado com sucesso!");

        } catch (SQLException e) {

        }
    }

    @Override
    public void apagar(Livro livro) {
        String sql = "DELETE FROM livro WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, livro.getCodigo());

            stm.executeUpdate();
            System.out.println("Livro removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro.");
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, editora = ?, ano = ?, isbn = ?, quantidade = ?, codigo_autor = ? WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, livro.getTitulo());
            stm.setString(2, livro.getEditora());
            stm.setInt(3, livro.getAno());
            stm.setString(4, livro.getIsbn());
            stm.setInt(5, livro.getQuantidade());
            stm.setInt(6, livro.getAutor().getCodigo());

            stm.setInt(7, livro.getCodigo());

            stm.executeUpdate();
            System.out.println("Livro atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro,");
            e.printStackTrace();
        }

    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM livro WHERE titulo LIKE ?";
        List<Livro> listaLivros = new ArrayList<>();

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1,"%" + titulo + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String tituloLivro = rs.getString("titulo");
                String editora = rs.getString("editora");
                int ano = rs.getInt("ano");
                String isbn = rs.getString("isbn");
                int quantidade = rs.getInt("quantidade");
                int codigoAutor = rs.getInt("codigo_autor");

                Autor autor = buscarAutorPorCodigo(codigoAutor);
                Livro l = new Livro(tituloLivro, editora, ano, isbn, quantidade, autor);
                l.setCodigo(codigo);

                listaLivros.add(l);
            }
            return listaLivros;
        } catch (SQLException e) {
            System.err.println("Livro não encontrado.");
            e.printStackTrace();
        }
        return listaLivros;
    }

    @Override
    public Autor buscarAutorPorNome(String nome) {
        String sql = "SELECT * FROM autor WHERE nome = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, nome);
            try (ResultSet rs = stm.executeQuery();) {

                if (rs.next()) {
                    int codigoAutor = rs.getInt("codigo");
                    String nomeAutor = rs.getString("nome");
                    String nacionalidade = rs.getString("nacionalidade");
                    LocalDate dataNasc = rs.getDate("data_nascimento").toLocalDate();
                    String email = rs.getString("email");
                    String telefone = rs.getString("telefone");

                    Autor autor = new Autor(nomeAutor, nacionalidade, dataNasc, email, telefone);
                    autor.setCodigo(codigoAutor);
                    System.out.println("Autor encontrado");
                    return autor;
                }
            }
        } catch (SQLException e) {
            System.err.println("Autor não encontrado.");
            e.printStackTrace();
        }
        return null;
    }

    public Autor buscarAutorPorCodigo (int codigo) {
        String sql = "SELECT * FROM autor WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, codigo);
            try (ResultSet rs = stm.executeQuery();) {

                if (rs.next()) {
                    int codigoAutor = rs.getInt("codigo");
                    String nome = rs.getString("nome");
                    String nacionalidade = rs.getString("nacionalidade");
                    LocalDate dataNasc = rs.getDate("data_nascimento").toLocalDate();
                    String email = rs.getString("email");
                    String telefone = rs.getString("telefone");

                    Autor autor = new Autor(nome, nacionalidade, dataNasc, email, telefone);
                    autor.setCodigo(codigoAutor);
                    System.out.println("Autor encontrado");
                    return autor;
                }
            }
        } catch (SQLException e) {
            System.err.println("Autor não encontrado.");
            e.printStackTrace();
        }
        return null;
    }

    public int buscarCodigoAutor(String nome) {
        String sql = "SELECT codigo FROM autor WHERE nome = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nome);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int codigo = rs.getInt("codigo");
                return codigo;
            }
        } catch (SQLException e) {
            System.err.println("Autor não cadastrado!");
            System.out.println("Por gentileza, cadastre o autor antes de cadastrar o livro.");
            e.printStackTrace();
        }
        return 0;
    }

}
