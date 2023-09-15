package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Main.Main;
import classes.Item;
import classes.Pedido;
import conexao.Conexao;

public class ItemDAO {
	private Conexao conexao;
	private String schema;
	
	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;
	
	public ItemDAO(Conexao conexao, String schema) {
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
        prepararSqlAlteracao();
        prepararSqlExclusao();
	}
	
	private void prepararSqlExclusao() {
		String sql = "delete from "+ this.schema + ".pedido_itens";
		sql += " where id_produto = ?";
		
		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private void prepararSqlInclusao() {
		String sql = "insert into "+ this.schema + ".pedido_itens";	
		sql += " (id_pedido, id_produto, quantidade, valor_venda)";
		sql += " values ";
		sql += " (?, ?, ?, ?)";
		
		try {
			this.pInclusao =  conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private void prepararSqlAlteracao() {
		String sql = "update "+ this.schema + ".pedido_itens";	
		sql += " set id_produto = ?,";
		sql += " quantidade = ?,";
		sql += " valor_venda = ?";
		sql += " where id_pedido_itens = ?";
		
		try {
			this.pAlteracao =  conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public void incluirItens(Pedido pedido ) {
		try {		
			for (int i = 0; i < pedido.getItens().size(); i++) {
				pInclusao.setInt(1, pedido.getId_pedido());
				pInclusao.setInt(2, pedido.getItens().get(i).getIdproduto() );
				pInclusao.setInt(3, pedido.getItens().get(i).getQuantidade());
				pInclusao.setDouble(4, pedido.getItens().get(i).getValorvenda());				
				pInclusao.executeUpdate();
			}
		} catch (Exception e) {
			
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public int alterarItem(Pedido pedido) {
		try {
			
			for (Item i : pedido.getItens()) {
				pAlteracao.setInt(1, i.getIdproduto() );
				pAlteracao.setInt(2, i.getQuantidade());
				pAlteracao.setDouble(3, i.getValorvenda());
				pAlteracao.setInt(4, i.getIdpedido_intens());	
				pAlteracao.executeUpdate();				
			}
			return pAlteracao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nProduto nao alterado.\nVerifique se foi chamado o conect:\n" + e);				
			} else {				
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	public int excluirItem(Item i) {
		try {
			pExclusao.setInt(1, i.getIdproduto());
			pExclusao.executeUpdate();
			
			return pExclusao.executeUpdate();
		} catch  (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nProduto nao incluido.\nVerifique se foi"
				+ " chamado o conect:\n" + e);				
			} else {		
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	public ResultSet carregarItem() {
		ResultSet tabela;				
		String sql = "select * from " + this.schema + ".pedido_itens order by id_pedido_itens";
		
		tabela = conexao.query(sql);
			
		return tabela;
	}
	
	public static int retornaIdPedidoItem(Conexao con, String schema) {
		ResultSet tabela;				
		String sql = "select id_pedido_itens from " + schema + ".pedido_itens order by id_pedido_itens limit 1 ";
		int i = 0;
		tabela = con.query(sql);
		
		try {
			while(tabela.next()) {
				i = tabela.getInt("id_pedido_itens");
			}
			return i;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}	