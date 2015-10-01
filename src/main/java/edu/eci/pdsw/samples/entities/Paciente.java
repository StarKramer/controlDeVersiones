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
package edu.eci.pdsw.samples.entities;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author hcadavid
 */
public class Paciente {
    
    private int id;
    private String tipo_id;
    private String nombre;
    private Date fechaNacimiento;
    Set<Consulta> consultas;
    

    public Paciente(int id, String tipo_id, String nombre, Date fechaNacimiento) {
        this.id = id;
        this.tipo_id = tipo_id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        consultas=new LinkedHashSet<>();
    }

    public Paciente() {
        consultas=new LinkedHashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo_id() {
        return tipo_id;
    }

    public void setTipo_id(String tipo_id) {
        this.tipo_id = tipo_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(Set<Consulta> consultas) {
        this.consultas = consultas;
    }

    @Override
    public String toString() {
        String rep="Paciente:["+id+","+tipo_id+","+nombre+","+fechaNacimiento+"]\n";
        for (Consulta c:consultas){
            rep+="\t["+c+"]\n";
        }
        return rep;
    }
    
    
    
}
