package classes;

import conexao.Conexao;
import dao.ItemDAO;

public class ItensDML {
	
	public static void gravarItensDML(Conexao con,String schema, Pedido p) {
		ItemDAO idao = new ItemDAO(con,schema);		
		
		idao.incluirItens(p);		
	}

	public static void alterarPedidos_ItensDML(Conexao con,String schema,Pedido i) {
		ItemDAO idao = new ItemDAO(con,schema);
		
		idao.alterarItem(i);		
	}
	
	public static void excluirItem(Conexao con,String schema,Item i) {
		ItemDAO idao = new ItemDAO(con,schema);
		
		idao.excluirItem(i);		
	}
}
