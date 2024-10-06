package com.mycompany.tvproyecto;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Subscripcion {
    private final Map<Cliente, List<Paquete>> subscripciones = new HashMap<>();

    public void agregarPaquete(Cliente cliente, Paquete paquete) {
        List<Paquete> paquetes = subscripciones.get(cliente);
        if (paquetes == null) {
            paquetes = new ArrayList<>();
            subscripciones.put(cliente, paquetes);
        }
        paquetes.add(paquete);
    }

    public void eliminarPaquete(Cliente cliente, Paquete paquete) {
        List<Paquete> paquetes = subscripciones.get(cliente);
        if (paquetes != null) {
            paquetes.remove(paquete);
            if (paquetes.isEmpty()) {
                subscripciones.remove(cliente);
            }
        } else {
            System.err.println("El cliente no tiene paquetes.");
        }
    }

    public void mostrarPaquetes(Cliente cliente) {
        List<Paquete> paquetes = subscripciones.get(cliente);
        if (paquetes != null) {
            System.out.println("Paquetes del cliente " + cliente.getNombreCliente() + ":");
            for (Paquete paquete : paquetes) {
                System.out.println("Nombre: " + paquete.getNombre());
                System.out.println("Cantidad de Canales: " + paquete.getCantidadCanales());
                System.out.println("Precio: $" + paquete.getPrecio());
                System.out.println("------------------------");
            }
        } else {
            System.out.println("El cliente no tiene paquetes.");
        }
    }

    public Paquete obtenerPaquetePorNombre(String nombrePaquete) {
        for (List<Paquete> listaPaquetes : subscripciones.values()) {
            for (Paquete paquete : listaPaquetes) {
                if (paquete.getNombre().equalsIgnoreCase(nombrePaquete)) {
                    return paquete;
                }
            }
        }
        return null;
    }

    public double obtenerTotalPago(Cliente cliente) {
        List<Paquete> paquetes = subscripciones.get(cliente);
        if (paquetes != null) {
            double totalPago = 0;
            for (Paquete paquete : paquetes) {
                totalPago += paquete.getPrecio();
            }
            System.out.println("Total a pagar del cliente " + cliente.getNombreCliente() + ": $" + totalPago);
            return totalPago;
        } else {
            System.out.println("El cliente no tiene paquetes.");
            return 0;
        }
    }

    public int obtenerTotalCanales(Cliente cliente) {
        List<Paquete> paquetes = subscripciones.get(cliente);
        if (paquetes != null) {
            int totalCanales = 0;
            for (Paquete paquete : paquetes) {
                totalCanales += paquete.getCantidadCanales();
            }
            System.out.println("Total de canales del cliente " + cliente.getNombreCliente() + ": " + totalCanales);
            return totalCanales;
        } else {
            System.out.println("El cliente no tiene paquetes.");
            return 0;
        }
    }
    public Paquete obtenerPaquetePorId(int idPaquete, Cliente cliente) {
        List<Paquete> paquetes = subscripciones.get(cliente);
        if (paquetes != null) {
            return paquetes.stream()
                .filter(paquete -> paquete.getId() == idPaquete)
                .findFirst()
                .orElse(null);
        }
        return null;
    }
}
