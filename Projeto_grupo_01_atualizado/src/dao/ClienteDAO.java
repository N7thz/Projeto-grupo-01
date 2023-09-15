package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import conexao.Conexao;
import classes.Cliente;

public class ClienteDAO {
	private Conexao conexao;
	private String schema;
	
	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;
	
	public ClienteDAO(Conexao conexao, String schema) { 
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
		prepararSqlAlteracao();
		prepararSqlExclusao();
	}
	
	private void prepararSqlExclusao() {
		String sql = "delete from "+ this.schema + ".cliente";
		sql += " where idcliente = ?";
		
		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private void prepararSqlInclusao() {
		String sql = "insert into "+ this.schema + ".cliente";	
		sql += " (nome, codigo, cpf_cnpj, endereco, telefone,email, data_hora)";
		sql += " values ";
		sql += " (?, ?, ?, ?, ?, ?, ?)";
		
		try {
			this.pInclusao =  conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private void prepararSqlAlteracao() {
		String sql = "update "+ this.schema + ".cliente";	
		sql += " set nome = ?,";
		sql += " codigo = ?,";
		sql += " cpf_cnpj = ?,";
		sql += " endereco = ?,";
		sql += " telefone = ?,";
		sql += " email = ?,";
		sql += " data_hora = ?";
		sql += " where idcliente = ?";
		
		try {
			this.pAlteracao =  conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public int alterarCliente(Cliente cliente) {
		try {
			pAlteracao.setString(1, cliente.getNome());
			pAlteracao.setInt(2, cliente.getCodigo());
			pAlteracao.setString(3, cliente.getCpf_cnpj());
			pAlteracao.setString(4, cliente.getEndereco());
			pAlteracao.setString(5, cliente.getTelefone());
			pAlteracao.setString(6, cliente.getEmail());
			pAlteracao.setString(7, cliente.getData_hora());
			pAlteracao.setInt(8, cliente.getIdcliente());
			
			return pAlteracao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nCliente nao alterado.\nVerifique se foi chamado o conect:\n" + e);				
			} else {				
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	public int incluirCliente(Cliente cliente) {
		try {		
							
			pInclusao.setString(1, cliente.getNome());
			pInclusao.setInt(2, cliente.getCodigo());
			pInclusao.setString(3, cliente.getCpf_cnpj());
			pInclusao.setString(4, cliente.getEndereco());
			pInclusao.setString(5, cliente.getTelefone());
			pInclusao.setString(6, cliente.getEmail());
			pInclusao.setString(7,  cliente.getData_hora());
			
			return pInclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nCliente nao incluido.\nVerifique se foi chamado o conect:\n" + e);				
			} else {				
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	public int excluirCliente(Cliente cliente) {
		try {
			pExclusao.setInt(1, cliente.getIdcliente());
			
			return pExclusao.executeUpdate();
		} catch  (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nCliente nao incluido.\nVerifique se foi"
				+ " chamado o conect:\n" + e);				
			} else {				
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	public ResultSet carregarClientes() {
		ResultSet tabela;				
		String sql = "select * from " + this.schema + ".cliente order by id_cliente";
		
		tabela = conexao.query(sql);
			
		return tabela;
	}
}