package classes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Cadastro {
	private String nome;
	private String cpf_cnpj;
	private String endereco;
	private String telefone;
	private String email;
	private String data_hora;
	
	public Cadastro() {		
	}

	public Cadastro(String nome, String cpf_cnpj, String endereco, String telefone, String email, String data_hora) {
		this.nome = nome;
		this.cpf_cnpj = cpf_cnpj;
		this.endereco = endereco;
		this.telefone = telefone;
		this.email = email;
		this.data_hora = data_hora;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf_cnpj() {
		return cpf_cnpj;
	}

	public void setCpf_cnpj(String cpf_cnpj) {
		this.cpf_cnpj = cpf_cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getData_hora() {
		return data_hora;
	}

	public void setData_hora(String data_hora) {
		this.data_hora = data_hora;
	}
	
	
}
