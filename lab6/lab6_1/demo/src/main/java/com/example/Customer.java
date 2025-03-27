package com.example;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long custumerid;

    @Column(nullable = false)
    private String Name;

    @Column(nullable = false)
    private String email;

    public Customer(long custumerid, String name, String email) {
        this.custumerid = custumerid;
        Name = name;
        this.email = email;
    }
    public Customer() {

    }
    public long getCustumerid() {
        return custumerid;
    }

    public void setCustumerid(long custumerid) {
        this.custumerid = custumerid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
}
