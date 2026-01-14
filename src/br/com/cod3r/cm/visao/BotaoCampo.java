package br.com.cod3r.cm.visao;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import br.com.cod3r.cm.modelo.Campo;
import br.com.cod3r.cm.modelo.CampoEvento;
import br.com.cod3r.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador {

	// TODO terminar de fazer a lógica do evento do mouse
	
	private Campo campo;
	private final Color BG_PADRAO = new Color(184, 184, 184);
	private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLOSAO = new Color(189, 66, 68);
	private final Color TEXT_GREEN = new Color(0, 100, 0);
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		
		
		campo.registrarObservador(this);
	}

	@Override
	public void eventoOcorreu(Campo c, CampoEvento e) {
		switch (e) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;

		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			AplicarEstiloPadrao();
		}

	}

	private void AplicarEstiloPadrao() {

	}

	private void aplicarEstiloExplodir() {

	}

	private void aplicarEstiloMarcar() {

	}

	private void aplicarEstiloAbrir() {

	}

}
