package encryptdecrypt;

import java.io.*;
import java.util.Scanner;

class Crypto {
    public String[] operation;
    public String strInc = "";
    public int key = 0;
    public int enc = 1;
    String fileIn;
    String fileOut;
    int data = 0;
    int alg = 1;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String[] getOperation() {
        return operation;
    }

    public void setOperation(String[] operation) {
        this.operation = operation;
    }

    public String getStrInc() {
        return strInc;
    }

    public void setStrInc(String strInc) {
        this.strInc = strInc;
    }

    public void cryptoDec() {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < strInc.length(); i++) {
            if (alg == 1) {
                if (strInc.charAt(i) >= 'a' && strInc.charAt(i) <= 'z' || strInc.charAt(i) >= 'A' && strInc.charAt(i) <= 'Z') {
                    if (strInc.charAt(i) > 'a' && strInc.charAt(i) - key < 'a' || strInc.charAt(i) > 'A' && strInc.charAt(i) - key < 'A') {
                        tmp.append((char) (strInc.charAt(i) - key + 26));
                    } else {
                        tmp.append((char) (strInc.charAt(i) - key));
                    }
                } else {
                    tmp.append(strInc.charAt(i));
                }
            } else {
                tmp.append((char) (strInc.charAt(i) - key));
            }
        }
        setStrInc(tmp.toString());
    }

    public void cryptoEnc() {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < strInc.length(); i++) {
            if (alg == 1) {
                if (strInc.charAt(i) >= 'a' && strInc.charAt(i) <= 'z' || strInc.charAt(i) >= 'A' && strInc.charAt(i) <= 'Z') {
                    if (strInc.charAt(i) < 'z' && strInc.charAt(i) + key > 'z' || strInc.charAt(i) < 'Z' && strInc.charAt(i) + key > 'Z') {
                        tmp.append((char) (strInc.charAt(i) + key - 26));
                    } else {
                        tmp.append((char) (strInc.charAt(i) + key));
                    }
                } else {
                    tmp.append(strInc.charAt(i));
                }
            } else {
                tmp.append((char) (strInc.charAt(i) + key));
            }
        }
        setStrInc(tmp.toString());
    }

    public void getStrFromFile() throws FileNotFoundException {
        File f = new File(fileIn);
        if (!f.exists() || !f.isFile()) {
            System.out.println("Error");
        } else {
                try {
                    Scanner sc = new Scanner(f);
                    StringBuilder str = new StringBuilder();
                    while (sc.hasNext()) {
                        str.append(sc.nextLine());
                    }
                    strInc = str.toString();
                    sc.close();
                } catch (Exception e) {
                    System.out.println("Error");
                }
        }
    }

    public void parse() throws FileNotFoundException {
        for (int i = 0; i < operation.length; i++) {
            if (operation[i].equals("-mode")) {
                if (operation[i + 1].equals("enc")) {
                    enc = 1;
                } else {
                    enc = 2;
                }
            }
            if (operation[i].equals("-alg")) {
                if (operation[i + 1] != null ) {
                    if (operation[i + 1].equals("unicode")) {
                        alg = 2;
                    }
                }
            }
            if (operation[i].equals("-key")) {
                key = Integer.parseInt(operation[i + 1]);
            }
            if (operation[i].equals("-data")) {
                strInc = operation[i + 1];
                data = 1;
            }
            if (operation[i].equals("-in") && strInc.equals("")) {
                fileIn = operation[i + 1];
                getStrFromFile();
            }
            if (operation[i].equals("-out")) {
                    fileOut = operation[i + 1];
                }
        }
    }

    public void outWrite() throws IOException {
        if (data == 1) {
            System.out.println(strInc);
        } else {
            File f = new File(fileOut);
            FileWriter writer = new FileWriter(f);
            writer.write(strInc);
            writer.close();
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        Crypto crypto = new Crypto();
        crypto.setOperation(args);
        crypto.parse();
        switch (crypto.enc) {
            case 1:
                crypto.cryptoEnc();
                break;
            case 2:
                crypto.cryptoDec();
                break;
            default:
                System.out.println("Error");
                System.exit(1);
        }
        crypto.outWrite();
    }
}
