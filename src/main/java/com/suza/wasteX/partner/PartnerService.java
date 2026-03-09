package com.suza.wasteX.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public Partner savePartner(String name, String email, String phone, MultipartFile imageFile) throws IOException {
        Partner partner = new Partner();
        partner.setName(name);
        partner.setEmail(email);
        partner.setPhone(phone);

        // Convert to Base64
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
        partner.setImage(base64Image);

        return partnerRepository.save(partner);
    }

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public List<String> getAllPartnerImages() {
        return partnerRepository.findAll()
                .stream()
                .map(Partner::getImage)
                .toList();
    }

    public byte[] getPartnerImage(Long id) throws IOException {
        Optional<Partner> partnerOpt = partnerRepository.findById(id);
        if (partnerOpt.isPresent() && partnerOpt.get().getImage() != null) {
            return Base64.getDecoder().decode(partnerOpt.get().getImage());
        }
        throw new IOException("Image not found for partner ID: " + id);
    }

    public Partner updatePartner(Long id, String name, String email, String phone, MultipartFile imageFile) throws IOException {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with ID: " + id));

        partner.setName(name);
        partner.setEmail(email);
        partner.setPhone(phone);

        if (imageFile != null && !imageFile.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            partner.setImage(base64Image);
        }

        return partnerRepository.save(partner);
    }

    public void deletePartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with ID: " + id));
        partnerRepository.delete(partner);
    }

}
