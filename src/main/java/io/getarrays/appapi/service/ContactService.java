package io.getarrays.appapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import io.getarrays.appapi.domaine.Contact;
import io.getarrays.appapi.repo.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.getarrays.appapi.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
//Cela permet d'utiliser des méthodes de journalisation telles que log.debug(), log.info(), etc.
@Slf4j
//transactionnal spécifier que chaque méthode de cette classe doit être exécutée dans une transaction.
// Cela garantit que si une exception se produit pendant l'exécution de la méthode,
// la transaction est annulée (rollback) et les modifications de base de données effectuées au sein de cette transaction sont annulées
@Transactional(rollbackOn = Exception.class)
//Genere un constructeur avec des arguments pour tout les champs de la classe qui sont marqué comme final
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepo contactRepo;

    public Page<Contact> getAllContacts(int page , int size) {
        //Recuperer les contacts de la table contact , pagerequest crée un objet pagerequest avec les infos
        //de pagination(num page et size de la page) ainsi que le tri par le nom du contact
        return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }
    public Contact getContact(String id) {
        //get contact
        return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }
    public Contact createContact(Contact contact) {
        //save new contact created
        return contactRepo.save(contact);
    }
    public void deleteContact(Contact contact) {
        //assignement
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for user ID: {}",id);
        //get the contact
        Contact contact = getContact(id);
        String photoUrl = photoFunction.apply(id, file);
        //set the photo on this contact
        contact.setPhotoUrl(photoUrl);
        //save to the new info
        contactRepo.save(contact);
        return photoUrl;
    }

    private final Function<String , String> fileExtension =
            //We have the filename at the optional we gonna filter that name and we want to have have "." for the name
            //beacause we cant work if we dont have .png or .jpg or somthing else for the file name
            filename -> Optional.of(filename).filter(name -> name.contains("."))
                    //after that need to map the name and substring the name to whaever can come after
                    .map(name -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");
    //Private because he gonna be used only in this class
    //we defined a function or a method gonna take a string , multipartfile and return a string and we
    //call it photoFunction and it take id and the image
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            //Cette ligne de code crée un objet Path qui représente le chemin vers un emplacement de stockage de fichiers.
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            //If Does not exists while we try to save the file it's just going to go ahead and create it
            //with this statement
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            //Saving the image we need id and extension of the image
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())),REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();

        }catch(Exception exception) {
             throw new RuntimeException("Unable to save the image");
        }

    };

}
