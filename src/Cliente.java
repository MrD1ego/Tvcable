import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class Cliente {
    private String nombreCliente;
    private String direccion;
    private String sector;
    private int id;

    private static List<Cliente> clientes = new ArrayList<>();
    private static int[] contadorPorSector = new int[7];

    public Cliente(String nombreCliente, String direccion, String sector, int id) {
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.sector = sector;
        this.id = id;
    }

    public static void agregarCliente(Scanner scanner) {
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

    public static void eliminarCliente(Scanner scanner) {
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

    public static void mostrarClientePorID(Scanner scanner) {
        System.out.print("Ingrese el ID del cliente a buscar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = obtenerClientePorID(id);

        if (cliente != null) {
            cliente.mostrarCliente();
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

    public static Cliente obtenerClientePorID(int idCliente) {
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

    private static int generarID(int sector) {
        contadorPorSector[sector]++;
        return sector * 1000 + contadorPorSector[sector];
    }

    public void mostrarCliente() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombreCliente);
        System.out.println("Dirección: " + direccion);
        System.out.println("Sector: " + sector);
    }

    public int getId() {
        return id;
    }
}
