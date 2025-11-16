package ru.practicum.shareit.user.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String userEmail;

    public User(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }
}