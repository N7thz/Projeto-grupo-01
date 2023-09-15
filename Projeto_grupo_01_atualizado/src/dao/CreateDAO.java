package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import conexao.Conexao;
import conexao.DadosConexao;

public class CreateDAO {
	private static Conexao conexao;	
	
	public static boolean createBD(String bd, String schema, DadosConexao dadosCon) {		
		boolean bdCriado = false;
		conexao = conectar("postgres", dadosCon);
		
		if (criarDatabase(conexao, bd)) {
			desconectar(conexao);
			
			conexao = conectar(bd, dadosCon);
			
			if (criarSchema(conexao, schema)) {
				criarEntidadeCliente(conexao, schema);
				criarEntidadeProduto(conexao, schema);
				criarEntidadePedido(conexao, schema);
				criarEntidadePedidoItens(conexao, schema);
				
				bdCriado = true;
			}
		}
		desconectar(conexao);
		
		return bdCriado;
	}
	
	private static Conexao conectar(String bd, DadosConexao dadosCon) {		
		dadosCon.setBanco(bd);
		Conexao conexao = new Conexao(dadosCon);
		conexao.conect();
		return conexao;
	}
	
	private static void desconectar(Conexao con) {
		con.disconect();
	}
	
	private static boolean criarDatabase(Conexao con, String bd) {		
		boolean bdExiste;
		int tentativas = 1;
		String sql;
				
		class Database {		
			public static ResultSet Exists(Conexao con, String bd) {
				ResultSet entidade;
				String sql = "select datname from pg_database where datname = '" + bd + "'";		
				entidade = con.query(sql);
				return entidade;
			}
		}
				
		do {
			try {
				bdExiste = Database.Exists(con, bd).next(); 
				
				if (!bdExiste) {
					sql = "create database "+ bd;		
					con.query(sql);
					tentativas++;
				}
			} catch (Exception e) {
				System.err.printf("Nao foi possivel criar o database %s: %s", bd, e);
				e.printStackTrace();
				return false;
			}
		} while (!bdExiste && (tentativas<=3));
		
		return bdExiste;
	}
	
	private static boolean criarSchema(Conexao con, String schema) {		
		boolean schemaExiste;
		int tentativas = 1;
		String sql;
				
		class Schema {		
			public static ResultSet Exists(Conexao con, String schema) {
				ResultSet entidade;
				String sql = "select * from pg_namespace where nspname = '" + schema + "'";		
				entidade = con.query(sql);
				return entidade;
			}
		}
				
		do {
			try {
				schemaExiste = Schema.Exists(con, schema).next(); 
				
				if (!schemaExiste) {
					sql = "create schema "+ schema;		
					con.query(sql);
					tentativas++;
				}
			} catch (Exception e) {
				System.err.printf("Nao foi possivel criar o schema %s: %s", schema, e);
				e.printStackTrace();
				return false;
			}
		} while (!schemaExiste && (tentativas<=3));
		
		return schemaExiste;
	}
	
	private static void criarTabela(Conexao con, String entidade, String schema) {				
		String sql = "create table " + schema + "." + entidade + " ()";		
		con.query(sql);		
	}
	
	private static void criarCampo(Conexao con, String schema, String entidade, 
            String atributo, String tipoAtributo, boolean primario, 
            boolean estrangeiro, String entidadeEstrangeira, 
            String atributoEstrangeiro, String... acoesCascade) {

        if (!atributoExists(con, schema, entidade, atributo)) {
            String sql = "alter table " + schema + "." + entidade + " add column " + 
                atributo + " " + tipoAtributo + " "; 

            if (primario) {
                sql += "primary key "; 
            }

            if (estrangeiro) {
                sql += "references " + entidadeEstrangeira + "(" + atributoEstrangeiro + ")";

                // Adicione as ações de cascata se fornecidas
                if (acoesCascade != null && acoesCascade.length > 0) {
                    for (String acao : acoesCascade) {
                        sql += " " + acao;
                    }
                }
            }

            con.query(sql);
        }
	}
	
	private static void criarChaveComposta(Conexao con, String schema, String entidade, 
			String nomesCamposCompostos ) {
		
		boolean chaveExist = false;
		String sql = "SELECT CONNAME FROM pg_constraint where conname = 'chave_pk'";				
		ResultSet result = con.query(sql);
		
		try {
			chaveExist = (result.next()?true:false);
			
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		if (!chaveExist) {
			sql = "alter table " + schema + "." + entidade + " add constraint chave_pk" +
					" primary key (" + nomesCamposCompostos + ")";
				
		con.query(sql);
		}
	}
		
	private static void criarEntidadeCliente(Conexao con, String schema) {
		String entidade = "cliente";
		
		if (!entidadeExists(con, schema, entidade))		
			criarTabela(con, entidade, schema);
		
		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "id_cliente","serial"	 	  , true , false, null, null);			
			criarCampo(con, schema, entidade, "nome"	  , "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "codigo"	  , "int" 	 	  , false, false, null, null);
			criarCampo(con, schema, entidade, "cpf_cnpj"  , "varchar(14)" , false, false, null, null);
			criarCampo(con, schema, entidade, "endereco"  , "varchar(150)", false, false, null, null);
			criarCampo(con, schema, entidade, "telefone"  , "varchar(20)" , false, false, null, null);
			criarCampo(con, schema, entidade, "email" 	  , "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "data_hora" , "varchar(100)", false, false, null, null);
			cadastrarClientes(con, schema, entidade);
		}		
	}
	
	private static void criarEntidadeProduto(Conexao con, String schema) {
		String entidade = "produto";
		
		if (!entidadeExists(con, schema, entidade))		
			criarTabela(con, entidade, schema);
		
		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "id_produto"	, "serial"	 	 	, true,  false, null, null);
			criarCampo(con, schema, entidade, "nome"	 	, "varchar(100)" 	, false, false, null, null);
			criarCampo(con, schema, entidade, "codigo"		, "int" 	 	 	, false, false, null, null);
			criarCampo(con, schema, entidade, "descricao"	, "text" 	 	 	, false, false, null, null);
			criarCampo(con, schema, entidade, "qt_estoque"  , "int"	 		 	, false, false, null, null);
			criarCampo(con, schema, entidade, "valorun"	 	, "double precision", false, false, null, null);			
			criarCampo(con, schema, entidade, "valor_venda"	, "double precision", false, false, null, null);
			criarCampo(con, schema, entidade, "data_hora"	, "varchar(100)"	, false, false, null, null);
			cadastrarProdutos(con, schema, entidade);
		}		
	}
	
	private static void criarEntidadePedido(Conexao con, String schema) {
		String entidade = "pedido";
		
		if (!entidadeExists(con, schema, entidade))		
			criarTabela(con, entidade, schema);
		
		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "id_pedido" 		, "serial"	 		, true,  false, null, null);
			criarCampo(con, schema, entidade, "codigo"	  		, "int"				, false, false, null, null);
			criarCampo(con, schema, entidade, "id_cliente"		, "int"				, false, true, schema + ".cliente" , "id_cliente");
			criarCampo(con, schema, entidade, "total_pedido"	, "double precision", false, false, null, null);
			criarCampo(con, schema, entidade, "data_hora" 		, "varchar(40)"		, false, false, null, null);
		}		
	}
	
	private static void criarEntidadePedidoItens(Conexao con, String schema) {
	    String entidade = "pedido_itens";

	    if (!entidadeExists(con, schema, entidade))
	        criarTabela(con, entidade, schema);
	    if (entidadeExists(con, schema, entidade)) {
	        criarCampo(con, schema, entidade, "id_pedido_itens", "serial"          , true , false, null, null);
	        criarCampo(con, schema, entidade, "id_pedido"     , "integer"        , false, true , schema + ".pedido", "id_pedido", "ON DELETE CASCADE");
	        criarCampo(con, schema, entidade, "id_produto"    , "integer"        , false, true , schema + ".produto", "id_produto", "ON DELETE CASCADE");
	        criarCampo(con, schema, entidade, "quantidade"    , "integer"        , false, false, null, null);
	        criarCampo(con, schema, entidade, "valor_venda"   , "double precision", false, false, null, null);
	    }
	}

	public static boolean databaseExists(Conexao con, String bd) {
		ResultSet entidade;
		boolean dbExists = false;
		
		String sql = "select datname from pg_database where datname = '" + bd + "'";		
		entidade = con.query(sql);
		
		try {
			dbExists = entidade.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dbExists;
	}

	public static boolean entidadeExists(Conexao con, String schema, String entidade) {
		boolean entidadeExist = false;
		String sql = 
				"SELECT n.nspname AS schemaname, c.relname AS tablename " +
				   "FROM pg_class c " +
				   "LEFT JOIN pg_namespace n ON n.oid = c.relnamespace " +
				   "LEFT JOIN pg_tablespace t ON t.oid = c.reltablespace " +
				"WHERE c.relkind = 'r' " +
				"AND n.nspname = '" + schema + "' " +
				"AND c.relname = '" + entidade + "'";
		
		ResultSet tabela = con.query(sql);
		
		try {
			entidadeExist = (tabela.next()?true:false);  // (condi  o?bloco:bloco)
			
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		return entidadeExist;
	}
	
	public static boolean atributoExists(Conexao con, String schema, 
			String entidade, String atributo) {
		
		boolean atributoExist = false;
		
		String sql = "select table_schema, table_name, column_name from information_schema.columns "
				+ "where table_schema = '" + schema + "' "
				+ "and table_name = '" + entidade + "' "
				+ "and column_name = '" + atributo + "'";
		
		ResultSet result = con.query(sql);
		
		try {
			atributoExist = (result.next()?true:false);
			
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return atributoExist;
	}
	
	private static void cadastrarClientes(Conexao con, String schema, String entidade) {
		
		ResultSet tabela = con.query("select nome from " + schema + "." + entidade + " limit 1");
		
		try {
			if (!tabela.next()) {
				String sql = "insert into " + schema + ".cliente";
				sql += " (nome, codigo, cpf_cnpj, endereco, telefone, email, data_hora)";
				sql += " values";
//					    | Nome			 	 |codigo|     cpf_cnpj	  |			Endereço 			 |		Telefone   |			E-mail				  |			Data			|
				sql += "('josé pereira'      , 3124 , '58088360005', 'rua carmelia dos santos 349', '(61) 6194-2710', 'floydNLincoln@rhyta.com'		  , '14-09-2023 06:42:01'),";
				sql += "('carlos flores' 	 , 4538 , '79530702000', 'rua sacramento 134'		  , '(21) 3853-5915', 'atef7731@uorak.com'			  , '22-08-1921 09:47:23'),";
				sql += "('lisa de padilha'	 , 3822 , '52266398059', 'quadra SCRN 714/715 Bloco C', '(61) 2254-2355', 'thalesmoura@urbam.com.br'		  , '16-01-1988 13:40:20'),";
				sql += "('gilmar de assunção', 9876 , '97740776017', 'rua são Judas Tadeu'		  , '(84) 3497-9206', 'sebastiana_santos@iaru.com'	  , '27-11-2010 23:06:50'),";
				sql += "('clara luiza branco', 5927 , '27206083005', 'rua doutor Zanete Cardinal' , '(79) 3540-8933', 'mateus_rodrigues@dpi.indl.com'  ,	'07-03-1973 22:42:21'),";
				sql += "('heloísa brito'	 , 4339 , '17220493088', 'rua edinor Lima de Moura'	  , '(83) 3967-4692', 'pietro_moreira@osbocops.com'	  ,	'20-04-2009 10:06:36'),";
				sql += "('joão robson marin' , 1116 , '17201493088', 'rua imburana'				  , '(89) 2064-8568', 'rebeca-rezende85@fortlar.com'	  ,	'27-07-1974 10:02:57'),";
				sql += "('luciano demilson'	 , 1484 , '17320493082', 'rua mena Barreto'			  , '(83) 3473-8134', 'oliver-freitas79@outershoes.com', '08-07-1990 08:38:07'),";
				sql += "('juliana cortês'	 , 2099 , '17520493088', 'rua macau'				  , '(79) 2777-5357', 'renata.castro@cntbrasil.com' 	  ,	'10-01-1976 04:35:35'),";
				sql += "('evandro assunção'	 , 3994 , '17220493042', 'quadra 405 norte alameda 1' , '(43) 2247-4753', 'arthur@pciinformatica.com'	  , '18-12-2018 23:59:56'),";
				sql += "('fernando correia'	 , 5755 , '53163363274', 'rua são francisco'          , '(48) 2666-1541', 'vicente-freitas96@reginfo.com'  , '16-05-1997 20:32:41')";
				
				con.query(sql);
				tabela.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private static void cadastrarProdutos(Conexao con, String schema, String entidade) {
		
		ResultSet tabela = con.query("select nome from " + schema + "." + entidade + " limit 1");
		
		try {
			if (!tabela.next()) {
				String sql = "insert into " + schema + "." + entidade;

				sql += " (nome, codigo, descricao, qt_estoque, valorun, valor_venda, data_hora)";
				sql += " values";
//				        | Nome			|codigo|      descricao	     |qte|valoru|val_v |		Data			|
				sql += "('tv'			, 10 , 'tela plana'			 , 20, 1500 , 1650 ,'14-09-2023 14:31:16'),";
				sql += "('geladeira'	, 20 , 'electrolux RE31'	 , 30, 3000 , 3300 ,'14-09-2023 14:45:20'),";
				sql += "('mesa'			, 30 , 'madeira'			 , 40, 1000 , 1100 ,'15-09-2023 13:40:20'),";
				sql += "('radio'		, 40 , 'retro'				 , 50, 150  , 165  ,'16-09-2023 10:45:20'),";
				sql += "('sofa'			, 50 , '4 lugares'			 , 15, 1200 , 1320 ,'17-09-2023 16:10:20'),";
				sql += "('cadeira Gamer', 60 , 'sem dores nas costas', 10, 900  , 999  ,'18-09-2023 19:30:00'),";
				sql += "('ventilador'	, 70 , 'de mesa'			 , 25, 110  , 121  ,'19-09-2023 19:25:35')";

				
				con.query(sql);
				tabela.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
