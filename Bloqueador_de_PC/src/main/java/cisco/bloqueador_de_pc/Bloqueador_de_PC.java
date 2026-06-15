/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package cisco.bloqueador_de_pc;

import entidad.AlumnoEntidad;
import persistencia.AlumnoDAO;
import persistencia.ConexionBD;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 *
 * @author golea
 */
public class Bloqueador_de_PC {

    public static void main(String[] args) throws PersistenciaException {
        System.out.println("Hello World!");
        
     // 1. Crear la conexión
        IConexionBD conexion = new ConexionBD();

        // 2. Crear instancia del DAO
        AlumnoDAO dao = new AlumnoDAO(conexion);

        // 3. Llamar el método
        AlumnoEntidad alumnoLeido = dao.buscarPorId(24001);

        // 4. Imprimir
        System.out.println(alumnoLeido);
    }
}
