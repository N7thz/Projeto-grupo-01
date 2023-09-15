package arquivo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import conexao.DadosConexao;

//classe_arquivo
public class ArquivoTxt {
	private String nmArquivo;
	File Arquivo;
	
	//cria_um_objeto_data_do_tipo_DadosConexao
	private DadosConexao data = new DadosConexao();	
	
	/**
	 * @author Ricardo de Jesus Oliveira
	 * @return Retorna os dados de conexção
	 */
	public DadosConexao getData() {
		return data;
	}
	
	/**
	 *  Construtor da classe
	 *  
	 *  @autor Ricardo de Jesus Oliveira
	 *  
	 */
	
	//metodo_que_cria_um_arquivo_com_argumento_String
	public ArquivoTxt(String nmArquivo) {
		this.nmArquivo = nmArquivo;
		
		//File_é_uma_classe_nativa_do_java 
		//cria_um_objt_do_tipo_File
		File Arq = new File(nmArquivo);
		Arquivo = Arq;
	}
	
	/**
	 * Cria um novo arquivo, se não existir.
	 * 
	 * 
	 * @author Ricardo de Jesus Oliveira
	 * @return Retorna verdadeiro se o arquivo for criado ou se j� existir.
	 */
	
	//retorna_um_boolean_true_se_conseguir_criar_o_arquivo
	public boolean criarArquivo() {
		//tenta_criar_um_arquivo
		//createNewFile_tambem_é_nativo
		try {
			Arquivo.createNewFile();		
			return true;
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}			
	}
	
	public void escreverArquivo(String linha) {
		try {
			FileWriter arqEscrita = new FileWriter(nmArquivo, true);
			BufferedWriter arq = new BufferedWriter(arqEscrita);
			arqEscrita.write(linha);
			arq.newLine();
			arq.close();
			arqEscrita.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public String lerArquivo() {	
		//iniciar_a_String_vazia
		String data = "";		
		try {
			if (Arquivo.exists()) {  
				Scanner Linhas = new Scanner(Arquivo);
				
				//enquanto_tiver_linhas_escreve_a_proxima_linha
				while (Linhas.hasNextLine()) {
					data += Linhas.nextLine() + "\n";
				}
		
				Linhas.close();
			} else
				System.out.println("Arquivo não existe.");
		
		} catch (FileNotFoundException e) {
			System.out.println("Ocorreu um erro na leitura.");
			e.printStackTrace();
		}
		
		return data;
	}
	
	//retorna_booleano_true_se_o_arquivo_for_alimentado
	public boolean alimentaDadosConexao() {		
		try {
			if (Arquivo.exists()) {  
				Scanner Linhas = new Scanner(Arquivo);
				Linhas.reset();
				String linha;
				while (Linhas.hasNextLine()) {
					linha = Linhas.nextLine();
					
					if (linha.substring(0, 6).equals("local=")) {
						data.setLocal(linha.substring(6, linha.length()));
					} else if (linha.substring(0, 8).equals("usuario=")) {
						data.setUser(linha.substring(8, linha.length()));
					} else if (linha.substring(0, 6).equals("senha=")) {
						data.setSenha(linha.substring(6, linha.length()));
					} else if (linha.substring(0, 6).equals("porta=")) {
						data.setPorta(linha.substring(6, linha.length()));
					} else if (linha.substring(0, 3).equals("bd=")) {
						data.setBd(linha.substring(3, linha.length()));
					} else if (linha.substring(0, 6).equals("banco=")) {
						data.setBanco(linha.substring(6, linha.length()));
					}
				}
				Linhas.close();
			}		
		} catch (FileNotFoundException e) {
		      System.out.println("Ocorreu um erro na leitura.");
		      e.printStackTrace();
		}
		
		//variavel_resultado
		boolean result;
		//se_a_tabela_esta_vazia_preenche-la_senao_retorna_result
		if (!(data == null) &&
			!(data.getBanco()==null) &&
			!(data.getBd()==null) &&
			!(data.getLocal()==null) &&
			!(data.getPorta()==null) &&
			!(data.getSenha()==null) &&
			!(data.getUser()==null)) { 
			result = !data.getBanco().isBlank();
			result = result && !data.getBanco().isEmpty();
			result = result && !data.getBd().isBlank();
			result = result && !data.getBd().isEmpty();
			result = result && !data.getLocal().isBlank();
			result = result && !data.getLocal().isEmpty();
			result = result && !data.getPorta().isBlank();
			result = result && !data.getPorta().isEmpty();
			result = result && !data.getSenha().isBlank();
			result = result && !data.getSenha().isEmpty();
			result = result && !data.getUser().isBlank();
			result = result && !data.getUser().isEmpty();
		} else
			result = false;
		
		return result;
	}
	
	/**
	 * Exclui um arquivo existente
	 * 
	 * @author Ricardo de Jesus Oliveira
	 * @return Retorna verdadeiro se a exclusção for realizada.
	 */
	public boolean apagarArquivo() {
		return Arquivo.delete();
	}
	
	
}
