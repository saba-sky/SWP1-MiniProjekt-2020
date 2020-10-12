package de.unibremen.swp.controller;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;
import de.unibremen.swp.services.session.SessionService;
import lombok.Data;
import lombok.NonNull;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Optional;

@Data
@Named
@RequestScoped
public class TemplateBean {

    @NotBlank
    @NonNull
    private String fullName = "";

    @Inject
    private ExternalContext external;

    @Inject
    private FacesContext facesContext;

    @Inject
    private SessionService sessionService;

    @Inject
    private UserDAO userDAO;


    public void logout() throws IOException {
        sessionService.logout((String) external.getSessionMap().get("principle"));
        facesContext.getExternalContext().redirect("login.xhtml");
    }

    public String getFullName() {
        Optional<User> user = userDAO.findByEmail((String) external.getSessionMap().get("principle"));
        user.ifPresent((User value) -> {
            fullName = value.getFirstName() + " " + value.getLastName();
        });
        return fullName;
    }

}
