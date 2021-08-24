package ru.iruchidesu.restaurantvotingsystem.to;

import ru.iruchidesu.restaurantvotingsystem.HasIdAndEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

public class UserTo implements HasIdAndEmail, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 5, max = 32)
    private String password;

    public UserTo(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
