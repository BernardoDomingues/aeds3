package Controller;

import Model.Pokemon;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MenuActions {

  public void loadData() {
    System.out.println("Carregando Dados...");
    Pokemon[] pokemons = new Pokemon[1500];
    int nPokemons = 0;
    try {
      URL urlAllPokemons = new URL(
        "https://pokeapi.co/api/v2/pokemon/?limit=2000&offset=0"
      );
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
        pokemons[nPokemons] = pokemon;
        nPokemons++;
        System.out.println(
          "Processado: " +
          i +
          "/" +
          urlsArr.length +
          " (" +
          ((i * 100) / urlsArr.length) +
          "%)"
        );
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

    try {
      RandomAccessFile raf = new RandomAccessFile("./db/pokemons.db", "rw");

      for (int i = 0; i < nPokemons; i++) {
        Pokemon pokemon = pokemons[i];
        byte bytes[] = pokemon.byteParse();
        raf.write(bytes.length);
        raf.write(bytes);
      }
      raf.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
