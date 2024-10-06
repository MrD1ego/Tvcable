package com.mycompany.tvproyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class PaqueteLoader {
    private static final Map<String, List<Paquete>> paquetesPorSector = new HashMap<>();

    public static void cargarPaquetes() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src/main/java/com/mycompany/tvproyecto/paquetes.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length != 5) {
                    System.err.println("Formato incorrecto en la linea: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    int cantidadCanales = Integer.parseInt(partes[2].trim());
                    double precio = Double.parseDouble(partes[3].trim());
                    String sector = partes[4].trim();  // Asumimos que el sector está en la 5ta columna

                    Paquete paquete = new Paquete(id, nombre, cantidadCanales, precio);

                    // Agregar el paquete al sector correspondiente
                    List<Paquete> paquetesSector = paquetesPorSector.getOrDefault(sector, new ArrayList<>());
                    paquetesSector.add(paquete);
                    paquetesPorSector.put(sector, paquetesSector);

                } catch (NumberFormatException e) {
                    System.err.println("Error en el formato de numeros en la línea: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de paquetes: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el BufferedReader: " + e.getMessage());
                }
            }
        }
        System.out.println("Paquetes cargados desde paquetes.txt");
    }

    public static List<Paquete> obtenerPaquetesPorSector(String sector) {
        return paquetesPorSector.getOrDefault(sector, new ArrayList<>());
    }
}
