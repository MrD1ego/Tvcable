import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaqueteLoader {
    private static Map<Integer, Paquete> paquetes = new HashMap<>();

    public static void cargarPaquetes() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src/paquetes.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length != 4) {
                    System.err.println("Formato incorrecto en la línea: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    int cantidadCanales = Integer.parseInt(partes[2].trim());
                    double precio = Double.parseDouble(partes[3].trim());

                    Paquete paquete = new Paquete(id, nombre, cantidadCanales, precio);
                    paquetes.put(id, paquete);
                } catch (NumberFormatException e) {
                    System.err.println("Error en el formato de números en la línea: " + line);
                    e.printStackTrace();
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
    }

    public static Paquete obtenerPaquetePorId(int id) {
        return paquetes.get(id);
    }

    public static Map<Integer, Paquete> getPaquetes() {
        return paquetes;
    }
}
