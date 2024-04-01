package com.igroup.blog.controller;

import com.igroup.blog.ejb.BlogFacadeLocal;
import com.igroup.blog.ejb.ReaderFacadeLocal;
import com.igroup.blog.model.Blog;
import com.igroup.blog.model.Reader;
import lombok.Data;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Data
@Named("blogController")
@ViewScoped
public class BlogController implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private BlogFacadeLocal blogEJB;

    private Blog blog;
    private List<Blog> listBlog;
    private String acction= "Register";

    @PostConstruct
    public void init(){
        listarBlog();
        clean();
    }

    public void listarBlog(){
        listBlog =  blogEJB.findAll();
    }

    public void registrar(){
        String ms = "Registro Exitoso";
        try{

            if(blog!=null){

                if (blog.getId()!=null){
                    blogEJB.edit(blog);
                    ms= "Se edito el registro";
                }else{
                    blogEJB.registrar(blog);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Detalle", ms));
                PrimeFaces.current().executeScript("PF('dlg2').hide()");
                clean();
            }else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", "Agrege los datos del blog"));
            }

        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Detalle",e.getMessage()));
        }
    }
    public void eliminar(){

        try{

            if(blog!=null){
                blogEJB.remove(blog);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Detalle","Registro eliminado"));

            }
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Detalle","Error al registrar"));
        }
    }

    public void edit(){
        PrimeFaces.current().executeScript("PF('dlg2').show()");
    }
    private void clean()
    {
        listBlog =  blogEJB.findAll();
        blog=new Blog();
    }

    public String getAction(){
        return blog!=null ? "Editar": acction;
    }

    public void showDialog() {
        clean();
        PrimeFaces.current().ajax().update("formBlog");
        PrimeFaces.current().executeScript("PF('dlg2').show()");
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
