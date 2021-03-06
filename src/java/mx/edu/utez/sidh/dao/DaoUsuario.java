package mx.edu.utez.sidh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mx.edu.utez.sidh.bean.Disponibilidad;
import mx.edu.utez.sidh.bean.Usuario;
import static mx.edu.utez.sidh.utils.Conexion.getConexion;

/**
 * Clase Dao para el Usuario, CRUD.
 *
 * @author Nancy
 * @author x
 */
public class DaoUsuario {


    /**
     * Método para buscar un usuario en especifico de la Base de Datos
     *
     * @param u
     * @return usuario
     * @author Nancy
     * @author x
     */
    public static Usuario getUsuario(Usuario u) {
        Usuario obj = new Usuario();
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("Select * from usuario where email=? AND contrasena=?");
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getContrasena());
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                obj.setId(res.getInt(1));
                obj.setNombre(res.getString(2));
                obj.setApellidoPaterno(res.getString(3));
                obj.setApellidoMaterno(res.getString(4));
                obj.setEmail(res.getString(5));
                obj.setContrasena(res.getString(6));
                obj.setEstado(res.getBoolean(7));
                obj.setTipoUsuario(res.getInt(8));
            }

            res.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("UsuarioDao.obtenerUsuario \n" + e.getMessage());
        }
        return obj;
    }

    /**
     * Método para obtener un usurio segun su id
     * @param u
     * @return 
     */
    public Usuario getUsuarioById(Usuario u) {
        Usuario obj = new Usuario();
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("Select * from usuario where id=? ");
            ps.setInt(1, u.getId());
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                obj.setId(res.getInt(1));
                obj.setNombre(res.getString(2));
                obj.setApellidoPaterno(res.getString(3));
                obj.setApellidoMaterno(res.getString(4));
                obj.setEmail(res.getString(5));
                obj.setContrasena(res.getString(6));
                obj.setEstado(res.getBoolean(7));
                obj.setTipoUsuario(res.getInt(8));
                obj.setTelefono(res.getString(9));
            }

            res.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("UsuarioDao.obtenerUsuario \n" + e.getMessage());
        }
        return obj;
    }

    public Usuario getUsuarioConDisponibilidadActualById(Usuario u) {
        Usuario usuario = new Usuario();
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement(""
                    + "SELECT u.id,u.nombre, u.apellido1, u.apellido2, u.email, u.contrasena,"
                    + "d.id, d.dia, d.h7, d.h8, d.h9, d.h10, d.h11, d.h12, d.h13, d.h14, d.h15, d.h16, d.h17, d.h18, d.h19, d.h20, d.notas, d.estado "
                    + "FROM usuario as u "
                    + "JOIN usuario_tiene_disponibilidad as ud on ud.id_usuario = u.id "
                    + "JOIN disponibilidad as d on ud.id_disponibilidad = d.id "
                    + "JOIN disponibilidad_tiene_periodos as dp on dp.id_disponibilidad=d.id "
                    + "JOIN periodos as p on p.id = dp.id_periodo "
                    + "where u.id=? AND p.id IN(Select MAX(id) from periodos where estado=1)");
            ps.setInt(1, u.getId());
            ResultSet rs = ps.executeQuery();

            int iterador = 0;
            Disponibilidad[] disponibilidades = new Disponibilidad[6];

            while (rs.next()) {

                //si es un nuevo usuario
                if (iterador == 0) {
                    usuario.setId(rs.getInt(1));
                    usuario.setNombre(rs.getString(2));
                    usuario.setApellidoPaterno(rs.getString(3));
                    usuario.setApellidoMaterno(rs.getString(4));
                    usuario.setEmail(rs.getString(5));
                    usuario.setContrasena(rs.getString(6));
                }

                Disponibilidad disp = new Disponibilidad();

                disp.setId(rs.getInt(7));
                disp.setDia(rs.getString("dia"));
                disp.setH7(rs.getString("h7").charAt(0));
                disp.setH8(rs.getString("h8").charAt(0));
                disp.setH9(rs.getString("h9").charAt(0));
                disp.setH10(rs.getString("h10").charAt(0));
                disp.setH11(rs.getString("h11").charAt(0));
                disp.setH12(rs.getString("h12").charAt(0));
                disp.setH13(rs.getString("h13").charAt(0));
                disp.setH14(rs.getString("h14").charAt(0));
                disp.setH15(rs.getString("h15").charAt(0));
                disp.setH16(rs.getString("h16").charAt(0));
                disp.setH17(rs.getString("h17").charAt(0));
                disp.setH18(rs.getString("h18").charAt(0));
                disp.setH19(rs.getString("h19").charAt(0));
                disp.setH20(rs.getString("h20").charAt(0));
                disp.setNotas(rs.getString("notas"));

                disponibilidades[iterador] = (disp);
                iterador++;
                if (iterador == 6) {
                    usuario.setDisponibilidad(disponibilidades);
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("" + e.getMessage());
        }
        return usuario;
    }

    public static ArrayList<Usuario> getUsuarios() {
        ArrayList<Usuario> lista = new ArrayList();
        DaoDisponibilidad dao = new DaoDisponibilidad();
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement(" SELECT  id,nombre, apellido1, apellido2, email, contrasena, tipoUsuario,telefono "
                    + "FROM usuario where estado=1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setNombre(rs.getString(2));
                usuario.setApellidoPaterno(rs.getString(3));
                usuario.setApellidoMaterno(rs.getString(4));
                usuario.setEmail(rs.getString(5));
                usuario.setContrasena(rs.getString(6));
                usuario.setEstado(dao.UsuarioTieneDisponibilidad(rs.getInt(1)));
                usuario.setTipoUsuario(rs.getInt(7));
                usuario.setTelefono(rs.getString(8));
                lista.add(usuario);
            }
            rs.close();
            ps.close();;
            con.close();
        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.getUsuarios:" + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * @return @author Derick
     */
    public ArrayList<Usuario> getUsuariosDisponibilidades() {
        ArrayList<Usuario> lista = new ArrayList();
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement(" "
                    + "SELECT u.id,u.nombre, u.apellido1, u.apellido2, u.email, u.contrasena,"
                    + "d.id, d.dia, d.h7, d.h8, d.h9, d.h10, d.h11, d.h12, d.h13, d.h14, d.h15, d.h16, d.h17, d.h18, d.h19, d.h20, d.notas, d.estado "
                    + "FROM usuario as u "
                    + "JOIN usuario_tiene_disponibilidad as ud on ud.id_usuario = u.id "
                    + "JOIN disponibilidad as d on ud.id_disponibilidad = d.id "
                    + "JOIN disponibilidad_tiene_periodos as dp on dp.id_disponibilidad=d.id "
                    + "JOIN periodos as p on p.id = dp.id_periodo "
                    + "where u.estado=1 AND p.id IN(Select MAX(id) from periodos)");
            ResultSet rs = ps.executeQuery();

            int iterador = 0;
            Disponibilidad[] disponibilidades = new Disponibilidad[6];
            Usuario usuario = new Usuario();
            while (rs.next()) {

                //si es un nuevo usuario
                if (iterador == 0) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt(1));
                    usuario.setNombre(rs.getString(2));
                    usuario.setApellidoPaterno(rs.getString(3));
                    usuario.setApellidoMaterno(rs.getString(4));
                    usuario.setEmail(rs.getString(5));
                    usuario.setContrasena(rs.getString(6));
                    disponibilidades = new Disponibilidad[6];
                }

                Disponibilidad disp = new Disponibilidad();

                disp.setId(rs.getInt(7));
                disp.setDia(rs.getString("dia"));
                disp.setH7(rs.getString("h7").charAt(0));
                disp.setH8(rs.getString("h8").charAt(0));
                disp.setH9(rs.getString("h9").charAt(0));
                disp.setH10(rs.getString("h10").charAt(0));
                disp.setH11(rs.getString("h11").charAt(0));
                disp.setH12(rs.getString("h12").charAt(0));
                disp.setH13(rs.getString("h13").charAt(0));
                disp.setH14(rs.getString("h14").charAt(0));
                disp.setH15(rs.getString("h15").charAt(0));
                disp.setH16(rs.getString("h16").charAt(0));
                disp.setH17(rs.getString("h17").charAt(0));
                disp.setH18(rs.getString("h18").charAt(0));
                disp.setH19(rs.getString("h19").charAt(0));
                disp.setH20(rs.getString("h20").charAt(0));
                disp.setNotas(rs.getString("notas"));

                disponibilidades[iterador] = (disp);
                iterador++;
                if (iterador == 6) {
                    iterador = 0;
                    usuario.setDisponibilidad(disponibilidades);
                    lista.add(usuario);
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.getUsuarios:" + e.getMessage());
        }
        return lista;
    }

    /**
     *Método que obtiene la lista de usuarios que NO tienen disponibilidades
     * @return ArrayList
     * @author Derick
     */
    public ArrayList<Usuario> getUsuariosSinDisponibilidades() {
        DaoPeriodo dao = new DaoPeriodo();
        ArrayList<Usuario> lista = new ArrayList();
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE usuario.id NOT IN ( select usuario_tiene_disponibilidad.id_usuario FROM `disponibilidad_tiene_periodos` join usuario_tiene_disponibilidad on usuario_tiene_disponibilidad.id_disponibilidad=disponibilidad_tiene_periodos.id_disponibilidad WHERE disponibilidad_tiene_periodos.id_periodo = ?) and usuario.estado=1");
            ps.setInt(1, dao.getIdNuevoPeriodo());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setNombre(rs.getString(2));
                usuario.setApellidoPaterno(rs.getString(3));
                usuario.setApellidoMaterno(rs.getString(4));
                usuario.setEmail(rs.getString(5));
                usuario.setContrasena(rs.getString(6));
                usuario.setEstado(rs.getBoolean(7));
                usuario.setTipoUsuario(rs.getInt(8));

                lista.add(usuario);

            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.getUsuarios:" + e.getMessage());
        }
        return lista;
    }

    /**
     * Método create, recibe un objeto Usuario y lo registra en la Base de Datos
     *
     * @param usuario
     * @return boolean
     * @author Nancy
     * @author x
     * @throws java.sql.SQLException
     */
    public boolean registrarUsuario(Usuario usuario) throws SQLException {
        boolean registrado = false;

        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("INSERT INTO usuario(nombre,apellido1,apellido2,email,contrasena,estado,tipoUsuario,telefono)"
                    + "VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidoPaterno());
            ps.setString(3, usuario.getApellidoMaterno());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getContrasena());
            ps.setBoolean(6, usuario.isEstado());
            ps.setInt(7, usuario.getTipoUsuario());
            ps.setString(8, usuario.getTelefono());

            registrado = (ps.executeUpdate() > 0);

            ps.close();
            con.close();

        } catch (SQLException e) {
            return registrado;
        }

        return registrado;
    }

    /**
     * Método update, recibe un objeto Usuario y lo actualiza en la Base de
     * Datos
     *
     * @param usuario
     * @return boolean
     * @author Nancy
     * @author x
     * @throws java.sql.SQLException
     */
    public boolean editarUsuario(Usuario usuario) throws SQLException {
        boolean editar = false;

        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("UPDATE usuario"
                    + "  set nombre=?, apellido1=?, apellido2=?, email=?, contrasena=?, telefono=? where id=?");
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidoPaterno());
            ps.setString(3, usuario.getApellidoMaterno());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getTelefono());
            ps.setInt(7, usuario.getId());

            editar = (ps.executeUpdate() > 0);
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.crearUsuario :" + e.getMessage());
            throw e;
        }
        return editar;
    }

    /**
     * Método delete, recibe un objeto Usuario y lo borra de la Base de Datos
     *
     * @param id
     * @return boolean
     * @author Nancy
     * @author x
     */
    public static boolean deleteUsuario(int id) {
        boolean eliminado = false;

        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("UPDATE usuario SET estado=0 WHERE id=?");
            ps.setInt(1, id);
            eliminado = (ps.executeUpdate() > 0);

            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.deleteUsuario :" + e.getMessage());
        }
        return eliminado;
    }
    
    public static boolean asignarAdmin(int id) {
        boolean cambiado = false;
        try {
            Connection con = getConexion();
            PreparedStatement ps;
            if (tipoUsuario(id) == 1) {
                ps = con.prepareStatement("UPDATE usuario SET tipoUsuario=0 WHERE id=?");
            } else {
                ps = con.prepareStatement("UPDATE usuario SET tipoUsuario=1 WHERE id=?");
            }
            ps.setInt(1, id);
            cambiado = ps.executeUpdate() > 0;

            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.asignarAdmin :" + e.getMessage());
        }
        return cambiado;
    }
    
    private static int tipoUsuario(int id) {
        int tipo = 0;
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement("select tipoUsuario from usuario WHERE id=?");
            ps.setInt(1, id);            
            ResultSet rs = ps.executeQuery();            
            rs.next();
            tipo = rs.getInt(1);
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error ocurrido en DaoUsuario.tipoUsuario :" + e.getMessage());
        }
        return tipo;
    }
    

}
