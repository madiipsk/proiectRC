package servere;

import java.util.Scanner;

public class ProgramAdunare {

    public static void main(String[] args) {
        int port = 8080;
        try (ServerAdunare server = new ServerAdunare(port)) {
            System.out.println(String.format("Server running on port %d. Type 'exit' to close.", port));
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String command = scanner.nextLine();
                    if (command == null || "exit".equals(command)) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }

}