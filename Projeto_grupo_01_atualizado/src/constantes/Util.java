package constantes;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Util {
	public static Scanner in = new Scanner(System.in);
	public static final String CABECALHO = "SISTEMA BIBLIOTECA NACIONAL";
	public static final String LINHA = "----------------------------------";
	public static final String LINHAD = "==================================";

	public enum CRUD {
		CADASTRAR, ALTERAR, EXCLUIR, IMPRIMIR
	}

	public static void br() {
		System.out.println("");
	}

	public static void escrever(String mensagem) {
		System.out.println(mensagem);
	}

	public static LocalDate validarData(String mensagem) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataConvertida = null;
		String sData;
		boolean dataValidada = false;

		do {
			System.out.println(mensagem);
			sData = in.nextLine();

			try {
				dataConvertida = LocalDate.parse(sData, dtf);
				dataValidada = true;
				return dataConvertida;
			} catch (Exception e) {
				System.out.println("Data invalida");
				return null;
			}
		} while (!dataValidada);
	}

	public static int validarInteiro(String mensagem) {
		int numero = 0;
		boolean validado = false;

		do {
			try {
				System.out.println(mensagem);
				String s = in.nextLine();
				numero = Integer.parseInt(s);
				validado = true;
			} catch (Exception e) {
				System.out.println("Informe um numero valido - " + e.getMessage());
			}
		} while (!validado);

		// in.close();

		return numero;
	}
	
	public static long cpfCnpjTratado() {
        Scanner in = new Scanner(System.in);
        boolean entradaValida = false;
        long i = 0;
         do {
                try {
                    i = in.nextLong();
                    entradaValida = true;
                } catch (InputMismatchException e) {
                    System.out.println("ERRO! Digite um valor valido.");
                    in.next();
                }
            } while (!entradaValida);
         return i;
    }

	public static double validarDouble() {
		String s;
		double numero = 0.0;
		boolean validado = false;
		Scanner in = new Scanner(System.in);

		do {
			try {
				s = in.next();
				numero = Double.parseDouble(s);
				validado = true;
			} catch (Exception e) {
				System.out.println("Informe um numero valido - " + e.getMessage());
			}
		} while (!validado);

		in.close();

		return numero;
	}

	public static int intTratado() {
		Scanner in = new Scanner(System.in);
		boolean entradaValida = false;
		int i = 0;
		do {
			try {
				System.out.println("Informe a quantidade: ");
				i = in.nextInt();
				entradaValida = true;
			} catch (InputMismatchException e) {
				System.out.println("ERRO! Digite um valor valido.");
				in.next();
			}
		} while (!entradaValida);
		return i;
	}

	public static int codTratado() {
		Scanner in = new Scanner(System.in);
		boolean entradaValida = false;
		int i = 0;
		do {
			try {
				System.out.println("Informe o codigo do produto: ");
				i = in.nextInt();
				entradaValida = true;
			} catch (InputMismatchException e) {
				System.out.println("ERRO! Digite um valor valido.");
				in.next();
			}
		} while (!entradaValida);
		return i;
	}

	public static double doubleTratado() {
		Scanner in = new Scanner(System.in);
		boolean entradaValida = false;
		double i = 0;
		do {
			try {
				System.out.println("Informe o valor unitario: ");
				i = in.nextDouble();
				entradaValida = true;
			} catch (InputMismatchException e) {
				System.out.println("ERRO! Digite um valor valido.");
				in.next();
			}
		} while (!entradaValida);
		return i;
	}

	public static double casaDecimalTratada(double d) {
		DecimalFormat formato = new DecimalFormat("#.##");
		String valorFormatadoStr = formato.format(d);
		valorFormatadoStr = valorFormatadoStr.replace(",", ".");
		return Double.parseDouble(valorFormatadoStr);
	}
}
