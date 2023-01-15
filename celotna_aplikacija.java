import java.util.Scanner;

public class celotna_aplikacija {
    public static void main(String[] args) {
        boolean konec = false;
        while (!konec) {
            System.out.println(
                    "Vpisi za zeljeno aktivnost \n'1' za izracun danega racuna \n'2' pretvorba med stevilskimi sistemi \n'3' uporaba logicnih vrat na poljubnih st. sistemih \n'4' branje in pretvorba med st. sistemi iz datoteke \n'5' uporaba logicnih operatorjev iz zapisa v datoteki \n'q' za konec");
            Scanner inputScanner = new Scanner(System.in);
            String input = inputScanner.nextLine();
            switch (input) {
                case "1":
                    computer.main(args);
                    break;
                case "2":
                    stevilskiSistemi.main(args);
                    break;
                case "3":
                    // logicna vrata
                    break;
                case "4":
                    stevilskiSistemi.convertFromFile();
                    break;
                case "5":
                    computer.main(args);
                    break;
                case "q":
                    System.out.println("Konec.");
                    konec = true;
                    break;
                default:
                    System.out.println("Napacen vnos.");
                    break;
            }
            //
        }
    }
}
