package br.com.mineswaper.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {
	private final int Qtdlinhas;
	private final int Qtdcolunas;
	private final int Qtdminas;

	private final List<Campo> campos = new ArrayList<Campo>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int qtdlinhas, int qtdcolunas, int qtdminas) {
		Qtdlinhas = qtdlinhas;
		Qtdcolunas = qtdcolunas;
		Qtdminas = qtdminas;

		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}
	
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
		
	}
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}

	public void notificarObservador(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}

	public void abrir(int Qtdlinhas, int Qtdcolunas) {

		try {
			campos.parallelStream().filter(c -> c.getLinha() == Qtdlinhas && c.getColuna() == Qtdcolunas).findFirst()
					.ifPresent(c -> c.abrir());
		} catch (Exception e) {
			// FIXME Ajustar a implementação do método abrir
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}

	}

	private void mostrarMinas() {
		campos.stream().filter(c -> c.isMinado()).forEach(c -> c.setAberto(true));
	}

	public void alternarMarcacao(int Qtdlinhas, int Qtdcolunas) {
		campos.parallelStream().filter(c -> c.getLinha() == Qtdlinhas && c.getColuna() == Qtdcolunas).findFirst()
				.ifPresent(c -> c.alternarMarcacao());
	}

	private void gerarCampos() {
		for (int linha = 0; linha < Qtdlinhas; linha++) {
			for (int coluna = 0; coluna < Qtdcolunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
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
	public void eventoOcorreu(Campo c, CampoEvento e) {
		if(e == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservador(false);
			

		} else if(objetivoAlcancado()){
			notificarObservador(true);
		}
		
	}

	public int getQtdlinhas() {
		return Qtdlinhas;
	}

	public int getQtdcolunas() {
		return Qtdcolunas;
	}

	public int getQtdminas() {
		return Qtdminas;
	}

	
	
}
