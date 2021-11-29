package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(length = 100, nullable = true, name = "url")
    private String url;

    @Column(length = 200, nullable = true, name = "description")
    private String description;

    @ManyToOne(targetEntity = Startup.class)
<<<<<<< Updated upstream
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup productStartup;
=======
    @JoinColumn(name = "id_startup", referencedColumnName = "id", updatable = false, insertable = true)
    @JsonBackReference
    private Startup startup;
>>>>>>> Stashed changes

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductPhoto> productPhotos;
}
