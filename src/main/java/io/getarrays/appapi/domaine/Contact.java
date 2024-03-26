package io.getarrays.appapi.domaine;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

//entity indique que cette classe est assosié a une table a la base de donnes
@Entity
//Getter and setter genère automatiquement des méthodes getter et setter pour toutes les propriétés de la classe, évitant ainsi d'écrire ces méthodes manuellement.
@Getter
@Setter
// génère un constructeur sans arguments
@NoArgsConstructor
//génère un constructeur prenant en compte tous les champs de la classe.
@AllArgsConstructor
//JsonInclude se charge de ne pas montrer les valeurs qui sont null dans le teste backend
@JsonInclude(NON_DEFAULT)
@Table(name = "contacts")
public class Contact {
    @Id
    @UuidGenerator
    //id is gonna be the primary key
    @Column(name ="id" , unique = true , updatable = false)
    private String id;
    private String name;
    private String email;
    private String title;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;
}
