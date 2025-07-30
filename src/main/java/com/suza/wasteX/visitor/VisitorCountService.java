package com.suza.wasteX.visitor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VisitorCountService {
    private static final Logger logger = LoggerFactory.getLogger(VisitorCountService.class);

    @Autowired
    private VisitorRepo visitorRepo;

    @Autowired
    private VisitorCountRepo visitorCountRepo;

    @PostConstruct
    public void initVisitorCount() {
        visitorCountRepo.findById(1L).orElseGet(() -> {
            VisitorCount vc = new VisitorCount();
            vc.setId(1L);
            vc.setCount(0);
            return visitorCountRepo.save(vc);
        });
    }


    public int incrementVisitorCount(HttpServletRequest request) {
//        String ipAddress = request.getHeader("X-Forwarded-For");
//        if (ipAddress == null || ipAddress.isEmpty()) {
//            ipAddress = request.getRemoteAddr();
//        }
        String ipAddress = getClientIP(request);

        Optional<Visitor> existingVisitor = visitorRepo.findByIpAddress(ipAddress);

        if (existingVisitor.isEmpty()) {
            // Save new visitor
            Visitor visitor = new Visitor();
            visitor.setIpAddress(ipAddress);
            visitor.setTimestamp(LocalDateTime.now());
            visitorRepo.save(visitor);

            VisitorCount countEntity = visitorCountRepo.findById(1L).orElse(new VisitorCount());
            countEntity.setId(1L); // just in case it's new
            countEntity.setCount((countEntity.getCount() == null ? 0 : countEntity.getCount()) + 1);
            visitorCountRepo.save(countEntity);
        }

        return getVisitorCount();
    }

    public Integer getVisitorCount() {
        return visitorCountRepo.findById(1L).map(VisitorCount::getCount).orElse(0);
    }

    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0]; // In case of multiple proxies
    }

}



