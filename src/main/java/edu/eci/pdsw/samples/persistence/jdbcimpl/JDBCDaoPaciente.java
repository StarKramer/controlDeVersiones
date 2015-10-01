/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCDaoPaciente implements DaoPaciente {

    Connection con;

    public JDBCDaoPaciente(Connection con) {
        this.con = con;
    }
        

    @Override
    public Paciente load(int idpaciente, String tipoid) throws PersistenceException {
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select pac.nombre, pac.fecha_nacimiento, con.idCONSULTAS, con.fecha_y_hora, con.resumen " +
                "from PACIENTES as pac inner join CONSULTAS as con on con.PACIENTES_id=pac.id and con.PACIENTES_tipo_id=pac.tipo_id "
                + "where pac.id=? and pac.tipo_id=?");
            
            ps.setInt(1, idpaciente);
            ps.setString(2, tipoid);
            ResultSet rs=ps.executeQuery();
                
            Paciente p;
            Consulta c;
            if (rs.next()){
                p = new Paciente(idpaciente,tipoid,rs.getString("nombre"),rs.getDate("fecha_nacimiento"));
                c=new Consulta(rs.getDate("fecha_y_hora"),rs.getString("resumen"));
                c.setId(rs.getInt("idCONSULTAS"));
                p.getConsultas().add(c);
            }            
            else{
                throw new PersistenceException("No row with the given id:"+idpaciente);
            }

            while (rs.next()){
                c=new Consulta(rs.getDate("fecha_y_hora"),rs.getString("resumen"));
                c.setId(rs.getInt("idCONSULTAS"));
                p.getConsultas().add(c);
            }            
            
            return p;
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading a product.",ex);
        }

    }

    @Override
    public void save(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into PACIENTES (id,tipo_id,nombre,fecha_nacimiento) values (?,?,?,?)");
            ps.setInt(1, p.getId());
            ps.setString(2, p.getTipo_id());
            ps.setString(3, p.getNombre());
            ps.setDate(4, p.getFechaNacimiento());            
            ps.execute();
            
            ps = con.prepareStatement("insert into CONSULTAS (fecha_y_hora,resumen,PACIENTES_id,PACIENTES_tipo_id) values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                     
            for (Consulta c:p.getConsultas()){
                ps.setDate(1, c.getFechayHora());
                ps.setString(2, c.getResumen());
                ps.setInt(3, p.getId());
                ps.setString(4, p.getTipo_id());
                ps.execute();
                
                ResultSet keys=ps.getGeneratedKeys();
                while(keys.next()){
                    c.setId(keys.getInt(1));
                }
            }
            
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading a product.",ex);
        }

    }

    @Override
    public void update(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into CONSULTAS (fecha_y_hora,resumen,PACIENTES_id,PACIENTES_tipo_id) values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            for (Consulta c:p.getConsultas()){
                //Insertar sólo las consultas que no han sido aún persistentes
                if (c.getId()==-1){
                    ps.setDate(1, c.getFechayHora());
                    ps.setString(2, c.getResumen());
                    ps.setInt(3, p.getId());
                    ps.setString(4, p.getTipo_id());
                    ps.execute();

                    ResultSet keys=ps.getGeneratedKeys();
                    //Asociar el identificador asignado por la base de datos
                    while(keys.next()){
                        c.setId(keys.getInt(1));
                    }
                }
            }
            
            
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading a product.",ex);
        }        
    }
    
}
