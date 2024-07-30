package com.suza.wasteX.wastex_site_api.services;


import com.suza.wasteX.DTO.WebsiteDTO.FeatureRequest;
import com.suza.wasteX.DTO.WebsiteDTO.FeatureResponse;
import com.suza.wasteX.wastex_site_api.models.Feature;
import com.suza.wasteX.wastex_site_api.repositories.FeatureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureService {
    private final FeatureRepository featureRepository;
    private final ModelMapper modelMapper;

    public FeatureResponse createFeature(FeatureRequest request) {
        Feature feature = modelMapper.map(request, Feature.class);
        feature = featureRepository.save(feature);
        return modelMapper.map(feature, FeatureResponse.class);
    }

    public List<FeatureResponse> getAllFeature() {
        List<Feature> features = featureRepository.findAll();
        return features
                .stream()
                .map(feature -> modelMapper.map(feature, FeatureResponse.class))
                .collect(Collectors.toList());
    }

    public FeatureResponse updateFeature(Long id, FeatureRequest featureRequest) {
        Optional<Feature> feature = featureRepository.findById(id);
        if(feature.isPresent()) {
            Feature new_feature = feature.get();
            new_feature.setTitle(featureRequest.getTitle());
            new_feature.setDescription(featureRequest.getDescription());
            new_feature = featureRepository.save(new_feature);
            return modelMapper.map(new_feature, FeatureResponse.class);
        }
        else {
            log.info("feature with id {} does not exists" ,id);
            return null;
        }
    }

    public boolean deleteFeature(Long id) {
        Optional<Feature> feature = featureRepository.findById(id);
        if(feature.isPresent()) {
            featureRepository.delete(feature.get());
            return true;
        }
        else {
            return false;
        }
    }
}
