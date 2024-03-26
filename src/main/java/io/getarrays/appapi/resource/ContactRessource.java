package io.getarrays.appapi.resource;
// importez les dépendances
import io.getarrays.appapi.domaine.Contact;
import io.getarrays.appapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.getarrays.appapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

//cette classe est un controleur REST
@RestController
//URL pour toutes les methodes de ce controleur est /contacts
@RequestMapping("/contacts")
//genère un constructeur avec tous les champs requis
@RequiredArgsConstructor
public class ContactRessource {
    private final ContactService contactService;
     //Methode creation d'un contact
    @PostMapping
    //la methode prend un objet "contact" en entrée fournis dans le corps de la requete @requestbody
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        //elle utilise contactservice.createcontact(contact) pour creer un new contact
        //ResponseEntity.created retoune status(201)
        return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
    }
    //recuperation de tout les contacts paginées
    @GetMapping
    //elle prend deux methodes page et size qui spécifient respectivement le numéro de page et la taille de la page pour la pagination.
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        //retourne une reponse status.200 et la page de contact dans le corps de la reponse
        return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
    }
      //Recuperation d'un contact depuis son id
    @GetMapping("/{id}")
    //Elle utilise @PathVariable pour extraire la valeur de l'ID du chemin de l'URL.
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        //Elle retourne une réponse HTTP avec le code de statut 200 (OK) et le contact correspondant dans le corps de la réponse.
        return ResponseEntity.ok().body(contactService.getContact(id));
    }
   //Cette méthode est annotée avec @PutMapping("/photo"),
   // ce qui signifie qu'elle répond aux requêtes HTTP PUT sur l'URL spécifiée dans la classe (/contacts/photo).
    @PutMapping("/photo")
    //elle prend l'id qui est l'identifiant du contact dont la photo doit etre en mise a jour
    //et file qui est le fichier de la nouvelle photo envoyé dans la requete multipartfile
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }



    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    //Elle utilise @PathVariable pour extraire le nom du fichier du chemin de l'URL.
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        //Elle lit le contenu du fichier image à partir du répertoire spécifié
        // (PHOTO_DIRECTORY + filename) et le retourne en tant que tableau de bytes.
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}