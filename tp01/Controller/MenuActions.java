package Controller;

import Model.Pokemon;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MenuActions {

  RandomAccessFile raf;

  public void startApp() throws Exception {
    raf = new RandomAccessFile("./db/pokemons.db", "rw");
  }

  public void finishApp() {
    try {
      raf.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void loadData() {
    System.out.println("Carregando Dados...");
    try {
      URL urlAllPokemons = new URL(
          "https://pokeapi.co/api/v2/pokemon/?limit=2000&offset=0");
      HttpURLConnection conn = (HttpURLConnection) urlAllPokemons.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();
      String inline = "";
      Scanner scanner = new Scanner(urlAllPokemons.openStream());
      while (scanner.hasNext()) {
        inline += scanner.nextLine();
      }
      scanner.close();
      String[] urlsArr = inline.split("url\":\"");
      for (int i = 1; i < urlsArr.length; i++) {
        String fullData = urlsArr[i];
        String pokemonUrl = "";

        for (int j = 0; j < fullData.length(); j++) {
          if (fullData.charAt(j) != '"') {
            pokemonUrl += fullData.charAt(j);
          } else {
            break;
          }
        }

        URL urlPokemon = new URL(pokemonUrl);
        conn = (HttpURLConnection) urlPokemon.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        String inlinePokemon = "";
        scanner = new Scanner(urlPokemon.openStream());
        while (scanner.hasNext()) {
          inlinePokemon += scanner.nextLine();
        }
        scanner.close();

        Pokemon pokemon = new Pokemon();
        pokemon.processPokemonData(inlinePokemon);
        try {
          byte bytes[] = pokemon.byteParse();
          raf.write(bytes.length);
          raf.write(bytes);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
        System.out.println(
            "Processado: " +
                i +
                "/" +
                urlsArr.length +
                " (" +
                ((i * 100) / urlsArr.length) +
                "%)");
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void findAll() throws FileNotFoundException {
    System.out.println("Mostrando Registros...");
    Pokemon pokemon = new Pokemon();
    try {
      raf.seek(0);
      while (true) {
        int size = raf.readByte();
        byte[] ba = new byte[size];
        raf.read(ba);
        pokemon.byteRead(ba);
        System.out.println(pokemon.toString());
      }
    } catch (Exception e) {
      System.out.println("\nFim dos Registros...");
    }
  }

  public void findOne() {
    System.out.println("Insira o ID do pokemon que você procura: \n");
    Scanner scanner = new Scanner(System.in);
    int id = scanner.nextInt();
    Pokemon pokemon = new Pokemon();

    try {
      raf.seek(0);
      while (true) {
        int size = raf.readByte();
        byte[] ba = new byte[size];
        raf.read(ba);
        pokemon.byteRead(ba);
        if (id == pokemon.getId()) {
          System.out.println(pokemon.toString());
        }
      }
    } catch (Exception e) {
      System.out.println("\nFim dos Registros...");
    }
    scanner.close();
  }

  public void update() {
    System.out.println("Atualizar Registro...");
  }

  public void delete() {
    System.out.println("Deletar Registro...");
  }
}
