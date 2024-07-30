package com.suza.wasteX.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Data
@MappedSuperclass
@EntityListeners(EntityListeners.class)
public class Auditable {
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;

    @CreatedDate
    private LocalDate createdDate;
    @LastModifiedDate
    private LocalDate modifiedDate;

}
