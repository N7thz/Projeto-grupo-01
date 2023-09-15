package classes;

public class Item extends Produto {
	private int idpedido_intens;
	private int idpedido;
	private int quantidade;
	
	public int getIdpedido_intens() {
		return idpedido_intens;
	}

	public void setIdpedido_intens(int idpedido_intens) {
		this.idpedido_intens = idpedido_intens;
	}

	public int getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(int idpedido) {
		this.idpedido = idpedido;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
}


