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
package edu.eci.pdsw.samples.tests;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class NewEmptyJUnitTest {
    
    public NewEmptyJUnitTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void registroPacienteTest() throws PersistenceException{
        Properties properties=new Properties();
        InputStream entrada =null;
        try{
            entrada = new FileInputStream("src\\main\\resources\\applicationconfig.properties");
            properties.load(entrada);
            Set<Consulta> consultas = new LinkedHashSet<>();;
            Paciente p = new Paciente(2365453, "cc", "lola", Date.valueOf("1993-06-08"));
            Consulta c = new Consulta(Date.valueOf("2001-06-06"), "blablabala");
            consultas.add(c);
            p.setConsultas(consultas);
            DaoFactory daof=DaoFactory.getInstance(properties);
            daof.beginSession();
            DaoPaciente dp = daof.getDaoPaciente();
            dp.save(p);
            daof.commitTransaction();
            daof.endSession();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Esta prueba esta bien.
     * @throws PersistenceException 
     */
    @Test
    public void registroConsultaTest() throws PersistenceException{
        Properties properties=new Properties();
        InputStream entrada =null;
        try{
            entrada = new FileInputStream("src\\main\\resources\\applicationconfig.properties");
            properties.load(entrada);
            DaoFactory daof=DaoFactory.getInstance(properties);
            daof.beginSession();
            DaoPaciente dp = daof.getDaoPaciente();
            Paciente p = dp.load(2365453, "cc");
            System.out.println(p.getNombre());
            daof.endSession();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
}
