package View;

import Controller.MenuActions;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu extends MenuActions {

  private int selectedOption;
  private Scanner scanner;

  public int getSelectedOption() {
    return selectedOption;
  }

  public Menu() {
    selectedOption = 0;
    scanner = new Scanner(System.in);
  }

  public void setOption() {
    System.out.println("Escolha uma opção: ");
    System.out.println("1: Carregar Dados");
    System.out.println("2: Mostrar Registros");
    System.out.println("3: Ler Registro");
    System.out.println("4: Atualizar Registro");
    System.out.println("5: Deletar Registro");
    System.out.println("6: Sair");
    int userEntry = Integer.parseInt(scanner.nextLine());
    while (userEntry < 1 || userEntry > 6) {
      System.out.println("Opção Inválida, tente novamente");
      userEntry = Integer.parseInt(scanner.nextLine());
    }
    selectedOption = userEntry;
  }

  public void execute() {
    try {
      selectedOption = 0;
      this.startApp();
      this.setOption();
      this.executeSelectedOption();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void findOneEntry() {
    System.out.println("Insira o ID do pokemon que você procura: ");
    int id = Integer.parseInt(scanner.nextLine());
    this.findOne(id);
  }

  private void updateEntry() {
    System.out.println("Insira o ID do pokemon que você quer atualizar: ");
    int id = Integer.parseInt(scanner.nextLine());
    System.out.println("Insira o novo nome do pokemon: ");
    String name = scanner.nextLine();
    System.out.println("Insira o novo peso do pokemon: ");
    int weight = Integer.parseInt(scanner.nextLine());
    this.update(id, name, weight);
  }

  public void executeSelectedOption() throws FileNotFoundException {
    switch (this.selectedOption) {
      case 1:
        this.loadData();
        this.execute();
        break;
      case 2:
        this.findAll();
        this.execute();
        break;
      case 3:
        this.findOneEntry();
        this.execute();
        break;
      case 4:
        this.updateEntry();
        this.execute();
        break;
      case 5:
        this.delete();
        this.execute();
        break;
      default:
        this.finishApp();
        scanner.close();
        break;
    }
  }
}
