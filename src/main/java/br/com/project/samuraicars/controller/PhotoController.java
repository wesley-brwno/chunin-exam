package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.service.VehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final VehiclePhotoService vehiclePhotoService;
    @PostMapping
    public ResponseEntity<?> save(@RequestParam List<MultipartFile> photos, @RequestParam Long vehicleId,
                                  UriComponentsBuilder uriBuilder) {
        vehiclePhotoService.save(photos, vehicleId);
        URI uri = uriBuilder.path("/photos/{vehicle_id}").buildAndExpand(vehicleId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        byte[] image = vehiclePhotoService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping()
    public ResponseEntity<List<String>> getImagesByVehicleId(@RequestParam Long vehicle_id) {
        List<String> imagesByVehicleId = vehiclePhotoService.getImagesPathByVehicleId(vehicle_id);
        return ResponseEntity.ok().body(imagesByVehicleId);
    }
}