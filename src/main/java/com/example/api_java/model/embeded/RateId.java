package com.example.api_java.model.embeded;

import com.example.api_java.model.entity.Product;
import com.example.api_java.model.entity.UserDetail;
import lombok.*;


import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RateId implements Serializable {
    @ManyToOne
    private Product product;
    @ManyToOne
    private UserDetail user;
}
