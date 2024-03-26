package io.getarrays.appapi.repo;


import io.getarrays.appapi.domaine.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//repository est utilisé pour indiquer a spring que cette interface est un bean de persistance
//permettant a spring de gerer les exceptions de persistance approprié
@Repository
//Jparepository fournit par spring data jpa fournit les methodes crud (create , read , update,delete)
// dans ce cas contact est l'entité géré et string  est le type de l'id de l'entité
public interface  ContactRepo extends JpaRepository<Contact , String> {

    //La méthode findById retourne un Optional, ce qui signifie que la valeur peut être présente ou non.
    // Cela est utile pour indiquer explicitement qu'il se peut qu'aucune entité avec cet ID ne soit trouvée dans la base de données.
    Optional<Contact> findById(String id);
}
