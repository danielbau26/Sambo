package model;

import java.util.ArrayList;
import java.util.List;
/**
 * Representa un barco en el juego.
 * Cada barco tiene un nombre, una longitud, una orientación (vertical u horizontal),
 * una lista de coordenadas donde está ubicado y un estado (SUNKEN o AFLOAT).
 */
public class Ship {
    private String name;
    private List<Coordinate> coordinates;
    private ShipState state;
    private int length;
    private boolean isVertical;
/**
     * Crea un nuevo barco.
     * @param name Nombre del barco.
     * @param x Coordenada X inicial 
     * @param y Coordenada Y inicial 
     * @param length Longitud del barco
     * @param isVertical true si el barco est en vertical, false si esta en horizontal
     */
    public Ship(String name, int x, int y, int length, boolean isVertical) {
        this.name = name;
        this.length = length;
        this.isVertical = isVertical;
        this.state = ShipState.AFLOAT;
        this.coordinates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public ShipState getState() {
        return state;
    }

    public void setState(ShipState state) {
        this.state = state;
    }

    public int getLength() {
        return length;
    }

    public boolean isVertical() {
        return isVertical;
    }
}