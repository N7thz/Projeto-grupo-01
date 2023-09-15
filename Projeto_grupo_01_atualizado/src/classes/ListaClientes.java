package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import conexao.Conexao;
import dao.ClienteDAO;

public class ListaClientes {
	private Conexao con;
	private String schema;
	
	public ArrayList<Cliente> cliente = new ArrayList<>();
		
	public ListaClientes (Conexao con, String schema) {
		this.con = con;
		this.schema = schema;		
		carregarListaClientes();
	}
	
	public void adicionarClienteLista(Cliente c) {
		this.cliente.add(c);
	}
	
	public Cliente localizarCliente(int idCliente) {
		Cliente localizado = null;
		
		for(Cliente c : this.cliente) {
			if(c.getIdcliente() == idCliente) {
				localizado = c;
				break;
			}
		}
		return localizado;
	}
	
	public Cliente localizarClienteNome(String nome) {
		Cliente localizado = null;
		
		for(Cliente c : this.cliente) {
			if(c.getNome().equals(nome)) {
				localizado = c;
				System.out.println("achei o cliente");
				break;
			}
		}
		return localizado;
	}

	public boolean localizarClienteCpfCnpj(String cpf_cnpj) {
        Cliente localizado = null;

        for(Cliente c : this.cliente) {
            if(c.getCpf_cnpj().equals(cpf_cnpj)) {
                localizado = c;
                break;
            }
        }
        return localizado != null;
    }
	
	private void carregarListaClientes() {
		ClienteDAO cdao = new ClienteDAO(con, schema);
		
		ResultSet tabela = cdao.carregarClientes();
		this.cliente.clear();
		
		try {
			tabela.beforeFirst();
			
			while (tabela.next()) {							
				this.cliente.add(dadosCliente(tabela));				
			}
			
			tabela.close();
		
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private Cliente dadosCliente(ResultSet tabela) { 
		Cliente c = new Cliente();
		int codigo;
		
		try {
			codigo = tabela.getInt("codigo");
			if(codigo != 0)
				c.setCodigo(codigo);
			c.setIdcliente(tabela.getInt("id_cliente"));
			c.setNome(tabela.getString("nome"));
			c.setCpf_cnpj(tabela.getString("cpf_cnpj"));			
			c.setEndereco(tabela.getString("endereco"));
			c.setTelefone(tabela.getString("telefone"));
			c.setEmail(tabela.getString("email"));
			c.setData_hora(tabela.getString("data_hora"));
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void imprimirClientes() {
		System.out.println("Lista de clientes");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("ID:\t\tNome:\t\tCPF ou CNPJ:\t\tEndereço:\t\tTelefone:\t\tEmail:\n");
		System.out.println("--------------------------------------------------------------------");
		
		for(Cliente c : cliente) {
			System.out.println(	c.getIdcliente()+"\t\t"+c.getNome()+"\t\t"+c.getCpf_cnpj()+"\t\t"
					+c.getEndereco()+"\t\t"+c.getTelefone()+"\t\t"+c.getEmail()+"\t\t");
		}
	}
	
	public void excluirCliente(Cliente c) {
		cliente.removeIf(cl -> cl.getIdcliente() == c.getIdcliente());
	}
	
	public boolean localizarCodigo(int codigo) {
		Cliente localizado = null;
		
		for(Cliente c : cliente) {
			if(c.getCodigo() == codigo) {
				localizado = c;
				break;
			}
		}
		return localizado != null;
	}
}
