//package com.suza.wasteX.projectType;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//    private final TypeRepository typeRepository;
//
//    @Override
//    public void run(String... args) {
//        String[] types = {"incubation","hackathon","meeting","conference","seminar","training"};
//        for (String typeName : types) {
//            if (typeRepository.findByName(typeName).isEmpty()) {
//                typeRepository.save(new Type(null, typeName, null));
//            }
//        }
//    }
//}
