package org.example;

import org.w3c.dom.NodeList;

import java.io.File;
import java.sql.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class PeliculaDAO {

    public void crearBD() {
        String crearDB = "CREATE DATABASE videoclub";

        try (Connection connection = ConexionBD.conectarServidor();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(crearDB);
            System.out.println("Base de datos creada con éxito");

        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos" + e.getMessage());
        }
    }

    public void crearTabla() {
        String crearTabla = "CREATE TABLE peliculas( id serial primary key, nombre varchar(30),duracion int, anio int, formato varchar(30),estado varchar(50))";

        try (Connection connection = ConexionBD.conectarServidor();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(crearTabla);
            System.out.println("Tabla creada con éxito");


        } catch (SQLException e) {
            System.out.println("Error al crear la tabla" + e.getMessage());
        }


    }

    public void insertarPeli(String nombre, int duracion, int anio, String formato, String estado) {
        String sql = "INSERT INTO peliculas (nombre, duracion, anio, formato, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectarServidor();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setInt(2, duracion);
            stmt.setInt(3, anio);
            stmt.setString(4, formato);
            stmt.setString(5, estado);
            stmt.executeUpdate();

            System.out.println("Pelicula insertada correctamente");

        } catch (SQLException e) {
            System.err.println("Error al insertar " + nombre + ": " + e.getMessage());
        }
    }

    public void borrarTabla() throws RuntimeException {
        String sql = "DROP table peliculas";
        try (Connection connection = ConexionBD.conectarServidor();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);
            System.out.println("Tabla borrada con exito");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static void eliminarBD() {
        String sql = "DROP DATABASE IF EXISTS videoclub";

        try (Connection conn = ConexionBD.conectarServidor();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("La base de datos 'videoclub' ha sido eliminada con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar la base de datos: " + e.getMessage());
        }
    }

    public void leerInsertarXML(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //Fabrica
            DocumentBuilder builder = factory.newDocumentBuilder(); //LEctor

            Document doc = builder.parse(archivo);


            NodeList listaNodos = doc.getElementsByTagName("pelicula");

            for (int i = 0; i < listaNodos.getLength(); i++) {

                Node nodo = listaNodos.item(i);

                Element elemento = (Element) nodo;

                String nombre = ((Element) nodo).getElementsByTagName("nombre").item(0).getTextContent();
                String duracionTexto = ((Element) nodo).getElementsByTagName("duracion").item(0).getTextContent();
                int duracion = Integer.parseInt(duracionTexto);
                String anioTexto = ((Element) nodo).getElementsByTagName("anio").item(0).getTextContent();
                int anio = Integer.parseInt(anioTexto);
                String formato = ((Element) nodo).getElementsByTagName("formato").item(0).getTextContent();
                String estado = ((Element) nodo).getElementsByTagName("estado").item(0).getTextContent();

                insertarPeli(nombre, duracion, anio, formato, estado);


            }
            System.out.println("Datos XML insertados correctamente");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void extraerTabla() {
        String sql = "SELECT id, nombre, duracion, anio, formato, estado FROM peliculas";
        try (Connection connection = ConexionBD.conectarServidor();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("peliculas");
            doc.appendChild(root);

            while (rs.next()) {

                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int duracion = rs.getInt("duracion");
                int anio = rs.getInt("anio");
                String formato = rs.getString("formato");
                String estado = rs.getString("estado");

                Element pelicula = doc.createElement("pelicula");

                Element elementoId = doc.createElement("id");
                elementoId.setTextContent(String.valueOf(id));
                pelicula.appendChild(elementoId);

                Element elementoNombre = doc.createElement("nombre");
                elementoNombre.setTextContent(nombre);
                pelicula.appendChild(elementoNombre);

                Element elementoDuracion = doc.createElement("duracion");
                elementoDuracion.setTextContent(String.valueOf(duracion));
                pelicula.appendChild(elementoDuracion);

                Element elementoAnio = doc.createElement("anio");
                elementoAnio.setTextContent(String.valueOf(anio));
                pelicula.appendChild(elementoAnio);

                Element elementFormato = doc.createElement("formato");
                elementFormato.setTextContent(formato);
                pelicula.appendChild(elementFormato);

                Element elementEstado = doc.createElement("estado");
                elementEstado.setTextContent(estado);
                pelicula.appendChild(elementEstado);

                root.appendChild(pelicula);

                TransformerFactory factory1 = TransformerFactory.newInstance();
                Transformer transformer = factory1.newTransformer();


                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("peliculas_exportadas.xml"));

                transformer.transform(source, result);


            }
            System.out.println("Tabla exportada con éxito");


        } catch (Exception e) {
            System.out.println("Error al exportar la tabla" + e.getMessage());
        }

    }
}
