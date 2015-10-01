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
package edu.eci.pdsw.samples.services;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class ServicesFacade {
    
    
    private static ServicesFacade instance=null;
    
    private final Properties properties=new Properties();
    
    private ServicesFacade(String propFileName) throws IOException{        
	InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream(propFileName);
        properties.load(input);
    }
    
    public static ServicesFacade getInstance(String propertiesFileName) throws RuntimeException{
        if (instance==null){
            try {
                instance=new ServicesFacade(propertiesFileName);
            } catch (IOException ex) {
                throw new RuntimeException("Error on application configuration:",ex);
            }
        }        
        return instance;
    }

    /**
     * Konsultar un paSiente dado su identificador.
     * @param idPaciente identificador del paciente
     * @param tipoid tipo de identificación del paciente
     * @return el paciente con el identificador dado
     * @throws ServiceFacadeException  si el paciente no existe
     */
    public Paciente consultarPaciente(int idPaciente,String tipoid) throws ServiceFacadeException{
        DaoFactory daof=DaoFactory.getInstance(properties);
        try {
            System.out.println("Entró acá!");
            daof.beginSession();
            Paciente p=daof.getDaoPaciente().load(idPaciente, tipoid);
            daof.endSession();
            return p;
        } catch (PersistenceException ex) {
            throw new ServiceFacadeException("Error al consultar paciente.",ex);
        }
    }
    
    
    /**
     * Registra un nuevo paciente en el sistema
     * @param p El nuevo paciente
     * @throws ServicesFacadeException si se presenta algún error lógico
     * o de persistencia (por ejemplo, si el paciente ya existe).
     */
    public void registrarNuevoPaciente(Paciente p) throws ServiceFacadeException{
       
    }
    
    /**
     * Agrega una consulta a un paciente ya registrado
     * @param idPaciente el identificador del paciente
     * @param tipoid el tipo de identificación
     * @param c la consulta a ser agregada
     */
    public void agregarConsultaAPaciente(int idPaciente,String tipoid,Consulta c) throws ServiceFacadeException{
        DaoFactory daof=DaoFactory.getInstance(properties);
        try{
            daof.beginSession();
            Paciente p=daof.getDaoPaciente().load(idPaciente, tipoid);
            Set<Consulta> cons = p.getConsultas();
            cons.add(c);
            p.setConsultas(cons);
            daof.getDaoPaciente().update(p);
            daof.endSession();
        }catch (PersistenceException ex) {
            throw new ServiceFacadeException("Error al consultar paciente.",ex);
        }
    }
    
}
