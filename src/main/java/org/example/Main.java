package org.example;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {
        PeliculaDAO dao = new PeliculaDAO();

        String rutaArchivo = "peliculas.xml";

       /* dao.crearTabla();
        dao.insertarPeli("Jurassic park", 127, 1998, "DVD", "Bueno");
        dao.insertarPeli("Peter pan", 90, 1999, "VHS", "Usado");
        dao.insertarPeli("Avatar", 162, 2010, "Blu-Ray", "Nuevo");
        dao.insertarPeli("300", 117, 2008, "DVD", "Usado");
        dao.insertarPeli("Padre solo hay uno", 98, 2002, "Digital", "Bueno");*/
        //dao.leerInsertarXML(rutaArchivo);
        dao.extraerTabla();


    }


}