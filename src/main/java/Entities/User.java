/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;


public class User {
    //attributs
    private int id;
    private String Email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public User(int id, String email) {
        this.id = id;
        Email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", Email='" + Email + '\'' +
                '}';
    }
}
