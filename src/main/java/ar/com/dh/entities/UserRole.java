package ar.com.dh.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;


@NoArgsConstructor
public enum UserRole {

    ROLE_USER,
    ROLE_ADMIN

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_role")
//    private Long idRole;
//
//    @OneToMany(mappedBy = "userRole")
//    private List<User> userList;


}
