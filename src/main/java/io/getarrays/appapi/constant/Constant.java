package io.getarrays.appapi.constant;

public class Constant {
    //System.getProperty("user.home"): Cela récupère le répertoire personnel de l'utilisateur à partir des propriétés système.
    // C'est l'emplacement du répertoire personnel de l'utilisateur sur le système d'exploitation.
    ///Downloads/uploads": C'est le chemin relatif supplémentaire où les photos seront stockées.
    // Il est ajouté à la fin du répertoire personnel de l'utilisateur.
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";


    public static final String X_REQUESTED_WITH = "X-Requested-With";
}



