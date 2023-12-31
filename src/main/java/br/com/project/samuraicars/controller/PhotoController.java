package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.service.VehiclePhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping
    public ResponseEntity<?> save(@RequestPart("photos") List<MultipartFile> photos,
                                  @RequestParam Long vehicleId,
                                  UriComponentsBuilder uriBuilder,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.save(photos, vehicleId, userDetails);
        URI uri = uriBuilder.path("/photos").queryParam("vehicle_id", vehicleId).build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.delete(id, userDetails);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        byte[] image = vehiclePhotoService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping("/{photo_id}")
    public ResponseEntity<Void> replace(@PathVariable("photo_id") Long photoId, @RequestParam MultipartFile photo) {
        vehiclePhotoService.replace(photoId, photo);
        return ResponseEntity.noContent().build();
    }
}
