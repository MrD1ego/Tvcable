package com.mycompany.tvproyecto;

import java.util.Scanner;


/**
 * Menu donde se presentan opciones al usuario, dando opciones para manejar clientes
 * o pasar al menu de las suscripciones y paquetes.
 */
public class Menu {
    
    private static final String CLIENTE_CSV = "src/main/java/com/mycompany/tvproyecto/Cliente.csv";

    public static void main(String[] args) {

        PaqueteLoader.cargarPaquetes();
        
        Scanner scanner = new Scanner(System.in);

        Cliente.cargarClientesDesdeCSV(CLIENTE_CSV);

        int opcion;

        do {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Eliminar Cliente");
            System.out.println("3. Mostrar Clientes");
            System.out.println("4. Mostrar Clientes por sector");
            System.out.println("5. Acceder al Menu de Suscripcion");
            System.out.println("6. Generar un reporte de los Clientes");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> {
                    try {
                        Cliente.agregarCliente(scanner, CLIENTE_CSV);
                    } catch (SectorInvalidoException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> Cliente.eliminarCliente(scanner, CLIENTE_CSV);
                case 3 -> Cliente.mostrarClientePorID(scanner);
                case 4 -> Cliente.mostrarClientesPorSector(scanner);
                case 5 -> Paquete.menuSuscripcion(scanner);
                case 6 -> {
                    System.out.print("Ingrese el nombre del archivo para guardar el reporte (ej: reporte_clientes.txt): ");
                    String nombreArchivo = scanner.nextLine();
                    Cliente.generarReporte(nombreArchivo);
                }
                case 0 -> {
                    Cliente.guardarClientesEnCSV(CLIENTE_CSV);
                    System.out.println("Saliendo del programa.");
                }
                default -> System.err.println("Opción no valida.");
            }
        } while (opcion != 0);
    }
}
