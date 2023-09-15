package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import conexao.Conexao;
import dao.ItemDAO;

public class ListaItens {
	private Conexao con;
	private String schema;	
	
	 public ArrayList<Item> itensLista = new ArrayList<>();

	public ListaItens (Conexao con, String schema) {
		this.con = con;
		this.schema = schema;
		carregarListaItens();
	}
	
	public void adicionaItens(Item i) {
		this.itensLista.add(i);
	}
	
	private void carregarListaItens() {
		ItemDAO idao = new ItemDAO(con, schema);
		
		ResultSet tabela = idao.carregarItem();
		this.itensLista.clear();
		
		try {
			tabela.beforeFirst();
			
			while (tabela.next()) {							
				this.itensLista.add(dadosItens(tabela));				
			}
			
			tabela.close();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private Item dadosItens(ResultSet tabela) { 
		Item i = new Item();
		int id;
		
		try {
			id = tabela.getInt("id_pedido_itens");
			if(id != 0)
				i.setIdpedido_intens(id);
			i.setIdpedido(tabela.getInt("id_pedido"));
			i.setIdproduto(tabela.getInt("id_produto"));
			i.setQuantidade(tabela.getInt("quantidade"));		
			i.setValorvenda(tabela.getFloat("valor_venda"));
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void excluirItem(Item i) {
		this.itensLista.removeIf(pd -> pd.getIdproduto() == i.getIdproduto());
	}
}
