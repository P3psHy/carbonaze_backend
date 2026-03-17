package com.carbonaze.backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "mail est obligatoire")
    @Email(message = "mail doit etre une adresse email valide")
    @Size(max = 256, message = "mail ne doit pas depasser 256 caracteres")
    private String mail;

    @NotBlank(message = "password est obligatoire")
    @Size(min = 8, max = 128, message = "password doit contenir entre 8 et 128 caracteres")
    private String password;

    @NotBlank(message = "societyName est obligatoire")
    @Size(min = 2, max = 256, message = "societyName doit contenir entre 2 et 256 caracteres")
    private String societyName;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }
}
