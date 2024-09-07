public class Cliente {
    private String nombreCliente;
    private String direccion;
    private String sector;
    private int id;

    public Cliente(String nombreCliente, String direccion, String sector, int id){
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.sector = sector;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setId(int id){
        this.id = id;
    }

    public void mostrarCliente(){
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombreCliente);
        System.out.println("Dirección " + direccion);
        System.out.println("Sector: " + sector);
    }
}
