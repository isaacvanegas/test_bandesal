/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igroup.blog.ejb;

import com.igroup.blog.model.Reader;
import com.igroup.blog.model.Usuario;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jvanegas
 */
@Stateless
public class ReaderFacade extends AbstractFacade<Reader> implements ReaderFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReaderFacade() {
        super(Reader.class);
    }

    @Override
    public Reader registrar(Reader prReader) throws Exception {
        String consulta;
        Reader reader = null;

        consulta= ("from Reader r where r.name = :name ");

        Query query = em.createQuery(consulta);
        query.setParameter("name",prReader.getName());
        try {
           reader = (Reader) query.getSingleResult();
        }catch (Exception e){

        }


        if(reader!=null){
            throw  new Exception("El lector ya existe") ;
        }else{
            em.persist(prReader);
        }

        return prReader;
    }
}
