package com.suza.wasteX.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

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
}
