package com.mycompany.tvproyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Paquete {
    private final int id;
    private final String nombre;
    private final int cantidadCanales;
    private double precio;
    private int contadorSuscripciones;
    private final double precioOriginal;

    private static final Subscripcion subscripcion = new Subscripcion();

    public Paquete(int id, String nombre, int cantidadCanales, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadCanales = cantidadCanales;
        this.precio = precio;
        this.precioOriginal = precio;
        this.contadorSuscripciones = 0;
    }

    public double getPrecioOriginal() {
        return precioOriginal;
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
            System.out.println("=== Menu de Suscripcion ===");
            System.out.println("1. Agregar Paquete");
            System.out.println("2. Eliminar Paquete");
            System.out.println("3. Mostrar Paquetes del Cliente");
            System.out.println("4. Total de Canales");
            System.out.println("5. Total a Pagar");
            System.out.println("0. Regresar al Menu Principal");
            System.out.print("Selecciona una opcion: ");
            opcionSuscripcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcionSuscripcion) {
                case 1 -> agregarPaquete(scanner);
                case 2 -> eliminarPaquete(scanner);
                case 3 -> mostrarPaquetes(scanner);
                case 4 -> obtenerTotalCanales(scanner);
                case 5 -> obtenerTotalPago(scanner);
                case 0 -> System.out.println("Regresando al Menu Principal.");
                default -> System.err.println("Opción no aálida.");
            }
        } while (opcionSuscripcion != 0);
    }


    public void aplicarDescuento(double porcentaje) {
        this.precio = this.precio * (1 - porcentaje);
    }
    
public static void agregarPaquete(Scanner scanner) {
    System.out.print("ID del cliente: ");
    int idCliente = scanner.nextInt();
    scanner.nextLine();

    Cliente cliente = Cliente.obtenerClientePorID(idCliente);
    if (cliente != null) {
        String sectorCliente = cliente.getSector();
        System.out.println("Paquetes disponibles en el sector " + sectorCliente + ":");

        List<Paquete> paquetesSector = PaqueteLoader.obtenerPaquetesPorSector(sectorCliente);
        if (paquetesSector.isEmpty()) {
            System.out.println("No hay paquetes disponibles en este sector.");
            return;
        }

        // Encuentra el paquete con menos suscripciones
        Paquete paqueteMenosSuscripciones = paquetesSector.stream()
            .min((p1, p2) -> Integer.compare(p1.getContadorSuscripciones(), p2.getContadorSuscripciones()))
            .orElse(null);

        System.out.println("Paquete con menos suscripciones: " + paqueteMenosSuscripciones.getNombre() + " (" + paqueteMenosSuscripciones.getContadorSuscripciones() + " suscripciones)");

        // Muestra los paquetes disponibles
        for (Paquete paquete : paquetesSector) {
            System.out.println(paquete.getId() + " - " + paquete.getNombre());
        }

        System.out.print("Selecciona el paquete por ID: ");
        int idPaquete = scanner.nextInt();
        scanner.nextLine();

        Paquete paqueteSeleccionado = paquetesSector.stream()
            .filter(p -> p.getId() == idPaquete)
            .findFirst()
            .orElse(null);

        if (paqueteSeleccionado != null) {
            if (paqueteSeleccionado.equals(paqueteMenosSuscripciones)) {
                double precioConDescuento = paqueteSeleccionado.getPrecio() * 0.5;
                System.out.println("Has seleccionado el paquete " + paqueteSeleccionado.getNombre() + " con un 50% de descuento. Precio a pagar: $" + precioConDescuento);

                subscripcion.agregarPaquete(cliente, paqueteSeleccionado);
                paqueteSeleccionado.incrementarContadorSuscripciones(); // Incrementar contador de suscripciones

                // Guardar cliente y paquete en el CSV
                guardarClientePaqueteEnCSV(cliente, paqueteSeleccionado, precioConDescuento);

                System.out.println("Paquete agregado exitosamente.");
            } else {
                // Ofrecer el paquete menos popular como 2x1
                System.out.println("Has seleccionado el paquete " + paqueteSeleccionado.getNombre() + ". Te ofrecemos el paquete " + paqueteMenosSuscripciones.getNombre() + " como un 2x1, costo $0.");
                
                subscripcion.agregarPaquete(cliente, paqueteSeleccionado);
                subscripcion.agregarPaquete(cliente, paqueteMenosSuscripciones);
                
                paqueteSeleccionado.incrementarContadorSuscripciones(); // Incrementar contador de suscripciones
                paqueteMenosSuscripciones.incrementarContadorSuscripciones(); // Incrementar el contador del paquete menos popular

                // Guardar ambos paquetes en el CSV
                guardarClientePaqueteEnCSV(cliente, paqueteSeleccionado, paqueteSeleccionado.getPrecio());
                guardarClientePaqueteEnCSV(cliente, paqueteMenosSuscripciones, 0.0);

                System.out.println("Paquete agregado exitosamente.");
            }

            // Mostrar el contador de suscripciones
            System.out.println("Este paquete ahora tiene " + paqueteSeleccionado.getContadorSuscripciones() + " suscripciones.");
        } else {
            System.err.println("Paquete no encontrado.");
        }
    } else {
        System.err.println("Cliente no encontrado.");
    }
}

// Método para guardar cliente y paquete en el CSV
public static void guardarClientePaqueteEnCSV(Cliente cliente, Paquete paquete, double precioFinal) {
    String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv"; // Ruta al archivo CSV
    try (FileWriter writer = new FileWriter(archivoCSV, true)) {
        writer.append(cliente.getId() + ",");
        writer.append(cliente.getNombreCliente() + ",");
        writer.append(cliente.getDireccion() + ",");
        writer.append(cliente.getSector() + ",");
        writer.append(paquete.getId() + ",");
        writer.append(paquete.getNombre() + ",");
        writer.append(precioFinal + ",");
        writer.append(paquete.getCantidadCanales() + "\n");
        writer.flush();
    } catch (IOException e) {
        System.out.println("Error al guardar en el archivo CSV.");
        //e.printStackTrace();
    }
}

// Método para verificar si el cliente ya tiene un paquete
public static boolean clienteYaTienePaquete(int idCliente, int idPaquete) {
    String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv";
    try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (Integer.parseInt(datos[0]) == idCliente && Integer.parseInt(datos[4]) == idPaquete) {
                return true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo CSV.");
        //e.printStackTrace();
    }
    return false;
}
    
public static void eliminarPaquete(Scanner scanner) {
    System.out.print("ID del cliente: ");
    int idCliente = scanner.nextInt();
    scanner.nextLine();

    Cliente cliente = Cliente.obtenerClientePorID(idCliente);
    if (cliente != null) {
        System.out.println("Paquetes del cliente:");
        
        // Mostrar los paquetes del cliente desde el CSV
        String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv"; // Reemplaza con la ruta correcta
        List<Paquete> paquetesCliente = new ArrayList<>();

        // Leer el archivo CSV y mostrar los paquetes del cliente
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Integer.parseInt(datos[0]) == idCliente) {
                    int idPaquete = Integer.parseInt(datos[4]); // ID del paquete
                    String nombrePaquete = datos[5]; // Nombre del paquete
                    double precio = Double.parseDouble(datos[6]); // Precio
                    int cantidadCanales = Integer.parseInt(datos[7]); // Cantidad de canales

                    // Mostrar información del paquete
                    System.out.println("ID Paquete: " + idPaquete + ", Nombre: " + nombrePaquete + 
                                       ", Precio: $" + precio + ", Canales: " + cantidadCanales);
                    
                    // Agregar paquete a la lista para eliminar más tarde
                    paquetesCliente.add(new Paquete(idPaquete, nombrePaquete, cantidadCanales, precio));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV.");
            //e.printStackTrace();
            return;
        }

        // Pedir ID del paquete a eliminar
        System.out.print("ID del paquete a eliminar: ");
        int idPaquete = scanner.nextInt();
        scanner.nextLine();

        // Buscar el paquete en la lista y eliminarlo
        Paquete paqueteAEliminar = paquetesCliente.stream()
            .filter(p -> p.getId() == idPaquete)
            .findFirst()
            .orElse(null);

        if (paqueteAEliminar != null) {
            // Eliminar paquete del CSV
            eliminarPaqueteDelCSV(idCliente, paqueteAEliminar.getId());
            System.out.println("Paquete eliminado exitosamente.");
        } else {
            System.err.println("Paquete no encontrado.");
        }
    } else {
        System.err.println("Cliente no encontrado.");
    }
}

// Método para eliminar el paquete del archivo CSV
private static void eliminarPaqueteDelCSV(int idCliente, int idPaquete) {
    String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv"; // Reemplaza con la ruta correcta
    String archivoTemporal = "clientes_paquetes_temp.csv"; // Nombre diferente para el archivo temporal

    try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV));
         BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            // Si la línea no corresponde al cliente y paquete a eliminar, se copia al nuevo archivo
            if (!(Integer.parseInt(datos[0]) == idCliente && Integer.parseInt(datos[4]) == idPaquete)) {
                bw.write(linea);
                bw.newLine();
            }
        }
    } catch (IOException e) {
        System.out.println("Error al eliminar el paquete del archivo CSV.");
        //e.printStackTrace();
    }

    // Reemplazar el archivo original con el archivo temporal
    // Asegurarse de que el archivo temporal fue creado correctamente
    File originalFile = new File(archivoCSV);
    File tempFile = new File(archivoTemporal);

    if (originalFile.delete()) {
        if (!tempFile.renameTo(originalFile)) {
            System.err.println("Error al renombrar el archivo temporal.");
        }
    } else {
        System.err.println("Error al eliminar el archivo original.");
    }
}


    public static void mostrarPaquetes(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
    
        // Ruta del archivo CSV
        String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv"; // Reemplaza con la ruta correcta
    
        boolean clienteEncontrado = false;
    
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            System.out.println("Paquetes del cliente con ID " + idCliente + ":");
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
    
                // Verificar si la línea corresponde al cliente buscado
                if (Integer.parseInt(datos[0]) == idCliente) {
                    clienteEncontrado = true;
                    // Mostrar información del paquete
                    System.out.println("Cliente: " + datos[1]);
                    System.out.println("Dirección: " + datos[2]);
                    System.out.println("Sector: " + datos[3]);
                    System.out.println("ID Paquete: " + datos[4]);
                    System.out.println("Nombre Paquete: " + datos[5]);
                    System.out.println("Precio: $" + datos[6]);
                    System.out.println("Cantidad de Canales: " + datos[7]);
                    System.out.println("--------------------------------------------------");
                }
            }
    
            if (!clienteEncontrado) {
                System.err.println("Cliente no encontrado.");
            }
    
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV.");
            //e.printStackTrace();
        }
    }

    public static void obtenerTotalCanales(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
    
        // Ruta del archivo CSV
        String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv"; // Reemplaza con la ruta correcta
    
        boolean clienteEncontrado = false;
        int totalCanales = 0;
    
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            System.out.println("Detalles de canales para el cliente con ID " + idCliente + ":");
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
    
                // Verificar si la línea corresponde al cliente buscado
                if (Integer.parseInt(datos[0]) == idCliente) {
                    clienteEncontrado = true;
                    int canalesPaquete = Integer.parseInt(datos[7]); // Obtener la cantidad de canales
                    totalCanales += canalesPaquete; // Sumar a total de canales
    
                    // Mostrar información del paquete
                    System.out.println("Cliente: " + datos[1]);
                    System.out.println("Dirección: " + datos[2]);
                    System.out.println("ID Paquete: " + datos[4]);
                    System.out.println("Nombre Paquete: " + datos[5]);
                    System.out.println("Cantidad de Canales: " + canalesPaquete);
                    System.out.println("--------------------------------------------------");
                }
            }
    
            if (clienteEncontrado) {
                System.out.println("Total de canales: " + totalCanales);
            } else {
                System.err.println("Cliente no encontrado.");
            }
    
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV.");
            //e.printStackTrace();
        }
    }
    

    public static void obtenerTotalPago(Scanner scanner) {
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
    
        // Ruta del archivo CSV
        String archivoCSV = "src/main/java/com/mycompany/tvproyecto/clientes_paquetes.csv"; // Reemplaza con la ruta correcta
    
        boolean clienteEncontrado = false;
        double totalPago = 0.0;
    
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            System.out.println("Detalles de pago para el cliente con ID " + idCliente + ":");
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
    
                // Verificar si la línea corresponde al cliente buscado
                if (Integer.parseInt(datos[0]) == idCliente) {
                    clienteEncontrado = true;
                    double precioPaquete = Double.parseDouble(datos[6]); // Obtener el precio del paquete
                    totalPago += precioPaquete; // Sumar al total
    
                    // Mostrar información del paquete
                    System.out.println("Cliente: " + datos[1]);
                    System.out.println("Dirección: " + datos[2]);
                    System.out.println("ID Paquete: " + datos[4]);
                    System.out.println("Nombre Paquete: " + datos[5]);
                    System.out.println("Precio: $" + precioPaquete);
                    System.out.println("--------------------------------------------------");
                }
            }
    
            if (clienteEncontrado) {
                System.out.println("Total a pagar: $" + totalPago);
            } else {
                System.err.println("Cliente no encontrado.");
            }
    
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV.");
            //e.printStackTrace();
        }
    }

    public int getContadorSuscripciones() {
        return contadorSuscripciones;
    }

    public void incrementarContadorSuscripciones() {
        this.contadorSuscripciones++;
    }
}
