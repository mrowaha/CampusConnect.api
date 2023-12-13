package com.campusconnect.ui.market.entity;

import com.campusconnect.ui.market.enums.ProductStatus;
import com.campusconnect.ui.market.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.*;
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    protected Long productId;

    @Column(name = "seller_id", nullable = false)
    protected UUID sellerId;

    @Column(name = "creation_date", nullable = false)
    protected LocalDate creationDate;

    @Column(name = "product_name", nullable = false)
    @NotNull
    protected String name;

    @Column(name = "product_description", nullable = false)
    @NotNull
    protected String description;

    @Column(name = "product_price", nullable = false)
    @NotNull
    protected Integer price;

    //protected ArrayList<String> images;

    @Column(name = "view_count", nullable = false)
    protected Integer viewCount = 0;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    protected ProductType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected ProductStatus status;

    @ElementCollection(targetClass =  UUID.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "wish_list_table", joinColumns = @JoinColumn(name = "bilkenteer_id"))
    @Column(name = "wish_listed_by")
    protected Set<UUID> wishListedBy = new HashSet<>();

    @ElementCollection(targetClass =  Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "bids_table", joinColumns = @JoinColumn(name = "bilkenteer_id"))
    @Column(name = "bids")
    protected List<Integer> bids = new ArrayList<>();

    @ElementCollection(targetClass =  Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "categorises_id_table", joinColumns = @JoinColumn(name = "bilkenteer_id"))
    @Column(name = "categorises_id")
    protected Set<Integer> categorizesId = new HashSet<>();

    //public void addImage()
    //public void removeImage()

    public Set<UUID> wishListedBy(){return new HashSet<UUID>();}

    public void List(){}
    public void unList(){}

    public void switchType(ProductType type){}

    public void onView(){}

    public void PlaceBid(Integer bid){}

    public Integer getHighestBid(){ return 0;}



}
