import View.Menu;

class App {
  public static void main(String[] args) {
    System.out.println("Bem Vindo a PokeDex!");

    Menu menu = new Menu();
    menu.setOption();
    menu.executeSelectedOption();
  }
}