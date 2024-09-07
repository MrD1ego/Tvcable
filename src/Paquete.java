public class Paquete {
    private int id;
    private String nombre;
    private int cantidadCanales;
    private double precio;

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
}

