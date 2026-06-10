package edu.curso.controller;

import edu.curso.DAO.EmprestimoDAO;
import edu.curso.DAO.EmprestimoDAOImplementation;
import edu.curso.DAO.LivroDAO;
import edu.curso.DAO.LivroDAOImplementation;
import edu.curso.entity.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class EmprestimoController {

    private final ObservableList<Emprestimo> lista = FXCollections.observableArrayList();
    private final ObservableList<Leitor> leitores = FXCollections.observableArrayList();
    private final ObservableList<Livro> livros = FXCollections.observableArrayList();
    private final ObservableList<String> titulosLivros = FXCollections.observableArrayList();
    private final ObservableList<String> nomesLeitores = FXCollections.observableArrayList();

    private IntegerProperty codigo = new SimpleIntegerProperty(0);
    private ObjectProperty<Leitor> leitor = new SimpleObjectProperty<>(null);
    private ObjectProperty<Livro> livro = new SimpleObjectProperty<>(null);
    private ListProperty<ItemEmprestimo> listaLivros = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<LocalDate> dataEmprestimo = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<LocalDate> dpDevolucao = new SimpleObjectProperty<>(LocalDate.now());
    private BooleanProperty status = new SimpleBooleanProperty(true);
    private StringProperty livroSelecionado = new SimpleStringProperty("");
    private StringProperty leitorSelecionado = new SimpleStringProperty("");

    private EmprestimoDAO dao  = new EmprestimoDAOImplementation();
    public EmprestimoController() {
        // Sempre que o nome no combo mudar, resolve o objeto Leitor correspondente.
        // Isso corrige o NullPointerException: leitor.get() estava sempre null porque
        // o combo só atualizava a StringProperty, nunca o ObjectProperty<Leitor>.
        leitorSelecionado.addListener((obs, antigo, novo) -> {
            if (novo == null || novo.isBlank()) {
                leitor.set(null);
                return;
            }
            leitores.stream()
                    .filter(l -> l.getNome().equals(novo))
                    .findFirst()
                    .ifPresentOrElse(leitor::set, () -> leitor.set(null));
        });

        // Mesmo problema para o livro: resolve o objeto Livro pelo título selecionado.
        livroSelecionado.addListener((obs, antigo, novo) -> {
            if (novo == null || novo.isBlank()) {
                livro.set(null);
                return;
            }
            livros.stream()
                    .filter(l -> l.getTitulo().equals(novo))
                    .findFirst()
                    .ifPresentOrElse(livro::set, () -> livro.set(null));
        });

    }
    public void fromEntity(Emprestimo emp) {
        if (emp != null) {
            codigo.set(emp.getCodigo());
            leitor.set(emp.getLeitor());
            listaLivros.setAll(emp.getListaLivros());
            dataEmprestimo.set(emp.getDataEmprestimo());
            dpDevolucao.set(emp.getDataDevolucaoPrevista());
            status.set(emp.isStatus());

            if (emp.getLeitor() != null) {
                leitorSelecionado.set(emp.getLeitor().getNome());
            } else {
                leitorSelecionado.set("");
            }
          livroSelecionado.set("");
            livro.set(null);
        }
    }

    public Emprestimo toEntity() {
        Emprestimo emp = new Emprestimo(leitor.get());
        emp.setCodigo(codigo.get());
        emp.setListaLivros(listaLivros.get());
        emp.setDataEmprestimo(dataEmprestimo.get());
        emp.setDataDevolucaoPrevista(dpDevolucao.get());
        emp.setStatus(status.get());
        return emp;
    }

    public void adicionarLivro() {
        String nomeSelecionado = livroSelecionado.get();
        if (nomeSelecionado == null || nomeSelecionado.isBlank()) {
            return;
        }
        Livro livroEncontrado = livros.stream()
                .filter(l -> l.getTitulo().equals(nomeSelecionado))
                .findFirst()
                .orElse(null);
        if (livroEncontrado != null) {
            boolean jaAdicionado = listaLivros.stream()
                    .anyMatch(i -> i.getLivro().getCodigo() == livroEncontrado.getCodigo());
            if (!jaAdicionado) {
                listaLivros.add(new ItemEmprestimo(livroEncontrado));
            }
        }
    }

    public void removerLivro(int indice) {
        if (indice >= 0 && indice < listaLivros.size()) {
            listaLivros.remove(indice);
        }
    }

    public void salvar() {
        Emprestimo emp = toEntity();
        System.out.println("Codigo do Livro ==> " + emp.getCodigo());
        if(codigo.get() > 0) {
            dao.atualizar(emp);
        } else {
            dao.cadastrar(emp);
        }
        limparCampos();
        carregar();
    }
    public void limparCampos() {
        codigo.set(0);
        leitor.set(null);
        livro.set(null);
        listaLivros.clear();
        dataEmprestimo.set(LocalDate.now());
        dpDevolucao.set(LocalDate.now().plusDays(30));
        status.set(true);
    }

    public void carregar() {
        lista.clear();
        lista.addAll(dao.listarTodos());
    }

    public void apagar(int indice) {
        Emprestimo emp = lista.get(indice);
        dao.apagar(emp);
        carregar();
    }

    public void pesquisar() {
        carregar();
    }

    public void carregarCombos() {
        leitores.setAll(dao.listarLeitores());
        livros.setAll(dao.listarLivros());

        nomesLeitores.clear();
        for (Leitor l : leitores) {
            if (l != null && l.getNome() != null) {
                nomesLeitores.add(l.getNome());
            }
        }
        titulosLivros.clear();
        for (Livro l : livros) {
            if (l != null && l.getTitulo() != null) {
                titulosLivros.add(l.getTitulo());
            }
        }
    }

    public ObservableList<Emprestimo> getLista() {
        return lista;
    }

    public ObservableList<Leitor> getLeitores() {
        return leitores;
    }

    public ObservableList<Livro> getLivros() {
        return livros;
    }

    public ObjectProperty<Leitor> leitorProperty() {
        return leitor;
    }

    public ObjectProperty<Livro> livroProperty() {
        return livro;
    }

    public ObjectProperty<LocalDate> dataEmprestimoProperty() {
        return dataEmprestimo;
    }

    public ObjectProperty<LocalDate> dpDevolucaoProperty() {
        return dpDevolucao;
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    public ListProperty<ItemEmprestimo> listaLivrosProperty() {
        return listaLivros;
    }

    public StringProperty leitorSelecionadoProperty() {
        return leitorSelecionado;
    }

    public ObservableList<String> getTitulosLivros() {
        return titulosLivros;
    }

    public StringProperty livroSelecionadoProperty() {
        return livroSelecionado;
    }

    public ObservableList<String> getNomesLeitores() {
        return nomesLeitores;
    }
}
