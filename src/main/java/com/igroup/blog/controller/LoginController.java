package com.igroup.blog.controller;

import com.igroup.blog.ejb.UsuarioFacadeLocal;
import com.igroup.blog.model.Usuario;
import com.igroup.blog.utils.URI;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Data
@Named("loginController")
@ViewScoped
public class LoginController implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioEJB;

    private Usuario usuario;

    private  final String index ="../faces/index.xhtml";

    @PostConstruct
    public void init() {
        clean();
    }

    public String iniciarSeccion() {
        String redirect = "index.xhtml" ;
        try {
            Usuario usuarioActual = usuarioEJB.inicarSeccion(usuario);
            if (usuarioActual != null) {
                HttpServletRequest oRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                oRequest.getSession().setAttribute("usuarioActual", usuarioActual);
                redirect = "/private/reader.xhtml";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", "Credenciales incorrectas"));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Detalle", "Error al iniciar seccion"));
        }
        return redirect;
    }

    private void clean() {
        usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void verificarSeccion() throws IOException {
        Usuario u;
        FacesContext f = FacesContext.getCurrentInstance();
        try {
            HttpServletRequest oRequest = (HttpServletRequest) f.getExternalContext().getRequest();
            u = (Usuario) oRequest.getSession().getAttribute("usuarioActual");
            if (u == null) {
                f.getExternalContext().redirect(index);
            }

        } catch (Exception e) {
            f.getExternalContext().redirect(index);
        }
    }

    public void cerrarSeccion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }



}
