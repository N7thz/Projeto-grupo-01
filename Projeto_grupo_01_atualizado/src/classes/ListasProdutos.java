package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import conexao.Conexao;
import dao.ProdutoDAO;

public class ListasProdutos {
	private Conexao con;
	private String schema;
	
	public ArrayList<Produto> produto = new ArrayList<>();
	
	public ListasProdutos(Conexao con, String schema) {
		this.con = con;
		this.schema = schema;
		carregarListaProdutos();
	}
	
	public void adicionarProdutoLista(Produto p) {
		this.produto.add(p);
	}
	
	public Produto localizarProduto(int idProduto) {
		Produto localizado = null;
		
		for(Produto p : produto) {
			if(p.getIdproduto() == idProduto) {
				localizado = p;
				break;
			}
		}
		return localizado;
	}
	
	public Produto localizarProdutoNome(String nome) {
		Produto localizado = null;
		
		for (Produto p : produto) {
			if (p.getNome().equals(nome)) {
				localizado = p;
				System.out.println("achei o produto");
				break;
			}
		}
		return localizado;
	}
	
	private void carregarListaProdutos() {
		ProdutoDAO pdao = new ProdutoDAO(con,schema);
		
		ResultSet tabela = pdao.carregarProduto();
		this.produto.clear();
		
		try {
			tabela.beforeFirst();
			
			while (tabela.next()) {							
				this.produto.add(dadosProduto(tabela));				
			}
			
			tabela.close();
		
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private Produto dadosProduto(ResultSet tabela) {
		Produto p = new Produto();
		int codigo;
		
		try {
			codigo = tabela.getInt("codigo");
			if(codigo != 0)
				p.setCodigo(codigo);
			p.setIdproduto(tabela.getInt("id_produto"));
			p.setNome(tabela.getString("nome"));
			p.setDescricao(tabela.getString("descricao"));
			p.setQt_estoque(tabela.getInt("qt_estoque"));
			p.setValorun(tabela.getFloat("valorun"));
			p.setValorvenda(tabela.getFloat("valor_venda"));
			p.setData_hora(tabela.getString("data_hora"));
			return  p ;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void imprimirProdutos() {
		System.out.println("Lista de produtos");
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("ID:\t\tNome:\t\tCodgo:\t\tqt_estoque:\t\tV.uni:\t\tV.venda:\n");
		System.out.println("--------------------------------------------------------------------------");
		
		for(Produto p : produto) {
			System.out.println(	p.getIdproduto()+"\t\t"+p.getNome()+"\t\t"+p.getCodigo()+"\t\t"
					+p.getQt_estoque()+"\t\t"+p.getValorun()+"\t\t"+p.getValorvenda()+"\t\t");
		}
	}
	
	public void excluirProduto(Produto p) {
		produto.removeIf(pd -> pd.getIdproduto() == p.getIdproduto());
	}
	
	public boolean localizarCodigo(int codigo) {
		Produto localizado = null;
		
		for(Produto p : produto) {
			if(p.getCodigo() == codigo) {
				localizado = p;
				break;
			}
		}
		return localizado != null;
	}
}
