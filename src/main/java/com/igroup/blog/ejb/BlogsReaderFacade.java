/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igroup.blog.ejb;

import com.igroup.blog.model.BlogsReader;
import com.igroup.blog.model.Reader;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jvanegas
 */
@Stateless
public class BlogsReaderFacade extends AbstractFacade<BlogsReader> implements BlogsReaderFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BlogsReaderFacade() {
        super(BlogsReader.class);
    }

    @Override
    public BlogsReader registrar(BlogsReader prBlogReader) throws Exception {
        String consulta;
        BlogsReader blogReader = null;

        consulta= ("from BlogsReader br where br.blogs = :blogs and br.readers = :readers ");

        Query query = em.createQuery(consulta);
        query.setParameter("blogs",prBlogReader.getBlogs());
        query.setParameter("readers",prBlogReader.getReaders());
        try {
            blogReader = (BlogsReader) query.getSingleResult();
        }catch (Exception e){

        }


        if(blogReader!=null){
            throw  new Exception("El lector ya existe") ;
        }else{
            em.persist(prBlogReader);
        }

        return prBlogReader;
    }
    
}
