package edu.curso.DAO;

import edu.curso.entity.Autor;
import edu.curso.entity.Emprestimo;
import edu.curso.entity.ItemEmprestimo;
import edu.curso.entity.Leitor;
import edu.curso.entity.Livro;
import edu.curso.entity.StatusItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAOImplementation implements EmprestimoDAO {

    @Override
    public void cadastrar(Emprestimo emprestimo) {
        String sqlEmp   = "INSERT INTO emprestimo (codigo_leitor, data_emprestimo, data_devolucao_prevista, status) VALUES (?, ?, ?, ?)";
        String sqlItem  = "INSERT INTO item_emprestimo (codigo_emprestimo, codigo_livro, data_devolucao, status_item) VALUES (?, ?, ?, ?)";
        String sqlBaixa = "UPDATE livro SET quantidade = quantidade - 1 WHERE codigo = ?";

        try (Connection con = Conexao.getConnection()) {
            con.setAutoCommit(false);
            int codigoEmp;
            try (PreparedStatement stm = con.prepareStatement(sqlEmp, Statement.RETURN_GENERATED_KEYS)) {
                stm.setInt(1, emprestimo.getLeitor().getCodigo());
                stm.setDate(2, java.sql.Date.valueOf(emprestimo.getDataEmprestimo()));
                stm.setDate(3, java.sql.Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
                stm.setBoolean(4, emprestimo.isStatus());
                stm.executeUpdate();

                try (ResultSet chaves = stm.getGeneratedKeys()) {
                    chaves.next();
                    codigoEmp = chaves.getInt(1);
                    emprestimo.setCodigo(codigoEmp);
                }
            }

            for (ItemEmprestimo item : emprestimo.getListaLivros()) {
                try (PreparedStatement stmItem = con.prepareStatement(sqlItem)) {
                    stmItem.setInt(1, codigoEmp);
                    stmItem.setInt(2, item.getLivro().getCodigo());
                    stmItem.setDate(3, item.getDataDevolucao() != null ? java.sql.Date.valueOf(item.getDataDevolucao()) : null);
                    stmItem.setString(4, item.getStatus().name());
                    stmItem.executeUpdate();
                }
                try (PreparedStatement stmBaixa = con.prepareStatement(sqlBaixa)) {
                    stmBaixa.setInt(1, item.getLivro().getCodigo());
                    stmBaixa.executeUpdate();
                }
            }
            con.commit();
            System.out.println("Empréstimo cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar empréstimo!");
            e.printStackTrace();
        }
    }

    @Override
    public void apagar(Emprestimo emprestimo) {
        String sqlEmprestimo = "DELETE FROM emprestimo WHERE codigo = ?";
        String sqlItens = "DELETE FROM item_emprestimo WHERE codigo_emprestimo = ?";

        try (Connection con = Conexao.getConnection()) {
            // É necessário apagar primeiro os itens do emprestimo
            try (PreparedStatement stmItens = con.prepareStatement(sqlItens)){
                stmItens.setInt(1, emprestimo.getCodigo());
                stmItens.executeUpdate();
            }
            // Com os itens apagados, podemos apagar o emprestimo
            try (PreparedStatement stmEmp = con.prepareStatement(sqlEmprestimo)) {
                stmEmp.setInt(1, emprestimo.getCodigo());
                stmEmp.executeUpdate();
            }
            System.out.println("Empréstimo removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover empréstimo!");
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimo SET codigo_leitor = ?, data_emprestimo = ?, data_devolucao_prevista = ?, " +
                "status = ? WHERE codigo = ?";
        try (Connection con = Conexao.getConnection(); PreparedStatement stm =con.prepareStatement(sql)) {
            stm.setInt(1, emprestimo.getLeitor().getCodigo());
            stm.setDate(2, Date.valueOf(emprestimo.getDataEmprestimo()));
            stm.setDate(3, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            stm.setBoolean(4, emprestimo.isStatus());
            stm.setInt(5, emprestimo.getCodigo());
            stm.executeUpdate();
            System.out.println("Empréstimo atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar empréstimo!");
            e.printStackTrace();
        }

    }

    @Override
    public Emprestimo pesquisarPorCodigo(int codigo) {
        String sql = "SELECT * FROM emprestimo WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1,codigo);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return montarEmprestimo(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Empréstimo não encontrado!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo ORDER BY data_emprestimo DESC";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarEmprestimo(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos!");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Leitor> listarLeitores() {
        List<Leitor> lista = new ArrayList<>();
        String sql = "SELECT * FROM leitor ORDER BY nome";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarLeitor(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar leitores!");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Livro> listarLivros() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livro ORDER BY titulo";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarLivro(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros!");
            e.printStackTrace();
        }
        return lista;
    }

    private Emprestimo montarEmprestimo(ResultSet rs) throws SQLException {
        Leitor leitor = buscarLeitor(rs.getInt("codigo_leitor"));
        Emprestimo emp = new Emprestimo(leitor);
        emp.setCodigo(rs.getInt("codigo"));
        emp.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
        emp.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());
        emp.setStatus(rs.getBoolean("status"));
        emp.setListaLivros(buscarItens(emp.getCodigo()));
        return emp;
    }

    private List<ItemEmprestimo> buscarItens(int codigoEmprestimo) throws SQLException {
        List<ItemEmprestimo> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_emprestimo WHERE codigo_emprestimo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, codigoEmprestimo);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    ItemEmprestimo item = new ItemEmprestimo(buscarLivro(rs.getInt("codigo_livro")));
                    item.setDataDevolucao(rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null);
                    item.setStatus(StatusItem.valueOf(rs.getString("status_item")));
                    itens.add(item);
                }
            }
        }
        return itens;
    }

    private Leitor buscarLeitor(int codigo) throws SQLException {
        String sql = "SELECT * FROM leitor WHERE codigo = ?";
        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, codigo);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return montarLeitor(rs);
                }
            }
        }
        return null;
    }

    private Livro buscarLivro(int codigo) throws SQLException {
        String sql = "SELECT * FROM livro WHERE codigo = ?";
        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, codigo);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return montarLivro(rs);
                }
            }
        }
        return null;
    }

    private Leitor montarLeitor(ResultSet rs) throws SQLException {
        Leitor leitor = new Leitor(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("cep"),
                rs.getDate("data_cadastro").toLocalDate()
                );
        leitor.setCodigo(rs.getInt("codigo"));
        leitor.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
        return leitor;
    }

    private Livro montarLivro(ResultSet rs) throws SQLException {
        Autor autor = buscarAutorPorCodigo(rs.getInt("codigo_autor"));
        Livro livro = new Livro(
                rs.getString("titulo"),
                rs.getString("editora"),
                rs.getInt("ano"),
                rs.getString("isbn"),
                rs.getInt("quantidade"),
                autor);
        livro.setCodigo(rs.getInt("codigo"));
        return livro;
    }

    private Autor buscarAutorPorCodigo (int codigo) throws SQLException  {
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
        }
        return null;
    }
}
