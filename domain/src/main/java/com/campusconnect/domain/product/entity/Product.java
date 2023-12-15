package com.campusconnect.domain.product.entity;

import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.product.enums.ProductType;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    protected UUID productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    private Bilkenteer seller;

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
    protected Double price;

    private LocalDate rentalStartDate;

    private LocalDate rentalEndDate;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    protected List<Bid> bids = new ArrayList<>();

    @ElementCollection(targetClass =  String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_tags_table", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tags")
    protected Set<String> tags = new HashSet<>();

    //public void addImage()
    //public void removeImage()

    public Set<UUID> wishListedBy(){return new HashSet<UUID>();}

    public void List(){}
    public void unList(){}

    public void switchType(ProductType type){}

    public void onView(){}

    public void PlaceBid(Integer bid){}

    public Integer getHighestBid(){ return 0;}

    @Override
    public String toString() {
        // Include all fields except 'seller'
        return "";
    }

}
