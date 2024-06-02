package com.estelair.iavatarstudioback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {

    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Autoincrement
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    private String imagenNombre;

    private LocalDate fechaCreacion;

    private Boolean favorito;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_creador"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity creador;
}
