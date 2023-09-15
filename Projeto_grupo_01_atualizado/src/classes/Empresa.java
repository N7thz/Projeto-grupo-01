package classes;

import java.time.LocalDateTime;

public class Empresa extends Cadastro{
	private String razao_social;

	public Empresa() {
		super();
	}

	public Empresa(String nome, String cpf_cnpj, String endereco, String telefone, String email, String data_hora,String razao_social) {
		super(nome, cpf_cnpj, endereco, telefone, email, data_hora);
		this.setRazao_social(razao_social);
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	


}
