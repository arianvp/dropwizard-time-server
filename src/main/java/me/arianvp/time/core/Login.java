package me.arianvp.time.core;

/**
 * Created by arian on 3/27/16.
 */
public class Login {
    private String name;
    private String password;

    public Login() {

    }

    public Login(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
