import java.util.Map;
import java.util.Scanner;

public class Paquete {
    private int id;
    private String nombre;
    private int cantidadCanales;
    private double precio;

    private static Map<Integer, Paquete> paquetes = PaqueteLoader.getPaquetes();
    private static Subscripcion subscripcion = new Subscripcion();

    public Paquete(int id, String nombre, int cantidadCanales, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadCanales = cantidadCanales;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadCanales() {
        return cantidadCanales;
    }

    public double getPrecio() {
        return precio;
    }

    public static void menuSuscripcion(Scanner scanner) {
        int opcionSuscripcion;
        do {
            System.out.println("=== Menú de Suscripción ===");
            System.out.println("1. Agregar Paquete");
            System.out.println("2. Eliminar Paquete");
            System.out.println("3. Mostrar Paquetes del Cliente");
            System.out.println("4. Total de Canales");
            System.out.println("5. Total a Pagar");
            System.out.println("0. Regresar al Menú Principal");
            System.out.print("Selecciona una opción: ");
            opcionSuscripcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcionSuscripcion) {
                case 1:
                    agregarPaquete(scanner);
                    break;
                case 2:
                    eliminarPaquete(scanner);
                    break;
                case 3:
                    mostrarPaquetes(scanner);
                    break;
                case 4:
                    obtenerTotalCanales(scanner);
                    break;
                case 5:
                    obtenerTotalPago(scanner);
                    break;
                case 0:
                    System.out.println("Regresando al Menú Principal.");
                    break;
                default:
                    System.err.println("Opción no válida.");
            }
        } while (opcionSuscripcion != 0);
    }

    public static void agregarPaquete(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = Cliente.obtenerClientePorID(idCliente);
        if (cliente != null) {
            System.out.println("Paquetes disponibles:");
            for (Paquete paquete : paquetes.values()) {
                System.out.println(paquete.getId() + " - " + paquete.getNombre());
            }

            System.out.print("Selecciona el paquete por ID: ");
            int idPaquete = scanner.nextInt();
            scanner.nextLine();

            Paquete paqueteSeleccionado = paquetes.get(idPaquete);
            if (paqueteSeleccionado != null) {
                subscripcion.agregarPaquete(cliente, paqueteSeleccionado);
                System.out.println("Paquete agregado exitosamente.");
            } else {
                System.err.println("Paquete no encontrado.");
            }
        } else {
            System.err.println("Cliente no encontrado.");
        }
    }

    public static void eliminarPaquete(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = Cliente.obtenerClientePorID(idCliente);
        if (cliente != null) {
            System.out.println("Paquetes del cliente:");
            subscripcion.mostrarPaquetes(cliente);

            System.out.print("ID del paquete a eliminar: ");
            int idPaquete = scanner.nextInt();
            scanner.nextLine();

            Paquete paqueteAEliminar = paquetes.get(idPaquete);
            if (paqueteAEliminar != null) {
                subscripcion.eliminarPaquete(cliente, paqueteAEliminar);
                System.out.println("Paquete eliminado exitosamente.");
            } else {
                System.err.println("Paquete no encontrado.");
            }
        } else {
            System.err.println("Cliente no encontrado.");
        }
    }

    public static void mostrarPaquetes(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = Cliente.obtenerClientePorID(idCliente);
        if (cliente != null) {
            subscripcion.mostrarPaquetes(cliente);
        } else {
            System.err.println("Cliente no encontrado.");
        }
    }

    public static void obtenerTotalCanales(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = Cliente.obtenerClientePorID(idCliente);
        if (cliente != null) {
            int totalCanales = subscripcion.obtenerTotalCanales(cliente);
            System.out.println("Total de canales: " + totalCanales);
        } else {
            System.err.println("Cliente no encontrado.");
        }
    }

    public static void obtenerTotalPago(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = Cliente.obtenerClientePorID(idCliente);
        if (cliente != null) {
            double totalPago = subscripcion.obtenerTotalPago(cliente);
            System.out.println("Total a pagar: $" + totalPago);
        } else {
            System.err.println("Cliente no encontrado.");
        }
    }
}
