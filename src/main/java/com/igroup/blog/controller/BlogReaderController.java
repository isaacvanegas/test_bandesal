package com.igroup.blog.controller;

import com.igroup.blog.ejb.BlogFacadeLocal;
import com.igroup.blog.ejb.BlogsReaderFacadeLocal;
import com.igroup.blog.ejb.ReaderFacadeLocal;
import com.igroup.blog.model.Blog;
import com.igroup.blog.model.BlogsReader;
import com.igroup.blog.model.Reader;
import lombok.Data;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Data
@Named
@SessionScoped
public class BlogReaderController implements Serializable {

    @EJB
    private BlogsReaderFacadeLocal blogReaderEJB;
    @EJB
    private BlogFacadeLocal blogEJB;
    @EJB
    private ReaderFacadeLocal readerEJB;

    private BlogsReader blogReader;
    private List<BlogsReader> listBlogReader;
    private List<Blog> listaBlogs;
    private Integer blog;
    private List<Reader> listaLectores;
    private Integer lector;
    private String acction= "Register";

    @PostConstruct
    public void init(){
        listarBlogReader();
        clean();
    }

    public void listarBlogReader(){
        listBlogReader =  blogReaderEJB.findAll();
    }

    public void registrar(){
        String ms = "Registro Exitoso";
        try{

            if(blog!=null && lector!=null){
                blogReader.setBlogs(blogEJB.find(blog));
                blogReader.setReaders(readerEJB.find(lector));


                if (blogReader.getId()!=null){
                    blogReaderEJB.edit(blogReader);
                    ms= "Se edito el registro";
                }else{
                    blogReaderEJB.registrar(blogReader);
                }

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Detalle", ms));
                clean();
                PrimeFaces.current().executeScript("PF('dlg2').hide()");
            }else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", "Agrege los datos del lector y blog"));
            }
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Detalle","Error al registrar"));
        }
    }
    public void eliminar(){

        try{

            if(blogReader!=null){
                blogReaderEJB.remove(blogReader);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Detalle","Registro eliminado"));
                clean();
            }
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Detalle","Error al registrar"));
        }
    }

    public void showDialog() {
        clean();
        PrimeFaces.current().ajax().update("formBlogReader");
        PrimeFaces.current().executeScript("PF('dlg2').show()");
    }

    private void clean()
    {
        blogReader=new BlogsReader();
        listaLectores = readerEJB.findAll();
        listaBlogs = blogEJB.findAll();
    }

    public String getAction(){
        return blogReader!=null ? "Editar": acction;
    }


    public String calculateRedirect(Integer option)  {
        String r = "";
        switch (option)
        {
            case 1:r="/private/reader.xhtml"; break;
            case 2:r="/private/blog.xhtml"; break;
            case 3:r="/private/readerBlog.xhtml"; break;
            default:r="../faces/index.xhtml";
        }
        return r;
    }

}

