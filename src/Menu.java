import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
//import java.util.*;

public class Menu {
    private static List<Cliente> clientes = new ArrayList<>();
    //private static Subscripcion subscripcion = new Subscripcion();
    //private static Map<Integer, Paquete> paquetes = PaqueteLoader.getPaquetes();

    private static int[] contadorPorSector = new int[7];

    public static void main(String[] args) {
        PaqueteLoader.cargarPaquetes();

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Eliminar Cliente");
            //System.out.println("3. Mostrar Clientes");
            System.out.println("4. Acceder al Menú de Suscripción");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    agregarCliente(scanner);
                    break;
                case 2:
                    eliminarCliente(scanner);
                    break;
                case 3:
                    mostrarClientes();
                    break;
                //case 4:
                    //menuSuscripcion(scanner);
                    //break;
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.err.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void agregarCliente(Scanner scanner) {
        System.out.println("\033[H\033[2J");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        System.out.println("Sectores disponibles:");
        System.out.println("0 - Valparaíso");
        System.out.println("1 - Viña del Mar");
        System.out.println("2 - Concón");
        System.out.println("3 - Quilpué");
        System.out.println("4 - Villa Alemana");
        System.out.println("5 - San Antonio");
        System.out.println("6 - Cartagena");
        System.out.print("Sector (0-6): ");
        int sector = scanner.nextInt();
        scanner.nextLine();

        String sectorNombre = obtenerNombreSector(sector);
        int id = generarID(sector);

        Cliente nuevoCliente = new Cliente(nombre, direccion, sectorNombre, id);
        clientes.add(nuevoCliente);
        System.out.println("Cliente agregado exitosamente.");
    }

    private static int generarID(int sector) {
        contadorPorSector[sector]++;
        return sector * 1000 + contadorPorSector[sector];
    }

    private static void mostrarClientes() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del cliente a buscar: ");
        String idString = scanner.nextLine();

        try {
            int id = Integer.parseInt(idString);

            Cliente clienteBuscado = null;
            for (Cliente cliente : clientes) {
                if (cliente.getId() == id) {
                    clienteBuscado = cliente;
                    break;
                }
            }

            if (clienteBuscado != null) {
                System.out.println("Datos del Cliente:");
                clienteBuscado.mostrarCliente();
            } else {
                System.out.println("Cliente con ID " + id + " no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID inválido. Por favor, ingrese un número válido.");
        }
    }
    
    private static void eliminarCliente(Scanner scanner) {
        System.out.print("ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Cliente clienteAEliminar = obtenerClientePorID(id);

        if (clienteAEliminar != null) {
            clientes.remove(clienteAEliminar);
            System.out.println("Cliente eliminado exitosamente.");
        } else {
            System.err.println("Cliente no encontrado.");
        }
    }

    private static Cliente obtenerClientePorID(int idCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == idCliente) {
                return cliente;
            }
        }
        return null;
    }

    private static String obtenerNombreSector(int sector) {
        switch (sector) {
            case 0: return "Valparaíso";
            case 1: return "Viña del Mar";
            case 2: return "Concón";
            case 3: return "Quilpué";
            case 4: return "Villa Alemana";
            case 5: return "San Antonio";
            case 6: return "Cartagena";
            default: return "Desconocido";
        }
    }
}