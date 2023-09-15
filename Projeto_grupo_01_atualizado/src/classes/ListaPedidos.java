package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import conexao.Conexao;
import constantes.Util;
import dao.PedidoDAO;

public class ListaPedidos {
	private Conexao con;
	private String schema;
	
	public ArrayList<Pedido> pedido = new ArrayList<>();
	
	public ListaPedidos(Conexao con, String schema) {
		this.con = con;
		this.schema = schema;
		carregarListaPedido();
	}
	
	public void adicionarPedidoLista(Pedido p) {
		this.pedido.add(p);
	}
	
	public Pedido localizarPedido(int idPedido) {
		Pedido localizado = null;
		
		for(Pedido p : pedido) {
			if(p.getId_pedido() == idPedido) {
				localizado = p;
				break;
			}
		}
		return localizado;
	}
	
	private void carregarListaPedido() {
		PedidoDAO pdao = new PedidoDAO(con,schema);
		
		ResultSet tabela = pdao.carregarPedido();
		this.pedido.clear();
		
		try {
			tabela.beforeFirst();
		
			while (tabela.next()) {							
				this.pedido.add(dadosPedido(tabela));				
			}
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}finally {
			try {
				tabela.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Pedido dadosPedido(ResultSet tabela) {
		Pedido p = new Pedido();
		ListaClientes lc = new ListaClientes(con , schema);
		int codigo;
		
		try {
			codigo = tabela.getInt("codigo");
			if(codigo != 0)
				p.setCodigo(codigo);
			p.setId_pedido(tabela.getInt("id_pedido"));			
			p.setCliente(lc.localizarCliente(tabela.getInt("id_cliente")));
			p.setTotal_pedido(tabela.getDouble("total_pedido"));
			p.setData_hora(tabela.getString("data_hora"));
			return p ;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void imprimirPedidos(ArrayList<Item> itensLista, ArrayList<Produto> produtos) {
		System.out.println(Util.LINHAD + Util.LINHAD+ Util.LINHAD);
		System.out.println("				     LISTA DE PEDIDOS");
		System.out.println(Util.LINHAD + Util.LINHAD+ Util.LINHAD);
		
		
		for (Pedido p : pedido) {
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.println("id-pedido: \tCódigo:\t\tid-cliente:\ttotal Pedido:\tData e hora:");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.print(	p.getId_pedido()+ "\t\t" + p.getCodigo() + "\t\t" + p.getCliente().getIdcliente() + "\t\t" + p.getTotal_pedido() + "\t\t" + p.getData_hora()+"\n\n");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.println("				     ITENS DO PEDIDO ");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.println(" Nome do Produto: \t   quantidade:\t\tValor:\n");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			for (Item i : itensLista) {
				if (i.getIdpedido() == p.getId_pedido()) {
					
					for (Produto pd : produtos) {
						if (i.getIdproduto() == pd.getIdproduto()) {
							i.setNome(pd.getNome());
						}
					}
					System.out.println(" "+i.getNome()+"\t\t\t"+i.getQuantidade()+"\t\t "+i.getValorvenda());
					System.out.println("-----------------------------------------------------------------------------------------------------");
				}
			} 
		}
	}
	
	
	public void excluirPedido(Pedido p) {
		pedido.removeIf(pd -> pd.getId_pedido() == p.getId_pedido());
	}
	
	public boolean localizarCodigo(int codigo) {
		Pedido localizado = null;
		
		for(Pedido p : pedido) {
			if(p.getCodigo() == codigo) {
				localizado = p;
				break;
			}
		}
		return localizado != null;
	}
}

