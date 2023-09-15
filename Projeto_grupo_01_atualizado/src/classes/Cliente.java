package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import constantes.Util;

public class Cliente extends Cadastro {
	private int idcliente;
	private int codigo;
	
	public int getIdcliente() {
		return idcliente;
	}
	public void setIdcliente(int idcliente) {
		this.idcliente = idcliente;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public static Cliente cadastrarCliente(ListaClientes clientes) {
		Cliente c = new Cliente();
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		System.out.println("-----------------------------------");
		System.out.println("Cadastro de cliente: ");
		System.out.println("-----------------------------------");
		
		System.out.println("");
		
		System.out.println("Informe o nome: ");
		String s = in.nextLine();
		c.setNome(s);
		
		c.setCodigo(Cliente.ClienteNaoRepetido(clientes));
		
		System.out.println("Informe o CPF ou CNPJ: ");
		c.setCpf_cnpj(Cliente.CpfNaoRepetido(clientes));
	
		System.out.println("Informe o endereco: ");
		s = in.nextLine();
		c.setEndereco(s);
				
		
		System.out.println("Informe o Telefone: ");
		s = in.nextLine();
		c.setTelefone(s);
		
		System.out.println("Informe o email: ");
		s = in.nextLine();
		c.setEmail(s);
				
		LocalDateTime data_hora_atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String dataHoraFormatada = data_hora_atual.format(formatter);
		c.setData_hora(dataHoraFormatada);
		
		return c;
	}
		
	public static int localizarCliente(String msg) {
		System.out.println("Localizando clientes");
		return Util.validarInteiro("Digite o id do cliente: ");
	}
	
	public static String localizarClienteNome() {
		Scanner in =  new Scanner(System.in);
		
		String msg;
		
		System.out.println("Digite o nome do cliente: ");
		msg = in.nextLine();
		return msg;
	}
	
	public static Cliente alterarCliente(Cliente c) {
		Scanner in =  new Scanner(System.in);
		System.out.println("Nome: ");
		c.setNome(in.nextLine());
		System.out.println("Cpf ou Cnpj: ");
		c.setCpf_cnpj(in.nextLine());
		System.out.println("Endereço: ");
		c.setEndereco(in.nextLine());
		System.out.println("Telefone: ");
		c.setTelefone(in.nextLine());
		System.out.println("Email: ");
		c.setEmail(in.nextLine());
		LocalDateTime data_hora_atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String dataHoraFormatada = data_hora_atual.format(formatter);
		c.setData_hora(dataHoraFormatada);
		
		return c;
	}
	
	public static int ClienteNaoRepetido(ListaClientes clientes) {
		int codigo = 0;	
		do {			
        int quantidadeNumeros = 1; 
        int valorMinimo = 1;       
        int valorMaximo = 10000;   

        Set<Integer> sequencia = gerarCodigoCliente(quantidadeNumeros, valorMinimo, valorMaximo);

        for (int numero : sequencia) {
        	codigo = numero;
        }
    
		}while(clientes.localizarCodigo(codigo));
			return codigo;
}

	public static Set<Integer> gerarCodigoCliente(int quantidade, int minimo, int maximo) {
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
	
	public static String CpfNaoRepetido(ListaClientes clientes) {
        Scanner in = new Scanner(System.in);
        long cpf_cnpj_tratar;
        String cpf_cnpj;
        do {
            cpf_cnpj_tratar = Util.cpfCnpjTratado();
            cpf_cnpj = Long.toString(cpf_cnpj_tratar);
            if(clientes.localizarClienteCpfCnpj(cpf_cnpj) == true)
                System.out.println("CPF ou CNPJ já exixte! Digite novamente.");
        } while(clientes.localizarClienteCpfCnpj(cpf_cnpj));

            return cpf_cnpj;
    }
}
