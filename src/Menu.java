import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Eliminar Cliente");
            System.out.println("3. Mostrar Clientes");
            System.out.println("4. Acceder al Menú de Suscripción");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    Cliente.agregarCliente(scanner);
                    break;
                case 2:
                    Cliente.eliminarCliente(scanner);
                    break;
                case 3:
                    Cliente.mostrarClientePorID(scanner);
                    break;
                case 4:
                    Paquete.menuSuscripcion(scanner);
                    break;
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.err.println("Opción no válida.");
            }
        } while (opcion != 0);
    }
}
