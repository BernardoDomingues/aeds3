package View;

import Controller.MenuActions;
import java.util.Scanner;

public class Menu extends MenuActions {

  private int selectedOption;

  public int getSelectedOption() {
    return selectedOption;
  }

  public Menu() {
    selectedOption = 0;
  }

  public void setOption() {
    System.out.println("Escolha uma opção: ");
    System.out.println("1: Carregar Dados");
    System.out.println("2: Ler Registro");
    System.out.println("3: Atualizar Registro");
    System.out.println("4: Deletar Registro");
    System.out.println("5: Sair");
    Scanner scanner = new Scanner(System.in);
    int userEntry = Integer.parseInt(scanner.nextLine());
    while (userEntry < 1 || userEntry > 5) {
      System.out.println("Opção Inválida, tente novamente");
      userEntry = Integer.parseInt(scanner.nextLine());
    }
    selectedOption = userEntry;
    scanner.close();
  }

  public void executeSelectedOption() {
    if (this.selectedOption == 1) {
      this.loadData();
    }
  }
}
