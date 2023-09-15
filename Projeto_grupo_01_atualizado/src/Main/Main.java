package Main;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import classes.*;
import arquivo.ArquivoTxt;
import conexao.Conexao;
import conexao.DadosConexao;
import constantes.Util;
import dao.CreateDAO;
import dao.ItemDAO;
import dao.PedidoDAO;

public class Main {
	// classe_estatica_para_nao_ser_preciso_objeto
	public static Conexao con;

	// cria_objeto_da_classe_dados_conexao
	public static DadosConexao dadosCon = new DadosConexao();

	// costantes_do_banco
	public static final String BANCO = "grupo01";
	public static final String SCHEMA = "sistema";
	public static final String PATH = "C:\\temp\\";
	public static final String SFILE = "grupo01.ini";

	public static ListasProdutos produtos;
	public static ListaClientes clientes;
	public static ListaPedidos pedidos;
	public static ListaItens itens;

	public static void main(String[] args) {

		if (configInicial()) {
			if (CreateDAO.createBD(BANCO, SCHEMA, dadosCon)) {
				// cria_um_objeto_do_tipo_conexao ( basicamente_cria_uma_conexao )
				con = new Conexao(dadosCon);
				con.conect();
				clientes = new ListaClientes(con, SCHEMA);
				produtos = new ListasProdutos(con, SCHEMA);
				pedidos = new ListaPedidos(con, SCHEMA);
				itens = new ListaItens(con, SCHEMA);
			} else {
				System.out.println("Ocorreu um problema na criacao do banco de dados");
			}
		} else
			System.out.println("Não foi possivel executar o sistema.");
		try {
			nossaEmpresa();
			menu();
		} finally {
			con.disconect();
		}
	}

	public static Empresa nossaEmpresa() {
		LocalDateTime data_hora_atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String dataHoraFormatada = data_hora_atual.format(formatter);

		Empresa grupo_1 = new Empresa("GRUPONE", "09.654.175/0001-97", "R. Carmélia Couto 126 Centro Teresópolis-RJ", "(21)94002-8922", "grupone01@gmail.com",
				dataHoraFormatada, "Grupo 01.Ltda");
		
		return grupo_1;
	}
	
	public static void imprimirEmpresa(Empresa e) {
		System.out.println(Util.LINHAD + Util.LINHAD+ Util.LINHAD);
		System.out.println("                                  	   " + e.getNome());
		System.out.println(Util.LINHAD + Util.LINHAD+ Util.LINHAD);
		System.out.println("Cnpj: " +  e.getCpf_cnpj());
		System.out.println("Endereço: " + e.getEndereco());
		System.out.println("E-mail: " + e.getEmail());
		System.out.println("Telefone de contato: " + e.getTelefone());
		System.out.println("Razão Social: " + e.getRazao_social());
		System.out.println("-----------------------------------------------------------------------------------------------------");
	}

	public static boolean configInicial() {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);

		// cria_um_objeto_do_tipo_arquivo_com_o_caminho_e_nome_das_constantes
		ArquivoTxt conexaoIni = new ArquivoTxt(PATH + SFILE);
		boolean abrirSistema = false;

		// se_o_arquivo_foi_criado_alimenta_o_arquivo
		if (conexaoIni.criarArquivo()) {
			if (conexaoIni.alimentaDadosConexao()) {
				dadosCon = conexaoIni.getData();
				abrirSistema = true;
				// se_o_abrirSistema_for_true_o_arquivo_foi_alimentado_senao_apaga_o_arquivo_e_cria_outro
			} else {
				conexaoIni.apagarArquivo();
				// dados_do_novo_arquivo
				System.out.println("Arquivo de configuração de conexão:\n");
				System.out.println("Local: ");
				String local = input.nextLine();
				System.out.println("Porta: ");
				String porta = input.nextLine();
				System.out.println("Usuário: ");
				String usuario = input.nextLine();
				System.out.println("Senha: ");
				String senha = input.nextLine();
				System.out.println("Database: ");
				String banco = input.nextLine();

				// se_o_novo_arquivo_foi_criado_Preeche_ele_com_os_dados
				if (conexaoIni.criarArquivo()) {
					conexaoIni.escreverArquivo("bd=PostgreSql");
					conexaoIni.escreverArquivo("local=" + local);
					conexaoIni.escreverArquivo("porta=" + porta);
					conexaoIni.escreverArquivo("usuario=" + usuario);
					conexaoIni.escreverArquivo("senha=" + senha);
					conexaoIni.escreverArquivo("banco=" + banco);

					// mesmo_if_da_linha_57_que_testa_se_o_arquivo_foi_alimentado
					if (conexaoIni.alimentaDadosConexao()) {
						dadosCon = conexaoIni.getData();
						abrirSistema = true;
						// se_nada_foi_possivel_printa_a_mensagem_abaixo
					} else
						System.out.println("Não foi possivel efetuar a configuração.\nVerifique");
				}
			}
			// se_nada_foi_possivel_printa_a_mensagem_abaixo
		} else
			System.out.println("Houve um problema na criação do arquivo de configuração.");

		return abrirSistema;
	}

	public static void cadastrarCliente() {
		Cliente c = Cliente.cadastrarCliente(clientes);

		ClienteDML.gravarCliente(con, SCHEMA, c);
		clientes.adicionarClienteLista(c);
		opcoesCliente();
	}

	public static void alterarCliente() {
		Cliente c = clientes.localizarCliente(Cliente.localizarCliente("Alteração de cliente"));

		if (!(c == null)) {
			Cliente.alterarCliente(c);
			ClienteDML.alterarCliente(con, SCHEMA, c);
		} else
			System.out.println("Cliente não localizado!");
	}

	public static void excluirCliente() {
		Cliente c = clientes.localizarCliente(Cliente.localizarCliente("Exclusão de cliente"));

		if (!(c == null)) {
			clientes.excluirCliente(c);
			ClienteDML.excluirCliente(con, SCHEMA, c);
		} else
			System.out.println("Cliente não localizado!");
	}

	public static void listarCliente() {
		imprimirEmpresa(nossaEmpresa());
		clientes.imprimirClientes();
	}

	public static void cadastrarProduto() {
		Produto p = Produto.cadastrarProduto(produtos);

		ProdutoDML.gravarProduto(con, SCHEMA, p);
		produtos.adicionarProdutoLista(p);
		opcoesProduto();
	}

	public static void alterarProduto() {
		Produto p = produtos.localizarProduto(Produto.localizarProduto("Alteração de produto"));

		if (!(p == null)) {
			Produto.alterarProduto(p);
			ProdutoDML.alterarProduto(con, SCHEMA, p);
		} else
			System.out.println("Produto não localizado!");
	}

	public static void excluirProduto() {
		Produto p = produtos.localizarProduto(Produto.localizarProduto("Exclusão de produto"));

		if (!(p == null)) {
			produtos.excluirProduto(p);
			ProdutoDML.excluirProduto(con, SCHEMA, p);
		} else
			System.out.println("Produto não localizado!");
	}

	public static void listarProduto() {
		imprimirEmpresa(nossaEmpresa());
		produtos.imprimirProdutos();
	}

//----------------------------------------------------
// CADASTRAR PEDIDO
//----------------------------------------------------

	public static void cadastrarPedido() {
		Scanner input = new Scanner(System.in);
		int r;
		Cliente c = null;
		int opcao;
		do {
			System.out.println("""
					-----------------------------------------------------------------------------------------------------
					       									Localizar cliente
					-----------------------------------------------------------------------------------------------------

					[1] Nome do cliente
					[2] Código do cliente

					            """);
			opcao = input.nextInt();

			switch (opcao) {
			case 1:
				c = clientes.localizarClienteNome(Cliente.localizarClienteNome());
				break;
			case 2:
				c = clientes.localizarCliente(Cliente.localizarCliente(""));
				break;
			default:
				System.out.println("[ERRO!!!] Tente novamente.");
				break;
			}
		} while (opcao != 2 && opcao != 1);

		if (!(c == null)) {
			Pedido p = criarPedido(c, cadastrarItemPedido());

			PedidoDML.gravarPedido(con, SCHEMA, p);

			p.setId_pedido(PedidoDAO.retornaIdPedido(con, SCHEMA));

			ItensDML.gravarItensDML(con, SCHEMA, p);
			pedidos.adicionarPedidoLista(p);
			System.out.println("O pedido foi adicionado com sucesso.");
			opcoesPedido();
		} else {
			System.out.println("Cliente não localizado!");
			System.out.println("Digite [1] para cadastrar um novo cliente: ");
			r = input.nextInt();
			if (r == 1) {
				cadastrarCliente();
			} else {
				menu();
			}
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public static ArrayList<Item> cadastrarItemPedido() {
		Scanner input = new Scanner(System.in);
		ArrayList<Item> itensPedido = new ArrayList<>();

		Produto p = null;
		boolean valorInvalido = false;
		String r = "2";
		int q;
		int opcao;
		do {
			do {
				System.out.println("""
						-----------------------------------------------------------------------------------------------------
						       									Localizando produtos
						-----------------------------------------------------------------------------------------------------

						[1] Nome do produto
						[2] Código do produto

						 """);

				opcao = input.nextInt();

				switch (opcao) {
				case 1:
					boolean produto = false;
					do {
						p = produtos.localizarProdutoNome(Produto.localizarProdutoNome());
						if (p != null) {
							produto = true;
						} else {
							System.out.println("Produto não encontrado!");
							produtos.imprimirProdutos();
						}
					} while (produto == false);
					break;
				case 2:
					produto = false;
					do {
						p = produtos.localizarProduto(Produto.localizarProduto(""));
						if (p != null) {
							produto = true;
						} else {
							System.out.println("Produto não encontrado!");
							produtos.imprimirProdutos();
						}
					} while (produto == false);
					break;
				default:
					System.out.println("[ERRO!!!] Tente novamente.");
					break;
				}
			} while (opcao != 2 && opcao != 1);

			do {
				q = Util.intTratado();
				if (q > p.getQt_estoque()) {
					System.out.println("Quantidade indisponivel.\n");
					valorInvalido = true;
				}
			} while (valorInvalido);

			itensPedido.add(devolveItem(p, q));
			
			System.out.println("Digite [1] para adicionar um novo produto: ");
			input.nextLine();
			r = input.nextLine();
			
		} while (r.equals("1"));
		return itensPedido;

	}

	public static Item devolveItem(Produto p, int q) {
		Item i = new Item();
		i.setCodigo(p.getCodigo());
		i.setDescricao(p.getDescricao());
		i.setIdproduto(p.getIdproduto());
		i.setNome(p.getNome());
		i.setValorun(p.getValorun());
		i.setValorvenda(p.getValorvenda());
		i.setData_hora(p.getData_hora());
		i.setQuantidade(q);
		atualizarEstoque(p, i);
		return i;
	}

	public static Pedido criarPedido(Cliente c, ArrayList<Item> ai) {
		double d = totalPedido(ai);
		Pedido pe = new Pedido();

		pe.setCodigo(Pedido.PedidoNaoRepetido(pedidos));
		pe.setCliente(c);
		pe.setItens(ai);
		pe.setTotal_pedido(d);
		LocalDateTime data_hora_atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String dataHoraFormatada = data_hora_atual.format(formatter);
		pe.setData_hora(dataHoraFormatada);

		return pe;
	}

	public static double totalPedido(ArrayList<Item> item) {
		double total = 0;

		for (Item i : item) {
			total += i.getValorvenda() * i.getQuantidade();
		}

		return Util.casaDecimalTratada(total);
	}
	
	public static void atualizarEstoque(Produto p, Item i) {
		p.setQt_estoque(p.getQt_estoque() - i.getQuantidade());

		System.out.println("O estoque foi atualizado com sucesso.");

		ProdutoDML.alterarProduto(con, SCHEMA, p);
	}
	
//----------------------------------------------------
// ALTERAR PEDIDO
//----------------------------------------------------	
	
	public static void alterarPedido() {
		Scanner input = new Scanner(System.in);
		
		int r;
		Pedido pe = pedidos.localizarPedido(Pedido.localizarPedido(""));		
		if(!(pe == null)) {
			pe = criarPedido(pe.getCliente(), cadastrarItemPedido());
			pe.setId_pedido(PedidoDAO.retornaIdPedido(con, SCHEMA));
			
			for (Item i : pe.getItens()) {
				i.setIdpedido_intens(ItemDAO.retornaIdPedidoItem(con, SCHEMA));
			}
			
			PedidoDML.alterarPedido(con, SCHEMA, pe);
			ItensDML.alterarPedidos_ItensDML(con, SCHEMA, pe);
			System.out.println("O pedido foi alterado com sucesso.");
			opcoesPedido();
		} else {
			System.out.println("pedido não localizado!");
			System.out.println("Digite [1] para cadastrar um novo pedido: ");
			r = input.nextInt();
			if (r == 1) {
				cadastrarPedido();
			} else {
				menu();
			}
		}
	}
	
//----------------------------------------------------
// EXCLUIR PEDIDO
//----------------------------------------------------		

	public static void excluirPedido() {
		Scanner input = new Scanner(System.in);
		int  r;
		String z;
		
		Pedido pe = pedidos.localizarPedido(Pedido.localizarPedido(""));

		if (!(pe == null)) {
			do {
				System.out.println("Deseja mesmo excluir o pedido? Todos os itens serão perdidos.");
				System.out.println("Digite [1] para excluir seu pedido: ");
				r = input.nextInt();
				
				switch (r) {
				case 1:
					pedidos.excluirPedido(pe);
					PedidoDML.excluirPedido(con, SCHEMA, pe);
					System.out.println("O pedido foi excluido com sucesso!");
					opcoesPedido();
					break;
				case 2:
					System.out.println("pedido não excluido!");
					opcoesPedido();
					break;
				default:
					System.out.println("ERRO!!! Tente novamente.");
					break;
				}
			} while (r != 1 && r != 2);
		} else {
			System.out.println("pedido não localizado!");
			System.out.println("Digite [1] para cadastrar um novo pedido: ");
			z = input.nextLine();
			if (z.equals("1")) {
				cadastrarPedido();
			} else {
				menu();
			}
		}
	}

	public static void listarPedido() {
		imprimirEmpresa(nossaEmpresa());
    	pedidos.imprimirPedidos(itens.itensLista, produtos.produto);
    }

	static void menu() {
		Scanner input = new Scanner(System.in);
		int opcao;
		do {
			System.out.println(Util.LINHAD + Util.LINHAD + Util.LINHAD);
			System.out.println("	  				     MENU INICIAL");
			System.out.println(Util.LINHAD + Util.LINHAD + Util.LINHAD);
			System.out.println();
			System.out.println("[1] Produto");
			System.out.println("[2] Cliente");
			System.out.println("[3] Pedido");
			System.out.println("[4] Sair");
			System.out.println("");
			opcao = input.nextInt();

			switch (opcao) {
			case 1:
				opcoesProduto();
				break;
			case 2:
				opcoesCliente();
				break;
			case 3:
				opcoesPedido();
				break;
			case 4:
				System.out.println("Programa encerrado.");
				break;
			default:
				System.out.println("ERRO!!! Tente novamente.");
				break;
			}
		} while (opcao != 4);
	}

	public static void opcoesProduto() {
		Scanner input = new Scanner(System.in);
		int opcao;

		System.out.println("""
				-----------------------------------------------------------------------------------------------------
										PRODUTO
				-----------------------------------------------------------------------------------------------------

				[1] Cadastrar Produto
				[2] Alterar Produto
				[3] Excluir Produto
				[4] Listar Produto
				[5] Menu inicial

				            """);
		System.out.println("");
		opcao = input.nextInt();

		switch (opcao) {
		case 1:
			cadastrarProduto();
			break;
		case 2:
			alterarProduto();
			break;
		case 3:
			excluirProduto();
			break;
		case 4:
			listarProduto();
			break;
		case 5:
			menu();
			break;
		default:
			System.out.println("[ERRO!!!] Tente novamente.");
			break;
		}
		while (opcao != 4)
			;
	}

	public static void opcoesCliente() {
		Scanner input = new Scanner(System.in);
		int opcao;
		System.out.println("""
				-----------------------------------------------------------------------------------------------------
										CLIENTE
				-----------------------------------------------------------------------------------------------------

				[1] Cadastrar Cliente
				[2] Alterar Cliente
				[3] Excluir Cliente
				[4] Listar Cliente
				[5] Menu inicial

				            """);
		System.out.println("");
		opcao = input.nextInt();

		switch (opcao) {
		case 1:
			cadastrarCliente();
			break;
		case 2:
			alterarCliente();
			break;
		case 3:
			excluirCliente();
			break;
		case 4:
			listarCliente();
			opcoesCliente();
			break;
		case 5:
			menu();
			break;
		default:
			System.out.println("[ERRO!!!] Tente novamente.");
			break;
		}
		while (opcao != 4)
			;
	}

	public static void opcoesPedido() {
		Scanner input = new Scanner(System.in);
		int opcao;
		System.out.println("""
				-----------------------------------------------------------------------------------------------------
										PEDIDO
				-----------------------------------------------------------------------------------------------------
				[1] Cadastrar Pedido
				[2] Alterar Pedido
				[3] Excluir Pedido
				[4] Listar Pedido
				[5] Menu inicial

	            """);
		System.out.println("");
		opcao = input.nextInt();

		switch (opcao) {
		case 1:
			cadastrarPedido();
			break;
		case 2:
			alterarPedido();
			break;
		case 3:
			excluirPedido();
			break;
		case 4:
			listarPedido();
			break;
		case 5:
			menu();
			break;
		default:
			System.out.println("[ERRO!!!] Tente novamente.");
			break;
		}
		while (opcao != 4);
	}
}
