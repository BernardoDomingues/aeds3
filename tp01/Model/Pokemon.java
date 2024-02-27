package Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pokemon {

  private int id;
  private String name;
  private char[] is_default = new char[5];
  private Date created_at;
  private String types;
  private int weight;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public char[] getIsDefault() {
    return is_default;
  }

  public String getIsDefaultAsString() {
    if (this.getIsDefault()[0] == 't') {
      return "true";
    }
    return "false";
  }

  public void setIsDefault(char[] is_default) {
    if (is_default.length <= 5) {
      this.is_default = is_default;
    } else {
      System.out.println("O tamanho do array is_default deve ser no máximo 5.");
    }
  }

  public Date getCreatedAt() {
    return created_at;
  }

  public int getSecsCreatedAt() {
    long timestamp = this.getCreatedAt().getTime();
    return (int) (timestamp / 1000);
  }

  public void setCreatedAt(Date created_at) {
    this.created_at = created_at;
  }

  public String getTypes() {
    return types;
  }

  public void setTypes(String types) {
    this.types = types;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public void processPokemonData(String json) {
    // SET PESO
    String[] splitToGetWeight = json.split("\"weight\":");
    String w = "";
    for (int i = 0; i < splitToGetWeight[1].length(); i++) {
      if (splitToGetWeight[1].charAt(i) != '}') {
        w += splitToGetWeight[1].charAt(i);
      } else {
        break;
      }
    }

    setWeight(Integer.parseInt(w));

    // -------
    // SET ID
    String[] splitToGetId = json.split("\"id\":");
    String id = "";
    for (int i = 0; i < splitToGetId[1].length(); i++) {
      if (splitToGetId[1].charAt(i) != ',') {
        id += splitToGetId[1].charAt(i);
      } else {
        break;
      }
    }

    setId(Integer.parseInt(id));

    // -------
    // SET DATE
    setCreatedAt(new Date());

    // -------
    // SET NOME
    String[] splitToGetName = json.split("\"order\":");
    String preName = "";
    for (int i = splitToGetName[0].length() - 1; i >= 0; i--) {
      if (splitToGetName[0].charAt(i) != ':') {
        if (splitToGetName[0].charAt(i) != '"' &&
            splitToGetName[0].charAt(i) != ',') {
          preName += splitToGetName[0].charAt(i);
        }
      } else {
        break;
      }
    }
    String name = "";
    for (int i = preName.length() - 1; i >= 0; i--) {
      name += preName.charAt(i);
    }

    setName(name);

    // -------
    // SET ISDEFAULT
    String[] splitToGetIsDefault = json.split("\"is_default\":");
    if (splitToGetIsDefault[1].startsWith("true")) {
      char[] t = { 't', 'r', 'u', 'e' };
      setIsDefault(t);
    } else {
      char[] f = { 'f', 'a', 'l', 's', 'e' };
      setIsDefault(f);
    }

    // -------
    // SET TYPES
    String[] splitToGetTypes = json.split("\"types\":");
    String[] typesNames = splitToGetTypes[1].split("\"name\":\"");
    String types = "|";
    for (int i = 1; i < typesNames.length; i++) {
      for (int j = 0; j < typesNames[i].length(); j++) {
        char charToAnalyse = typesNames[i].charAt(j);
        if (charToAnalyse != '"') {
          types += charToAnalyse;
        } else {
          types += '|';
          break;
        }
      }
    }

    if (types.contains(this.name)) {
      types = types.split(name)[0];
    }
    if (types.contains("rotom")) {
      types = types.split("rotom")[0];
    }
    setTypes(types);
  }

  public byte[] byteParse() throws IOException {
    ByteArrayOutputStream by = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(by);
    dos.writeInt(this.getId());
    dos.writeUTF(this.getName());
    dos.writeInt(this.getWeight());
    dos.writeInt(this.getSecsCreatedAt());
    dos.writeUTF(this.getTypes());
    dos.writeUTF(this.getIsDefaultAsString());
    dos.close();

    return by.toByteArray();
  }

  public void byteRead(byte[] arr) throws IOException, ParseException {
    ByteArrayInputStream by = new ByteArrayInputStream(arr);
    DataInputStream dis = new DataInputStream(by);
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

    this.id = dis.readInt();
    this.name = dis.readUTF();
    this.weight = dis.readInt();
    String secs = String.valueOf(dis.readInt());
    this.created_at = originalFormat.parse(secs);
    this.types = dis.readUTF();
    this.is_default = dis.readUTF().toCharArray();
    dis.close();

  }

  public String toString() {
    return ("ID: " +
        this.getId() +
        "\nNome: " +
        this.getName() +
        "\nPeso: " +
        this.getWeight() +
        "\nData de Criação: " +
        this.getCreatedAt() +
        "\nTipos: " +
        this.getTypes() +
        "\nIs Default: " +
        this.getIsDefaultAsString() +
        "\n");
  }
}
