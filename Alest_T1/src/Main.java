import java.io.*;
import java.util.Scanner;

public class Main {

    public static class Palavra {
        String texto;
        int frequencia;

        Palavra(String texto) {
            this.texto = texto;
            this.frequencia = 1;
        }
    }

    public static class ListaPalavras {
        Palavra[] palavras;
        int tamanho;

        ListaPalavras(int capacidadeInicial) {
            palavras = new Palavra[capacidadeInicial];
            tamanho = 0;
        }

        public void adicionarPalavra(String palavra) {
            for (int i = 0; i < tamanho; i++) {
                if (palavras[i].texto.equals(palavra)) {
                    palavras[i].frequencia++;
                    return;
                }
            }
            if (tamanho == palavras.length) {
                dobrarCapacidade();
            }
            palavras[tamanho++] = new Palavra(palavra);
        }

        public void dobrarCapacidade() {
            int novaCapacidade = palavras.length * 2;
            Palavra[] palavrasUnidas = new Palavra[novaCapacidade];
            for (int i = 0; i < palavras.length; i++) {
                palavrasUnidas[i] = palavras[i];
            }
            palavras = palavrasUnidas;
        }

        int totalPalavras() {
            int total = 0;
            for (int i = 0; i < tamanho; i++) {
                total += palavras[i].frequencia;
            }
            return total;
        }

        public void ordenarPorFrequencia() {
            for (int i = 0; i < tamanho - 1; i++) {
                for (int j = 0; j < tamanho - i - 1; j++) {
                    if (palavras[j].frequencia < palavras[j + 1].frequencia) {
                        Palavra temp = palavras[j];
                        palavras[j] = palavras[j + 1];
                        palavras[j + 1] = temp;
                    }
                }
            }
        }
    }

    public static class NoPalindromo {
        String palavra;
        NoPalindromo proximo;

        NoPalindromo(String palavra) {
            this.palavra = palavra;
            this.proximo = null;
        }
    }

    public static class ListaPalindromos {
        NoPalindromo inicio;

        void adicionar (String palavra) {
            if (existe(palavra)) return;

            NoPalindromo novo = new NoPalindromo(palavra);
            novo.proximo = inicio;
            inicio = novo;
        }

        boolean existe(String palavra) {
            NoPalindromo atual = inicio;
            while (atual != null) {
                if (atual.palavra.equals(palavra)) return true;
                atual = atual.proximo;
            }
            return false;
        }

        public void imprimir() {
            NoPalindromo atual = inicio;
            while (atual != null) {
                System.out.println(atual.palavra);
                atual = atual.proximo;
            }
        }
    }

    public static boolean palindromo(String palavra) {
        int i = 0, j = palavra.length() - 1;
        while (i < j) {
            if (palavra.charAt(i) != palavra.charAt(j)) return false;
            i++;
            j--;
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("ContadorPalavras <arquivo.txt>");
            return;
        }

        String arquivo = args[0];
        ListaPalavras listaPalavras = new ListaPalavras(1000);
        ListaPalindromos listaPalindromos = new ListaPalindromos();

        try (Scanner scanner = new Scanner(new File(arquivo))) {
            while (scanner.hasNext()) {
                String token = scanner.next();
                String palavra = token.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

                if (palavra.isEmpty()) continue;

                listaPalavras.adicionarPalavra(palavra);

                if (palavra.length() > 1 && palindromo(palavra)) {
                    listaPalindromos.adicionar(palavra);
                }
            }

            System.out.println("Total de palavras no texto: " + listaPalavras.totalPalavras());

            listaPalavras.ordenarPorFrequencia();
            System.out.println("20 palavras mais frequentes: ");
            for (int i = 0; i < Math.min(20, listaPalavras.tamanho); i++) {
                System.out.printf("%2d. %-15s %d\n", i + 1,
                        listaPalavras.palavras[i].texto,
                        listaPalavras.palavras[i].frequencia);
            }

            System.out.println("Palavras que são palíndromos: ");
            listaPalindromos.imprimir();

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}