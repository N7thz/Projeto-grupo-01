package classes;

import dao.ProdutoDAO;
import conexao.Conexao;

public class ProdutoDML {
	public static void gravarProduto(Conexao con,String schema,Produto p) {
		ProdutoDAO pdao = new ProdutoDAO(con,schema);
		
		pdao.incluirProduto(p);		
	}
	
	public static void alterarProduto(Conexao con,String schema,Produto p) {
		ProdutoDAO pdao = new ProdutoDAO(con,schema);
		
		pdao.alterarProduto(p);		
	}
	
	public static void excluirProduto(Conexao con,String schema,Produto p) {
		ProdutoDAO pdao = new ProdutoDAO(con,schema);
		
		pdao.excluirProduto(p);		
	}
}
