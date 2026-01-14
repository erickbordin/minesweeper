package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.cod3r.cm.excecao.ExplosaoException;

public class Tabuleiro {
	private int Qtdlinhas;
	private int Qtdcolunas;
	private int Qtdminas;

	private final List<Campo> campos = new ArrayList<Campo>();

	public Tabuleiro(int qtdlinhas, int qtdcolunas, int qtdminas) {
		Qtdlinhas = qtdlinhas;
		Qtdcolunas = qtdcolunas;
		Qtdminas = qtdminas;

		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}

	public void abrir(int Qtdlinhas, int Qtdcolunas) {

		try {
			campos.parallelStream().filter(c -> c.getLinha() == Qtdlinhas && c.getColuna() == Qtdcolunas).findFirst()
					.ifPresent(c -> c.abrir());
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}

	}

	public void alternarMarcacao(int Qtdlinhas, int Qtdcolunas) {
		campos.parallelStream().filter(c -> c.getLinha() == Qtdlinhas && c.getColuna() == Qtdcolunas).findFirst()
				.ifPresent(c -> c.alternarMarcacao());
	}

	private void gerarCampos() {
		for (int linha = 0; linha < Qtdlinhas; linha++) {
			for (int coluna = 0; coluna < Qtdcolunas; coluna++) {
				campos.add(new Campo(linha, coluna));
			}

		}
	}

	private void associarOsVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < Qtdminas);

	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		sb.append(" ");

		for (int c = 0; c < Qtdcolunas; c++) {
			sb.append(" ");
			sb.append(c);
			sb.append(" ");
		}
		sb.append("\n");
		
		int i = 0;

		for (int l = 0; l < Qtdlinhas; l++) {
			sb.append(l);
			sb.append(" ");
			
			for (int c = 0; c < Qtdcolunas; c++) {
				sb.append(" ");
				sb.append(campos.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");

		}
		return sb.toString();
	}
}
