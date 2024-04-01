package com.igroup.blog.controller;

import com.igroup.blog.ejb.ReaderFacadeLocal;
import com.igroup.blog.ejb.UsuarioFacadeLocal;
import com.igroup.blog.model.Reader;
import com.igroup.blog.model.Usuario;
import com.igroup.blog.seguridad.Seguridad;
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

@Named
@Data
@ViewScoped
public class ReaderController implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private ReaderFacadeLocal readerEJB;

    private Reader reader;
    private List<Reader> listRreader;
    private String acction = "Guardar";


    @PostConstruct
    public void init() {
        clean();
    }


    public void registrar() {
        String ms = "Registro Exitoso";
        try {

            if (reader != null ) {

                if (reader.getId() != null) {
                    readerEJB.edit(reader);
                    ms = "Se edito el registro";
                }else {
                    readerEJB.registrar(reader);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Detalle", ms));
                PrimeFaces.current().executeScript("PF('dlg2').hide()");
                clean();
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", "Agrege el nombre del lector"));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", e.getMessage()));
        }
    }

    public void eliminar() {
        try {
            if (reader != null) {
                readerEJB.remove(reader);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Detalle", "Registro eliminado"));
                clean();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", "Error al registrar"));
        }
    }

    public void clean() {
        reader = new Reader();
        listRreader = readerEJB.findAll();
    }

    public void showDialog() {
        clean();
        PrimeFaces.current().ajax().update("formReader");
        PrimeFaces.current().executeScript("PF('dlg2').show()");
    }


    public void edit(){
        PrimeFaces.current().executeScript("PF('dlg2').show()");
    }

    public String getAction() {
        try{
            return reader.getId() != null ? "Editar" : acction;
        }catch (Exception e){
            return acction;
        }
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
