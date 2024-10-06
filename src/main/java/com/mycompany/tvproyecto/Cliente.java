package com.mycompany.tvproyecto;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Cliente {
    private final String nombreCliente;
    private final String direccion;
    private final String sector;
    private final int id;

    private static final List<Cliente> clientes = new ArrayList<>();
    private static final int[] contadorPorSector = new int[7];

    public Cliente(String nombreCliente, String direccion, String sector, int id) {
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.sector = sector;
        this.id = id;
    }

    public static void agregarCliente(Scanner scanner, String filename) throws SectorInvalidoException  {
        System.out.println("\033[H\033[2J");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        System.out.println("Sectores disponibles:");
        System.out.println("1 - Valparaiso");
        System.out.println("2 - Vina del Mar");
        System.out.println("3 - Concon");
        System.out.println("4 - Quilpue");
        System.out.println("5 - Villa Alemana");
        System.out.println("6 - San Antonio");
        System.out.println("7 - Cartagena");
        System.out.print("Sector (1-7): ");
        int sector = scanner.nextInt();
        scanner.nextLine();
        
        if (sector < 1 || sector > 7) {
            throw new SectorInvalidoException("El sector seleccionado no es válido. Debe ser un número entre 1 y 7.");
        }
        
        String sectorNombre = obtenerNombreSector(sector);
        int id = generarIDDisponible(sector);
        
        Cliente nuevoCliente = new Cliente(nombre, direccion, sectorNombre, id);
        clientes.add(nuevoCliente);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(nuevoCliente.getId() + "," + nuevoCliente.getNombreCliente() + "," + nuevoCliente.getDireccion() + "," + nuevoCliente.getSector());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar el cliente en el archivo CSV: " + e.getMessage());
        }
    
        System.out.println("Cliente agregado exitosamente.");
        System.out.println("El ID del cliente es: " + id);
    }

    private static int generarIDDisponible(int sector) {
        int id = generarID(sector);
        while (obtenerClientePorID(id) != null) {
            contadorPorSector[sector]++;
            id = generarID(sector);
        }
        return id;
    }

    public static void eliminarCliente(Scanner scanner, String filename) {
        System.out.print("ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
    
        // Eliminar cliente de la lista
        Cliente clienteAEliminar = obtenerClientePorID(id);
        if (clienteAEliminar != null) {
            clientes.remove(clienteAEliminar);
            System.out.println("Cliente eliminado de la lista interna exitosamente.");
        } else {
            System.err.println("Cliente no encontrado en la lista interna.");
            return; // Salir si el cliente no se encontró
        }
    
        // Ahora eliminar del archivo CSV
        eliminarClienteDelCSV(id, filename);
    }
    

    // Método para eliminar el cliente del archivo CSV
    private static void eliminarClienteDelCSV(int idCliente, String filename) {
        String archivoTemporal = "temp_clientes.csv"; // Archivo temporal para reescribir datos

        try (BufferedReader br = new BufferedReader(new FileReader(filename));
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal))) {
            String linea;
        
            // Escribir la línea de encabezados en el archivo temporal
            bw.write("ID,Nombre,Direccion,Sector");
            bw.newLine();
        
            // Lee la primera línea (encabezados) y la ignora
            br.readLine(); // Esto sigue ignorando los encabezados
        
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) != idCliente) {
                    // Si la línea no corresponde al cliente a eliminar, se copia al nuevo archivo
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar el cliente del archivo CSV.");
            //e.printStackTrace();
        }

    // Reemplazar el archivo original con el archivo temporal
    new File(filename).delete();
    new File(archivoTemporal).renameTo(new File(filename));
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

    public static void mostrarClientesPorSector(Scanner scanner) {
        System.out.println("Selecciona el sector para mostrar los clientes:");
        System.out.println("1 - Valparaiso");
        System.out.println("2 - Vina del Mar");
        System.out.println("3 - Concon");
        System.out.println("4 - Quilpue");
        System.out.println("5 - Villa Alemana");
        System.out.println("6 - San Antonio");
        System.out.println("7 - Cartagena");
        System.out.print("Sector (1-7): ");
        int sectorSeleccionado = scanner.nextInt();
        scanner.nextLine();

        String sectorNombre = obtenerNombreSector(sectorSeleccionado);

        System.out.println("Clientes del sector: " + sectorNombre);
        boolean hayClientes = false;
    
        for (Cliente cliente : clientes) {
            if (cliente.getSector().equals(sectorNombre)) {
                cliente.mostrarCliente();
                hayClientes = true;
            }
        }
    
        if (!hayClientes) {
            System.out.println("No hay clientes en el sector seleccionado.");
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
        return switch (sector) {
            case 1 -> "Valparaiso";
            case 2 -> "Vina del Mar";
            case 3 -> "Concin";
            case 4 -> "Quilpue";
            case 5 -> "Villa Alemana";
            case 6 -> "San Antonio";
            case 7 -> "Cartagena";
            default -> "Desconocido";
        };
    }

    private static int generarID(int sector) {
        contadorPorSector[sector]++;
        return sector * 1000 + contadorPorSector[sector];
    }

    public void mostrarCliente() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombreCliente);
        System.out.println("Direccion: " + direccion);
        System.out.println("Sector: " + sector);
    }

    public int getId() {
        return id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getSector() {
        return sector;
    }

    public static Set<Integer> leerIDsExistentes(String filename) {
    Set<Integer> idsExistentes = new HashSet<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        reader.readLine(); // Saltar la primera línea si tiene encabezados
        while ((line = reader.readLine()) != null) {
            String[] datos = line.split(",");
            if (datos.length > 0) {
                int id = Integer.parseInt(datos[0]);
                idsExistentes.add(id);
            }
        }
        } catch (IOException e) {
            System.err.println("Error al leer IDs existentes: " + e.getMessage());
        }
        return idsExistentes;
    }

    public static void guardarClientesEnCSV(String filename) {
    Set<Integer> idsExistentes = leerIDsExistentes(filename);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
        for (Cliente cliente : clientes) {
            if (!idsExistentes.contains(cliente.getId())) {
                writer.write(cliente.getId() + "," + cliente.getNombreCliente() + "," +
                             cliente.getDireccion() + "," + cliente.getSector());
                writer.newLine();
                idsExistentes.add(cliente.getId());
            }
        }
        System.out.println("Clientes guardados en " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar los clientes: " + e.getMessage());
        }
    }
    //Cargar los clientes del archibo CSV
    public static void cargarClientesDesdeCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Lee la primera línea (encabezados) y la ignora
            reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(",");
                if (datos.length == 4) {
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    String direccion = datos[2];
                    String sector = datos[3];
                    Cliente cliente = new Cliente(nombre, direccion, sector, id);
                    clientes.add(cliente);
                }
            }
            System.out.println("Clientes cargados desde " + filename);
        } catch (IOException e) {
            System.err.println("Error al cargar los clientes: " + e.getMessage());
        }
    }
    
    public static void generarReporte(String filename) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        writer.write("=== Reporte de Clientes ===");
        writer.newLine();
        for (Cliente cliente : clientes) {
            writer.write("ID: " + cliente.getId() + ", Nombre: " + cliente.getNombreCliente() +
                         ", Dirección: " + cliente.getDireccion() + ", Sector: " + cliente.getSector());
            writer.newLine();
        }
        System.out.println("Reporte generado en " + filename);
    } catch (IOException e) {
        System.err.println("Error al generar el reporte: " + e.getMessage());
    }
}
}
