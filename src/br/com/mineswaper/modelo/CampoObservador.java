package br.com.mineswaper.modelo;
@FunctionalInterface
public interface CampoObservador {
	
	public void eventoOcorreu(Campo c, CampoEvento e);
	
}	
