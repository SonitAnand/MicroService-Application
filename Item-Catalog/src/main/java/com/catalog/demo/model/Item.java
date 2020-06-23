package com.catalog.demo.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Data
@EqualsAndHashCode
public class Item implements Serializable {

    @Id
    private String item_id;
    private String item_name;
    private String item_price;
}
