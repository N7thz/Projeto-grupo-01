package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import constantes.Util;

public class Produto {
	private int idproduto; 
	private String nome;
	private int codigo; 
	private String descricao;
	private int qt_estoque;
	private double valorun;
	private double valorvenda;
	private String data_hora;
	
	public int getIdproduto() {
		return idproduto;
	}
	public void setIdproduto(int idproduto) {
		this.idproduto = idproduto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getQt_estoque() {
		return qt_estoque;
	}
	public void setQt_estoque(int qt_estoque) {
		this.qt_estoque = qt_estoque;
	}
	public double getValorun() {
		return valorun;
	}
	public void setValorun(double valorun) {
		this.valorun = valorun;
	}
	public double getValorvenda() {
		return valorvenda;
	}
	public void setValorvenda(double valorvenda) {
		this.valorvenda = valorvenda;
	}
	public String getData_hora() {
		return data_hora;
	}
	public void setData_hora(String data_hora) {
		this.data_hora = data_hora;
	}
	
	public static Produto cadastrarProduto(ListasProdutos produtos){
		Produto p = new Produto();
		
		Scanner in = new Scanner(System.in);
		
		System.out.println(Util.LINHA);
		System.out.println("Cadastro de produto: ");
		System.out.println(Util.LINHA);
				
		Util.br();
		
		System.out.println("Informe o nome do produto: ");
		String s = in.nextLine();
		p.setNome(s);
	
		System.out.println("Informe o codigo do produto: ");
		p.setCodigo(Produto.ProdutoNaoRepetido(produtos));
		
		System.out.println("Informe a descrição do produto: ");
		s = in.nextLine();
		p.setDescricao(s);
		
		System.out.println("Informe a quantidade: ");
		int i = 0;
		i = in.nextInt();
		p.setQt_estoque(i);
		
		System.out.println("Informe o valor unitario: ");
		double d = in.nextDouble();
		p.setValorun(d);
		double margem;
		margem = d*0.4;
		d += margem;
		d = Double.parseDouble(String.format("%.2f", d));
		p.setValorvenda(d);
		LocalDateTime data_hora_atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String dataHoraFormatada = data_hora_atual.format(formatter);
		p.setData_hora(dataHoraFormatada);
        
		return p;	
	}
	
	public static int localizarProduto(String msg) {
		return Util.validarInteiro("Informe o id do produto: ");
	}
	
	public static String localizarProdutoNome() {
		Scanner in =  new Scanner(System.in);
		
		String msg;
		
		System.out.println("Digite o nome do produto: ");
		msg = in.nextLine();
		return msg;
	}
	
	public static Produto alterarProduto(Produto p) {
		Scanner in =  new Scanner(System.in);
		System.out.println("Nome: ");
		p.setNome(in.nextLine());
		System.out.println("Codigo: ");
		p.setCodigo(in.nextInt());
		System.out.println("Descricao: ");
		p.setDescricao(in.nextLine());
		p.setDescricao(in.nextLine());
		System.out.println("Quantidade: ");
		p.setQt_estoque(in.nextInt());
		System.out.println("Valor unitario: ");
		double d = in.nextDouble();
		p.setValorun(d);
		double margem;
		margem = d*0.4;
		d += margem;
		p.setValorvenda(d);
		LocalDateTime data_hora_atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String dataHoraFormatada = data_hora_atual.format(formatter);
		p.setData_hora(dataHoraFormatada);
		return p;
	}
	
	public static int ProdutoNaoRepetido(ListasProdutos produtos) {
		Scanner in = new Scanner(System.in);
		int codigo = 0;
		do {
			codigo = in.nextInt();
			
			if (produtos.localizarCodigo(codigo) == true)
				System.out.println("Codigo já exixte! Digite novamente.");
		} while(produtos.localizarCodigo(codigo));
		
			return codigo;
	}
}
