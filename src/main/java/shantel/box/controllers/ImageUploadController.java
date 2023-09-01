package shantel.box.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/upload")
@CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
public class ImageUploadController {
//    private static String imageDirectory = System.getProperty("user.dir") + "/images/";
//    String imageDirectory = ImageUploadController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

//    @CrossOrigin(value = "https://kutija.net", allowCredentials = "true")
    @RequestMapping(value = "/image", produces = {MediaType.IMAGE_PNG_VALUE, "application/json"})
    public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile file) {
    	HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "https://kutija.net");
		responseHeaders.set("Access-Control-Allow-Credentials", "true");
		File directory = new File("/var/www/vhosts/kutija.net/httpdocs/images");
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        makeDirectoryIfNotExist(directory);
       
        Path fileNamePath = Paths.get(directory.getPath(),
        		file.getOriginalFilename()); // .concat(".").concat(FilenameUtils.getExtension(file.getOriginalFilename()))
        
        String returnData = "https://kutija.net/images/" + file.getOriginalFilename();
        
        System.out.println("DIREKTORIJUM: " + directory + " PATH: " + fileNamePath );
        try {
            Files.write(fileNamePath, file.getBytes());
            return new ResponseEntity<>(returnData, HttpStatus.CREATED);
        } catch (IOException ex) {
            return new ResponseEntity<>("Image is not uploaded", HttpStatus.BAD_REQUEST);
        }
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        
    }
}
