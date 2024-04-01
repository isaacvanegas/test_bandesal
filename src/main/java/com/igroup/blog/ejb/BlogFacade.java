/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.igroup.blog.ejb;

import com.igroup.blog.model.Blog;
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
public class BlogFacade extends AbstractFacade<Blog> implements BlogFacadeLocal {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BlogFacade() {
        super(Blog.class);
    }

    @Override
    public Blog registrar(Blog prBlog) throws Exception {
        String consulta;
        Blog blog = null;

        consulta= ("from Blog b where b.title = :title ");

        Query query = em.createQuery(consulta);
        query.setParameter("title",prBlog.getTitle());

        try {
            blog = (Blog) query.getSingleResult();
        }catch (Exception e){

        }

        if(blog!=null){
            throw  new Exception("El título del blog ya existe") ;
        }else{
            em.persist(prBlog);
        }

        return prBlog;
    }
    
}
