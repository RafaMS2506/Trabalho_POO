package edu.curso.DAO;

import edu.curso.entity.Leitor;
import org.mariadb.jdbc.export.Prepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LeitorDAOImplementation implements LeitorDAO{

    // Recebe um objeto do tipo Leitor e cadastra no banco de dados
    @Override
    public void cadastrar(Leitor leitor) {
        String sql = "INSERT INTO leitor (cpf, nome, telefone, email, cep, data_cadastro) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, leitor.getCpf());
            stm.setString(2, leitor.getNome());
            stm.setString(3, leitor.getTelefone());
            stm.setString(4, leitor.getEmail());
            stm.setString(5, leitor.getCep());
            stm.setDate(6,java.sql.Date.valueOf(leitor.getDataCadastro()));

            stm.executeUpdate();
            System.out.println("Leitor cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar leitor!");
            e.printStackTrace();
        }
    }

    // Recebe um objeto do tipo Leitor e o apaga do banco de dados a partir do seu código
    @Override
    public void apagar(Leitor leitor) {
        String sql = "DELETE FROM leitor WHERE codigo = ?";

        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, leitor.getCodigo());

            stm.executeUpdate();
            System.out.println("Leitor removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover leitor!");
            e.printStackTrace();
        }
    }

    // Recebe um objeto do tipo Leitor e um código
    @Override
    public void atualizar(Leitor leitor) {
        String sql = "UPDATE leitor SET cpf = ?, nome = ?, telefone = ?, email = ?, cep = ? WHERE codigo = ?";
        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, leitor.getCpf());
            stm.setString(2, leitor.getNome());
            stm.setString(3, leitor.getTelefone());
            stm.setString(4, leitor.getEmail());
            stm.setString(5, leitor.getCep());
            stm.setInt(6, leitor.getCodigo());

            stm.executeUpdate();
            System.out.println("Leitor atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar leitor!");
            e.printStackTrace();
        }
    }

    @Override
    public Leitor pesquisarPorCpf(String cpf) {
        String sql = "SELECT * FROM leitor WHERE cpf = ?";
        try (Connection con = Conexao.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setString(1, cpf);

            ResultSet rs = stm.executeQuery();
            System.out.println("Leitor selecionado com sucesso");
            if (rs.next()) {
                int codigo = rs.getInt("codigo");
                String cpfLeitor = rs.getString("cpf");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String cep = rs.getString("cep");
                LocalDate dataCadastro = rs.getDate("data_cadastro").toLocalDate();

                Leitor leitor = new Leitor(cpfLeitor, nome, telefone, email, cep, dataCadastro);
                leitor.setCodigo(codigo);
                leitor.setDataCadastro(dataCadastro);
                return leitor;
            }
        } catch (SQLException e) {
            System.err.println("Leitor não encontrado");
            e.printStackTrace();

        }
        return null;
    }
}
