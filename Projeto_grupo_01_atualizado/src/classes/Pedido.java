package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import constantes.Util;

public class Pedido {
	
	private int id_pedido;
	private int codigo;
	private Cliente cliente;
	private double total_pedido;
	private String data_hora;
	private ArrayList<Item> itens = new ArrayList<>();
	
	public ArrayList<Item> getItens() {
		return itens;
	}
	public void setItens(ArrayList<Item> itens) {
		this.itens = itens;
	}
	public int getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}	
	public double getTotal_pedido() {
		return total_pedido;
	}
	public void setTotal_pedido(double total_pedido) {
		this.total_pedido = total_pedido;
	}
	public String getData_hora() {
		return data_hora;
	}
	public void setData_hora(String data_hora) {
		this.data_hora = data_hora;
	}
		
	public static int localizarPedido(String msg) {
		System.out.println("""
				----------------------------------
				       Localizando pedidos
				----------------------------------
				            """);
		return Util.validarInteiro("Informe o codigo do pedido: ");
	}

	public static int PedidoNaoRepetido(ListaPedidos pedidos) {
		int codigo = 0;	
		do {			
        int quantidadeNumeros = 1; 
        int valorMinimo = 1;       
        int valorMaximo = 1000000;   

        Set<Integer> sequencia = gerarCodigoPedido(quantidadeNumeros, valorMinimo, valorMaximo);

        for (int numero : sequencia) {
        	codigo = numero;
        }
    
		}while(pedidos.localizarCodigo(codigo));
			return codigo;
	}

	public static Set<Integer> gerarCodigoPedido(int quantidade, int minimo, int maximo) {
	    if (quantidade > (maximo - minimo + 1)) {
	        throw new IllegalArgumentException("Não é possível gerar uma sequência não repetida com esses parâmetros.");
	    }
	
	    Set<Integer> sequencia = new HashSet<>();
	    Random random = new Random();
	
	    while (sequencia.size() < quantidade) {
	        int numeroAleatorio = random.nextInt(maximo - minimo + 1) + minimo;
	        sequencia.add(numeroAleatorio);
	    }
	    return sequencia;
	}
}